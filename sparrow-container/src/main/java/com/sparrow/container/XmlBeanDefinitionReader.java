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
package com.sparrow.container;

import com.sparrow.protocol.constant.magic.Symbol;
import com.sparrow.xml.DefaultDocumentLoader;
import com.sparrow.xml.DocumentLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private BeanDefinitionParserDelegate delegate;
    private AnnotationBeanDefinitionReader annotationBeanDefinitionReader;

    public XmlBeanDefinitionReader(SimpleBeanDefinitionRegistry registry,
        AnnotationBeanDefinitionReader annotationBeanDefinitionReader, BeanDefinitionParserDelegate delegate) {
        super(registry);
        this.annotationBeanDefinitionReader = annotationBeanDefinitionReader;
        this.delegate = delegate;
    }

    protected void beforeParse(String xmlFileName) {
    }

    protected void afterParse(String xmlFileName) {

    }

    private void parseDefaultElement(Element element) throws Exception {
        if (delegate.isComponentScan(element)) {
            String basePackages = element.getTextContent();
            String[] basePackageArray = basePackages.split(",");
            this.annotationBeanDefinitionReader.loadBeanDefinitions(basePackageArray);
            return;
        }
        if (delegate.isImport(element)) {
            processImportElement(element);
            return;
        }
        if (delegate.isBeanElement(element)) {
            String beanName = element.getAttribute(BeanDefinitionParserDelegate.NAME_ATTRIBUTE).trim();
            BeanDefinition bd = delegate.processBeanElement(element);
            this.getRegistry().pubObject(beanName, bd);
        }
    }

    public void processImportElement(Element element) throws Exception {
        String resource = element.getAttribute("resource");
        if (!resource.startsWith(Symbol.SLASH)) {
            resource = Symbol.SLASH + resource;
        }
        logger.info("-------------init bean " + resource + " ...---------------------------");
        this.loadBeanDefinitions(resource);
    }

    private void parse(Element root) throws Exception {
        if (delegate.isDefaultNamespace(root)) {
            NodeList nl = root.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                if (node instanceof Element) {
                    Element ele = (Element) node;
                    if (delegate.isDefaultNamespace(ele)) {
                        parseDefaultElement(ele);
                    } else {
                        delegate.parseCustomElement(ele);
                    }
                }
            }
        } else {
            delegate.parseCustomElement(root);
        }
    }

    @Override public void loadBeanDefinitions(String xmlFileName) throws Exception {
        DocumentLoader documentLoader = new DefaultDocumentLoader();
        Document doc = documentLoader.loadDocument(xmlFileName, false);
        this.beforeParse(xmlFileName);
        if (doc != null) {
            this.parse(doc.getDocumentElement());
        }
        this.afterParse(xmlFileName);
    }
}
