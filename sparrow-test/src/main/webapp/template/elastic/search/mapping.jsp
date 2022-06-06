<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mapping</title>
    <script type="text/javascript"
            src="http://r.sparrowzoo.net/scripts/sparrow.js"></script>

    <script type="text/javascript">
        function mapping() {
            var mappingInfo = {
                index: {
                    ctrlId: 'txtIndex'
                },
                type: {
                    ctrlId: 'txtType'
                }
            };
            var postString = $.getFormData(mappingInfo);
            $.ajax.req("POST",
                $.url.root + "/elastic/search/mapping.json",
                function (responseText) {
                    $("#.txtResult").value($.format(responseText));
                }, true, postString);
        }
    </script>
</head>
<body>
<form method="post">
    index:<input id="txtIndex" name="index" type="text"/> <br/>
    type:<input id="txtType" name="type" type="text"/> </br>
    <input type="button" value="submit" onclick="mapping()"/>
    <br/>
    <textarea rows="200" cols="100" id="txtResult">
    </textarea>
</form>
</body>
</html>
