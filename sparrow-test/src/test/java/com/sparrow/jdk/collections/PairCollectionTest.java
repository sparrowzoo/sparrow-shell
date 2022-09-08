package com.sparrow.jdk.collections;

import com.sparrow.core.algorithm.collections.KeyPOJO;
import com.sparrow.core.algorithm.collections.PairCollection;

import java.util.ArrayList;
import java.util.List;

public class PairCollectionTest {
    public static class Expected extends KeyPOJO {
        public Expected(String key,Integer id) {
            this.id = id;
            this.key=key;
        }

        protected Integer id;

        @Override
        public String toString() {
            return "Expected{" +
                    "id=" + id +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    public static class Actual extends Expected{
        public Actual(String key, Integer id, String name) {
            super(key, id);
            this.name = name;
        }

        private String name;

        @Override
        public String toString() {
            return "Actual{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", key='" + key + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        List<Expected> expectedList=new ArrayList<>();
        List<Actual> actualList=new ArrayList<>();
        for(int i=1;i<10;i++){
            expectedList.add(new Expected(i+"",i));
        }

        for(int i=5;i<8;i++){
            actualList.add(new Actual(i+"",i,"mame"+i));
        }
        PairCollection<Expected,Actual> pairCollection=new PairCollection<>(expectedList,actualList);
        pairCollection.compare();
        System.out.println("intersection "+pairCollection.getIntersection());

        System.out.println("difference expected"+pairCollection.getDifferenceExpected());

    }
}
