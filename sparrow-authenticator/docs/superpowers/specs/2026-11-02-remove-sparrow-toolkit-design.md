# TRD：移除 authenticator 对 sparrow 工具库的依赖

日期：2026-11-02。状态：方案待用户确认，尚未开始实施。

方案检查：独立子代理已按实施者视角检查，结论为“没有重要遗漏”。此检查只说明方案可供评估，不代表方案已获用户批准，也不代表代码或测试通过。

## 1. 已确认范围

用户已明确：只移除 `com.sparrowzoo:sparrow` 工具库，其他不动。

- 保留 `sparrow-protocol`、`sparrow-parent`、所有模块坐标和模块间依赖。
- 保留 `LoginUser`、`BusinessException`、错误码、JWT 格式、Redis key 和认证业务规则。
- 保留已有 JJWT、Spring Boot、Servlet、Redis、Fastjson 的技术选择和父 POM 管理的版本，不升级框架。
- 仅修改本目录内为解除工具库依赖所需的代码、POM、测试和迁移说明。不修改调用方工程、父工程和 protocol 工程；不提交、推送或发布。
- “只移除工具库”的范围已经确认；下文具体替换方式、不可避免的调用调整仍待确认。

## 2. 已核实的现状

- 用户已从 core、monolithic starter、microservice starter 的 POM 移除 `sparrow` 依赖，并在 core 加入 `commons-lang3`；这些改动继续保留。
- 用户已删除 `JwtRSSignature` 中两个工具类 import，但调用仍在。不能恢复旧依赖来掩盖问题。
- 先前运行 `mvn -o -B -DskipTests compile`，core 报 35 处错误，主要是工具类、上下文、容器、JSON 和 RSA 引用无法解析；其余模块未进入编译。
- 本机 Maven 3.8.6、Java 21；已解析的父 POM 管理 Spring Boot 4.0.8、JJWT 0.13.0、Fastjson 1.2.83。父 POM 默认跳过测试，验收必须显式设置 `-DskipTests=false`。
- `SparrowJsonImpl` 的有关方法本身就是调用 `JSON.toJSONString` / `JSON.parseObject`，可直接调用已有第三方库，不必换 JSON 实现。
- 仓库中的旧测试主要是 `main` 示例，不能将其编译成功当成自动测试通过。

## 3. 替换设计

| 原依赖 | 替换方式 | 必须保留的行为 |
| --- | --- | --- |
| `StringUtility` | `org.apache.commons.lang3.StringUtils` | 原判空等于 null 或 `trim()` 后为空；用 `StringUtils.isEmpty(StringUtils.trim(value))` 保持边界，避免直接换成判空字符串而漏掉空格。token 原有的字符串 `null` 判断另行保留。 |
| `CollectionsUtility` | JDK 的 null / `isEmpty()` 判断 | null 与空集合的处理保持一致。 |
| `RegexUtility` | JDK 正则，在认证过滤器内部保留少量共用代码 | 保留原先 `*` 转为 `.*` 后整串匹配的规则；不切换为 Spring Ant 路径匹配。null/空配置作为无规则处理。 |
| `JsonFactory`、`Json` | core 显式声明 `com.alibaba:fastjson`，直接调用已有 JSON API | 版本仍由父 POM 管理；JWT body 仍为 JSON 字符串，字段名、枚举、数字、错误响应和 Redis value 结构不变。 |
| `FileUtility` | JDK 文件流与 Spring `ClassPathResource`，UTF-8，自动关闭流 | 先找文件系统路径；再兼容原 classpath 根路径 `/rsa/test` 以及原工具包相对资源语义。资源不存在和空内容仍按原构造流程处理，不额外改成启动失败。 |
| `RSAUtils` | JDK `KeyFactory`、`Base64`、`PKCS8EncodedKeySpec`、`X509EncodedKeySpec` | 保留去除空白后的 Base64 PKCS#8 私钥 / X.509 公钥，继续 RS256，不新支持其他密钥格式。签名验签错误仍转为原业务错误。 |
| `Asserts` | 原调用位置直接 `if (...) throw new BusinessException(...)` | 原 `isTrue` 在条件为 true 时抛错，不能误换成条件为 false 才抛错的断言工具。 |
| `CookieUtility`、`ServletUtility` | Servlet API | header 优先、同名 cookie 回退；Referer、Ajax 判断、登录跳转拼接顺序维持现状。 |
| `FilterOrders` | 认证模块内固定常量 | 继续使用认证过滤器顺序 2。 |
| `FactoryBean` | `NotifyRegistry` 不再实现旧容器接口 | 保留单例和现有五个注册/查询方法；不改成 Spring 的同名接口，二者含义不同。 |
| `ApplicationContext`、`WebConfigReader` | Spring `Environment` 注入 | 继续读取 `login_url`、`support_template_engine`、`ajax_patterns`，不增加必须配置的新键。 |
| `SessionContext` | 认证模块自己的业务上下文，底层使用 JDK | 见下一节；不在旧包名下复制工具类，避免和仍使用 Sparrow 的调用方产生同名类冲突。 |

