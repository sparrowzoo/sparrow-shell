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
package com.sparrow.facade.monitor;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.monitor.ElapsedTimeMonitor;
import com.sparrow.core.spi.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by harry
 */
public class Monitor {
    private static Logger logger= LoggerFactory.getLogger(Monitor.class);
    public static void main(String[] args) {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder());
        final ElapsedTimeMonitor elapsedTimeMonitor = container.getBean("elapsedTimeMonitor");
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override public void run() {
                    elapsedTimeMonitor.start();
                    try {
                        Thread.sleep(Integer.valueOf(Thread.currentThread().getName()) * 99);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    elapsedTimeMonitor.start();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(elapsedTimeMonitor.toString());
                    elapsedTimeMonitor.elapsed("sleep", "100");
                    System.out.println(elapsedTimeMonitor.toString());
                    elapsedTimeMonitor.elapsed("sleep", "100+99*"+Thread.currentThread().getName());

                    System.out.println(elapsedTimeMonitor.toString());
                }
            });
            thread.setName(i + "");
            thread.start();
        }
    }
}
