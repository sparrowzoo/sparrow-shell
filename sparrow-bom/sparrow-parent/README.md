# sparrow-parent

`sparrow-parent` 是 Sparrow 工程的通用 Maven Parent，统一管理第三方开源依赖版本和公共工程配置，提供相对稳定、可复用的技术基线。

## 背景

多个工程共享开源技术栈时，需要统一依赖版本、编译与构建规则、质量检查和发布约定，减少重复维护和配置不一致带来的问题。`sparrow-parent` 集中维护这些公共能力，使通用工程基线与公司平台组件的日常迭代保持独立。

## 职责

- 管理 Spring Boot、Spring Cloud、Spring Cloud Alibaba、Spring AI 等上游 BOM 的版本。
- 管理 Netty、Jakarta、SLF4J、数据库驱动及其他第三方基础库的版本。
- 管理公共版本属性、Java 编译配置、字符编码和源码目录约定。
- 管理通用构建插件的版本、配置及执行规则，包括编译、测试、资源处理、代码检查、打包、源码和 Javadoc 附件。
- 提供 `dev` 和 `prod` 环境 profiles，分别设置 `env=dev`、`env=prod`。
- 提供 `release` profile，统一正式版本检查、源码和 Javadoc 附件及签名约定。
- 提供公共许可证和开发者信息，供下游工程继承。

开源依赖与公共工程配置作为统一基线维护，经兼容性验证后更新，保持相对稳定。

## 与 infra-bom 的关系

`infra-bom` 继承 `sparrow-parent`，复用其开源依赖基线和通用工程配置，自身管理公司平台组件的依赖版本。业务项目通过继承 `infra-bom` 接入平台能力。

| 项目 | 核心职责 |
| --- | --- |
| `sparrow-parent` | 第三方开源依赖版本管理，以及通用构建、编译、质量检查、发布和项目公共信息 |
| `infra-bom` | 公司平台组件的 `dependencyManagement` 和各组件独立的版本属性，并通过继承复用通用工程配置 |

继承关系为：业务项目 → `infra-bom` → `sparrow-parent`，箭头指向父项目。仓库根项目 `sparrow-bom` 仅聚合这两个模块。

两个模块独立维护版本。平台组件的迭代不要求通用工程基线同步升级。

## 接入方式

仅需通用依赖版本和工程配置的项目，可以直接继承 `sparrow-parent`。需要公司平台组件版本管理的项目，请按 [infra-bom 接入说明](../infra-bom/README.md) 继承 `infra-bom`，它会同时继承本模块的公共基线。

直接接入的最小 `pom.xml` 示例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.sparrowzoo</groupId>
        <artifactId>sparrow-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>example-service</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</project>
```

将业务项目的 `groupId`、`artifactId` 和 `version` 替换为实际坐标。`<relativePath/>` 表示不查找相邻目录中的父 POM；接入前，父 POM 需已安装到本地 Maven 仓库，或由项目可访问的远程仓库提供。通过远程仓库使用示例中的 `SNAPSHOT` 版本时，需要启用该仓库的快照下载。

继承后，`dependencyManagement` 提供依赖版本管理；项目仍需在自己的 `dependencies` 中声明实际使用的依赖。已管理的依赖通常可以省略版本。

## 默认构建行为

要求 JDK 18+、Maven 3.8.6+，编译的 `release`、`source`、`target` 统一使用 `maven.compiler.release=18`。Enforcer 在 `validate` 检查工具版本，Checkstyle 同阶段按外部规则执行。

构建前，将 `SPARROW_STYLE_DIR` 环境变量指向包含 `sparrow_checkstyle.xml` 和 `check_style_suppressions.xml` 的目录；Parent 分别通过 `${env.SPARROW_STYLE_DIR}/sparrow_checkstyle.xml` 和 `${env.SPARROW_STYLE_DIR}/check_style_suppressions.xml` 定位规则与抑制文件。配置方式见 [Checkstyle 环境变量](../README.md#checkstyle-环境变量)。

编译、测试、资源处理等生命周期插件使用继承的 `pluginManagement` 配置。测试默认执行，失败阻断；临时跳过测试使用 `mvn -B verify -DskipTests`。

普通 Java 库无需声明 Spring Boot 插件。可执行应用需要重新打包时，在业务 POM 中添加：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

默认激活 `dev` profile，设置 `env=dev`；使用 `-Pprod` 选择生产环境，设置 `env=prod`。应用可补充自身的环境配置。激活 `release` 会停用默认的 `dev`；发布时如需生产环境属性，使用 `-Prelease,prod`。

各项目维护自身 URL、SCM 和部署仓库，凭据放在 Maven `settings.xml` 中。源码、Javadoc 附件及 GPG 签名仅在 `-Prelease` 时启用；该 profile 拒绝 SNAPSHOT 项目、父级和实际依赖。

本仓库的构建和独立发布流程见[本地构建](../README.md#本地构建)及[独立发布](../README.md#独立发布)。
