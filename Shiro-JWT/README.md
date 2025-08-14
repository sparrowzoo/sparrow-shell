# SpringBoot 集成 JWT 和 Apache Shiro

<!-- wp:paragraph -->
<p>任何后端管理系统软件都避免不了登录验证和权限管理，大多数系统一开始就要设计登录认证以及权限管理模块。掌握登录认证以及权限管理的模块设计已经成为基础知识，本篇文章将采用 JWT 与 Apache Shiro 来讲解前后端分离中常用的认证与权限管理。</p>
<!-- /wp:paragraph -->

<!-- wp:heading -->
<h2>Shiro 简介</h2>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>Apache Shiro是一个功能强大且易于使用的Java安全框架，可以执行身份验证、授权、加密和会话管理。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>Shiro的体系结构有三个主要概念 Subject、SecurityManager 和 Realms。</p>
<!-- /wp:paragraph -->

<!-- wp:heading {"level":3} -->
<h3><strong>Subject</strong></h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>在保护应用程序安全时，最关键的问题是：“当前用户是谁？是否允许当前用户执行X？” 。只有辨识用户后才能判断用户权限。<strong>Subject</strong> 表示“当前正在执行的主体”，该主体可以是另一个系统的调用，也可以是用户登录。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>获取当前登录主体</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>import org.apache.shiro.subject.Subject;
import org.apache.shiro.SecurityUtils;
...
Subject currentUser = SecurityUtils.getSubject();</code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":3} -->
<h3><strong>SecurityManager</strong></h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p><strong>Subject </strong>代表用户的操作，<strong>SecurityManager</strong> 管理用户的操作。它是 Shiro 体系结构的核心，其内部有许多安全组件。每个应用程序一般只有一个 SecurityManager 实例，SecurityManager 的部分参数可以使用 .ini 文件进行配置。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p><strong>使用 shiro.ini</strong></p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>&#91;users]
jane.admin = password, admin
john.editor = password2, editor
zoe.author = password3, author
paul.reader = password4

&#91;roles]
admin = /
editor = /articles
author = /articles/drafts</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>ini 配置文件的 [users] 部分定义了 SecurityManager 能够识别的用户凭据。格式为: principal (username) = password, role1, role2...。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>[roles] 部分声明角色及其关联的权限。管理员角色被授予对应用程序的每个部分的权限和访问权。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>使用 IniRealm 来从 shiro.ini 文件加载用户和角色定义，然后使用它来配置DefaultSecurityManager 对象</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>IniRealm iniRealm = new IniRealm("classpath:shiro.ini");
SecurityManager securityManager = new DefaultSecurityManager(iniRealm);

SecurityUtils.setSecurityManager(securityManager);
Subject currentUser = SecurityUtils.getSubject();</code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":3} -->
<h3>Realms</h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>当需要获取用户帐户数据执行身份验证(登录)和授权(访问控制)时，Shiro会从为应用程序配置的领域 Realm 中查找用户帐户数据内容。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>常用的方式是在 Realm 实现对象中调用 DAO 方法获取用户账户信息。</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>public class MyCustomRealm extends AuthorizingRealm {

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = this.shiroSampleDao.getPasswordByUsername(username);
        return new SimpleAuthenticationInfo(username, password, getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) super.getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set&lt;String> roles = shiroSampleDao.getRolesByUsername(username);
        authorizationInfo.setRoles(roles);
        roles.forEach(role -> {
            Set&lt;String> permissions = this.shiroSampleDao.getPermissionsByRole(role);
            authorizationInfo.addStringPermissions(permissions);
        });
        return authorizationInfo;
    }

}</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>我们已经了解了 Shiro 的基本环境，如果想要定制化开发，我们还需要了解两个重要的概念，<strong>Authentication</strong> 和 <strong>Authorization</strong>。</p>
<!-- /wp:paragraph -->

<!-- wp:heading {"level":3} -->
<h3><strong>Authentication 认证</strong></h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>身份验证是验证用户身份的过程。当用户通过应用程序进行身份验证时，他们在证明自己是他们所说的身份。有时也验证称为“登录”，通常分为三步：</p>
<!-- /wp:paragraph -->

