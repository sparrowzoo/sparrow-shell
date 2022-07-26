package com.sparrow.container;

import com.sparrow.utility.ClassUtility;
import com.sparrow.utility.StringUtility;
import java.util.List;
import javax.inject.Named;

public class AnnotationBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public AnnotationBeanDefinitionReader(SimpleBeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override public void loadBeanDefinitions(String basePackages) throws Exception {
        String[] basePackageArray = basePackages.split(",");
        for (String basePackage : basePackageArray) {
            List<Class> classes = ClassUtility.getClasses(basePackage);
            for (Class clazz : classes) {
                Named named = (Named) clazz.getAnnotation(Named.class);
                if (named == null) {
                    continue;
                }
                String beanName = named.value();
                if (StringUtility.isNullOrEmpty(beanName)) {
                    beanName = StringUtility.setFirstByteLowerCase(clazz.getSimpleName());
                }
                BeanDefinition bd = null;
                this.getRegistry().pubObject(beanName, bd);
            }
        }
    }
}
