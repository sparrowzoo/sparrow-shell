# sparrow-protocol

> 麻雀虽小，五脏俱全 —— sparrow 框架的「协议 / 契约」基础模块

`sparrow-protocol` 是 sparrow-shell 框架中**最底层、0 业务依赖**的模块，只依赖 Lombok。它承载了所有跨模块复用的**类型契约**：

- POJO / BO / DTO / Query 等数据对象的标记体系；
- 服务端统一的**返回协议 `Result`** 与**错误模型**（`ErrorSupport` / `BusinessException`）；
- DDD 领域抽象（`Entity` / `ValueObject`）；
- 常用 DTO、分页、枚举、常量与 SPI 扩展接口。

该模块只做「定义」，不做「实现」——具体行为（如 Bean 拷贝、消息组装、下载落地）由上层模块以 SPI 方式按需提供，最大化解耦，符合框架「0 依赖、可插拔」的定位。

---

## 在框架中的位置

```text
sparrow-shell (聚合工程)
 ├─ sparrow-protocol      ← 本模块：协议、契约、类型体系（仅依赖 Lombok）
 ├─ sparrow-protocol-dao  (DAO 层协议)
 ├─ sparrow-protocol-mq   (消息协议)
 ├─ sparrow-mvc / sparrow-orm / sparrow-json / ...
 └─ ...
```

图中表示模块聚合关系；这些核心模块各自继承 `sparrow-parent` 的构建配置。

坐标：`com.sparrowzoo:sparrow-protocol:1.0.0-SNAPSHOT`

---

## 核心设计

### 1. POJO 标记体系 —— 数据对象分层

以 `POJO`（继承 `java.io.Serializable`）为根的纯标记接口，目的是**给参数注入与方法访问打标记**，保证这些对象由 sparrow 的 `MethodAccessor` 体系访问，并与 `Serializable` 语义区分：

| 接口 | 继承 | 用途 |
| --- | --- | --- |
| `POJO` | `Serializable` | 一切框架对象的总标记 |
| `BO` | `POJO` | 业务对象（业务层出参） |
| `DTO` | `POJO` | 传输对象（对外/跨层出参、协议体） |
| `Query` | `POJO` | 查询入参（查询条件对象） |
| `Param` | `POJO` | 请求参数统一类型标记 |

> 约定：**入参**用 `Query`/`Param`，**出参**用 `BO`（内部业务）/`DTO`（跨层传输），由包名/命名区分层次。

### 2. 统一返回协议 `Result<T>`

服务端对外返回的固定报文结构，规范前后端协议（实现了 `DTO`）：

```json
{
  "code": "0",            // 成功恒为 "0"，失败为模块化错误码（如 "0001"）
  "key":  "",             // 错误 / i18n key（失败时对应 ErrorSupport.name() 小写）
  "message": "success",   // 提示文本
  "instruction": null,    // 指令，用于 websocket 返回指令
  "data":  {}
}
```

- 成功：`Result.success()`（默认单例，GC-free、DCL 懒加载）、`success(data)`、`success(data, key)`；
- 失败：`Result.fail(ErrorSupport)`、`Result.fail(Exception)`（`BusinessException` 自动转换为错误结果）；
- `data` 若实现了 `ErrorSupport`，构造器会自动识别为「错误对象」而非业务数据；
- 属性只读风格设计，保证线程安全与协议稳定。

### 3. 错误模型 —— 枚举化 + 国际化

错误接口**枚举化**定义（枚举不可继承，因此错误码用组合而非继承）：

