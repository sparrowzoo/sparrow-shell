package com.sparrow.jdk.sort;

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }
}

public class SortTest2 {
    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(2);
        head.next.next.next = new ListNode(3);
        head.next.next.next.next = new ListNode(4);
        ListNode result = new Solution().deleteDuplicates(head);
        System.out.println(result);
    }

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode(int x) {
     * val = x;
     * next = null;
     * }
     * }
     */
    public static class Solution {
        public ListNode deleteDuplicates(ListNode head) {
            if (head == null) {
                return null;
            }
            if (head.next == null) {
                return head;
            }
            ListNode cusor = head;
            ListNode resultHead = null;
            ListNode result = null;
            int occurTimes = 0;

            while (true) {
                ListNode current = new ListNode(cusor.val);
                occurTimes++;
                if (cusor.next == null) {
                    result = setOnceNode(result, occurTimes, current);
                    if (resultHead == null) {
                        resultHead = result;
                    }
                    break;
                }
                cusor = cusor.next;
                if (current.val == cusor.val) {
                    continue;
                }

                result = setOnceNode(result, occurTimes, current);
                if (resultHead == null) {
                    resultHead = result;
                }
                occurTimes = 0;
            }
            return resultHead;
        }

        private ListNode setOnceNode(ListNode result, int occurTimes, ListNode current) {
            if (occurTimes != 1) {
                return result;
            }
            if (result == null) {
                return current;
            }
            result.next = current;
            return current;
        }
    }

}
