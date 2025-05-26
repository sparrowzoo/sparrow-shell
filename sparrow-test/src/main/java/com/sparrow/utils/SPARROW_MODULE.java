package com.sparrow.utils;

import com.sparrow.protocol.ModuleSupport;

public class SPARROW_MODULE {
    public static final ModuleSupport DEMO = new ModuleSupport() {
        @Override
        public String code() {
            return "09";
        }

        @Override
        public String name() {
            return "DEMO";
        }
    };
}
