package com.sparrow.orm.dao;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.orm.Parameter;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.sql.CriteriaProcessor;
import com.sparrow.orm.query.sql.OperationEntity;
import com.sparrow.orm.query.sql.impl.criteria.processor.SqlCriteriaProcessorImpl;

import java.util.List;

/**
 * @author: zhanglizhi01@meicai.cn
 * @date: 2019/6/13 15:34
 * @description:
 */
public class CriteriaTest {
    public static void main(String[] args) {
        Container container = ApplicationContext.getContainer();
        container.init(new ContainerBuilder());

        CriteriaProcessor criteriaProcessor=SqlCriteriaProcessorImpl.getInstance();
        BooleanCriteria b = BooleanCriteria.criteria
                (
                        //code.uuid>null and code.code<=3
                        BooleanCriteria.criteria(Criteria.field("user.userId").greaterThan(0)).
                                and(Criteria.field("user.userName").greaterThanEqual(3L)).
                                and(Criteria.alias("user.userName").notContains("zhangsan"))
                ).or(//or
                //code.value in () and code.sort like 2% or code.remark is null
                BooleanCriteria.criteria(Criteria.field("user.cent").in(new Integer[]{1,2,3})).
                        and(Criteria.field("user.userName").startWith("2")).or(Criteria.field("user.password").isNull())
                ).and(BooleanCriteria.criteria(Criteria.field("user.cent").notIn(new Integer[]{1,2,3})).or(Criteria.field("user.password").mod(10, 2)));

        OperationEntity operationEntity = criteriaProcessor.where(b);
        String sql = operationEntity.getClause().toString();
        List<Parameter> parameters = operationEntity.getParameterList();
        System.out.println("hello" + sql);
    }
}
