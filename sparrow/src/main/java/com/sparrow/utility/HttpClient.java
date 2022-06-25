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

package com.sparrow.utility;

import com.sparrow.constant.*;
import com.sparrow.constant.File;
import com.sparrow.enums.HttpMethod;

import com.sparrow.protocol.constant.Constant;
import com.sparrow.protocol.constant.Extension;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileLock;
import java.util.Map;
import java.util.UUID;

public class HttpClient {

    static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public static String getRedirectUrl(String pageUrl) {
        try {
            URL url = new URL(pageUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
            connection.connect();
            connection.disconnect();
            return connection.getURL().toString();
        } catch (Exception e) {
            logger.error("http connection error", e);
        }
        return null;
    }

    public static int getResponseCode(String pageUrl) {
        try {
            URL url = new URL(pageUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
            connection.connect();
            connection.disconnect();
            return connection.getResponseCode();
        } catch (Exception e) {
            logger.error("get response code error", e);
        }
        return 500;
    }

    public static String get(String pageUrl) {
        return get(pageUrl, null);
    }

    public static String get(String pageUrl, String charset) {
        if (StringUtility.isNullOrEmpty(charset)) {
            charset = Constant.CHARSET_UTF_8;
        }
        HttpURLConnection connection = null;
        try {
            URL url = new URL(pageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(60 * 1000);
            connection.setReadTimeout(60 * 1000);
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), charset));

            StringBuilder content = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append("\r\n");
            }
            connection.disconnect();
            return content.toString().trim();
        } catch (Exception e) {
            logger.error("get method error", e);
            return null;
        }
    }

    public static String postBody(String pageUrl, String data) {
        return post(HttpMethod.POST, pageUrl, data, "text/html");
    }

    public static String post(String pageUrl, String data) {
        return post(HttpMethod.POST, pageUrl, data, null);
    }

    public static String put(String pageUrl, String data) {
        return post(HttpMethod.PUT, pageUrl, data, null);
    }

    public static String putJson(String pageUrl, String json) {
        return post(HttpMethod.PUT, pageUrl, json, "application/json");
    }

    public static String post(HttpMethod method, String pageUrl, String data,
        String contentType) {
        return post(method, pageUrl, data, contentType, null, false);
    }

    /**
     * @return 2013-8-26下午9:48:02 harry put与post类似，将method改成put即可
     */
    public static String post(HttpMethod method, String pageUrl, String data,
        String contentType, Map<String, String> header, Boolean gzip) {
        StringBuilder buffer = new StringBuilder();
        HttpURLConnection httpUrlConnction = null;
        DataOutputStream dataOutputStream = null;
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(pageUrl);
            httpUrlConnction = (HttpURLConnection) url.openConnection();
            httpUrlConnction.setRequestMethod(method.toString());
            httpUrlConnction.setRequestProperty("Accept-Charset", Constant.CHARSET_UTF_8);

            //对post表单提交有影响
            if (StringUtility.isNullOrEmpty(contentType)) {
                httpUrlConnction.setRequestProperty("Content-Type",
                    Constant.CONTENT_TYPE_FORM);
            } else {
                httpUrlConnction.setRequestProperty("Content-Type",
                    contentType);
            }

            if (header != null) {
                for (String key : header.keySet()) {
                    httpUrlConnction.addRequestProperty(key, header.get(key));
                }
            }

            httpUrlConnction.setDoOutput(true);
            httpUrlConnction.setDoInput(true);
            httpUrlConnction.setConnectTimeout(30000);
            httpUrlConnction.setReadTimeout(30000);
            httpUrlConnction.connect();

            if (gzip) {
                httpUrlConnction.setRequestProperty("Content-Encoding", "gzip");
                ByteArrayOutputStream originalContent = new ByteArrayOutputStream();
                originalContent
                    .write(data.getBytes(Charset.forName(Constant.CHARSET_UTF_8)));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzipOut = new GZIPOutputStream(baos);
                originalContent.writeTo(gzipOut);
                gzipOut.finish();
                httpUrlConnction.getOutputStream().write(baos.toByteArray());
            } else {
                dataOutputStream = new DataOutputStream(
                    httpUrlConnction.getOutputStream());
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(
                    dataOutputStream, Constant.CHARSET_UTF_8));
                if (data != null) {
                    bufferedWriter.write(data);
                }
                bufferedWriter.flush();
                dataOutputStream.flush();
            }

            int responseCode = 0;
            responseCode = httpUrlConnction.getResponseCode();
            if (responseCode == 200) {
                inputStream = httpUrlConnction.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(
                    inputStream, Constant.CHARSET_UTF_8));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line);
                }
            } else {
                buffer.append("POST ERROR|" + responseCode);
                buffer.append(httpUrlConnction.getResponseMessage());
            }
        } catch (Exception e) {
            logger.error("post method error", e);
            throw new RuntimeException(e);
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.flush();
                } catch (IOException ignore) {
                }
            }
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.close();
                } catch (IOException ignore) {
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ignore) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignore) {
                }
            }
            if (httpUrlConnction != null) {
                httpUrlConnction.disconnect();
            }
        }
        return buffer.toString();
    }

    /**
     * 下载二进制文件
     */
    public static boolean downloadFile(String fileUrl, String fileFullPath) {
        URL url;
        FileOutputStream fos = null;
        BufferedInputStream bis = null;
        HttpURLConnection httpURLConnection = null;
        int bufferSize = 1024;
        try {
            byte[] buf = new byte[bufferSize];
            int size = 0;
            // 建立链接
            url = new URL(fileUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 连接指定的资源
            httpURLConnection.connect();
            // 获取网络输入流
            bis = new BufferedInputStream(httpURLConnection.getInputStream());

            String descDirectoryPath = fileFullPath.substring(0,
                fileFullPath.lastIndexOf('/') + 1);
            java.io.File descDirectory = new java.io.File(descDirectoryPath);
            if (!descDirectory.exists()) {
                descDirectory.mkdirs();
            }
            // 建立文件
            fos = new FileOutputStream(fileFullPath);
            // 保存文件
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            return true;

        } catch (Exception e) {
            logger.error("download file error", e);
            throw new RuntimeException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignore) {
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ignore) {

                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public static ByteBuffer download(String fileUrl) {
        URL url;
        BufferedInputStream bis = null;
        HttpURLConnection httpURLConnection = null;
        int bufferSize = 1024;
        try {
            byte[] buf = new byte[bufferSize];
            int size = 0;
            // 建立链接
            url = new URL(fileUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 连接指定的资源
            httpURLConnection.connect();
            // 获取网络输入流
            bis = new BufferedInputStream(httpURLConnection.getInputStream());
            ByteBuffer buffer = ByteBuffer.allocate(bis.available());
            // 保存文件
            while ((size = bis.read(buf)) != -1) {
                buffer.put(buf, 0, size);
            }
            return buffer;
        } catch (Exception e) {
            logger.error("download file error", e);
            throw new RuntimeException(e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ignore) {

                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public static String downloadImage(String url) {
        String fileUUID = UUID.randomUUID().toString();
        String temp = ConfigUtility.getValue(com.sparrow.constant.Config.TEMP);
        String tempPhysicalPath = ConfigUtility.getValue(com.sparrow.constant.Config.RESOURCE_PHYSICAL_PATH);
        int hashcode = fileUUID.hashCode();
        if (hashcode == Integer.MIN_VALUE) {
            hashcode += 1;
        }
        hashcode = Math.abs(hashcode);
        int remaining1 = hashcode % 1000;
        int div = hashcode / 1000;
        int remaining2 = div % 1000;
        String extension = FileUtility.getInstance().getFileNameProperty(url).getExtension();
        if (!Extension.JPG.equalsIgnoreCase(extension) && !Extension.PNG.equalsIgnoreCase(extension)
            && !Extension.GIF.equalsIgnoreCase(extension)) {
            extension = ".jpg";
        }
        String imageUrl = String.valueOf(remaining1) + "/"
            + String.valueOf(remaining2) + "/" + fileUUID + extension;
        String descFilePath = tempPhysicalPath + "/" + imageUrl;
        HttpClient.downloadFile(url, descFilePath);
        String webUrl = temp + "/" + imageUrl;
        return String.format(Regex.TAG_TEMP_IMAGE_FORMAT.pattern(), webUrl,
            url.hashCode());
    }

    /**
     * 下载文本文件
     */
    public static boolean downloadPage(String url, String htmlFileName,
        String charset) {
        try {
            if (StringUtility.isNullOrEmpty(charset)) {
                charset = Constant.CHARSET_UTF_8;
            }
            String html = get(url, charset);
            if (html.trim().startsWith(File.ERROR_STATIC_HTML)) {
                logger.warn(File.ERROR_STATIC_HTML + url);
                return false;
            }
            String descDirectoryPath = htmlFileName.substring(0,
                htmlFileName.lastIndexOf('/') + 1);
            java.io.File descDirectory = new java.io.File(descDirectoryPath);
            if (!descDirectory.exists()) {
                descDirectory.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(htmlFileName, false);
            FileLock fl = fos.getChannel().tryLock();
            OutputStreamWriter os = new OutputStreamWriter(fos, charset);
            BufferedWriter bw = new BufferedWriter(os);
            if (fl != null && fl.isValid()) {
                bw.write(html);
                fl.release();
            }
            bw.flush();
            os.flush();
            fos.flush();
            bw.close();
            fos.close();
            os.close();
            return true;
        } catch (Exception e) {
            logger.error("downloadPage error", e);
            return false;
        }
    }

    public static void clearNginxCache(String url) {
        try {
            String rootPath = ConfigUtility.getValue(com.sparrow.constant.Config.ROOT_PATH);
            if (!StringUtility.isNullOrEmpty(rootPath)) {
                if (!url.startsWith(rootPath)) {
                    url = rootPath + "/purge" + url;
                }
                HttpClient.get(url);
            }
        } catch (RuntimeException ignore) {
        }
    }
}
