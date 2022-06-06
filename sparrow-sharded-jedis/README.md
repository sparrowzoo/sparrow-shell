功能列表
---
1. 进一步对客户端友好封装，对数据类型的转换，封装进框架内。
2. 增加CacheDataNotFound hook，并保留原有异常抛出接口，提供客户端灵活调用。
3. 对key的business 部分提供灵活扩展接口，为特定业务的监控提供扩展。
4. 接口不只依赖redis，理论上可以对redis 的不同接口进行实现，包括jedis sharded jedis jedisson以及其他异构缓存系统。
5. 将接口按类型进行拆分，避免类过于宠大，不利于维护。
6. 提供monitor接口 befare和montor 方法,客户端可根据需要进行扩展，如果需要对redis 限流，可实现before接口，如果不需要则直接返回true。
7. 提供限流工具类。AbstractLock 客户端可以根本需要进行实现。
8. 将redis 客户端从sparrow 分拆出来，单独部署，单独维护，单独版本控制。

实现类图如下
![image](class-diagram.png)

测试用例
[https://github.com/sparrowzoo/sparrow-test](https://github.com/sparrowzoo/sparrow-test)

欢迎大家转载，也欢迎各位有兴趣的朋友一起加入.
由于个人能力有限，难免有些不足之处，警请谅解。谢谢!
zh_harry#163.com