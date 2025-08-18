//package com.sparrow.jdk;
//
///**
// * @author by harry
// */
//public class ShiftTest {
//    public static void main(String[] args) {
//        char[] chars = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
//        shift(chars, 3);
//        System.out.println(chars);
//
//        String str = "hello2018";
//        System.out.println(getDigit(str));
//
//    }
//
//    private static String getDigit(String str) {
//        if (str == null || str.length() == 0) {
//            return "";
//        }
//
//        char[] chars2 = str.toCharArray();
//        StringBuilder sb = new StringBuilder(chars2.length);
//        for (char c : chars2) {
//            if (Character.isDigit(c)) {
//                sb.append(c);
//            }
//        }
//        return sb.toString();
//    }
//
//    private static void shift(char[] array, int index) {
//        if (array == null || array.length == 0) {
//            return;
//        }
//
//        if (index < 0 || index >= array.length) {
//            throw new IllegalArgumentException("index must >=0 and <=" + array.length);
//        }
//
//        boolean direction = array.length - index > index;
//        index = direction ? index : array.length - index-2;
//        for (int i = 0; i <= index; i++) {
//            if (direction) {
//                leftShift(array);
//            } else {
//                rightShift(array);
//            }
//        }
//    }
//
//    /**
//     * @param array
//     */
//    private static void leftShift(char[] array) {
//        char temp = array[0];
//        for (int i = 0; i < array.length - 1; i++) {
//            array[i] = array[i + 1];
//        }
//        array[array.length - 1] = temp;
//    }
//
//    /**
//     * @param array
//     */
//    private static void rightShift(char[] array) {
//        char temp = array[array.length - 1];
//        for (int i = array.length - 1; i >= 1; i--) {
//            array[i] = array[i - 1];
//        }
//        array[0] = temp;
//    }
//}
