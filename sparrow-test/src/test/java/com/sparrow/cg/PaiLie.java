package com.sparrow.cg;

import java.util.Stack;

/**
 * @author by harry
 */
public class PaiLie {

    private static char[] chars={'a','b','c'};

    private static Stack<String> arrange(char[]chars,boolean full){
        Stack<String>stacks=new Stack<>();
        Stack<Character> stack=new Stack<>();
        for(char c:chars){
            stack.clear();
            stack.push(c);
            if(!full) {
                stacks.push(stack.toString());
            }
            for(char c1:chars){
                if(c1==c){continue;}
                    stack.push(c1);
                if(full){
                    if(stack.size()==chars.length) {
                        stacks.push(stack.toString());
                    }
                }
                else
                {
                    stacks.push(stack.toString());
                }
            }
        }
        return  stacks;
    }
    public static void main(String[] args) {

        Stack<String> stacks=arrange(chars,false);
        for(String stack:stacks){
            System.out.println(stack);
        }
    }
}
