/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.controller;

import com.sparrow.mvc.RequestParameters;
import com.sparrow.utility.HttpClient;
import com.sparrow.utility.QueryStringParser;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ElasticSearchController {
    //http://www.bejson.com/jsonviewernew/
    private static final String ES_ROOT = "http://192.168.248.188:9200/";

    @RequestParameters("index,type")
    public void mapping(String index, String type, HttpServletResponse response) {
        String mapping = ES_ROOT + String.format("%s/_mapping/%s", index, type);
        String json = HttpClient.get(mapping);
        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestParameters("analyzer,text")
    public void analyze(String analyzer, String text, HttpServletResponse response) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("analyzer", analyzer);
        parameters.put("text", text);
        String analyzeUrl = ES_ROOT + "_analyze?" + QueryStringParser.serial(parameters);
        String json = HttpClient.get(analyzeUrl);
        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestParameters("tokenizer,text")
    public void tokenize(String tokenizer, String text, HttpServletResponse response) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("tokenizer", tokenizer);
        parameters.put("text", text);
        String analyzeUrl = ES_ROOT + "_analyze?" + QueryStringParser.serial(parameters);
        String json = HttpClient.get(analyzeUrl);
        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void info(HttpServletResponse response) {
        String infoUrl = ES_ROOT;
        String info = HttpClient.get(infoUrl);
        try {
            response.getWriter().write(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestParameters("index,type,mapping")
    public void mappingCreate(String index, String type, String mapping, HttpServletResponse response) {
        String mappingUrl = ES_ROOT + String.format("%s/_mapping/%s", index, type);
        String json = HttpClient.putJson(mappingUrl, mapping);
        try {
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
