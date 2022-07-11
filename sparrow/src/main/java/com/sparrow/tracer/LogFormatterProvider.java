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

package com.sparrow.tracer;

import java.util.Iterator;
import java.util.ServiceLoader;

public class LogFormatterProvider {
    private static volatile LogFormatter logFormatter;

    public static LogFormatter getLogFormatter() {
        if (logFormatter != null) {
            return logFormatter;
        }
        synchronized (LogFormatterProvider.class) {
            if (logFormatter != null) {
                return logFormatter;
            }
            ServiceLoader<LogFormatter> loader = ServiceLoader.load(LogFormatter.class);
            Iterator<LogFormatter> it = loader.iterator();
            if (it.hasNext()) {
                logFormatter = it.next();
                return logFormatter;
            }
            String defaultProvider = "com.sparrow.tracer.impl.MarkdownLogFormatter";
            try {
                Class<?> logProvider = Class.forName(defaultProvider);
                logFormatter = (LogFormatter) logProvider.newInstance();
                return logFormatter;
            } catch (ClassNotFoundException x) {
                throw new RuntimeException(
                    "Provider " + defaultProvider + " not found", x);
            } catch (Exception x) {
                throw new RuntimeException(
                    "Provider " + defaultProvider + " could not be instantiated: " + x,
                    x);
            }
        }
    }
}