<!-- wp:list {"ordered":true} -->
<ol><li>收集用户的标识信息（称为身份 principals）和支持身份证明的凭证 （也叫凭据 credentials）。</li><li>将身份 principals 和凭据 credentials 提交到系统。</li><li>如果提交的凭据 credentials  与系统对于该用户身份（principal）的期望匹配，则认为该用户已通过身份验证 authentication 。如果它们不匹配，则不认为用户已通过身份验证 authentication 。</li></ol>
<!-- /wp:list -->

<!-- wp:paragraph -->
<p>每个人都熟悉的此过程的一个常见示例是 username/password  组合。当大多数用户登录软件应用程序时，通常会提供其 username （身份信息 principal）和 password （凭据 credential）。如果存储在系统中的密码与用户指定的密码匹配，则认为它们已通过身份验证。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>你想通过 Shiro 做的所有事情都可以通过与调用 Subject 的 API 来实现。要实现登录，可以调用 Subject 的 login 方法，并传递一个 AuthenticationToken 实例，该实例代表提交的身份信息和凭据（在本例中为用户名和密码）。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p><strong>Subject Login</strong></p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>//1. Acquire submitted principals and credentials:
AuthenticationToken token = new UsernamePasswordToken(username, password);

//2. Get the current Subject:
Subject currentUser = SecurityUtils.getSubject();

//3. Login:
currentUser.login(token);</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>处理登录失败，您可以选择捕获各种异常并对异常进行各种处理</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>//3. Login:
try {
    currentUser.login(token);
} catch (IncorrectCredentialsException ice) { …
} catch (LockedAccountException lae) { …
}
…
catch (AuthenticationException ae) {…
} </code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p></p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>确定允许用户执行的操作称为授权</p>
<!-- /wp:paragraph -->

<!-- wp:heading {"level":3} -->
<h3>Authorization 授权</h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>授权实质上是访问控制，控制用户可以在应用程序中访问的内容（例如资源，网页等）。大多数用户通过使用角色和权限等概念来执行访问控制。 Subject API 使您可以非常轻松地执行角色和权限检查。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>检查 Subject 是否被分配了特定角色</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>if ( subject.hasRole(“administrator”) ) {
    //show the ‘Create User’ button
} else {
    //grey-out the button?
} </code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>检查 Subject 是否被分配了特定权限</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>if ( subject.isPermitted(“user:create”) ) {
    //show the ‘Create User’ button
} else {
    //grey-out the button?
} </code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>如上，任何角色或用户被赋予 “user:create”  权限就可以点击“创建用户”按钮。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>Shiro 还支持更细力度的实例级权限检查</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>if ( subject.isPermitted(“user:delete:jsmith”) ) {
    //delete the ‘jsmith’ user
} else {
    //don’t delete ‘jsmith’
}</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>因为 JWT 是无状态的，所以本篇就不讲解 SessionManager 内容。接下来我们就来了解一下 JWT。</p>
<!-- /wp:paragraph -->

<!-- wp:heading -->
<h2>RESTful API 认证方式</h2>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>一般来说，RESTful API 通过身份验证和授权来保证 API 的安全性。</p>
<!-- /wp:paragraph -->

<!-- wp:heading {"level":3} -->
<h3>认证（Authentication） vs 授权（Authorization）</h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>认证是指用户的身份认证，授权是确认用户拥有的操作权限。</p>
<!-- /wp:paragraph -->

<!-- wp:heading {"level":3} -->
<h3>认证（Authentication）的方式</h3>
<!-- /wp:heading -->

<!-- wp:list -->
<ul><li><em><em><strong>Basic Authentication</strong></em></em> 这意味着将用户名和密码直接放入HTTP请求标头中。这是最简单的方法，但不建议这样做。</li><li><em><strong>TOKEN Authentication</strong></em> 这是将JWT令牌直接放入HTTP请求标头中的最常用方法。这是推荐的方法。</li><li><em><strong>OAuth2.0</strong></em> 这是最安全的方法，也是最复杂的方法。如果没有必要，不要考虑这种方式。</li></ul>
<!-- /wp:list -->

<!-- wp:paragraph -->
<p>通常，我们只在项目中使用JWT进行身份验证。</p>
<!-- /wp:paragraph -->

