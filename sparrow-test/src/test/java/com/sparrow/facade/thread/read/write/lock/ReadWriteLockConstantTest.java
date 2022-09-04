package com.sparrow.facade.thread.read.write.lock;

public class ReadWriteLockConstantTest {
    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(OuterSync.Sync.EXCLUSIVE_MASK));
        System.out.println(Integer.toBinaryString(OuterSync.Sync.SHARED_UNIT));
        System.out.println(Integer.toBinaryString(OuterSync.Sync.MAX_COUNT));
    }
}
