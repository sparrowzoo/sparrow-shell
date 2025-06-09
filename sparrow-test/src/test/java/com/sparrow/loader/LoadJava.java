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

package com.sparrow.loader;

import com.sparrow.cg.impl.DynamicCompiler;
import com.sparrow.jdk.hash.Obj;

import java.net.URISyntaxException;

public class LoadJava {
    public static void main(String[] args) throws URISyntaxException, IllegalAccessException, InstantiationException {
        String sourceCode = "package com.sparrow.coding.protocol;\n" +
                "\n" +
                "import com.sparrow.protocol.FieldOrder;\n" +
                "import com.sparrow.protocol.dao.PO;\n" +
                "import lombok.Data;\n" +
                "import javax.persistence.*;\n" +
                "\n" +
                "@Data\n" +
                "@Table(name = \"t_table_config\")\n" +
                "public class TableDef extends PO {\n" +
                "    @Id\n" +
                "    @GeneratedValue(strategy = GenerationType.IDENTITY)\n" +
                "    @Column(name = \"id\", columnDefinition = \"int\")\n" +
                "    @FieldOrder(order = 0)\n" +
                "    private Long id;\n" +
                "    @Column(name = \"primary_key\",updatable = false,nullable = false,columnDefinition = \"varchar(32) default '' comment '主键'\")\n" +
                "    private String primaryKey;\n" +
                "    @Column(name = \"table_name\", updatable = false, nullable = false, unique = true, columnDefinition = \"varchar(32)  default '' comment '表名'\")\n" +
                "    private String tableName;\n" +
                "    @Column(name = \"class_name\", updatable = false, nullable = false, unique = true, columnDefinition = \"varchar(32)  default '' comment '类名'\")\n" +
                "    private String className;\n" +
                "    @Column(name = \"description\", nullable = false, columnDefinition = \"varchar(255)  default '' comment '描述'\")\n" +
                "    private String description;\n" +
                "    @Column(name = \"checkable\", nullable = false, columnDefinition = \"tinyint(1)  default 0 comment '是否可勾选 0-不可 1-可'\")\n" +
                "    private Boolean checkable;\n" +
                "    @Column(name = \"row_menu\", nullable = false, columnDefinition = \"tinyint(1)  default 0 comment '是否显示行操作 0-不显示 1-显示'\")\n" +
                "    private Boolean rowMenu;\n" +
                "    @Column(name = \"column_filter\", nullable = false, columnDefinition = \"tinyint(1)  default 0 comment '是否显示列过滤器 0-不显示 1-显示'\")\n" +
                "    private Boolean columnFilter;\n" +
                "    @Column(name = \"status_command\", nullable = false, columnDefinition = \"tinyint(1)  default 0 comment '是否显示状态命令 0-不显示 1-显示'\")\n" +
                "    private Boolean statusCommand;\n" +
                "    @Column(name = \"column_configs\", columnDefinition = \"text  default '' comment '列配置'\")\n" +
                "    private String columnConfigs;\n" +
                "}\n";
        Object obj = DynamicCompiler
                .getInstance().sourceToObject(
                        "com.sparrow.coding.protocol" + ".TableDef",
                        sourceCode);
        System.out.println(obj);
    }
}
