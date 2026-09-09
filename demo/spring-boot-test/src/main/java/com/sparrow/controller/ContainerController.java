package com.sparrow.controller;

import com.google.common.collect.Lists;
import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.spring.container.SpringContext;
import com.sparrow.spring.container.SpringServletContainer;
import jakarta.inject.Inject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContainerController {

    @Inject
    private SpringServletContainer springServletContainer;

    @RequestMapping("api/container")
    public List<String> container() {
        Container container =
                ApplicationContext.getContainer();
        return Lists.newArrayList(container.getClass().getName(), springServletContainer.getClass().getName(), SpringContext.getContext().getClass().getName());
    }
}
