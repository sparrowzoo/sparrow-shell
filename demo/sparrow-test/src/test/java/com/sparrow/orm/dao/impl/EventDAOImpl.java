package com.sparrow.orm.dao.impl;

import com.sparrow.orm.dao.EventDAO;
import com.sparrow.orm.po.Event;
import com.sparrow.orm.template.impl.ORMStrategy;
import javax.inject.Named;

@Named
public class EventDAOImpl extends ORMStrategy<Event, Long> implements
        EventDAO {
}
