///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.sparrow.facade.bucket;
//
//import com.sparrow.core.algorithm.bucket.Bucket;
//import com.sparrow.core.algorithm.bucket.Overflow;
//import java.util.List;
//
///**
// * @author by harry
// */
//public class BucketTest {
//    public static void main(String[] args) {
//        Bucket<Integer> bucket = new Bucket<Integer>(10, new Overflow<Integer>() {
//            @Override public boolean hook(List<Integer> list) {
//                if (list != null) {
//                    for (Integer item : list) {
//                        System.out.print(item + ",");
//                    }
//                    System.out.println();
//                }
//                return true;
//            }
//        });
//        for (int i = 0; i < 1002; i++) {
//            bucket.synFill(i);
//        }
//        bucket.over();
//        System.out.println(bucket.getCount());
//    }
//}