<!-- wp:heading -->
<h2>什么是 JWT</h2>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>JSON Web Token （JWT）是基于 JSON 的开放标准（RFC 7519），用于创建声明的访问令牌，这些令牌 token 包含一系列声明 claims。例如，服务器可以生成令牌，该令牌声明持有者具有“以管理员身份登录”的权利，该令牌会提供给客户端。然后，客户端可以使用该令牌来证明它以管理员身份登录。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>令牌一般是由服务器方的私钥进行签名，另一方已经通过可靠方式如 Post 请求获取了相应的公钥，双方能够验证令牌是否合法。令牌被设计为紧凑，URL 安全的，并且特别是在 Web 浏览器单点登录（SSO）上下文中可用。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>JWT 声明（claims）通常可用于在身份提供者和服务提供者之间传递经过身份验证的用户的身份，<strong>官方网站：</strong><a rel="noreferrer noopener" href="https://jwt.io/" target="_blank">https ://jwt.io/  </a><strong> </strong>。 <strong> JWT</strong> 由三部分组成，所有这些部分共同形成了 JWS 字符串，如下所示：</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOixMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydhV​​gNF3FkTHFOF</code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":3} -->
<h3>JWT 的三部分</h3>
<!-- /wp:heading -->

<!-- wp:list -->
<ul><li>Header 头部</li><li>Payload 负载</li><li>Signature 签名</li></ul>
<!-- /wp:list -->

<!-- wp:heading {"level":4} -->
<h4>Header 头</h4>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>头部有两部分</p>
<!-- /wp:paragraph -->

<!-- wp:list -->
<ul><li>声明类型 <strong><em>Type</em></strong> ：通常是 JWT</li><li>声明加密算法 <strong><em>algorithm</em></strong> ：典型的加密算法有HMAC和SHA-256 (HS256)， RSA签名和SHA-256 (RS256)。JWA (JSON Web算法) RFC 7518 引入了更多关于身份验证和加密的内容。</li></ul>
<!-- /wp:list -->

<!-- wp:paragraph -->
<p>Header 通常如下：</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>{
  'typ': 'JWT',
  'alg': 'HS256'
}</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>然后加密 Header ，如下：</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9</code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":4} -->
<h4>Payload 负载</h4>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>Payload 包括三个部分</p>
<!-- /wp:paragraph -->

<!-- wp:list -->
<ul><li>Registered Claim 注册声明</li><li>Public Claim 公共声明</li><li>Private Claim 私有声明</li></ul>
<!-- /wp:list -->

<!-- wp:paragraph -->
<p><strong>Registered Claim</strong></p>
<!-- /wp:paragraph -->

<!-- wp:table {"className":"is-style-stripes"} -->
<figure class="wp-block-table is-style-stripes"><table><tbody><tr><td>CODE</td><td>NAME</td><td>DESCRIPTION</td></tr><tr><td>iss</td><td>Issuer  </td><td>标识发行 JWT 的签发者</td></tr><tr><td>sub</td><td>Subject  </td><td>标识 JWT 的主题</td></tr><tr><td>aud</td><td>Audience </td><td>标识 JWT 接收人。</td></tr><tr><td>exp</td><td>Expiration Time  </td><td>标识过期时间。过期时间之后的 JWT 不会进行处理。</td></tr><tr><td>nbf</td><td>Not Before  </td><td>确定开始接受处理 JWT 的时间</td></tr><tr><td>iat</td><td>Issued at  </td><td>标识发布JWT的时间。NumericDate 类型</td></tr><tr><td>jti</td><td>JWT ID </td><td>区分大小写的令牌的唯一标识符，即使在不同发行方之间也是如此。</td></tr></tbody></table><figcaption><strong>Registered Claim</strong></figcaption></figure>
<!-- /wp:table -->

