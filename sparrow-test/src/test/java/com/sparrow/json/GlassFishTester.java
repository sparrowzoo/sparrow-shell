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

package com.sparrow.json;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.core.spi.JsonFactory;

import javax.json.spi.JsonProvider;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by harry on 2015/5/13.
 */
public class GlassFishTester {
    // volatile since multiple threads may access queue reference
    private static volatile WeakReference<ConcurrentLinkedQueue<char[]>> queue;

    public static void main(String[] args) {
        User user = new User();
        System.out.println(user.hashCode());
        System.out.println(new User().hashCode());

        Container container = ApplicationContext.getContainer();

        container.init(new ContainerBuilder());
        User parent = new User("1", "zhangsan", null, null);
        User lisi = new User("2", "lisi", parent,
                new HashMap<String, String>() {{
                    put("key1", "value1");
                }});


        Json json = JsonFactory.getProvider();
        String jsonString = json.toString(parent);

        long current = System.currentTimeMillis();
        JsonProvider provider = JsonProvider.provider();
        for (int i = 0; i < 10000; i++) {

        //    Json js=JsonFactory.getProvider();
      //      JsonReader jsonReader=  js.parse(jsonString);
    //        System.out.println(jsonReader.readObject().list("userId"));
  //         System.out.println(jsonReader.readObject().list("userId"));

            //new JsonReaderImpl(new StringReader(jsonString), new BufferPoolImpl());
//jsonReader.close();
            //JsonParserImpl parser = new JsonParserImpl(new StringReader(jsonString), new BufferPoolImpl());

            // new StringReader(jsonString);


           // new BufferPoolImpl().take();

            take();
            //StateIterator stateIterator = new StateIterator();

            //JsonReader jsonReader= provider.createReader(new StringReader(jsonString));
           // JsonObject jsonObject= jsonReader.readObject();
           // json.parse(jsonString,User.class);
            //StringUtility.setFirstByteUpperCase("sss");
        }
        System.out.println(System.currentTimeMillis() - current);
    }

    private static ConcurrentLinkedQueue<char[]> getQueue() {
        WeakReference<ConcurrentLinkedQueue<char[]>> q = queue;
        if (q != null) {
            ConcurrentLinkedQueue<char[]> d = q.get();
            if (d != null)
                return d;
        }

        // overwrite the queue
        ConcurrentLinkedQueue<char[]> d = new ConcurrentLinkedQueue<char[]>();
        queue = new WeakReference<ConcurrentLinkedQueue<char[]>>(d);

        return d;
    }

    //todo 问题在空间开避
    public static final char[] take() {
        //char[] t = getQueue().poll();
        //if (t==null)
            return new char[64];
        //return t;
    }
}
