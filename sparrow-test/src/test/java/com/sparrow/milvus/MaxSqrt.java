package com.sparrow.milvus;

public class MaxSqrt {
    public static void main(String[] args) {
        System.out.println(sqrt(16));
    }

    /**
     * 1,2,3,4,5,6,7,8,9 10
     *
     * @param x
     * @return
     */
    public static int sqrt(int x) {
        int left = 0;
        int right = x;
        int mid = x / 2;
        while (left < right) {
            long square = mid * mid;
            if (square == x) {
                return mid;
            } else if (square > x) {
                right = mid - 1;
            } else
                left = mid;
            mid = (left + right + 1) >> 1;
        }
        return left + 1;
    }

    private static int maxSqrt(int i) {
        while (Math.sqrt(i) % 1 != 0) {
            i++;
        }
        return i;
    }
}
