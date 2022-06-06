<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>tokenize</title>
    <script type="text/javascript"
            src="http://r.sparrowzoo.net/scripts/sparrow.js"></script>

    <script type="text/javascript">
        function mapping() {
            var mappingInfo = {
                tokenizer: {
                    ctrlId: 'sltTokenizer'
                },
                text: {
                    ctrlId: 'txtText'
                }
            };
            var postString = $.getFormData(mappingInfo);
            $.ajax.req("POST",
                $.url.root + "/elastic/search/tokenize.json",
                function (responseText) {
                    $("#.txtResult").value($.format(responseText));
                }, true, postString);
        }
    </script>
</head>
<body>
https://www.elastic.co/guide/en/elasticsearch/reference/5.6/configuring-analyzers.html
<form method="post" action="/elastic/search/tokenize.json">
    analyzer:<select id="sltTokenizer" name="tokenizer" type="text">
    <option value="standard">standard</option>
    <option value="english">english</option>
    <option value="simple">simple </option>
    <option value="stop">stop </option>
    <option value="keyword">keyword </option>
    <option value="pattern">pattern </option>
    <option value="whitespace">whitespace </option>
    <option value="ik_max_word">Ikanalyzer </option>
</select> <br/>
    text:<textarea id="txtText" rows="5" cols="100" name="text">here is beijing and i love beijing tiananmen
我爱北京天安门，天安门上太阳升</textarea> </br>
    <input type="button" value="submit" onclick="mapping()"/><br/>

    <textarea rows="200" cols="100" id="txtResult">
    </textarea>
</form>
</body>
</html>
