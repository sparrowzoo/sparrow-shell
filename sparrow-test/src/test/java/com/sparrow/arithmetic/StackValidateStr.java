package com.sparrow.arithmetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class StackValidateStr {
    static class BinaryTreeNode {
        private char node;
        private BinaryTreeNode left;
        private BinaryTreeNode right;

        public BinaryTreeNode(char node) {
            this.node = node;
        }
    }

    public static boolean checkValidString(String s) {
        if(s.length()%2!=0){
            return false;
        }
        // possible range
        int min = 0, max = 0;
        // 维护当前左括号的数量范围：[min, max]
        for (char c : s.toCharArray()) {
            if (c == '(') {
                ++min;
                ++max;
            } else if (c == ')') {
                if (min > 0) {
                    min--;
                }
                if (max-- == 0) {
                    return false;
                }// 左括号不够
            } else {
                if (min > 0) {
                    min--;
                }// 可作为右括号，抵消
                ++max; // 可作为左括号
            }
        }
        return min == 0;
    }

    public static void main(String[] args) {
        String str = "*(***)";
        System.out.println(checkValidString(str));

        str = "(()())()()((()))";
        System.out.println(checkValidString(str));

        str = "*(******(**)";
       // System.out.println(checkValidString(str));

        str = "***))";
        System.out.println(checkValidString(str));

        str = "(*)*)((**)))))))";
        //System.out.println(checkValidString(str));
        //System.out.println(originIsValid(str));
    }

    private static boolean originIsValid(String str) {
        if (str == null) {
            return false;
        }
        if (str.length() == 0) {
            return true;
        }
        if (str.length() % 2 != 0) {
            return false;
        }
        if (str.charAt(0) == ')' || str.charAt(str.length() - 1) == '(') {
            return false;
        }
        BinaryTreeNode root = new BinaryTreeNode('0');
        List<BinaryTreeNode> binaryTreeNodes = new ArrayList<>();
        binaryTreeNodes.add(root);
        for (char c : str.toCharArray()) {
            binaryTreeNodes = buildTree(binaryTreeNodes, c);
        }
        Stack<Character> stack = new Stack<>();
        return traverse(stack, root, str.length());
    }

    private static boolean traverse(Stack<Character> stack, BinaryTreeNode currentNode, int maxSize) {
        if (currentNode == null) {
            return false;
        }
        stack.push(currentNode.node);
        if (currentNode.left != null) {
            if (traverse(stack, currentNode.left, maxSize)) {
                return true;
            }
        }
        if (currentNode.right != null) {
            if (traverse(stack, currentNode.right, maxSize)) {
                return true;
            }
        }

        if (stack.size() > maxSize) {
            System.out.println(stack.toString());
            if (isValid(stack)) {
                return true;
            }
        }
        stack.pop();
        return false;
    }

    private static List<BinaryTreeNode> buildTree(List<BinaryTreeNode> nodes, char node) {
        List<BinaryTreeNode> children = new ArrayList<>();

        for (BinaryTreeNode binaryTreeNode : nodes) {
            if (node == '(' || node == ')') {
                binaryTreeNode.left = new BinaryTreeNode(node);
                children.add(binaryTreeNode.left);
            } else if (node == '*') {
                binaryTreeNode.left = new BinaryTreeNode('(');
                children.add(binaryTreeNode.left);
                binaryTreeNode.right = new BinaryTreeNode(')');
                children.add(binaryTreeNode.right);
            }
        }
        return children;
    }


    private static boolean isValid(Stack<Character> originStack) {
        Stack<Character> stack = new Stack<>();
        for (Character anOriginStack : originStack) {
            char c = anOriginStack;
            if (c == '0') {
                continue;
            }
            if (c == '(') {
                stack.add(c);
                continue;
            }
            if (c == ')') {
                if (stack.isEmpty()) {
                    return false;
                }
                if (stack.peek() == '(') {
                    stack.pop();
                    continue;
                }
                return false;
            }
        }
        return stack.size() == 0;
    }
}
