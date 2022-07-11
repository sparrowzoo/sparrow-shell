/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow.constant;

import com.sparrow.protocol.ModuleSupport;

public class SparrowModule {
    public static final ModuleSupport GLOBAL = new ModuleSupport() {
        @Override
        public String code() {
            return "00";
        }

        @Override
        public String name() {
            return "GLOBAL";
        }
    };

    public static final ModuleSupport USER = new ModuleSupport() {
        @Override
        public String code() {
            return "01";
        }

        @Override
        public String name() {
            return "User";
        }
    };

    public static final ModuleSupport FORUM = new ModuleSupport() {
        @Override
        public String code() {
            return "02";
        }

        @Override
        public String name() {
            return "FORUM";
        }
    };

    public static final ModuleSupport GROUP = new ModuleSupport() {
        @Override
        public String code() {
            return "03";
        }

        @Override
        public String name() {
            return "GROUP";
        }
    };

    public static final ModuleSupport PRIVILEGE = new ModuleSupport() {
        @Override
        public String code() {
            return "04";
        }

        @Override
        public String name() {
            return "PRIVILEGE";
        }
    };

    public static final ModuleSupport CODE = new ModuleSupport() {
        @Override
        public String code() {
            return "05";
        }

        @Override
        public String name() {
            return "CODE";
        }
    };

    public static final ModuleSupport CMS = new ModuleSupport() {
        @Override
        public String code() {
            return "06";
        }

        @Override
        public String name() {
            return "CMS";
        }
    };

    public static final ModuleSupport ATTACH = new ModuleSupport() {
        @Override
        public String code() {
            return "07";
        }

        @Override
        public String name() {
            return "ATTACH";
        }
    };

    public static final ModuleSupport ATTENTION = new ModuleSupport() {
        @Override
        public String code() {
            return "08";
        }

        @Override
        public String name() {
            return "ATTENTION";
        }
    };

    public static final ModuleSupport BLOG = new ModuleSupport() {
        @Override
        public String code() {
            return "09";
        }

        @Override
        public String name() {
            return "BLOG";
        }
    };

    public static final ModuleSupport LOCATION = new ModuleSupport() {
        @Override
        public String code() {
            return "10";
        }

        @Override
        public String name() {
            return "LOCATION";
        }
    };

    public static final ModuleSupport TAG = new ModuleSupport() {
        @Override
        public String code() {
            return "11";
        }

        @Override
        public String name() {
            return "TAG";
        }
    };

    public static final ModuleSupport COUNT = new ModuleSupport() {
        @Override
        public String code() {
            return "12";
        }

        @Override
        public String name() {
            return "COUNT";
        }
    };

    public static final ModuleSupport SHOP = new ModuleSupport() {
        @Override
        public String code() {
            return "13";
        }

        @Override
        public String name() {
            return "SHOP";
        }
    };

    public static final ModuleSupport UPLOAD = new ModuleSupport() {
        @Override
        public String code() {
            return "14";
        }

        @Override
        public String name() {
            return "UPLOAD";
        }
    };

    public static final ModuleSupport ACTIVITY = new ModuleSupport() {
        @Override
        public String code() {
            return "15";
        }

        @Override
        public String name() {
            return "ACTIVITY";
        }
    };
}