- `ErrorSupport`：错误契约，暴露 `system()` / `module()` / `getCode()` / `getMessage()` / `name()`；
- `ModuleSupport`：模块标识（`code()` + `name()`）；
- `SparrowError`：全局系统错误枚举；错误码按 **`system(0/1) + module.code + 序号`** 拼接，例如 `GlobalModule.GLOBAL("00")` 下的 `SYSTEM_SERVER_ERROR("01")` → 错误码 `0001`；
- `BusinessException`：业务异常（中断型），携带 `ErrorSupport` 与 `key`、`parameters`，`key` 用于**提示信息国际化**（前端按 `ErrorSupport.name().suffix` 取文案，参数可带入）；
- `NotTryException`：标记「不应被捕获重试」的受检异常；
- `Constant.RESULT_OK_CODE = "0"` 为成功码。

```java
// 业务侧自定义错误：实现 ErrorSupport（枚举），复用全局模块或自定 ModuleSupport
public enum UserError implements ErrorSupport {
    USER_NOT_FOUND("01", "user not found");
    // ... 实现 system()/module()/code/message
}

throw new BusinessException(UserError.USER_NOT_FOUND);
```

### 4. DDD 领域抽象

| 接口 | 语义 |
| --- | --- |
| `Entity<T, ID>` | 实体：靠**身份**比较（`sameIdentityAs` / `identity()`） |
| `ValueObject<T>` | 值对象：靠**属性值**比较（`sameValueAs`） |
| `CopyableValueObject<T>` | 可自由深拷贝的值对象（`copy()`） |

### 5. SPI / 扩展点

模块只定义接口，实现在上层按需装配：

- `BeanCopier` —— 属性拷贝（支持忽略属性），由具体实现提供；
- `Downloader` —— 图片下载器（`downloadImage(url, authorId)`）；
- `EventSupport` —— 事件/埋点统一事件名访问器；
- `SFunction<T,R>` —— 可序列化函数式接口（Lambda 方法引用，供反射取属性名）；
- `DisplayTextAccessor` / `EnumIdentityAccessor` / `EnumUniqueName` —— 枚举展示文案、整型身份（DB/传输稳定表达）、唯一业务名注解三件套；
- `Exclude` —— 类型级注解，标记某类从指定处理（默认 `"POJO"`）中排除。

---

## 包结构

```text
com.sparrow.protocol
├── BO / DTO / Query / Param / POJO         数据对象标记接口
├── Result<T>                               统一返回协议
├── ErrorSupport / BusinessException /
│   NotTryException / ModuleSupport         错误模型
├── LoginUser                               登录用户契约接口（userId/租户/设备/有效期...）
├── ClientInformation                       客户端信息（设备、平台、定位、网络、IMEI...）
├── IdentityDTO / ListRecordTotalBO / TreeItem /
│   KeyValue / Neighbor / Size / WebsiteConfig  通用对象
├── BeanCopier / Downloader / EventSupport /
│   SFunction / DisplayTextAccessor / ...    SPI 与扩展
├── ddd                                     Entity / ValueObject / CopyableValueObject
├── enums                                   AuditStatus / DeviceType / EditorType / Media /
│                                           OrderBy / Platform / StatusRecord
├── pager                                   SimplePager / PagerResult<T>
└── constant
    ├── Constant / SparrowError / GlobalModule / Extension /
    │   OpenType / ClientInfoConstant        全局常量与错误码
    └── magic                               魔法值常量（避免散落魔法数字/串）
        ├── CapitalRmb                       人民币大写（壹贰叁…元角分）
        ├── CharSymbol / Symbol              char / String 版符号常量（含 true/false/null、HTML 实体等）
        ├── Digit                             整型常量（0–12、K=1024、ALL=-1…）
        ├── Escaped                           HTML 转义实体（&lt; &amp; &nbsp;…）
        └── Letter                            A–Z / a–z 字符常量
```

---

## 常用类型速查

