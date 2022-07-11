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
import com.sparrow.utility.CollectionsUtility;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class SystemIdSchemaMappingResolver implements EntityResolver {

    public static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "/META-INF/sparrow.schemas";

    private static Logger logger = LoggerFactory.getLogger(SystemIdSchemaMappingResolver.class);

    private final String schemaMappingsLocation;

    /**
     * Stores the mapping of schema URL -> local schema path.
     */
    private volatile Map<String, String> schemaMappings;

    public SystemIdSchemaMappingResolver() {
        this.schemaMappingsLocation = DEFAULT_SCHEMA_MAPPINGS_LOCATION;
    }

    public SystemIdSchemaMappingResolver(String schemaMappingsLocation) {
        this.schemaMappingsLocation = schemaMappingsLocation;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws IOException {
        if (logger.isTraceEnabled()) {
            logger.trace("Trying to resolve XML entity with public id [" + publicId +
                "] and system id [" + systemId + "]");
        }

        if (systemId != null) {
            String resourceLocation = getSchemaMappings().get(systemId);
            if (resourceLocation != null) {
                InputStream inputStream = EnvironmentSupport.getInstance().getFileInputStream("/" + resourceLocation);
                if (inputStream == null) {
                    logger.warn("system id {} local schema file is not exits", systemId, resourceLocation);
                    return null;
                }
                InputSource source = new InputSource(inputStream);
                source.setPublicId(publicId);
                source.setSystemId(systemId);
                if (logger.isTraceEnabled()) {
                    logger.trace("Found XML schema [" + systemId + "] in classpath: " + resourceLocation);
                }
                return source;
            }
        }
        return null;
    }

    /**
     * Load the specified schema mappings lazily.
     */
    private Map<String, String> getSchemaMappings() {
        Map<String, String> schemaMappings = this.schemaMappings;
        if (schemaMappings != null) {
            return schemaMappings;
        }
        synchronized (this) {
            schemaMappings = this.schemaMappings;
            if (schemaMappings != null) {
                return schemaMappings;
            }
            if (logger.isTraceEnabled()) {
                logger.trace("Loading schema mappings from [" + this.schemaMappingsLocation + "]");
            }
            try {
                Properties mappings = new Properties();
                mappings.load(EnvironmentSupport.getInstance().getFileInputStream(this.schemaMappingsLocation));
                if (logger.isTraceEnabled()) {
                    logger.trace("Loaded schema mappings: " + mappings);
                }
                schemaMappings = new ConcurrentHashMap<>(mappings.size());
                CollectionsUtility.fillMapWithProperties(schemaMappings, mappings);
                this.schemaMappings = schemaMappings;
            } catch (IOException ex) {
                throw new IllegalStateException(
                    "Unable to load schema mappings from location [" + this.schemaMappingsLocation + "]", ex);
            }
        }
        return schemaMappings;
    }

    @Override
    public String toString() {
        return "EntityResolver using mappings " + schemaMappings;
    }

}
