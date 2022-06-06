package com.sparrow.container;

import com.sparrow.exception.Asserts;
import com.sparrow.protocol.constant.magic.SYMBOL;
import com.sparrow.utility.StringUtility;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author by harry
 */
public class BeanDefinitionParserDelegate {

    public static final String SCOPE_SINGLETON="singleton";
    public static final String SCOPE_PROTOTYPE="prototype";

    public static final String BEANS_NAMESPACE_URI = "http://www.sparrowzoo.com/schema/beans";

    public static final String IMPORT_ELEMENT = "import";

    public static final String TRUE_VALUE = "true";

    public static final String FALSE_VALUE = "false";

    public static final String DEFAULT_VALUE = "default";

    public static final String DESCRIPTION_ELEMENT = "description";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String ALIAS_ATTRIBUTE = "alias";


    public static final String BEAN_ELEMENT = "bean";

    public static final String ID_ATTRIBUTE = "id";

    public static final String PARENT_ATTRIBUTE = "parent";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String ABSTRACT_ATTRIBUTE = "abstract";

    public static final String SCOPE_ATTRIBUTE = "scope";

    public static final String INIT_METHOD_ATTRIBUTE = "init-method";

    public static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";

    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";

    public static final String INDEX_ATTRIBUTE = "index";

    public static final String TYPE_ATTRIBUTE = "type";

    public static final String PROPERTY_ELEMENT = "property";

    public static final String REF_ATTRIBUTE = "ref";

    public static final String VALUE_ATTRIBUTE = "value";

    public static final String CONTROLLER_ATTRIBUTE="controller";

    public static final String INTERCEPTOR_ATTRIBUTE="interceptor";

    public static final String PLACEHOLDER_ATTRIBUTE = "placeholder";

    public static final String DEFAULT_INIT_METHOD_ATTRIBUTE = "default-init-method";

    public static final String DEFAULT_DESTROY_METHOD_ATTRIBUTE = "default-destroy-method";

    public boolean isDefaultNamespace(Node node) {
        String namespaceUri=node.getNamespaceURI();
        return (StringUtility.isNullOrEmpty(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    private boolean nodeNameEquals(Node node, String desiredName) {
        return desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName());
    }

    public boolean isBeanElement(Node node){
        return nodeNameEquals(node,BEAN_ELEMENT);
    }

    public boolean isImport(Node node){
        return nodeNameEquals(node,IMPORT_ELEMENT);
    }

    public void parseCustomElement(Element element) {

    }

    private void parseBeanDefinitionAttributes(Element element,AbstractBeanDefinition bd){
        String className = null;
        if (element.hasAttribute(CLASS_ATTRIBUTE)) {
            className = element.getAttribute(CLASS_ATTRIBUTE).trim();
        }
        bd.setClassName(className);

        if (element.hasAttribute(SCOPE_ATTRIBUTE)) {
            bd.setScope(element.getAttribute(SCOPE_ATTRIBUTE));
            bd.setSingleton(SCOPE_SINGLETON.equalsIgnoreCase(bd.getScope()));
            bd.setPrototype(SCOPE_PROTOTYPE.equalsIgnoreCase(bd.getScope()));
        }
        else
        {
            bd.setSingleton(true);
            bd.setPrototype(false);
        }

        if(element.hasAttribute(CONTROLLER_ATTRIBUTE)){
            bd.setController(TRUE_VALUE.equalsIgnoreCase(element.getAttribute(CONTROLLER_ATTRIBUTE)));
        }

        if(element.hasAttribute(INTERCEPTOR_ATTRIBUTE)){
            bd.setInterceptor(TRUE_VALUE.equalsIgnoreCase(element.getAttribute(INTERCEPTOR_ATTRIBUTE)));
        }

        if(element.hasAttribute(ALIAS_ATTRIBUTE)){
            bd.setAlias(element.getAttribute(ALIAS_ATTRIBUTE));
        }
    }

    public ValueHolder parsePropertyValue(Element ele) {
        String name=  ele.getAttribute(NAME_ATTRIBUTE);
        boolean hasRefAttribute = ele.hasAttribute(REF_ATTRIBUTE);
        boolean hasValueAttribute = ele.hasAttribute(VALUE_ATTRIBUTE);
        if (hasRefAttribute) {
            String ref = ele.getAttribute(REF_ATTRIBUTE);
            return new ValueHolder(name,ref,true);
        }

        if (hasValueAttribute) {
            String value=ele.getAttribute(VALUE_ATTRIBUTE);
            return new ValueHolder(name,value,false);
        }
        return null;
    }


    public ValueHolder parseConstructorArgElement(Element ele) throws ClassNotFoundException {

        String typeAttr = ele.getAttribute(TYPE_ATTRIBUTE);
        String name = ele.getAttribute(NAME_ATTRIBUTE);

        boolean hasRefAttribute = ele.hasAttribute(REF_ATTRIBUTE);
        boolean hasValueAttribute = ele.hasAttribute(VALUE_ATTRIBUTE);


        if (hasRefAttribute) {
            String ref = ele.getAttribute(REF_ATTRIBUTE);
            return new ValueHolder(name, ref, true);
        }

        if (hasValueAttribute) {
            String value = ele.getAttribute(VALUE_ATTRIBUTE);
            if(StringUtility.isNullOrEmpty(typeAttr)){
                typeAttr="java.lang.String";
            }
            if(!typeAttr.contains(SYMBOL.DOT)){
                typeAttr="java.lang."+StringUtility.setFirstByteUpperCase(typeAttr);
            }
            return new ValueHolder(name, value, Class.forName(typeAttr), false);
        }
        return null;
    }



    public BeanDefinition  processBeanElement(Element element) throws ClassNotFoundException {
        AbstractBeanDefinition bd = new GenericBeanDefinition();
        parseBeanDefinitionAttributes(element, bd);

        NodeList nodeList = element.getChildNodes();
        if (nodeList.getLength() == 0) {
            return bd;
        }
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node=nl.item(i);
            if(!(node instanceof Element)){
                continue;
            }
            Element ele =(Element) node;
            if (nodeNameEquals(node, PROPERTY_ELEMENT)) {
                boolean hasPlaceholder=ele.hasAttribute(PLACEHOLDER_ATTRIBUTE);
                ValueHolder valueHolder = this.parsePropertyValue(ele);
                bd.addProperty(valueHolder);
                if(hasPlaceholder){
                    bd.addPlaceholder(ele.getAttribute(PLACEHOLDER_ATTRIBUTE),valueHolder);
                }
                continue;
            }
            if (nodeNameEquals(node, CONSTRUCTOR_ARG_ELEMENT)) {
                String indexAttr = ele.getAttribute(INDEX_ATTRIBUTE);
                Asserts.illegalArgument(StringUtility.isNullOrEmpty(indexAttr), " index attribute is null");
                int index = Integer.parseInt(indexAttr);
                Asserts.illegalArgument(index <= 0, " index can't be lower than 0 " + ele);
                ValueHolder valueHolder = this.parseConstructorArgElement(ele);
                bd.addConstructorArg(index, valueHolder);
            }
        }
        return bd;
    }
}
