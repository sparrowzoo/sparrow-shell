package com.sparrow.container;

import com.sparrow.protocol.constant.magic.SYMBOL;
import com.sparrow.xml.DefaultDocumentLoader;
import com.sparrow.xml.DocumentLoader;
import com.sparrow.xml.DtdSchemaResolverAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author by harry
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private BeanDefinitionParserDelegate delegate;

    public XmlBeanDefinitionReader(SimpleBeanDefinitionRegistry registry,BeanDefinitionParserDelegate delegate) {
        super(registry);
        this.delegate=delegate;
    }

    protected void beforeParse(String xmlFileName){

    }

    protected  void afterParse(String xmlFileName){

    }

    private void parseDefaultElement(Element element) throws Exception {
        if (delegate.isImport(element)) {
            processImportElement(element);
            return;
        }
        if (delegate.isBeanElement(element)) {
            String beanName = element.getAttribute(BeanDefinitionParserDelegate.NAME_ATTRIBUTE).trim();
            BeanDefinition bd=  delegate.processBeanElement(element);
            this.getRegistry().pubObject(beanName,bd);
        }
    }

    public void processImportElement(Element element) throws Exception {
        String resource = element.getAttribute("resource");
        if (!resource.startsWith(SYMBOL.SLASH)) {
            resource = SYMBOL.SLASH + resource;
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
                    }
                    else {
                        delegate.parseCustomElement(ele);
                    }
                }
            }
        }
        else {
            delegate.parseCustomElement(root);
        }
    }

    @Override public void loadBeanDefinitions(String xmlFileName) throws Exception {
        DocumentLoader documentLoader = new DefaultDocumentLoader();
        Document doc = documentLoader.loadDocument(xmlFileName,false);
        this.beforeParse(xmlFileName);
        this.parse(doc.getDocumentElement());
        this.afterParse(xmlFileName);
    }
}
