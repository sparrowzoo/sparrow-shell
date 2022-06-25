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

package com.sparrow.xml;

import com.sparrow.support.EnvironmentSupport;
import com.sparrow.utility.FileUtility;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class ClassesPathDtdResolver implements EntityResolver {

    private static final String DTD_EXTENSION = ".dtd";

    private static Logger logger = LoggerFactory.getLogger(ClassesPathDtdResolver.class);

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("Trying to resolve XML entity with public ID [" + publicId +
                "] and system ID [" + systemId + "]");
        }
        if (systemId == null || !systemId.endsWith(DTD_EXTENSION)) {
            return null;
        }

        String dtdFile = "/" + FileUtility.getInstance().getFileNameProperty(systemId).getFullFileName();
        if (logger.isTraceEnabled()) {
            logger.trace("Trying to locate [" + dtdFile + "] in Spring jar on classpath");
        }
        InputStream inputStream = EnvironmentSupport.getInstance().getFileInputStream(dtdFile);
        if (inputStream == null) {
            logger.warn("system id {} local resource {} is not exist ", systemId, dtdFile);
        }
        InputSource source = new InputSource(inputStream);
        source.setPublicId(publicId);
        source.setSystemId(systemId);
        if (logger.isTraceEnabled()) {
            logger.trace("Found beans DTD [" + systemId + "] in classpath: " + dtdFile);
        }
        return source;
    }

    @Override
    public String toString() {
        return "EntityResolver for sparrow-beans DTD";
    }

}
