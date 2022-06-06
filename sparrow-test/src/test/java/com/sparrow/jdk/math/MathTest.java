package com.sparrow.jdk.math;

import java.util.Random;

/**
 * @author: zhanglizhi@kanzhun.com
 * @date: 2019-03-07 13:15
 * @description:
 */
public class MathTest {
    public static void main(String[] args) {
        double array[][] = new double[100][50];
        for (int i = 0; i < 100; i++) {

            for (int j = 0; j < 50; j++) {
                array[i][j] = new Random().nextDouble();
            }
        }

        double sum = 0D;
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 50; j++) {
                sum += array[i][j];
            }
        }
        System.out.println(sum / 60D);
        sum = 0;
        for (int i = 0; i < 100; i++) {
            double sumOf = 0D;
            for (int j = 0; j < 50; j++) {
                sumOf += array[i][j];
            }
            sum += sumOf/60D;
        }
        System.out.println(sum);
    }
}
