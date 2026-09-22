# infra-bom

`infra-bom` 是公司 Infra 平台架构组面向业务研发团队提供的 Maven Parent，自身管理公司平台组件的依赖版本，并通过继承 `sparrow-parent` 复用开源依赖基线和通用工程配置。

## 背景

业务项目通常需要同时使用第三方开源框架和公司平台组件。随着项目数量增加，各团队分别维护依赖版本和构建配置，会带来重复配置、版本不一致及兼容性验证成本。

平台架构组通过统一的 Parent 向业务团队提供平台组件和公共工程基线，使业务团队能够基于一致的技术栈开发，集中精力实现业务功能。通用工程基线保持相对稳定，平台组件根据业务支持需要持续演进。

## 职责

| 维度 | sparrow-parent | infra-bom |
| --- | --- | --- |
| 项目定位 | 相对稳定的通用 Maven Parent | 公司 Infra 平台面向业务团队的 Parent |
| 依赖管理 | 第三方开源框架、基础库及上游 BOM 的版本与兼容组合 | Sparrow 组件、认证组件等平台自有依赖的版本 |
| 工程配置 | 通用构建插件、编译、编码、质量检查、发布约定、许可证和开发者信息 | 继承 `sparrow-parent` 提供的通用工程配置 |
| 自身维护内容 | 开源依赖版本与公共工程基线 | 平台自有依赖的 `dependencyManagement` 和各组件独立的版本属性 |
| 服务对象 | 需要统一开源依赖和通用工程配置的工程 | 使用公司平台能力的业务工程 |

`infra-bom` 通过 `dependencyManagement` 声明平台自有组件，每个组件使用独立的版本属性，属性名为 `<artifactId>.version`，例如 `sparrow.version`、`sparrow-container.version` 和 `authenticator-core.version`。各组件可以独立发布和升级，由 `infra-bom` 集中管理业务项目使用的版本组合。业务项目继承它后，同时获得这些平台组件版本和 `sparrow-parent` 提供的公共配置。

两个模块独立维护版本，平台组件的迭代不要求通用工程基线同步升级。

## 继承关系

箭头指向父项目：

```mermaid
flowchart LR
    business[业务项目] -->|parent| bom[infra-bom]
    bom -->|parent| base[sparrow-parent]
```

业务项目通过 Maven `<parent>` 继承 `infra-bom`，使用平台统一维护的依赖版本和构建配置，并按需声明实际使用的依赖。

仓库根项目 `sparrow-bom` 仅聚合 `sparrow-parent` 和 `infra-bom` 两个模块；业务项目使用的 Parent 是 `infra-bom`。

## 业务项目接入

### 准备条件

使用 JDK 18+、Maven 3.8.6+。默认按 Java 18 API 和字节码编译。

接入前，`com.sparrowzoo:infra-bom:1.0.0-SNAPSHOT` 及其父项目 `com.sparrowzoo:sparrow-parent:1.0.0-SNAPSHOT` 需要已安装到本地 Maven 仓库，或已发布到业务项目可访问的制品仓库。远程使用这些 SNAPSHOT 版本时，仓库配置需要启用快照解析。

构建前还需配置 `SPARROW_STYLE_DIR` 环境变量，指向包含 `sparrow_checkstyle.xml` 和 `check_style_suppressions.xml` 的规则目录，详见 [Checkstyle 环境变量](../README.md#checkstyle-环境变量)。

以下示例使用当前仓库声明的版本；全部 20 个平台组件的当前管理版本均为 `1.0.0-SNAPSHOT`。业务项目实际使用的平台组件也需要能够从其配置的仓库解析。

### 最小 POM 示例

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.sparrowzoo</groupId>
        <artifactId>infra-bom</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>demo-service</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>com.sparrowzoo</groupId>
            <artifactId>sparrow-json</artifactId>
        </dependency>
    </dependencies>
</project>
```

`<relativePath/>` 表示不从默认的 `../pom.xml` 查找平台 Parent，而是按坐标解析。业务项目显式声明自己的坐标，组件版本由 `infra-bom` 统一提供。

`dependencyManagement` 只管理版本，不会自动添加依赖。业务项目应在 `<dependencies>` 中声明实际使用的组件；上例仅声明 `sparrow-json`。

### 继承的构建规则

继承后默认在 `validate` 检查 JDK/Maven 版本，并运行采用外部规则的 Checkstyle。测试默认执行且失败阻断，临时跳过测试可用 `-DskipTests`。Spring Boot `repackage` 需要应用主动声明插件，示例见 [sparrow-parent 默认构建行为](../sparrow-parent/README.md#默认构建行为)。

默认激活 `dev` profile 并设置 `env=dev`，使用 `-Pprod` 选择生产环境并设置 `env=prod`；应用可补充自身的环境配置。各项目自行维护 URL、SCM 和部署地址。源码、Javadoc 附件和签名仅由 `release` profile 启用；正式发布时项目使用固定正式版本，Parent 和组件也需固定到已经发布的正式版本。

### 覆盖单个组件版本

继承 `infra-bom` 后，可以在业务项目的 `<properties>` 中使用相同属性名覆盖某个组件的版本。例如，下列属性控制 `sparrow-json`：

```xml
<properties>
    <sparrow-json.version>1.0.0-SNAPSHOT</sparrow-json.version>
</properties>
```

示例值为当前默认版本，按需替换为已发布并验证的目标版本即可。其他组件仍使用 Parent 管理的版本。全部组件属性可在 [pom.xml](pom.xml) 的 `<properties>` 中查看；通常直接使用平台提供的版本组合即可。

### 多模块工程

业务根 POM 继承 `infra-bom`，设置 `<packaging>pom</packaging>` 并声明 `<modules>`。各业务子模块通过 `<parent>` 继承业务根 POM，从而继续获得平台版本管理和公共工程配置。业务内部的父子 POM 可使用对应的本地 `relativePath`。

### Parent 与 BOM 导入

本文采用 `<parent>` 接入，同时继承依赖管理和公共工程配置。如果仅通过 `<dependencyManagement>` 以 `type=pom`、`scope=import` 导入 `infra-bom`，只会获得其依赖管理，不会继承编译、构建插件等 Parent 配置；上述通过业务项目属性覆盖组件版本的方式也以 Parent 继承为前提。

## 构建与发布

仓库内通过 `../sparrow-parent/pom.xml` 定位父 POM，外部业务项目使用上述 `<relativePath/>` 按坐标接入。`infra-bom` 独立发布；发布前需固定已发布的 Parent 和全部 20 个平台组件的正式版本。

完整步骤见[本地构建](../README.md#本地构建)及[独立发布](../README.md#独立发布)。
