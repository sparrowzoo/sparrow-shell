package com.sparrow.protocol.db;

public class UniqueKeyCriteria {
    /**
     * 返回结果字段
     */
    private String resultFiled;
    /**
     * unique key
     */
    private Object key;
    /**
     * unique filed name default is primary key
     */
    private String uniqueFieldName;

    private UniqueKeyCriteria(String resultFiled, Object key, String uniqueFieldName) {
        this.resultFiled = resultFiled;
        this.key = key;
        this.uniqueFieldName = uniqueFieldName;
    }

    public static UniqueKeyCriteria createUniqueCriteria(Object key, String uniqueFieldName) {
        return new UniqueKeyCriteria(null, key, uniqueFieldName);
    }

    public static UniqueKeyCriteria createFieldCriteria(Object key, String resultFiled) {
        return new UniqueKeyCriteria(resultFiled, key, null);
    }

    public static UniqueKeyCriteria createUniqueWithFieldCriteria(String resultFiled, Object key, String uniqueFieldName) {
        return new UniqueKeyCriteria(resultFiled, key, uniqueFieldName);
    }

    public String getResultFiled() {
        return resultFiled;
    }

    public void setResultFiled(String resultFiled) {
        this.resultFiled = resultFiled;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getUniqueFieldName() {
        return uniqueFieldName;
    }

    public void setUniqueFieldName(String uniqueFieldName) {
        this.uniqueFieldName = uniqueFieldName;
    }
}
