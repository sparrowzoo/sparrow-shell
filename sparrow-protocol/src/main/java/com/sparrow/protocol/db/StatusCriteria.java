package com.sparrow.protocol.db;

import com.sparrow.protocol.enums.STATUS_RECORD;

public class StatusCriteria {
    public StatusCriteria(String ids, STATUS_RECORD status) {
        this.ids = ids;
        this.status = status;
    }

    private String ids;
    private STATUS_RECORD status;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public STATUS_RECORD getStatus() {
        return status;
    }

    public void setStatus(STATUS_RECORD status) {
        this.status = status;
    }
}
