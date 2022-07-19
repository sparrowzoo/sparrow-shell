package com.sparrow.jdk.hash;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public class HashTest {
    /**
     * <pre>
     *     在openjdk8根路径/hotspot/src/share/vm/runtime路径下的synchronizer.cpp文件中,有生成哈希值的代码：
     *
     * static inline intptr_t get_next_hash(Thread * Self, oop obj) {
     *   intptr_t value = 0 ;
     *   if (hashCode == 0) {
     *      // 返回随机数
     *      value = os::random() ;
     *   } else
     *   if (hashCode == 1) {
     *      //用对象的内存地址根据某种算法进行计算
     *      intptr_t addrBits = cast_from_oop<intptr_t>(obj) >> 3 ;
     *      value = addrBits ^ (addrBits >> 5) ^ GVars.stwRandom ;
     *   } else
     *   if (hashCode == 2) {
     *      // 始终返回1，用于测试
     *      value = 1 ;
     *   } else
     *   if (hashCode == 3) {
     *      //从0开始计算哈希值
     *      value = ++GVars.hcSequence ;
     *   } else
     *   if (hashCode == 4) {
     *      //输出对象的内存地址
     *      value = cast_from_oop<intptr_t>(obj) ;
     *   } else {
     *      // 默认的hashCode生成算法，利用xor-shift算法产生伪随机数
     *      unsigned t = Self->_hashStateX ;
     *      t ^= (t << 11) ;
     *      Self->_hashStateX = Self->_hashStateY ;
     *      Self->_hashStateY = Self->_hashStateZ ;
     *      Self->_hashStateZ = Self->_hashStateW ;
     *      unsigned v = Self->_hashStateW ;
     *      v = (v ^ (v >> 19)) ^ (t ^ (t >> 8)) ;
     *      Self->_hashStateW = v ;
     *      value = v ;
     *   }
     *
     *   value &= markOopDesc::hash_mask;
     *   if (value == 0) value = 0xBAD ;
     *   assert (value != markOopDesc::no_hash, "invariant") ;
     *   TEVENT (hashCode: GENERATE) ;
     *   return value;
     * }
     *
     * </pre>
     * <pre>
     * java -XX:+PrintFlagsFinal | grep hash intx hashCode                                  = 5
     * {product}
     * </pre>
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(Long.toBinaryString(Long.MIN_VALUE));

//        for (int i = 0; i < 10; i++) {
//            System.out.println(new Object().hashCode());
//            System.out.println(new Integer(100).hashCode());
//
//        }
    }
}
