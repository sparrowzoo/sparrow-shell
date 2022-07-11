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

package com.sparrow.enums;

public enum ComparisonOperator {

    EQUAL {
        @Override
        public ComparisonOperator negated() {
            return NOT_EQUAL;
        }

        @Override
        public String rendered() {
            return "=";
        }
    },
    NOT_EQUAL {
        @Override
        public ComparisonOperator negated() {
            return EQUAL;
        }

        @Override
        public String rendered() {
            return "<>";
        }
    },
    LESS_THAN {
        @Override
        public ComparisonOperator negated() {
            return GREATER_THAN_OR_EQUAL;
        }

        @Override
        public String rendered() {
            return "<";
        }
    },
    LESS_THAN_OR_EQUAL {
        @Override
        public ComparisonOperator negated() {
            return GREATER_THAN;
        }

        @Override
        public String rendered() {
            return "<=";
        }
    },
    GREATER_THAN {
        @Override
        public ComparisonOperator negated() {
            return LESS_THAN_OR_EQUAL;
        }

        @Override
        public String rendered() {
            return ">";
        }
    },
    GREATER_THAN_OR_EQUAL {
        @Override
        public ComparisonOperator negated() {
            return LESS_THAN;
        }

        @Override
        public String rendered() {
            return ">=";
        }
    },
    IN {
        @Override public ComparisonOperator negated() {
            return NOT_IN;
        }

        @Override public String rendered() {
            return "IN";
        }
    },
    NOT_IN {
        @Override public ComparisonOperator negated() {
            return IN;
        }

        @Override public String rendered() {
            return "NOT IN";
        }
    },
    START_WITH {
        @Override public ComparisonOperator negated() {
            return END_WITH;
        }

        @Override public String rendered() {
            return "LIKE";
        }
    },
    END_WITH {
        @Override public ComparisonOperator negated() {
            return START_WITH;
        }

        @Override public String rendered() {
            return "LIKE";
        }
    },
    CONTAIN {
        @Override public ComparisonOperator negated() {
            return NOT_CONTAIN;
        }

        @Override public String rendered() {
            return "LIKE";
        }
    },
    NOT_CONTAIN {
        @Override public ComparisonOperator negated() {
            return CONTAIN;
        }

        @Override public String rendered() {
            return "NOT LIKE";
        }
    },

    IS_NULL {
        @Override public ComparisonOperator negated() {
            return IS_NOT_NULL;
        }

        @Override public String rendered() {
            return "is null";
        }
    },
    IS_NOT_NULL {
        @Override public ComparisonOperator negated() {
            return IS_NULL;
        }

        @Override public String rendered() {
            return "is not null";
        }
    },
    MOD {
        @Override public ComparisonOperator negated() {
            return null;
        }

        @Override public String rendered() {
            return "%";
        }
    };

    public abstract ComparisonOperator negated();

    public abstract String rendered();
}