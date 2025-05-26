package com.sparrow.controller;

import com.sparrow.mvc.RequestParameters;
import com.sparrow.utility.StringUtility;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class YamlController {

    private void put(String key, Object v, Map<String, Object> map) {
        String[] keys = key.split("\\.");
        Map<String, Object> currentPair = map;
        for (int i = 0; i < keys.length - 1; i++) {
            String k = keys[i];
            if (currentPair.containsKey(k)) {
                currentPair = (Map<String, Object>) currentPair.get(k);
            } else {
                Map<String, Object> newMap = new LinkedHashMap<String, Object>();
                currentPair.put(k, newMap);
                currentPair = newMap;
            }
        }
        currentPair.put(keys[keys.length - 1], v);
    }

    @RequestParameters("content")
    public String toYaml(String content) throws IOException {
        content = content.replace("%26", "&");
        Properties properties = new Properties();
        properties.load(new StringReader(content));
        Enumeration enumerations = properties.propertyNames();


        Map<String, Object> root = new LinkedHashMap<>();
        while (enumerations.hasMoreElements()) {
            String key = (String) enumerations.nextElement();
            Object value = properties.getProperty(key);
            this.put(key, value, root);
        }
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(dumperOptions);
        return yaml.dump(root);
    }

    @RequestParameters("content")
    public String toProperties(String content) {
        Yaml yaml = new Yaml();
        Map map = yaml.load(new StringReader(content));

        Stack<String> keys = new Stack();

        StringBuilder properties = new StringBuilder();
        this.traverse(map, properties, keys);
        return properties.toString();
    }


    private String getKey(Stack stack) {
        return StringUtility.join(stack, ".");
    }

    private void traverse(Map<String, Object> map, StringBuilder properties, Stack keys) {
        for (String key : map.keySet()) {
            keys.push(key);
            Object value = map.get(key);
            if (value instanceof Map) {
                traverse((Map) value, properties, keys);
            } else {
                properties.append(String.format("%s=%s\n", this.getKey(keys), value));
            }
            keys.pop();
        }
    }
}
