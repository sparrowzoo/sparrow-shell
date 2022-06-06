package com.sparrow.protocol;

/**
 * @author: zh_harry@163.com
 * @date: 2019-04-06 12:20
 * @description:
 */
public class MobileShortMessaging {
    private MobileShortMessaging() {
    }

    /**
     * app key
     */
    private String key;
    /**
     * company name
     * <p>
     * e.g sparrow zoo
     */
    private String companyName;
    /**
     * mobile number
     */
    private String mobile;
    /**
     * template id
     */
    private String templateId;
    /**
     * send content
     * <p>
     * e.g your validate code is 00001 by sparrowzoo
     */
    private String content;
    /**
     * user id
     */
    private String userId;
    /**
     * validate code
     */
    private String validateCode;

    /**
     * sms send time
     */
    private Long sendTime;
    /**
     * business suffix for error msg show
     * <p>
     * message show label
     */
    private String business;

    public String getKey() {
        return key;
    }


    public String getMobile() {
        return mobile;
    }


    public String getTemplateId() {
        return templateId;
    }


    public String getUserId() {
        return userId;
    }


    public String getValidateCode() {
        return validateCode;
    }


    public Long getSendTime() {
        return sendTime;
    }


    public String getCompanyName() {
        return companyName;
    }


    public String getContent() {
        return content;
    }


    public String getBusiness() {
        return business;
    }


    public static class Builder {
        private String key;
        private String companyName;
        private String mobile;
        private String templateId;
        private String content;
        private String userId;
        private String validateCode;
        private Long sendTime;
        private String business;

        public String getKey() {
            return key;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getMobile() {
            return mobile;
        }

        public String getTemplateId() {
            return templateId;
        }

        public String getContent() {
            return content;
        }

        public String getUserId() {
            return userId;
        }

        public String getValidateCode() {
            return validateCode;
        }

        public Long getSendTime() {
            return sendTime;
        }

        public String getBusiness() {
            return business;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder templateId(String templateId) {
            this.templateId = templateId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder validateCode(String validateCode) {
            this.validateCode = validateCode;
            return this;
        }

        public Builder sendTime(Long sendTime) {
            this.sendTime = sendTime;
            return this;
        }

        public Builder business(String business) {
            this.business = business;
            return this;
        }



        public MobileShortMessaging build() {
            MobileShortMessaging msm = new MobileShortMessaging();
            msm.companyName=this.companyName;
            msm.content=this.content;
            msm.key=this.key;
            msm.mobile=this.mobile;
            msm.sendTime=this.sendTime;
            msm.templateId=this.templateId;
            msm.mobile=this.mobile;
            msm.userId=this.userId;
            msm.validateCode=this.validateCode;
            msm.business=this.business;
            return msm;
        }
    }
}
