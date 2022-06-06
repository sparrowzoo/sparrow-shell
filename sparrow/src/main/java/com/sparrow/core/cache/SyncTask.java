package com.sparrow.core.cache;

import java.util.Map;

public interface SyncTask {
    Map<String,Object> sync();
    int getExpire(String key);
}
