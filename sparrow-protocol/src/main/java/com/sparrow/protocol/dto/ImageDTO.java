package com.sparrow.protocol.dto;

import com.sparrow.protocol.POJO;

/**
 * @author by harry
 */
public class ImageDTO implements POJO {
    private Boolean inner;
    private String imageId;
    private String remark;
    private String extension;
    private String imageMark;

    public ImageDTO(String imageId, String imageHtml, String extension, String remark, Boolean inner) {
        this.imageId = imageId;
        this.imageMark = imageHtml;
        this.extension=extension;
        this.remark = remark;
        this.inner=inner;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageMark() {
        return imageMark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Boolean getInner() {
        return inner;
    }

    public void setInner(Boolean inner) {
        this.inner = inner;
    }
}