<!-- wp:paragraph -->
<p><strong>Public Claim</strong></p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>一般来说，公开声明可以包含任何信息，但不建议在这里添加敏感信息，因为这些信息很容易被解密。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p><strong>Private Claim</strong></p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>私有声明是客户端和服务器端的声明。敏感信息不建议在这里声称。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>一个典型的 PayLoad 如下</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>{
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>进行 Base64 加密后我们得到了 JWT 的第二段 </p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9</code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":4} -->
<h4>Signature 签名</h4>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>Signature 部分对前两部分签名，防止数据篡改。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>Signature  包含三部分</p>
<!-- /wp:paragraph -->

<!-- wp:list -->
<ul><li>header （加密后的）</li><li>payload （加密后的）</li><li>secret</li></ul>
<!-- /wp:list -->

<!-- wp:paragraph -->
<p>JWT 的第三部分是之前的几部分的组合</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>加密后的 header + 加密后的 payload + secret</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>这部分加密后就得到了 JWT 串</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>注意 secret 保存在服务端，不应该被泄露。</p>
<!-- /wp:paragraph -->

<!-- wp:heading {"level":3} -->
<h3>在应用程序中使用 JWT</h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>通常，我们将这些 JWT 信息添加到 HTTP 请求头中</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>fetch('api/user/1', {
  headers: {
    'Authorization': 'Bearer ' + token
  }
})</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>服务器负责分析用于身份验证和授权的 HTTP 头</p>
<!-- /wp:paragraph -->

<!-- wp:heading {"level":3} -->
<h3>安全问题</h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>JWT 协议本身没有安全传输功能，所以他必须依赖安全信道 SSL/TLS，所以推荐方式如下</p>
<!-- /wp:paragraph -->

<!-- wp:list -->
<ul><li>敏感信息不能存储在 jwt 的 payload 部分，因为这部分客户端可以解密。</li><li>一定要保存好私钥，不外泄</li><li>如果可以的话使用 HTTPS 协议</li></ul>
<!-- /wp:list -->

<!-- wp:heading -->
<h2>与 SpringBoot 集成</h2>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>我们想要实现完全的前后端分离，所以不可能使用 session, cookie 方式进行认证，我们使用 JWT。</p>
<!-- /wp:paragraph -->

<!-- wp:heading {"level":3} -->
<h3>程序逻辑</h3>
<!-- /wp:heading -->

<!-- wp:list {"ordered":true} -->
<ol><li>发送 Post 请求到 /login ，如果成功返回 Token 如果失败返回 UnauthorizedException 异常。</li><li> 用户访问每个需要权限的 URL 请求时，必须在头部添加授权字段，如 Authorization: Token。</li><li>后端将会对请求进行验证，否则返回 401</li></ol>
<!-- /wp:list -->

<!-- wp:paragraph -->
<p>我们将使用的安全框架：</p>
<!-- /wp:paragraph -->

<!-- wp:list -->
<ul><li>Apache Shiro</li><li>java-jwt</li></ul>
<!-- /wp:list -->

<!-- wp:paragraph -->
<p>首先添加 Maven 依赖</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>        &lt;dependencies>
       
		&lt;dependency>
			&lt;groupId>org.springframework.boot&lt;/groupId>
			&lt;artifactId>spring-boot-starter&lt;/artifactId>
		&lt;/dependency>
		&lt;dependency>
			&lt;groupId>org.apache.shiro&lt;/groupId>
			&lt;artifactId>shiro-spring-boot-web-starter&lt;/artifactId>
			&lt;version>1.7.1&lt;/version>
		&lt;/dependency>
		&lt;dependency>
			&lt;groupId>com.auth0&lt;/groupId>
			&lt;artifactId>java-jwt&lt;/artifactId>
			&lt;version>3.4.1&lt;/version>
		&lt;/dependency>
		&lt;dependency>
			&lt;groupId>org.projectlombok&lt;/groupId>
			&lt;artifactId>lombok&lt;/artifactId>
			&lt;optional>true&lt;/optional>
		&lt;/dependency>
       &lt;/dependencies></code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":3} -->
<h3>构建模拟数据源</h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>为了专注于如何使用 JWT 而不是 DB，我伪造了一个如下所示的数据源</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>| username | password | role  | permission |
| -------- | -------- | ----- | ---------- |
| smith    | smith123 | user  | view       |
| danny    | danny123 | admin | view,edit  |</code></pre>
<!-- /wp:code -->

<!-- wp:quote -->
<blockquote class="wp-block-quote"><p>通常权限管理模块 RBAC 需要 5 张数据表，分别是用户表、角色表、权限表、用户_角色表、角色_权限表，此处直接伪造了一个关联查询后获取的记录。</p></blockquote>
<!-- /wp:quote -->

<!-- wp:paragraph -->
<p>接下来，构建一个 UserService 来模拟数据库查询，并将结果放入 UserBean 中。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p><strong>UserService.java</strong></p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>@Component
public class UserService {
    public UserBean getUser(String username) {
        // If no such user return null
        if (! DataSource.getData().containsKey(username))
            return null;
        UserBean user = new UserBean();
        Map&lt;String, String> detail = DataSource.getData().get(username);
        user.setUsername(username);
        user.setPassword(detail.get("password"));
        user.setRole(detail.get("role"));
        user.setPermission(detail.get("permission"));
        return user;
    }
}</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p><strong>UserBean.java</strong></p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>@Data
public class UserBean {
    private String username;
    private String password;
    private String role;
    private String permission;
}</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>配置 JWT</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>我们构建了一个简单的 JWT 加密工具，并将用户密码作为加密密码，从而确保令牌即使被窃取也无法破解。另外，我们把用户名放在令牌里，设置5分钟后令牌过期。</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>public class JWTUtil {
    // 5 分钟后过期
    private static final long EXPIRE_TIME = 5*60*1000;
    /**
     * Verify TOKEN
     * @param token 
     * @param secret User password
     * @return 
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username", username)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
    /**
     * Get username from TOKEN
     * @return token contains username information
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
    /**
     * 生成 signature, signature 5 分钟后过期
     * @param username 
     * @param secret 
     * @return Encryted token
     */
    public static String sign(String username, String secret) {
         
  		Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
		Algorithm algorithm = Algorithm.HMAC256(secret);

		return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);
    }
}</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>ResponseBean.java</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>@Data
@AllArgsConstructor
public class ResponseBean {
    private int code;
    private String msg;
    private Object data;
}
</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>自定义异常</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }
    public UnauthorizedException() {
        super();
    }
}</code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":3} -->
<h3>URL</h3>
<!-- /wp:heading -->

