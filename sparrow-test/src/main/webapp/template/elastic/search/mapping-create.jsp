
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="/elastic/search/mapping-create.json">
    index:<input name="index" type="text"/> <br/>
    type:<input name="type" type="text"/> </br>
    mapping:<textarea name="mapping"></textarea> </br>
    <input type="submit"/>
</form>
</body>
</html>
