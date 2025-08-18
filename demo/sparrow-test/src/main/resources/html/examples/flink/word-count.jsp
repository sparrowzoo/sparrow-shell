<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://www.sparrowzoo.com/ui" prefix="j" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html style="height: 100%">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sparrow Demo &ndash; Word Count</title>
    <jsp:include page="/template/examples/head.jsp"/>
    <script type="text/javascript">
        var metricChart;
        require(['sparrow', 'domReady', 'echarts'], function ($, dom, echarts) {
            metricChart = $.metricChart;
            var container = document.getElementById("container");

            window.setInterval(function () {
                $.ajax.json('${root_path}/flink/word-count.json', "",
                    function (result) {
                        new metricChart(container, "wordCount", result.data).init(echarts);
                        metricChart.changeType("wordCount", "bar");
                    }
                )
            }, 1000);
        });
    </script>
</head>
<body style="height: 100%; margin: 0">
<input type="button" value="line" onclick="metricChart.changeType('wordCount', 'line')"/>
<input type="button" value="bar" onclick="metricChart.changeType('wordCount', 'bar')"/>
<div id="container" style="height:100%"></div>
</body>
</html>