以上是 AI 提议的实现选择，不代表用户已批准。

## 4. 上下文与 Web 配置的必要调整

### 4.1 登录用户和客户端信息

增加 `com.sparrow.authenticator.context.SessionContext`，只保存认证流程需要的 `LoginUser` 和 `ClientInformation`，保留原来的六个静态 bind/get/clear 方法和 `InheritableThreadLocal` 语义，不扩建通用工具库。

- 两个过滤器、参数解析器和现有测试统一使用此上下文。
- 原来 controller 直接声明 `LoginUser` 参数的用法保持不变。
- 直接使用旧 `com.sparrow.context.SessionContext` 的业务代码需要更换 import。旧对象和新对象不共享线程变量；不会宣称旧调用方无需迁移。
- 正常、下游异常时均清理本过滤器绑定的登录用户；不清除上游绑定的客户端信息。已有上下文嵌套时保存并恢复外层值。
- 保留子线程继承行为，不把线程池上下文传播、Servlet 异步派发作为本次新增能力。
- 单体过滤器优先读取新上下文已绑定的 `ClientInformation.deviceId`；未绑定时读 `DEVICE_ID` 请求头，再回退至 Servlet 的 `getRemoteAddr()`。不要求安装旧 Sparrow 客户端过滤器才能认证。
- 若部署依赖旧工具库的可信代理 IP 解析，应用应提供已解析的 `ClientInformation` 或由服务器配置可信代理后提供正确的 `remoteAddr`。本次不修改服务器配置，也不直接信任客户端传入的转发头。未迁移此接入方式时，原来以客户端 IP 充当设备 ID 的用户可能需重新登录。

### 4.2 Web 配置

`MonolithicBearerFilter` 构造器第三个参数由旧 `WebConfigReader` 改为 Spring `Environment`，自动配置同步调整。沿用现有键：

- `support_template_engine`：缺省 false，与旧代码一致。
- `ajax_patterns`：逗号分隔的正则，按旧规则整串匹配；缺省空集合。
- `login_url`：保持原有取值和跳转格式，不改成新的返回地址参数协议。
- `sparrow.authc.*` 现有配置和 `AuthenticatorConfigReader` 接口不变。

直接构造过滤器的调用方要调整第三个参数。原先从 Sparrow 自定义配置中心提供上述三个值的应用，需要将它们接入 Spring `Environment`；已在 application.properties/yml 中提供同名键的应用可继续使用。

### 4.3 JSON 和 Redis

`RedisSessionDao` 直接使用 Fastjson，并以已有具体类 `DefaultSessionStatus` 读取状态，避免将状态接口交给第三方库动态构造。保持 JSON 字段和存储 key 不变，不需要清理已有 Redis 数据。

原来通过 Sparrow JSON SPI 插入自定义提供者的应用，本次之后不会再使用该提供者；只承诺兼容已核实的默认 Fastjson 数据格式。此影响属于移除工具库的必要迁移说明。

不顺带修复 Redis 会话列表遍历、TTL 单位、tokenKey 配置等已有但与依赖替换无关的问题；若它们阻碍指定验收，只报告并确认是否扩大范围。

## 5. 影响范围和实施顺序

1. **依赖声明**：在三个已有修改的 POM 上继续处理，仅删除不再使用的 `sparrow.version` 属性；保留 protocol 的版本和依赖。core 显式依赖已有 Fastjson，保留 `commons-lang3`。测试依赖仅加在 core 的 test scope。
2. **直接替换**：处理 `DefaultSecurityManager`、`NotifyRegistry`、`AuthenticatorConfig`、`JwtRSSignature`、`RedisSessionDao` 中的工具引用。RSA 解析可在签名类中封装私有方法，无需新造公共加密 API。
3. **Web 接入**：增加认证业务上下文，调整 `AbstractAuthcFilter`、两个过滤器、两个过滤器自动配置和 `LoginUserArgumentResolver`；保持 bean 名称和过滤器顺序。
4. **测试和说明**：移除旧示例对工具库的 import，RSA 示例改用 JDK；为实际改动添加自动测试，并在 core README 记录上下文 import、构造器、配置来源、设备 ID 回退的迁移方式。
5. **本地验证**：运行下一节列出的检查，报告实际执行数量和结果，不将跳过测试描述为通过。

