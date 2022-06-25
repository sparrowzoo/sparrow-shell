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

package com.sparrow.orm;

import java.util.List;

public class JDBCParameter {
    private boolean autoIncrement;
    private String command;
    private List<Parameter> parameters;
    private boolean readOnly;

    public JDBCParameter(String command) {
        this.command = command;
    }

    public JDBCParameter(String command, List<Parameter> parameters, boolean autoIncrement, boolean readOnly) {
        this.command = command;
        this.parameters = parameters;
        this.autoIncrement = autoIncrement;
        this.readOnly = readOnly;
    }

    public JDBCParameter(String command, List<Parameter> parameters, boolean autoIncrement) {
        this.command = command;
        this.parameters = parameters;
        this.autoIncrement = autoIncrement;
    }

    public JDBCParameter(String command, List<Parameter> parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    public JDBCParameter(String command, Object... args) {
        this.command = command;
        for (Object arg : args) {
            this.parameters.add(new Parameter(arg));
        }
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isReadOnly() {
        return readOnly;
    }
}
