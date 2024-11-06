package com.turnkey.turnquest.gis.quotation.exception.error;

/**
 * @author Paul Gichure
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    public <T> ResourceNotFoundException(Class<T> clazz, String fieldName, Object fieldValue) {
        this(clazz.getName(), fieldName, fieldValue);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

}