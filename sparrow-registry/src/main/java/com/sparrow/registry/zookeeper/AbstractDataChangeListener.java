package com.sparrow.registry.zookeeper;

import com.sparrow.protocol.constant.CONSTANT;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

import java.nio.charset.Charset;

public abstract class AbstractDataChangeListener implements TreeCacheListener {

    @Override
    public final void childEvent(final CuratorFramework client, final TreeCacheEvent event) throws Exception {
        ChildData childData = event.getData();
        if (null == childData) {
            return;
        }
        String path = childData.getPath();
        if (path.isEmpty()) {
            return;
        }
        dataChanged(path, event.getType(), null == childData.getData() ? "" : new String(childData.getData(), Charset.forName(CONSTANT.CHARSET_UTF_8)));
    }

    protected abstract void dataChanged(final String path, final TreeCacheEvent.Type eventType, final String data);
}
