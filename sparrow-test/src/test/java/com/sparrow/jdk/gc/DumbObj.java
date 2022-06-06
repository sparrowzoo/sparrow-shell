package com.sparrow.jdk.gc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 在 jvm 参数里添加-XX:PrintCMSStatistics=1 通过 gc 日志可以看到 cms 回收器在 preclean 阶段执行的操作：
 * <p>
 * -XX:CMSScheduleRemarkEdenPenetration=50 -XX:CMSScheduleRemarkEdenSizeThreshold=2m -Xms101m -Xmn50m -Xmx101m -verbose:gc -XX:MetaspaceSize=1m -XX:+UseConcMarkSweepGC  -XX:+PrintGCCause -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -XX:CMSMaxAbortablePrecleanTime=5000 -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=50
 * G1 -Xmx10m -Xms10m -XX:+UseG1GC -XX:+PrintGCDetails -XX:G1HeapRegionSize=2m -XX:InitiatingHeapOccupancyPercent=10
 */
public class DumbObj {
    //存活对象
    private static List<DumbObj> liveObjs = new ArrayList<>(45);

    public static void main(String[] args) throws InterruptedException {
        //创建新对象触发gc
        while (true) {
            for (int i = 0; i < 44; i++) {
                DumbObj obj=new DumbObj(1,null);
                DumbObj dumb = new DumbObj(1, null);
                if (liveObjs.size() < 45) {
                    liveObjs.add(new DumbObj(1, dumb));
                }
            }
            //等待gc线程工作
            TimeUnit.SECONDS.sleep(50);
        }
    }

    public DumbObj(int sizeM, DumbObj next) {
        this.data = getM(sizeM);
        this.next = next;
    }

    private Byte[] getM(int m) {
        return new Byte[1024 * 1024 * m];
    }

    private DumbObj next;
    private Byte[] data;

    public DumbObj getNext() {
        return next;
    }

    public void setNext(DumbObj next) {
        this.next = next;
    }

    public Byte[] getData() {
        return data;
    }

    public void setData(Byte[] data) {
        this.data = data;
    }
}