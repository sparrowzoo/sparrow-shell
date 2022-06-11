package com.sparrow.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author by harry
 */
public class XmlTest {
    public static void main(String[] args) throws Exception {

        DocumentLoader documentLoader=new DefaultDocumentLoader();
        Document doc= documentLoader.loadDocument("/schema-beans.xml",new DtdSchemaResolverAdapter(),false);


        Element root= doc.getDocumentElement();

        System.out.println(doc.getNamespaceURI());

        NodeList nodeList= root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                System.out.println(ele.getNamespaceURI());
                System.out.println(ele.getLocalName());

            }
        }
    }
}
