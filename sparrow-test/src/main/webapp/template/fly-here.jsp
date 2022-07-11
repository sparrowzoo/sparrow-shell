<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>hello sparrow mvc</title>
</head>
<body>
<h1>redirect 重定向示例</h1>

<pre>
    public ViewWithModel fly() throws BusinessException {
        return ViewWithModel.redirect("fly-here", new HelloVO("我是从fly.jsp飞过来，你是不是没感觉？"));
    }
</pre>

<h1>返回值展示</h1>
hi \${flash_success_result.hello}
后台的结果用flash_success_result 接收

<h1>结果</h1>
${flash_success_result.hello}
</body>
</html>
