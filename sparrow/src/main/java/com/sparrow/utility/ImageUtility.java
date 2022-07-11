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

import com.sparrow.cg.MethodAccessor;
import com.sparrow.constant.File;
import com.sparrow.constant.Regex;
import com.sparrow.constant.SparrowError;
import com.sparrow.core.TypeConverter;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.exception.Asserts;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.POJO;

import com.sparrow.protocol.constant.Extension;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class ImageUtility {

    /**
     * JAVA等比缩放函数
     *
     * @param srcImagePath   原图路径
     * @param descImagePath  缩放后路径
     * @param width          缩放后宽度
     * @param height         缩放后高度 -1则只按宽度比例进行缩放
     * @param waterImagePath 水印图位置
     * @param fillWhite      不足是否需要补白
     */
    public static void makeThumbnail(String srcImagePath, String descImagePath,
        int width, int height, String waterImagePath, boolean fillWhite) throws BusinessException {
        // 目标图扩展名
        String extension = FileUtility.getInstance().getFileNameProperty(srcImagePath).getExtension();
        // 如果为gif图则直接保存
        if (Extension.GIF.equalsIgnoreCase(extension)) {
            if (descImagePath.contains(File.SIZE.BIG)) {
                FileUtility.getInstance().copy(srcImagePath, descImagePath);
            }
            return;
        }
        // 目录图所在路径
        String descDirectoryPath = descImagePath.substring(0,
            descImagePath.lastIndexOf('/') + 1);
        // 判断并创建原图路径
        java.io.File descDirectory = new java.io.File(descDirectoryPath);
        if (!descDirectory.exists()) {
            Boolean result = descDirectory.mkdirs();
        }
        // 目标图文件对象
        Asserts.isTrue(srcImagePath.equals(descImagePath), SparrowError.UPLOAD_SRC_DESC_PATH_REPEAT);
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = new FileOutputStream(new java.io.File(descImagePath));
            inputStream = new FileInputStream(new java.io.File(srcImagePath));
        } catch (FileNotFoundException e) {
            throw new BusinessException(SparrowError.FILE_NOT_FOUND);
        }
        try {
            BufferedImage srcImage = ImageIO.read(inputStream);
            if (srcImage == null) {
                throw new BusinessException(SparrowError.FILE_CAN_NOT_READ);
            }
            makeThumbnail(srcImage, extension, outputStream, width, height, waterImagePath, fillWhite);
        } catch (IOException e) {
            throw new BusinessException(SparrowError.FILE_CAN_NOT_READ);
        }
    }

    public static void makeThumbnail(String srcImagePath, OutputStream descImage,
        int width, int height, String waterImagePath, boolean fillWhite)
        throws IOException {
        BufferedImage srcImage = ImageIO.read(new FileInputStream(new java.io.File(srcImagePath)));
        makeThumbnail(srcImage, FileUtility.getInstance().getFileNameProperty(srcImagePath).getExtension(), descImage,
            width, height, waterImagePath, fillWhite);
    }

    public static void makeThumbnail(InputStream srcImage, String extension, OutputStream descImage,
        int width, int height, String waterImagePath, boolean fillWhite)
        throws IOException {
        makeThumbnail(ImageIO.read(srcImage), extension, descImage, width, height, waterImagePath, fillWhite);
    }

    public static void makeThumbnail(BufferedImage srcImage, String extension, String descImagePath,
        int width, int height, String waterImagePath, boolean fillWhite)
        throws IOException {
        OutputStream outputStream = new FileOutputStream(new java.io.File(descImagePath));
        makeThumbnail(srcImage, extension, outputStream, width, height, waterImagePath, fillWhite);
    }

    public static void makeThumbnail(BufferedImage srcImage, String extension, OutputStream descImage,
        int width, int height, String waterImagePath, boolean fillWhite) throws IOException {
        BufferedImage thumbnailBufferImage = makeThumbnail(srcImage, width, height, waterImagePath, fillWhite);
        //注意文件扩展名 不能有.
        ImageIO.write(thumbnailBufferImage, extension.substring(1), descImage);
        thumbnailBufferImage.flush();
    }

    /**
     * JAVA等比缩放函数
     *
     * @param srcImage       原图路径
     * @param width          缩放后宽度
     * @param height         缩放后高度 -1则只按宽度比例进行缩放
     * @param waterImagePath 水印图位置
     * @param fillWhite      不足是否需要补白
     */
    public static BufferedImage makeThumbnail(BufferedImage srcImage,
        int width, int height, String waterImagePath, boolean fillWhite)
        throws IOException {
        // 目标图宽度
        int descWidth = width;
        // 目标图高度
        int descHeight = height;
        // 缩放比例
        double scale = 1;
        // 剪切后的目标图
        BufferedImage descImageBuffer;
        // 是否需要缩放
        boolean isThumbnail = true;
        // 剪切x轴位置
        int x = 0;
        // 剪切Y轴位置
        int y = 0;
        // 原图的宽度
        int srcWidth = srcImage.getWidth();
        // 原图的高度
        int srcHeight = srcImage.getHeight();
        // 如果两边都小于目标边则不进行缩放
        if (srcWidth < width && srcHeight < height) {
            isThumbnail = false;
            // 如果需要补白则宽高与目标宽高一致。需要剪切
            if (fillWhite) {
                x = (width - srcWidth) / 2;
                y = (height - srcHeight) / 2;
            } else {
                // 不需要补白按原图处理
                descWidth = srcWidth;
                descHeight = srcHeight;
            }
        }
        // 如果宽度不足且高>目标高且要求截高则不进行缩放直接截高
        else if (srcWidth < width && srcHeight > height) {
            isThumbnail = false;
            // 如果不限高则按原图处理
            if (height == -1) {
                descWidth = srcWidth;
                descHeight = srcHeight;
            } else {
                // 如果需要补白
                if (fillWhite) {
                    x = (width - srcWidth) / 2;
                } else {
                    descWidth = srcWidth;
                }
                descHeight = height;
                y = (height - srcHeight) / 2;
            }
        }
        // 如果原图高度不足且宽>目标图且要求截宽则不进行缩放直接截宽
        else if (srcHeight < height && srcWidth > width) {
            isThumbnail = false;
            // 如果宽不限制则按原图处理
            if (width == -1) {
                descWidth = srcWidth;
                descHeight = srcHeight;
            } else {
                descWidth = width;
                // 如果需要补白
                if (fillWhite) {
                    y = (height - srcHeight) / 2;
                } else {
                    descHeight = srcHeight;
                }
                x = (width - srcWidth) / 2;
            }
        }
        // 原图的宽>目标图的宽且原图的高>目标图的高
        else {
            // 宽比例
            double widthScale = (double) width / srcWidth;
            // 高比例
            double heightScale = (double) height / srcImage.getHeight();
            // 原图比例
            double srcScale = (double) srcImage.getHeight()
                / srcImage.getWidth();
            // 目标图比例
            double descScale = (double) height / width;
            // 原图的高宽比例小说明:当高缩放成一致时，宽相对较长,此时将多余的宽切掉
            if (srcScale < descScale) {
                scale = heightScale;
                // 缩放后的源图高度
                srcHeight = height;
                // 缩放后的源图宽度
                srcWidth = (int) (srcWidth * scale);
                if (width != -1) {
                    x = (width - srcWidth) / 2;
                } else {
                    descWidth = srcWidth;
                }
            }
            // 原图的高/宽大说明:当宽度缩放成一致时，高相对较长,此时将多余的高切掉
            else {
                scale = widthScale;
                // 缩放后的源图宽度
                srcWidth = width;
                // 缩放后的源图高度
                srcHeight = (int) (srcHeight * scale);
                if (height != -1) {
                    y = (height - srcHeight) / 2;
                } else {
                    descHeight = srcHeight;
                }
            }
        }
        // 图片类型
        int imageType = srcImage.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_RGB
            : srcImage.getType();

        if (isThumbnail) {
            // 临时图片流
            BufferedImage tempImage = srcImage;
            // 缩放后的源图对象
            srcImage = new BufferedImage(srcWidth, srcHeight, imageType);
            srcImage.getGraphics()
                .drawImage(tempImage.getScaledInstance(srcWidth, srcHeight, Image.SCALE_SMOOTH), 0, 0, null);
        }
        // 剪切后的目标图对象
        descImageBuffer = new BufferedImage(descWidth, descHeight, imageType);
        // 剪切后的目标图画板
        Graphics2D descGraphic = descImageBuffer.createGraphics();
        // 设置目标图画板前景色
        descGraphic.setColor(Color.LIGHT_GRAY);
        // 按目标宽高添充背景
        descGraphic.fillRect(0, 0, descWidth, descHeight);
        descGraphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        // 将源图画剪切后画在画板上
        descGraphic.drawImage(srcImage, x, y, null);
        // 如果图片宽度>300且水印图片不为null则加水印
        if (!StringUtility.isNullOrEmpty(waterImagePath)
            && descImageBuffer.getWidth() >= 300
            && descImageBuffer.getHeight() >= 200) {
            java.io.File waterFile = new java.io.File(waterImagePath);
            if (waterFile.exists()) {
                InputStream water = new FileInputStream(waterFile);
                BufferedImage waterImage = ImageIO.read(water);
                x = descWidth - waterImage.getWidth() - 15;
                y = descHeight - waterImage.getHeight() - 15;
                descGraphic.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_ATOP, 1f));
                descGraphic.drawImage(waterImage, x, y, null);
            }
        }
        descGraphic.dispose();
        return descImageBuffer;
    }

    /*
    public static void encodeImage(BufferedImage bufferedImage, File imageFile) throws IOException {
        FileOutputStream out = new FileOutputStream(imageFile);
        // Encodes image as a JPEG data stream
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam param = encoder
                .getDefaultJPEGEncodeParam(bufferedImage);
        param.setQuality(1.0f, true);
        encoder.setJPEGEncodeParam(param);
        encoder.encode(bufferedImage);
    }
 */

    /**
     * 图版剪切
     */
    public static void saveSubImage(String imagePath, Rectangle subImageBounds,
        String subImageFilePath) throws IOException {
        BufferedImage subImage = getSubImage(imagePath, subImageBounds);
        String extension = FileUtility.getInstance().getFileNameProperty(subImageFilePath).getExtensionWithoutDot();
        ImageIO.write(subImage, extension, new FileOutputStream(new java.io.File(subImageFilePath)));
        subImage.flush();
        subImage.flush();
    }

    public static BufferedImage getSubImage(String imagePath, Rectangle subImageBounds) throws IOException {
        java.io.File file = new java.io.File(imagePath);
        InputStream in = new FileInputStream(file);
        BufferedImage image = ImageIO.read(in);
        if (subImageBounds.x < 0 || subImageBounds.y < 0
            || subImageBounds.width - subImageBounds.x > image.getWidth()
            || subImageBounds.height - subImageBounds.y > image.getHeight()) {
            return image;
        }
        BufferedImage subImage = image.getSubimage(subImageBounds.x,
            subImageBounds.y, subImageBounds.width, subImageBounds.height);

        image.flush();
        return subImage;
    }

    public static BufferedImage reduceSize(String srcImageFileName) {

        // 源图文件对象
        java.io.File srcFile = new java.io.File(srcImageFileName);
        // 原图缓存区
        BufferedImage srcImage = null;
        try {
            srcImage = ImageIO.read(new FileInputStream(srcFile));
        } catch (Exception e) {
            return null;
        }
        // 临时图片流
        BufferedImage tempImage = srcImage;

        int imageType = srcImage.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_RGB
            : srcImage.getType();
        double scale = 8d / srcImage.getWidth();
        // 缩放后的源图对象
        srcImage = new BufferedImage(8, 8, imageType);
        // 缩放后的源图画板
        Graphics2D thumbnailSrcGraphics = srcImage.createGraphics();
        thumbnailSrcGraphics.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        // 将原图缩放并画在画板上
        thumbnailSrcGraphics.drawRenderedImage(tempImage,
            AffineTransform.getScaleInstance(scale, scale));
        thumbnailSrcGraphics.dispose();
        // try { ImageIO.write(srcImage, "jpg", new File("e:\\test.jpg")); }
        // catch (IOException e) { }
        return srcImage;
    }

    /**
     * 百度搜索源图
     */
    public static String searchImageWithBaidu(String imageUrl, int n) {
        String result = HttpClient
            .get(String
                .format("http://stu.baidu.com/i?objurl=%1$s&filename=&rt=1&rn=%2$s&ftn=indexstu&ct=1&stt=0&tn=baiduimage",
                    JSUtility.encodeURIComponent(imageUrl), n));
        Matcher imageMatcher = Regex.BAIDU_IMAGE_SEARCH.matcher(
            result);
        if (imageMatcher.find()) {
            return imageMatcher.group(1);
        }
        return null;
    }

    /**
     * 获取当前实体中的数据的ID列表
     */
    public static java.util.List<Long> getImageList(POJO entity) {
        java.util.List<Long> imageIdList = new ArrayList<Long>();
        java.util.List<TypeConverter> fieldList = ApplicationContext.getContainer().getFieldList(entity.getClass());
        MethodAccessor methodAccessor = ApplicationContext.getContainer().getProxyBean(entity.getClass());
        for (TypeConverter type : fieldList) {
            Object value = methodAccessor.get(entity, type.getName());
            if (!(value instanceof String)) {
                continue;
            }
            List<List<String>> innerImageGroupList = RegexUtility
                .multiGroups(value.toString(), Regex.URL_INNER_IMAGE.pattern());
            if (innerImageGroupList.size() == 0) {
                continue;
            }
            for (List<String> innerImageGroup : innerImageGroupList) {
                imageIdList.add(Long
                    .valueOf(FileUtility.getInstance().getFileNameProperty(innerImageGroup.get(0)).getName()));
            }
        }
        return imageIdList;
    }
}
