https://sparrowzoo.feishu.cn/docx/Vo7CdhofQoejxsxmq6vcwYhNnMg

## 移除 sparrow 工具库后的接入调整

保留 `sparrow-protocol`、`sparrow-parent` 和现有认证接口。字符串处理使用
Commons Lang，JSON 直接使用已有 Fastjson，RSA 使用 JDK；JWT 和 Redis 数据格式保持不变。

- 业务代码若直接读写登录用户或客户端上下文，请将
  `com.sparrow.context.SessionContext` 改为
  `com.sparrow.authenticator.context.SessionContext`。bind/get/clear 方法不变；
  两个类不共享上下文。Controller 的 `LoginUser` 参数注入方式不变。
- `MonolithicBearerFilter` 构造器的第三个参数改为 Spring `Environment`。
  Spring 自动配置会注入该参数；手动构造的调用方需要调整。
- `login_url`、`support_template_engine`、`ajax_patterns` 从 Spring 配置读取，
  配置键名不变。原来由 Sparrow 自定义配置中心提供这些值的应用，需将其接入
  Spring `Environment`。`sparrow.authc.*` 配置仍然保留。
- 单体过滤器优先使用新上下文中的客户端设备 ID；未绑定时依次读取 `DEVICE_ID`
  请求头和 Servlet 的 `remoteAddr`。依赖代理后真实客户端 IP 的应用，应由可信的
  服务器配置解析该地址，或在上游绑定客户端信息；过滤器不会直接信任转发头。
- `NotifyRegistry` 保留原有方法，但不再实现 Sparrow 的 `FactoryBean` 接口。
  原 Sparrow JSON SPI 提供者也不再参与本模块的序列化。

本地验证：在 `sparrow-authenticator` 根目录运行
`mvn -o -B -DskipTests=false verify`。测试无需启动 Redis。
