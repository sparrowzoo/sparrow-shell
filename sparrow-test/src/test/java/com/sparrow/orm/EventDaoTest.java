package com.sparrow.orm;

import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.orm.dao.impl.EventDAOImpl;
import com.sparrow.orm.po.Event;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.protocol.enums.Platform;
import org.junit.Test;

/**
 * Created by harry on 2018/2/6.
 */
public class EventDaoTest {
    @Test
    public void eventTest() {
        Container container = ApplicationContext.getContainer();
        container.setContextConfigLocation("/dao.xml");
        container.init();
        EventDAOImpl eventDAO = container.getBean("eventDao");
        Event event = new Event();
        event.setEvent("event");
        event.setAppId(11);
        event.setUserId(2L);
        event.setBusinessId(1L);
        event.setBusinessType("t");
        event.setAppVersion(1.1F);
        event.setBssid("bssid");
        event.setChannel("channel");
        event.setClientVersion("client version");
        event.setContent("content");
        event.setCreateTime(System.currentTimeMillis());
        event.setDevice("device id");
        event.setDeviceModel("device-model");
        event.setResumeTime(System.currentTimeMillis());
        event.setPlatform(Platform.PC);
        event.setTimes(1);
        event.setIdfa("idfa");
        event.setImei("imei");
        event.setIp("ip");
        event.setLatitude(1.0D);
        event.setLongitude(2.0d);
        event.setNetwork("network");
        event.setOs("os");
        event.setSimulate(true);
        event.setUpdateTime(System.currentTimeMillis());
        event.setUserType("admin");
        event.setUserAgent("agent");
        event.setDeviceId("device id");
        event.setIp("111");
        event.setWebsite("www.baidu.com");
        event.setSsid("ssid");
        Long id = eventDAO.insert(event);
        event.setUserType("common");
        id = eventDAO.insert(event);

        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setTableSuffix(2L,"admin");
        searchCriteria.setWhere(Criteria.field("event.eventId").equal(id));
        Event event1 = eventDAO.getEntity(searchCriteria);
    }
}