| 类型 | 说明 |
| --- | --- |
| `SimplePager` | 无 HTML 的简单分页入参（`pageSize`/`pageNo`），实现 `Query` |
| `PagerResult<T>` | 分页出参（含 `recordTotal`、`list`、`dictionary` 字典），实现 `DTO`；提供 `getLastPageIndex()` |
| `IdentityDTO<T>` | `id + data` 的轻量 DTO，如「按 id 携带附属数据」返回 |
| `ListRecordTotalBO<T>` | BO 列表 + 总数（`empty()` 提供空结果），常用于业务层出参 |
| `KeyValue<K,V>` | 键值对（也作分页字典项） |
| `Neighbor<F,S>` | 两个关联值的二元组（当前/下一个），`create` / `split` 工厂 |
| `TreeItem` | 树形节点 DTO（id/parentId/name/code/icon），不递归持子节点 |
| `Size` | 宽高（px / auto），提供 CSS 值与图片容器等比缩放计算 |
| `WebsiteConfig` | 站点配置（title/keywords/logo/banner/icp…） |
| `LoginUser` | 登录用户契约：userId、tenantId、category、昵称、头像、设备、有效期、扩展字段、`isVisitor()` |

### 常用枚举

| 枚举 | 值 / 说明 |
| --- | --- |
| `Platform` | PC(0)/IOS(1)/Android(2)/WECHAT(3)/Unkonwn(-1)，`getByPlatform(int)` 反查 |
| `DeviceType` | PC(1)/MOBILE(2)/APP(3)，`getDeviceById` 反查 |
| `StatusRecord` | DISABLE(0)/ENABLE(1)，`@EnumUniqueName("status")` |
| `AuditStatus` | PENDING/APPROVE/REJECT，`valueOf(int)` 反查 |
| `OrderBy` | DEFAULT/NEWEST/HOTTEST/RANDOM/SORT |
| `EditorType` | MARKDOWN/HTML |
| `Media` | NEWS/LINK/TEXT/IMAGE/… 带中文 `text` 与布尔字段名 `field` |

---

## 快速示例

```java
// 1) 成功返回
return Result.success(dto);

// 2) 业务校验失败 → 抛业务异常，由上层统一转为 Result.fail(...)
if (name == null) {
    throw new BusinessException(SparrowError.GLOBAL_CONTENT_IS_NULL);
}

// 3) 出参声明对象
public class UserBO implements BO { /* 业务对象字段 */ }
public class UserDTO implements DTO { /* 传输对象字段 */ }

// 4) 分页
SimplePager pager = new SimplePager(10, 1);
PagerResult<UserBO> page = new PagerResult<>(pager);
page.setRecordTotal(total);
page.setList(list);
```

---

## 构建

```bash
# 在仓库根目录统一构建（推荐）
sh build.sh

# 或单独构建本模块（需先安装父 POM）
mvn clean install -Dmaven.test.skip
```

> 该模块继承 `sparrow-parent`（`relativePath` 空），单独构建前请先安装该父 POM。
> Checkstyle 配置继承自 `sparrow-parent`，规则路径为 `${env.SPARROW_STYLE_DIR}/sparrow_checkstyle.xml`。构建前请按 [Checkstyle 构建说明](../style/readme.md) 设置环境变量。

---

## 设计要点小结

- **纯契约、0 依赖**：只引入 Lombok，模块本身无框架依赖，方便被任意业务模块复用；
- **枚举化错误 + 组合错误码**：错误不可继承则用「模块 × 序号」组合，保证全局唯一、可读、可 i18n；
- **单例复用成功/失败结果**：`Result` 通过 DCL 懒加载复用固定成功/系统错误实例，减少 GC；
- **以 SPI 留扩展点**：拷贝、下载、消息组装等只声明接口，把「是否依赖、如何实现」交给上层业务决定。

---

## 备注

- 历史沿革中 `constant/magic` 内个别字符/数字常量存在拼写笔误（如 `Digit.TOW`、`Letter` 部分小写常量实际为大写值、`Escaped.DOUBLE_QUOTES` 与 `SINGLE_QUOTES` 同为 `&apos;`），作为既有约定保留，业务引用时以实际值为准。
- 测试：`src/test` 目前仅有一个 `ResultTest` 主方法（占位），暂无自动化单测，建议后续补充。
