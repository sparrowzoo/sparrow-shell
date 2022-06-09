<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>从url获取参数</title>
</head>
<body>

<h1>后端代码示例</h1>

<div>
    <pre>
    @RequestParameters("threadId,pageIndex")
    public ViewWithModel thread(Long threadId, Integer pageIndex) {
        return ViewWithModel.forward("thread", new HelloVO("服务器的threadId=" + threadId + ",pageIndex=" + pageIndex));
    }
        </pre>
</div>
<h1>前端获取参数</h1>
<div>
    <pre>
hello 为HelloVO的类名,自动将POJO 结尾字符（DTO,VO,BO）去除<br/>
        hi \${hello.hello}
        </pre>
</div>
<h1>
    展示结果
</h1>
hi ${hello.hello}
</body>
</html>
