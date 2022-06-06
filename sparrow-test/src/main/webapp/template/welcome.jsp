<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>welcome to java sparrow framework mvc</title>
</head>
<body>

<a href="/elastic/search/index">elastic search</a>

<h1>com from server ［ ${helloVO.hello}］</h1>

<h1>hi</h1>
welcome to java sparrow framework mvc
你可以直接访问jsp文件 毛配置都不用!!!

<h2>这是最简单的实例 hello.jsp 把服务器的内容读出来</h2>
<a href="hello" target="_blank">hello</a>

<h2>重定向示例，读服务器内容 带flash存储的哟~~~</h2>
<a href="fly" target="_blank">fly</a>

<h2>中转站</h2>
<a href="transit" target="_blank">到中转站歇一会再走</a>
</body>

<h2>URL重写</h2>
<a href="thread-10000-1" target="_blank">看看参数过去没</a>

<h2>拦截器</h2>
有拦截器的，关注一下哟~~~~ 看console

<h2>json</h2>
<a href="json-test.json" target="_blank">走起json</a>

<h2>服务器控件支持</h2>
敬请期待 ....

<h2>国际化支持</h2>
敬请期待 ....
<h2>异常结构化</h2>
敬请期待 ....
</html>
