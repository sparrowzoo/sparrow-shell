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
package com.sparrow.jdk.chars;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author by harry
 */
public class Chars {
    private Chars chars = new Chars();

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println("我".getBytes("GBK").length);
        System.out.println("我".getBytes(StandardCharsets.UTF_8).length);
    }

//    #include <iostream>
//
//    using namespace std;
//    //方法1
//    void Utf8SubStr(string &name, string convert) {
//        size_t i=0;
//        size_t j=0;
//        while (i<1 && j<name.length()) {
//            unsigned char c = (unsigned char)name[j++];
//            i += ((c & 0xc0) != 0x80);
//        }
//
//        while (j<name.length()) {
//            unsigned char c = (unsigned char)name[j];
//            if ((c & 0xc0) == 0x80) {
//                j++;
//            } else {
//                break;
//            }
//        }
//        name.replace(0, j, convert);
//    }
    //方法2 自己根据utf8格式写的

//    int32_t FirstWordReplace(std::string & src,std::string replace = "*"){
//        // 1. is chinese ?
//        char *c = (char *)src.c_str();
//        int first_word_len = 1;
//        if ((*c & 0x80)==0x80){
//            first_word_len = 1;
//            if ((*c & 0xc0)==0xc0){
//                first_word_len = 2;
//                if ((*c & 0xe0) ==0xe0){
//                    first_word_len = 3;
//                    if ((*c & 0xf0) == 0xf0){
//                        first_word_len = 4;
//                        if ((*c & 0xf8)==0xf8){
//                            first_word_len = 5;
//                            if ((*c & 0xfc)==0xfc){
//                                first_word_len = 6;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        // 2. replace
//        cout<<first_word_len<<endl;
//        src.replace(0, first_word_len, replace);
//    }
//    int main() {
//
//        std::string test("龠编码aa");
//        char * ptr =  (char *)test.c_str();
//        printf("%x,%x,%x,%x,%x,%x,%x,%x\n",ptr[0],ptr[1],ptr[2],ptr[3],ptr[4],ptr[5],ptr[6],ptr[7]);
//        cout<<test.c_str()<<endl;
//
//        FirstWordReplace(test,"*");
//        cout<<test.c_str()<<endl;
//        printf("%x,%x,%x,%x,%x,%x,%x,%x\n",ptr[0],ptr[1],ptr[2],ptr[3],ptr[4],ptr[5],ptr[6],ptr[7]);
//
//        return 0;
//    }
}
