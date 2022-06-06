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

package com.sparrow.jdk.io;

import com.sparrow.utility.FileUtility;
import com.sparrow.utility.ImageUtility;
import com.sparrow.utility.StringUtility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * ImageTest
 *
 * @author harry
 */
public class ImageTest {
    public static void main(String[] args) {

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        try {
            Font font = new Font("新宋体", Font.PLAIN, 12);
            graphics.setFont(font);
            graphics.fillRect(0, 0, 100, 200);
            graphics.setColor(new Color(0, 0, 0));//设置黑色字体,同样可以graphics.setColor(Color.black);
            graphics.drawString("产品：深圳雅辉呼叫器", 0, 10);
            graphics.drawString("网址:www.szsyhaf.com", 0, 36);
            BufferedImage srcImage = ImageIO.read(new FileInputStream("d:\\image.png"));
            graphics.drawImage(srcImage, 0, 0, null);
            ImageIO.write(image, "PNG", new FileOutputStream(new File("D:\\bbbbbb.png")));//生成图片方法一
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            ImageUtility.makeThumbnail("/Users/harry/Documents/test.jpg",
                new FileOutputStream("/Users/harry/Documents/test_back.jpg"), 1280, 768, "", false);
            ImageUtility.makeThumbnail("/D:/image.png",
                new FileOutputStream("d:\\999999.png"), 1280, 768, "", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makeThumbnail(String srcImagePath, OutputStream descImage,
        int width, int height, String waterImagePath, boolean fillWhite)
        throws IOException {

        InputStream inputStream = new FileInputStream(new File(srcImagePath));
        // 原图缓存区
        BufferedImage srcImage = ImageIO.read(inputStream);
        // 剪切后的目标图
        BufferedImage descImageBuffer;
        // 剪切后的目标图对象
        descImageBuffer = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        // 剪切后的目标图画板
        Graphics2D descGraphic = descImageBuffer.createGraphics();
        // 设置目标图画板前景色
        descGraphic.setColor(Color.LIGHT_GRAY);
        // 按目标宽高添充背景
        descGraphic.fillRect(0, 0, 100, 200);
        descGraphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        descGraphic.drawString("hello", 0, 0);
        // 将源图画剪切后画在画板上
        descGraphic.drawImage(srcImage, 0, 0, null);

        ImageIO.write(descImageBuffer, "png", descImage);
        descGraphic.dispose();
        descImage.flush();
        descImage.close();
    }
}
