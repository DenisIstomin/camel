/* Generated by camel build tools - do NOT edit this file! */
package org.apache.camel.component.dozer;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.GeneratedPropertyConfigurer;
import org.apache.camel.spi.PropertyConfigurerGetter;
import org.apache.camel.util.CaseInsensitiveMap;
import org.apache.camel.support.component.PropertyConfigurerSupport;

/**
 * Generated by camel build tools - do NOT edit this file!
 */
@SuppressWarnings("unchecked")
public class DozerEndpointConfigurer extends PropertyConfigurerSupport implements GeneratedPropertyConfigurer, PropertyConfigurerGetter {

    private static final Map<String, Object> ALL_OPTIONS;
    static {
        Map<String, Object> map = new CaseInsensitiveMap();
        map.put("name", java.lang.String.class);
        map.put("lazyStartProducer", boolean.class);
        map.put("mappingConfiguration", org.apache.camel.converter.dozer.DozerBeanMapperConfiguration.class);
        map.put("mappingFile", java.lang.String.class);
        map.put("marshalId", java.lang.String.class);
        map.put("sourceModel", java.lang.String.class);
        map.put("targetModel", java.lang.String.class);
        map.put("unmarshalId", java.lang.String.class);
        map.put("basicPropertyBinding", boolean.class);
        map.put("synchronous", boolean.class);
        ALL_OPTIONS = map;
    }

    @Override
    public boolean configure(CamelContext camelContext, Object obj, String name, Object value, boolean ignoreCase) {
        DozerEndpoint target = (DozerEndpoint) obj;
        switch (ignoreCase ? name.toLowerCase() : name) {
        case "basicpropertybinding":
        case "basicPropertyBinding": target.setBasicPropertyBinding(property(camelContext, boolean.class, value)); return true;
        case "lazystartproducer":
        case "lazyStartProducer": target.setLazyStartProducer(property(camelContext, boolean.class, value)); return true;
        case "mappingconfiguration":
        case "mappingConfiguration": target.getConfiguration().setMappingConfiguration(property(camelContext, org.apache.camel.converter.dozer.DozerBeanMapperConfiguration.class, value)); return true;
        case "mappingfile":
        case "mappingFile": target.getConfiguration().setMappingFile(property(camelContext, java.lang.String.class, value)); return true;
        case "marshalid":
        case "marshalId": target.getConfiguration().setMarshalId(property(camelContext, java.lang.String.class, value)); return true;
        case "sourcemodel":
        case "sourceModel": target.getConfiguration().setSourceModel(property(camelContext, java.lang.String.class, value)); return true;
        case "synchronous": target.setSynchronous(property(camelContext, boolean.class, value)); return true;
        case "targetmodel":
        case "targetModel": target.getConfiguration().setTargetModel(property(camelContext, java.lang.String.class, value)); return true;
        case "unmarshalid":
        case "unmarshalId": target.getConfiguration().setUnmarshalId(property(camelContext, java.lang.String.class, value)); return true;
        default: return false;
        }
    }

    @Override
    public Map<String, Object> getAllOptions(Object target) {
        return ALL_OPTIONS;
    }

    @Override
    public Object getOptionValue(Object obj, String name, boolean ignoreCase) {
        DozerEndpoint target = (DozerEndpoint) obj;
        switch (ignoreCase ? name.toLowerCase() : name) {
        case "basicpropertybinding":
        case "basicPropertyBinding": return target.isBasicPropertyBinding();
        case "lazystartproducer":
        case "lazyStartProducer": return target.isLazyStartProducer();
        case "mappingconfiguration":
        case "mappingConfiguration": return target.getConfiguration().getMappingConfiguration();
        case "mappingfile":
        case "mappingFile": return target.getConfiguration().getMappingFile();
        case "marshalid":
        case "marshalId": return target.getConfiguration().getMarshalId();
        case "sourcemodel":
        case "sourceModel": return target.getConfiguration().getSourceModel();
        case "synchronous": return target.isSynchronous();
        case "targetmodel":
        case "targetModel": return target.getConfiguration().getTargetModel();
        case "unmarshalid":
        case "unmarshalId": return target.getConfiguration().getUnmarshalId();
        default: return null;
        }
    }
}

