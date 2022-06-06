package com.sparrow.web;

import com.sparrow.container.Container;
import com.sparrow.support.Initializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author harry
 */
public class WebApplicationInitializer implements Initializer {

    private final Logger logger = LoggerFactory.getLogger(WebApplicationInitializer.class);

    @Override
    public void init(Container container) {
        logger.info("welcome to web application initializer 你可以在这里初始化任何东西，这里的容器已经启动了，神马都准备好了... 我在这 container-->" + container);
    }

    @Override
    public void destroy(Container container) {

    }
}
