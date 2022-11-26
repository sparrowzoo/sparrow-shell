package com.sparrow.jdk.hash;

import com.google.common.math.IntMath;
import java.math.BigDecimal;

public class PoissonTest {

    private static String poisson(int k) {
        /**
         * 翻译：
         *  尽管因为调整粒度而产生较大的方差，但是理想的情况，在随机hashCodes下，桶中节   点的频率遵循泊松分布。
         *  默认调整阈值为0.75的条件下，泊松分布中的概率参数λ=0.5。
         * 解释：
         *  k表示数量，这里指桶中节点的个数。
         *  λ表示事件的频率。这里λ=0.5，代表理想情况下，平均100个桶，50个数据，则每个桶有数据的概率是0.5。
         *  忽略方差，把λ代入。则求一个桶中出现k个节点的概率，公式为：
         */
        //泊松分布 Java
        double value = Math.exp(-0.5) * Math.pow(0.5, k) / IntMath.factorial(k);
        //格式化参数，保留10位小数。
        return new BigDecimal(value + "").setScale(10, BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    public static void main(String[] args) {
        System.out.println("1个桶中出现1个节点的概率:" + poisson(1));
        System.out.println("1个桶中出现2个节点的概率:" + poisson(2));
        System.out.println("1个桶中出现3个节点的概率:" + poisson(3));
        System.out.println("1个桶中出现4个节点的概率:" + poisson(4));
        System.out.println("1个桶中出现5个节点的概率:" + poisson(5));
        System.out.println("1个桶中出现6个节点的概率:" + poisson(6));
        System.out.println("1个桶中出现7个节点的概率:" + poisson(7));
        System.out.println("1个桶中出现8个节点的概率:" + poisson(8));//亿分之六
        System.out.println("1个桶中出现9个节点的概率:" + poisson(9));
    }
}