预计涉及 core 的约 14 个生产文件、现有 4 个示例、少量新增测试/上下文文件，以及 3 个 POM。gateway/passport 无需改源码，但纳入整组编译。

## 6. 验证计划

### 6.1 自动测试

- **认证拒绝条件**：过期、设备不匹配、被踢出/禁用时仍抛出相同 `BusinessException` 错误码，检查断言方向没有反转。
- **JWT**：测试时临时生成 RSA 密钥，签名后验签能恢复用户；使用错误公钥、被篡改或过期 token 均拒绝；文件/classpath 读取、空白 Base64 和原测试资源路径受覆盖，不打印私钥。
- **JSON**：同样的用户、扩展字段、枚举和会话状态可往返；已有风格的 JWT body、Redis JSON 和失败响应可读；状态读取为 `DefaultSessionStatus`。不启用任意类型反序列化。
- **Web**：排除 URL、header/cookie 回退、Ajax 401 和 HTML 登录跳转保持；上下文正确传给 `LoginUserArgumentResolver`，正常/异常退出清理并恢复；无上游客户端信息时不因缺少旧上下文而空指针。
- **装配**：用 Spring 测试上下文验证过滤器 bean 可以在没有旧 `WebConfigReader`、Sparrow 容器或 JSON SPI 的情况下创建，名称和顺序不变。

测试使用父 POM 管理的 JUnit / Spring Test / Mockito，精确的 test scope 依赖由实施者按已有本地依赖选定；不运行真实 Redis。仅覆盖本次替换边界，不能凭这些测试声称整个项目所有旧业务问题都已消除。

### 6.2 命令和验收标准

在本目录执行：

```sh
mvn -o -B -DskipTests=false test
mvn -o -B -DskipTests=false verify
mvn -o -B dependency:tree -Dincludes=com.sparrowzoo:sparrow
git diff --check
```

验收要求：

- 所有模块主代码和测试代码编译通过；新增自动测试确实执行，失败数为零。
- 父 POM 的现有构建规则通过；依赖树中没有 `com.sparrowzoo:sparrow`。
- 生产/测试源码不再引用工具库包；允许保留本项目 `com.sparrow.authenticator.*` 和已确认保留的 `com.sparrow.protocol.*`。
- 缺缓存依赖或插件造成验证无法执行时，列明未验证项目；不临时跳过规则或恢复旧依赖。

## 7. 环境、数据、恢复

- 实施后的验证仅运行本机 Maven/JDK；写入范围为本工程源码、测试、文档及 `target/`，临时密钥写入系统临时目录并由测试清理。
- 优先离线构建。需要下载测试依赖或插件时，仅使用本工程配置的 Maven 仓库；涉及网络/用户缓存写入时按环境权限要求单独申请，不更改全局 Maven 配置。
- 不启动 Docker/Redis、不读取真实业务数据、不更换应用密钥、不迁移 Redis 数据、不产生云资源费用。
- 修复或撤回仅针对本次新增补丁，保留用户已经移除依赖、加入 commons-lang3 和删除 import 的改动；不执行整仓 reset，不修改 protocol 中已暂存的删除。

## 8. 主要取舍与待确认事项

采用已有 Fastjson、JDK 和 Spring/Servlet API，减少额外依赖与数据格式变化；不在这次任务里另做 JSON 升级或认证框架替换。

无法完全保留旧工具库的 Java 类型引用：业务方若直接读写旧 SessionContext、构造过滤器、将 NotifyRegistry 赋值为旧 FactoryBean，或使用旧配置/JSON SPI，都需要相应调整。仅使用已保留的认证接口和 controller LoginUser 参数的代码不需要改类型。

用户需要确认的是：是否按本文所列的必要接入调整，进入依赖替换、相关重构和本地验证。源代码实现尚未开始；本次方案确认不包含提交、推送、发布或调用方工程修改。

## 9. 核实依据

- 本地 `sparrow` 源码：StringUtility、RegexUtility、RSAUtils、EnvironmentSupport、SessionContext、ServletUtility、WebConfigReader、FactoryBean、Asserts。
- 本地 `sparrow-json` 默认实现：SparrowJsonImpl；本地父 POM 和当前模块 POM。
- [Apache Commons StringUtils API](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/StringUtils.html)：字符串 null、trim、empty 等工具方法。
- [Fastjson 官方用法](https://github.com/alibaba/fastjson/wiki/Quick-Start-CN)：直接对象序列化和解析 API。
- [JDK 21 KeyFactory](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/security/KeyFactory.html)：标准 RSA 密钥工厂与密钥规格转换。
