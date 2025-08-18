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

package com.sparrow.facade.utility;

import com.sparrow.cryptogram.Hmac;

/**
 * Created by harry on 2015/5/14.
 */
public class Base64Test {
    public static void main(String[] args) {
        //System.out.println(MessageSignature.getInstance().sha1("jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&noncestr=Wm3WZYTPz0wzccnW&timestamp=1414587457&url=http://mp.weixin.qq.com?params=value"));
        System.out.println(Hmac.getInstance().getSHA1Base64("sha1",
                "9484355851B24df59C2FED38851D052D"));

        System.out.println(Hmac.getInstance().getSHA512Base64("sha1",
                "9484355851B24df59C2FED38851D052D"));

        System.out.println(Hmac.getInstance().getMD5Base64("sha1",
                "9484355851B24df59C2FED38851D052D"));

        System.out.println(Hmac.getInstance().getSHA512Base64("sha1",
                "9484355851B24df59C2FED38851D052D"));

        //System.out.println(MessageSignature.getInstance().md5("_input_charset=utf-8&body=s&notify_url=http://商户网关地址/create_direct_pay_by_user-JAVA-UTF-8/notify_url.jsp&out_trade_no=afsaf&partner=2088121342755839&payment_type=1&return_url=http://商户网关地址/create_direct_pay_by_user-JAVA-UTF-8/return_url.jsp&seller_email=contact@joinaturelife.com&service=create_direct_pay_by_user&show_url=s&subject=safsafd&total_fee=1zhwhbybzevpb92loz65vf046e0sydodj","utf-8"));

//        String token = "harry|" + System.currentTimeMillis();
//        ThreeDES threeDES = ThreeDES.getInstance();
//        String crypt = threeDES.encryptHex("68FAE5124f5C2ED3851D332F", token, ThreeDES.CIPHER_ALGORITHM_CBC);
//        System.out.println(crypt);
//        String decrypt = threeDES.decryptHex("68FAE5124f5C2ED3851D332F", crypt, ThreeDES.CIPHER_ALGORITHM_CBC);
//        System.out.println(decrypt);
    }
}