<!-- wp:table {"className":"is-style-stripes"} -->
<figure class="wp-block-table is-style-stripes"><table><tbody><tr><td>URL</td><td>FUNCTION</td></tr><tr><td>/login</td><td>登录</td></tr><tr><td>/article</td><td>每个人都可以访问，但是不同的角色会得到不同的内容</td></tr><tr><td>/require_auth</td><td>登录用户可访问</td></tr><tr><td>/require_role</td><td>管理员角色可以访问</td></tr><tr><td>/require_permission</td><td> 查看和编辑角色可以访问</td></tr></tbody></table><figcaption>测试用的 URL</figcaption></figure>
<!-- /wp:table -->

<!-- wp:heading {"level":3} -->
<h3>Controller</h3>
<!-- /wp:heading -->

<!-- wp:code -->
<pre class="wp-block-code"><code>@RestController
public class WebController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseBean login(@RequestParam("username") String username,
                              @RequestParam("password") String password) {
        UserBean userBean = userService.getUser(username);
        if (userBean.getPassword().equals(password)) {
            return new ResponseBean(200, "Login success", JWTUtil.sign(username, password));
        } else {
            throw new UnauthorizedException();
        }
    }

    @GetMapping("/article")
    public ResponseBean article() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new ResponseBean(200, "You are already logged in", null);
        } else {
            return new ResponseBean(200, "You are guest", null);
        }
    }

    @GetMapping("/require_auth")
    @RequiresAuthentication
    public ResponseBean requireAuth() {
        return new ResponseBean(200, "You are authenticated", null);
    }

    @GetMapping("/require_role")
    @RequiresRoles("admin")
    public ResponseBean requireRole() {
        return new ResponseBean(200, "You are visiting require_role", null);
    }

    @GetMapping("/require_permission")
    @RequiresPermissions(logical = Logical.AND, value = {"view", "edit"})
    public ResponseBean requirePermission() {
        return new ResponseBean(200, "You are visiting permission require edit,view", null);
    }

    @RequestMapping(path = "/401")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseBean unauthorized() {
        return new ResponseBean(401, "Unauthorized", null);
    }
}
</code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":3} -->
<h3>异常处理</h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>正如前面提到的，RESTFUL 需要统一的样式，所以我们需要处理 Spring Boot 异常。</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>我们可以使用 @RestControllerAdvice 来处理异常</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>@RestControllerAdvice
public class ExceptionController {

    // Catch Shiro Exception
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResponseBean handle401(ShiroException e) {
        return new ResponseBean(401, e.getMessage(), null);
    }

    // Catch UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseBean handle401() {
        return new ResponseBean(401, "Unauthorized", null);
    }

    // Catch Other Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseBean globalException(HttpServletRequest request, Throwable ex) {
        return new ResponseBean(getStatus(request).value(), ex.getMessage(), null);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}</code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":3} -->
