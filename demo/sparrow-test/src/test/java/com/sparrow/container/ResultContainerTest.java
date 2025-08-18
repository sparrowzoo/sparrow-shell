package com.sparrow.container;

import com.sparrow.container.impl.SparrowContainer;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;
import com.sparrow.protocol.constant.SparrowError;
import com.sparrow.support.Initializer;

/**
 * @author by harry
 */
public class ResultContainerTest {

    public static void main(String[] args) throws Exception {
        Container container = new SparrowContainer();
        ContainerBuilder builder = new ContainerBuilder().contextConfigLocation("/sparrow_application_context.xml");
        container.init(builder);


        Initializer initializer = container.getBean("initializer");
        System.out.println(initializer);

        System.out.println(Result.success().isSuccess());
        System.out.println(Result.success("操作成功").isSuccess());
        System.out.println(new Result<>(true).isSuccess());
        System.out.println(new Result<Boolean>(true,"success").getMessage());

        System.out.println(Result.fail().getMessage());
        System.out.println(Result.fail(SparrowError.GLOBAL_DB_ADD_ERROR).getMessage());
        System.out.println(Result.fail(new BusinessException(SparrowError.GLOBAL_SMS_SEND_ERROR)).getMessage());
    }
}
