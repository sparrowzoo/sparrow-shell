package com.sparrow.core.algorithm.collections;

import java.util.*;

public class PairCollection<E extends KeyPOJO,A extends E> {
    private Collection<E> expectedCollection;
    private Collection<A> actualCollection;

    /**
     * exist expectedCollection and actualCollection
     */
    private Set<A> intersection;
    /**
     * exist expectedCollection and not exist actualCollection
     */
    private Set<E> differenceExpected;

    public void compare(){
        //for performance
        Map<String,A> actualMap=new HashMap<>(this.expectedCollection.size());
        for(A a:this.actualCollection){
            actualMap.put(a.getKey(),a);
        }

        for(E e:this.expectedCollection){
            String expectedKey=e.getKey();
            if(actualMap.containsKey(expectedKey)){
                this.intersection.add(actualMap.get(expectedKey));
                continue;
            }
            this.differenceExpected.add(e);
        }
    }


    public Set<A> getIntersection() {
        return intersection;
    }

    public Set<E> getDifferenceExpected() {
        return differenceExpected;
    }

    public PairCollection(Collection<E> expected, Collection<A> actual) {
        this.expectedCollection = expected;
        this.actualCollection = actual;

        this.intersection =new HashSet<>(expected.size());
        this.differenceExpected =new HashSet<>(expected.size());
    }
}
