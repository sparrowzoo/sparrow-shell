package com.sparrow.protocol.pager;

import java.util.Map;

public class PagerResultWithDictionary<T, DK, DV, A> extends PagerResult<T, A> {
    public PagerResultWithDictionary() {
    }

    private Map<DK, DV> dictionaries;

    public PagerResultWithDictionary(SimplePager simplePager) {
        super(simplePager);
    }


    public Map<DK, DV> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(Map<DK, DV> dictionaries) {
        this.dictionaries = dictionaries;
    }
}
