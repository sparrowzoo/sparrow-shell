package com.sparrow.utility;

public final class BlockUtility {
    
    public static void waitingShortTime() {
        sleep(100L);
    }
    
    public static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}