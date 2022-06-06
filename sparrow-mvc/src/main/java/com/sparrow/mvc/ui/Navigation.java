package com.sparrow.mvc.ui;

import com.sparrow.constant.Config;
import com.sparrow.constant.ConfigKeyLanguage;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.protocol.constant.CONSTANT;
import com.sparrow.protocol.constant.magic.SYMBOL;
import com.sparrow.support.NavigationService;
import com.sparrow.support.web.HttpContext;
import com.sparrow.utility.ConfigUtility;
import com.sparrow.utility.StringUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.jsp.tagext.TagSupport;

public class Navigation extends TagSupport {
    private static Logger logger = LoggerFactory.getLogger(Navigation.class);
    private static final long serialVersionUID = 5386900805630461809L;
    private String id;
    private String current;
    private String top = SYMBOL.ZERO;
    private String index = SYMBOL.EMPTY;
    private String separator = ">>";
    private boolean showIndex = false;
    private boolean manage;

    public boolean isShowIndex() {
        return showIndex;
    }

    public void setShowIndex(boolean showIndex) {
        this.showIndex = showIndex;
    }

    private NavigationService navigationService = ApplicationContext.getContainer().getBean("navigationService");

    public String getCurrent() {
        Object requestForum = null;
        if (this.getId() != null) {
            requestForum = this.pageContext.getRequest()
                    .getAttribute(this.getId() + "." + "current");
        }
        if (requestForum == null) {
            requestForum = HttpContext.getContext().get(this.getId() + "." + "current");
        }
        if (requestForum == null) {
            requestForum = this.pageContext.getRequest()
                    .getAttribute(CONSTANT.REQUEST_ACTION_CURRENT_FORUM);
        }
        if (requestForum != null) {
            this.current = requestForum.toString();
            this.pageContext.getRequest()
                    .setAttribute(CONSTANT.REQUEST_ACTION_CURRENT_FORUM, this.current);
        }

        return this.current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getIndex() {
        if (StringUtility.isNullOrEmpty(this.index)) {
            this.index = ConfigUtility.getValue(Config.ROOT_PATH);
        }
        return this.index;
    }

    @Override
    public int doStartTag() {
        StringBuffer navigation = new StringBuffer();
        navigation.append("<div class=\"navigation\">");
        try {
            String language = (String) this.pageContext.getSession().getAttribute(
                    "language");
            if (language == null) {
                language = ConfigUtility.getValue(Config.LANGUAGE);
            }


            if (this.showIndex) {
                String webSitName = ConfigUtility.getLanguageValue(
                        ConfigKeyLanguage.WEBSITE_NAME, language);
                navigation.append(String.format(
                        "<a target=\"_blank\" href=\"%1$s\">%2$s</a>",
                        this.index, webSitName));
                navigation.append(this.separator);
            }


            navigation.append(navigationService.navigation(this.getTop(),
                    this.getCurrent(), manage, this.separator));
            navigation.append("</div>");
            this.pageContext.getOut().print(navigation.toString());
        } catch (Exception ex) {
            logger.error("start tag", ex);
        }
        return TagSupport.SKIP_BODY;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public boolean isManage() {
        return manage;
    }

    public void setManage(boolean manage) {
        this.manage = manage;
    }
}
