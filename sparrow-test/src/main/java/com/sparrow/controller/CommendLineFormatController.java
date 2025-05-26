package com.sparrow.controller;

import com.sparrow.mvc.RequestParameters;

public class CommendLineFormatController {
    @RequestParameters("content")
    public String format(String content) {
        try {
            String array[] = content.split("\n");
            StringBuilder builder = new StringBuilder();
            builder.append("<table class=\"pure-table \">");
            for (int i = 0; i < array.length; i++) {
                String line = array[i].trim();
                String[] dataArray = line.split(" +");
                if (i == 0) {
                    builder.append("<thead><tr>");
                    for (String data : dataArray) {
                        builder.append(String.format("<th>%s</th>", data));
                    }
                    builder.append("</tr></thead><tbody>");
                    continue;
                }

                builder.append("<tr>");
                for (String data : dataArray) {
                    builder.append(String.format("<td>%s</td>", data));
                }
                builder.append("</tr>");

            }
            builder.append("</tbody></table>");
            return builder.toString();
        } catch (Throwable e) {
            e.printStackTrace();
            return "service error";
        }
    }
}
