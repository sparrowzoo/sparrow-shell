package com.sparrow.xml;

import com.sparrow.exception.Asserts;
import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author by harry
 */

public class DtdSchemaResolverAdapter implements EntityResolver {

    /** Suffix for DTD files. */
    public static final String DTD_SUFFIX = ".dtd";

    /** Suffix for schema definition files. */
    public static final String XSD_SUFFIX = ".xsd";

    private final EntityResolver dtdResolver;

    private final EntityResolver schemaResolver;

    public DtdSchemaResolverAdapter() {
        this.dtdResolver = new ClassesPathDtdResolver();
        this.schemaResolver = new SystemIdSchemaMappingResolver();
    }

    public DtdSchemaResolverAdapter(EntityResolver dtdResolver, EntityResolver schemaResolver) {
        Asserts.illegalArgument(dtdResolver == null, "'dtdResolver' is required");
        Asserts.illegalArgument(schemaResolver == null, "'schemaResolver' is required");
        this.dtdResolver = dtdResolver;
        this.schemaResolver = schemaResolver;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if (systemId == null) {
            return null;
        }
        if (systemId.endsWith(DTD_SUFFIX)) {
            return this.dtdResolver.resolveEntity(publicId, systemId);
        }
        if (systemId.endsWith(XSD_SUFFIX)) {
            return this.schemaResolver.resolveEntity(publicId, systemId);
        }
        return null;
    }

    @Override
    public String toString() {
        return "EntityResolver adpater " + XSD_SUFFIX + " to " + this.schemaResolver +
            " and " + DTD_SUFFIX + " to " + this.dtdResolver;
    }
}

