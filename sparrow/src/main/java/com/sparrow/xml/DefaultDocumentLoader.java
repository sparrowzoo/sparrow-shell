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

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DefaultDocumentLoader implements DocumentLoader {

    /**
     * JAXP attribute used to configure the schema language for validation.
     */
    private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /**
     * JAXP attribute value indicating the XSD schema language.
     */
    private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

    private static Logger logger = LoggerFactory.getLogger(DefaultDocumentLoader.class);

    private final XmlValidationModeDetector xmlValidationModeDetector = new XmlValidationModeDetector();

    /**
     * Load the {@link Document} at the supplied {@link InputSource} using the standard JAXP-configured XML parser.
     */
    @Override
    public Document loadDocument(String xmlFilePath, EntityResolver entityResolver,
        ErrorHandler errorHandler,
        boolean namespaceAware) throws IOException, ParserConfigurationException, SAXException {
        int validationMode = xmlValidationModeDetector.detectValidationMode(xmlFilePath);
        DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
        DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
        InputStream inputStream = EnvironmentSupport.getInstance().getFileInputStream(xmlFilePath);
        InputSource inputSource = new InputSource(inputStream);
        try {
            return builder.parse(inputSource);
        } finally {
            inputStream.close();
        }
    }

    @Override public Document loadDocument(String xmlFilePath, EntityResolver entityResolver,
        boolean namespaceAware) throws ParserConfigurationException, SAXException, IOException {
        return this.loadDocument(xmlFilePath, entityResolver, new SimpleSaxErrorHandler(logger), namespaceAware);
    }

    @Override public Document loadDocument(
        String xmlFilePath, boolean namespaceAware) throws IOException, SAXException, ParserConfigurationException {
        return this.loadDocument(xmlFilePath, new DtdSchemaResolverAdapter(), namespaceAware);
    }

    protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware)
        throws ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(namespaceAware);

        if (validationMode != XmlValidationModeDetector.VALIDATION_NONE) {
            factory.setValidating(true);
            if (validationMode == XmlValidationModeDetector.VALIDATION_XSD) {
                // Enforce namespace aware for XSD...
                factory.setNamespaceAware(true);
                try {
                    factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
                } catch (IllegalArgumentException ex) {
                    ParserConfigurationException pcex = new ParserConfigurationException(
                        "Unable to validate using XSD: Your JAXP provider [" + factory +
                            "] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? " +
                            "Upgrade to Apache Xerces (or Java 1.5) for full XSD support.");
                    pcex.initCause(ex);
                    throw pcex;
                }
            }
        }

        return factory;
    }

    protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory,
        EntityResolver entityResolver, ErrorHandler errorHandler)
        throws ParserConfigurationException {

        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        if (entityResolver != null) {
            docBuilder.setEntityResolver(entityResolver);
        }
        if (errorHandler != null) {
            docBuilder.setErrorHandler(errorHandler);
        }
        return docBuilder;
    }

}
