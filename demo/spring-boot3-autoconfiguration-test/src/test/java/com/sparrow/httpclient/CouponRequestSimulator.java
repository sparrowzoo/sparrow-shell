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

package com.sparrow.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class CouponRequestSimulator {
    public static void main(String[] args) {
        try {
            // 创建信任所有证书的SSLContext（仅用于测试）
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true)
                    .build();

            BasicCookieStore cookieStore = new BasicCookieStore();
            BasicClientCookie cookie = new BasicClientCookie("name", "value");
            cookie.setDomain(".example.com");
            cookie.setPath("/");
            cookieStore.addCookie(cookie);

            // 创建HttpClient实例
            CloseableHttpClient httpClient = HttpClients.custom()
//                    .setDefaultCookieStore(cookieStore)
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .build();

            // 创建GET请求
            HttpGet request = new HttpGet("https://www.coupang.com/");
            request = new HttpGet("https://www.coupang.com/n-api/srp/jikgu-promotion?q=%EB%B9%85%EC%82%AC%EC%9D%B4%EC%A6%88+%EB%B8%8C%EB%9D%BC");
            // 设置浏览器常用请求头
            request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            request.setHeader("Accept-Language", "en-US,en;q=0.5");
            request.setHeader("Accept-Encoding", "gzip, deflate, br");
            request.setHeader("Connection", "keep-alive");
            request.setHeader("Upgrade-Insecure-Requests", "1");
            request.setHeader("Cookie", "x-coupang-target-market=KR; x-coupang-accept-language=ko-KR; PCID=17560178432874423959011; bm_mi=83A289FD9D552FF9C206C5C60B643C21~YAAQDVHMF4wWisWYAQAAhfHR2hxYoKHd1oU8UqeI1MUCKRPE7+MtAjKAZG3DnQQ9TNGhQ3f9+4WFxS7pnBMpFcByK8xgqnDUdem3b0BTBLcgvwlhzNE3G0ExDI9IpgbC5O4NdmNof9+uR1BeU/MCB2Jek+W1JVKDNLBf5ekaRpvkX48CNbTaVP8f9HAt8AzdhbWceyqqf5XJyo+p38iLG1YXW7Nx94SRWMgCnHqOG6voI6BV9LHEib+t7SYvuiXda82oT23HqCK4uYdEomE007jD2maTS33705kAuwiBdFY9/u1AeZBtlW2MtJrGRov81/B3EssWg6rpzhDH5DYSvbUYxQDH~1; ak_bmsc=0E945A7A055B9861044F1CBC7E574317~000000000000000000000000000000~YAAQDVHMF/IYisWYAQAAwfXR2hyKmGQRaxn4B9akvfngbzprBRF+KYgiMmiz8U2VBBNuG4d70NCUfwiCnwcskFE2P/JSKTlW6YCp+tXC6743hxtqvZM9rDmjwxXLZkCYlK57UFmpc0JijewowlnXbRB86ZsJuMvprkahpoZXMgl6XWDCDNoyGfufqCBRpjup3P25mZAROReLWM7GoHCh4vDoDDkvfXNmBZFkSiFDHPifWLkB2qKOIet2aOwG6GUecraIhBgROaKNNvByQDSHI1aq8vFVYoQoNJO5s8j7IzFaYCpOnRTV3IYGPe/1C1Ux7PyHldf2fwg+9YYy1Scn3cYWNPRjrRWAfMV1BckVACT2oQjsvf1N0+1RnfL1eIUCDegikPwu1v6FB7lEEQPSlVXxe/FSuobg7jAGXKb1K4V9MP/ZKRDkG2sHGaV5NyVNi0ATnFz4V2Kx+fcdUpYQiMhEFP0Hpcpv35N5/gnjrlDrXipKQAh+VCuB2Q==; sid=d47968841fb5409994ebe1ff84a5b3fb4e954dd3; bm_lso=415B222A59DB1071FD2C406ACB7F80F5A1758CB71ECBC562FE717DE41185C899~YAAQJfAgF9uNz7aYAQAAXdjX2gR7Yn+zLGSZkCNYMlhbDsi6H/fBXaOsfCsYxmTUULRwM2ZnkAdYHpsiv7+uSCwSO8pE9/9ZlAhVGDgm+iyS86isc4SKXmykKXi5uyxRCmqBMStI4GdILIBmcbRw+o0YRyBuMe19hggz00UfFkLWU8HFllV1GnNkSGE7KO7/TubdQbYCt4NGswYBijAWxezqgYJmIRyOj1L9qyhYHnl2o0Zk1jxSP/rUU/WS/MBRbOrnSdFD51ya2Ds4vdHBs5/x6XOX1snrHCe0guilQ71i1CbPoeaGGJLYiqPLkxRwJsY37r8RugRQsUqzoNijpH1Zvhb9fgxAEr7NBIvbhDp2LRTa6fxMTej9WVyWAl2MQNxDwxsPXo3GDWzGl7+s9kImKL8pedUmpCkEHjVCILj9x5PV0B/A5K6JCJ4c58hs2twRkyFBALqCcpTlfzk=^1756018236876; cto_bundle=sLAxxV90Y3p2QXVqSHJjT1lLQU9oaDEzeDF1dWI1dDlidCUyRkplcEladGtDVjAzM0JGd3J0WlBLRm96ODdkenM2c09OZyUyRllWZEFac0tlUWx0MW9vOG51N29SUUJIRzl5bTN3blM2VTh5bnFqV1lFTk5ENlBBeUVXWHNKM0ZsUVZlaWt4SnRjczVvJTJGbGJVQVJGNHRkcSUyRlN0VEtiUSUzRCUzRA; bm_ss=ab8e18ef4e; bm_so=A4BC51404278C43000EE37D64F124D2AD600A8962BC2D2613A9D8AAD2C3DB8BE~YAAQLZZUaNXOPsyYAQAAf/8K2wS3IBXCiLwATpBqJ8us28gUGkThWqswS1eu151esPW6+w1hdsRT0DOlO1aEtC/YmgPez2TuSBWqce9kWS5D7Mw3IlQ6DbHrK+FcjxMOddE3vFcPcIaij7WyZn1zwvTe9sOLL0hZp2sUojRGtVkl89u958P09Kes5kLsfQw1U9raUIOzBJb4KcY5Sci5pd4qbhR/+2CSOfYtjkw0GX6ivSqLc91C4RyuePe87jQHAClYFD5D0gV+x8yqCaRuy+e73PcFoC5K8YtRuN0pqYkKMSRQGt0T6oTR/DSJFzTBYLXl3luwKwmDjRCK7i+qbv+MJBP48Z9hnw2Ue2pE/QRj1GcCr+Ewyqwv47AMjfL7lUdStvrsJygATElpxZGE3d7OtI7eJg5sGyX7Fn1ytTQjjoBvWK42ZpUaT37slXSm1S49CG4VlM3ktlOrFEw=; _abck=6E38D3FE717707A5EA677EE6FDA09FDA~0~YAAQLZZUaOPOPsyYAQAAAAEL2w6tDyw3M/jshxIsQpZp4/ru8wRfOw481eshALE2MS5bgKnikDKh1N4MmdB5H61Qn8uUBgivwJJaTXR8E3yAGaqJNqu8iAFIMXmwutiCCTvADmAXBrC4VXAw8c2hpZGsFtK7wX/jHBo7nn74wSZ5Vz7iMYVTSDotup8koQXLpaQoT33AqtL/E7wgW7vYNMsHYjBxYWNR64+odpWidYHfGPeUFaxfhpvfeHkDLuhB6Bcbiu8EeoC9oLWGEDk3fZsZFFXQAm5VkQCF7+rpTIYrMqjjxhaHxobXUtblTkE4wLKoI1Gev6GRcScprxM5gwjn4iEBYK2sOaAqsLWQa9ALrJL6RoCQruf+2X63iGoaG74UC4bjOcmUkKw7BzTof+609kSkxQCTI7ig40szEjB3rGT9FDJNA1H2Rw9VudQVSoPWo7nN64KkO5KFf3VPbAqJSp1R5tzO5DivOeinc9LpGKIrKq+RIw5Ti/xZU3+k6pF2aBVag5oo/LakW1yBg851KE3Teoos1rtYpmXJ5aEFspWWrrMEIFuycnXlueax04lsSlmk4Xa8woWS0W6HabRUWaurE1YOqi2nIuXjO8PDXF7+dKs4jG9ia/tClCGA9cvVDyhWi2J6Tr7JyKUwtTyAvFPFHS5ECsxPk/yhaBTpbu6J2V5SvcXbKka1+ovU~-1~-1~-1~~; _fbp=fb.1.1756021585604.209480661417485676; __cf_bm=VnAqJbcGEqIeei9bIClFZXmQokOo.bsQulkvtc_r6_s-1756021593-1.0.1.1-sJU.S4Au2eDQew.UxstFYq5IqMA.cvP.Uw3OP1Shw_LcZ65gRdul5r8_M6D7LKTpYXRuJcVg4l5BgWkT1tJI53VXjTi8l8_EFbAhz98sHY0; bm_s=YAAQLZZUaAncPsyYAQAA8RsM2wP7YgKt8WN/Enb/fVo4PNuG5pUipoeW8QgSTK8Z8dFOLlsOGBSlDnQ1pnite8W/1alHCjJuCGu7g9BUDtG+/F4BoINRsiuzKHXc+MUMA1ho34RlWmtv3YH0eCpctUxO8md7ZgdUxLvn+iWq7ZuadFqiwALBnsmKlUAetTfE65Z+8wHC7cxffjiFqhkNHaFPU923tABE8Qurs35AbX+inWLClJLEPSkf1UNRvhdRD1Hjf8YNTeqN59kGmoSrb4T7/2s2PoRYdMas4OU5Wf4eKTAghqCjNSP3VSE1nhDuxKDOQf9ZxGWwyJAnf2hJ3POG/1bI8ZAdfiNVXLsjYoDkVSBSjCVETnmBR/OpFYNmpxpknfUDaNCR9ly/70NRIttn/GTX9EF0WiTWd8g4aJJ7RJVTp6SdWxhG4w2J8BdnGS4weDkEc4tOsdsVz13mLxOL77Tv4ND8E+pWg5/SChpafvfSJY3P5aePPZbd04PiDePOSS+EH7pYjo5T1+ppL1zeGo8hbOAh0U7b35rZJCFlal7Bx4opFx4bA80683Ffke4=; bm_sv=E0DCDF5B88655B40215A5AFB779C153B~YAAQLZZUaArcPsyYAQAA8RsM2xwEkTs3uQX3a0vAPhabR7Llz0SctfYdWPd4Y1tglc6bqMtsHKTvgDjjY+9ReJp4xx2bEyscb/78lrgLzNd+UMj/s51GSsCOJJSFvGYbUBsZRQUhnbyJf55vJUwkF+O7Psk3cbo5UO4VoOxVVRBRohGfBjHL7NV+8YUOxxPDFvEBykxa8r7so+7kKla+iK3oRhSU5pcANXXzHj4/+0n9RVHzfL+Y73Aeegb+NS/qWJk=~1; bm_sz=790E36659D3B2DF663682B9296965636~YAAQLZZUaAvcPsyYAQAA8RsM2xz6D7dAuZccYmL977naBVI5h47bMiFbEBves9uU7fkB75HR9urMrsWraPqpKjw6fQWvtgFc8nFhvkIcm0KjcVmr+6LPADjs7q9CtfdQxXxsJHXKjluzNoMlg7+HxOAXKRnMd0Yxc+UUXqL6TrWYfNZnKXaOiufLM3Z4B2pvPsJZWYblsqNIECjPkh8BsJSIui41gjk4F37uRIQXo+N9p6P/J2+Ag6FDjfTfyVUNpUZWq28t8EDOsvj1QkahaHyWGnk2xuXZLJUSFjxiJQx5SG0+4wwd+dJFZ2zd2OmZ39IegGvdU3DxosCAr488zqlmKX1r2QEq4MuF9bCiM8YrhLs3ZmytXRoqqqpyCi0t4Qe6Dg5VCDcZJzXLjP0ld2J+N8BM4hxll/WAHsvmBbjoB9dVelg8IpK2RHBDiYApfTICFEMZ5m1ZbYercqslTTvzCwQCy8/Vu5YZBuEAPuQvM2ka3s6ToOGgPH/OzLsIG+uR0YyeG6Tg4wIJy0tb~4408624~3747896");


            // 执行请求
            CloseableHttpResponse response = httpClient.execute(request);

            // 输出响应内容
            System.out.println(EntityUtils.toString(response.getEntity()));

            // 关闭资源
            response.close();
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
