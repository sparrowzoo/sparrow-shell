package com.sparrow.log;

import java.util.logging.Logger;

public class JDKLog {
    static Logger log = Logger.getLogger("lavasoft");

    public static void main(String[] args) {
        log.fine("OK");
    }
}