<h3>配置 Shiro</h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>JWTToken</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>public class JWTToken implements AuthenticationToken {
    // TOKEN
    private String token;
    public JWTToken(String token) {
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }
    @Override
    public Object getCredentials() {
        return token;
    }
}</code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>Realm</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>@Service
public class MyRealm extends AuthorizingRealm {
    
    @Autowired
    private UserService userService;
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }
   
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = JWTUtil.getUsername(principals.toString());
        UserBean user = userService.getUser(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole(user.getRole());
        Set&lt;String> permission = new HashSet&lt;>(Arrays.asList(user.getPermission().split(",")));
        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }
   
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }
        UserBean userBean = userService.getUser(username);
        if (userBean == null) {
            throw new AuthenticationException("User didn't existed!");
        }
        if (! JWTUtil.verify(token, username, userBean.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}</code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":3} -->
<h3>定义 Filter</h3>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>有的请求都将转发给 Filter，我们扩展 BasicHttpAuthenticationFilter 以覆盖一些方法</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>执行流程:preHandle - > isAccessAllowed - > isLoginAttempt - > executeLogin</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code>public class JWTFilter extends BasicHttpAuthenticationFilter {
    
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }
    
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        JWTToken token = new JWTToken(authorization);
        getSubject(request, response).login(token);
        return true;
    }
   
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (Exception e) {
                response401(request, response);
            }
        }
        return true;
    }
    
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
    /**
     * Illege request foward to /401
     */
    private void response401(ServletRequest req, ServletResponse resp) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
            httpServletResponse.sendRedirect("/401");
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}</code></pre>
<!-- /wp:code -->

<!-- wp:heading {"level":3} -->
<h3>配置 Shiro</h3>
<!-- /wp:heading -->

<!-- wp:code -->
<pre class="wp-block-code"><code>@Configuration
public class ShiroConfig {

    @Bean("securityManager")
    public DefaultWebSecurityManager getManager(MyRealm realm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        
        manager.setRealm(realm);
       
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean factory(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        // define your filter and name it as jwt
        Map&lt;String, Filter> filterMap = new HashMap&lt;>();
        filterMap.put("jwt", new JWTFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        factoryBean.setUnauthorizedUrl("/401");
        /*
         * difine custom URL rule
         * http://shiro.apache.org/web.html#urls-
         */
        Map&lt;String, String> filterRuleMap = new HashMap&lt;>();
        // All the request forword to JWT Filter
        filterRuleMap.put("/**", "jwt");
        // 401 and 404 page does not forward to our filter
        filterRuleMap.put("/401", "anon");
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }
   
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}</code></pre>
<!-- /wp:code -->

<!-- wp:heading -->
<h2>运行</h2>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>通过发送 POST 请求获得令牌</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>在 HTTP 请求 header 中加入 Token 并获取返回结果</p>
<!-- /wp:paragraph -->

<!-- wp:paragraph -->
<p>如果不加入 Token 或使用错误 Token 会出现错误</p>
<!-- /wp:paragraph -->

<!-- wp:heading -->
<h2>源代码</h2>
<!-- /wp:heading -->

<!-- wp:paragraph -->
<p>Git 仓库地址</p>
<!-- /wp:paragraph -->

<!-- wp:code -->
<pre class="wp-block-code"><code></code></pre>
<!-- /wp:code -->

<!-- wp:paragraph -->
<p>Git 仓库页面</p>
<!-- /wp:paragraph -->

<!-- wp:heading -->
<h2>参考资料</h2>
<!-- /wp:heading -->

<!-- wp:list -->
<ul><li><a rel="noreferrer noopener" href="http://www.andrew-programming.com/2019/01/23/springboot-integrate-with-jwt-and-apache-shiro/#How_Could_We_Use_JWT_In_Our_Application" data-type="URL" data-id="http://www.andrew-programming.com/2019/01/23/springboot-integrate-with-jwt-and-apache-shiro/#How_Could_We_Use_JWT_In_Our_Application" target="_blank">SpringBoot Integrate With JWT And Apache Shiro</a></li><li><a href="https://tools.ietf.org/html/rfc7519" data-type="URL" data-id="https://tools.ietf.org/html/rfc7519" target="_blank" rel="noreferrer noopener">IETF JWT</a></li></ul>
<!-- /wp:list -->

<!-- wp:paragraph -->
<p></p>
<!-- /wp:paragraph -->
