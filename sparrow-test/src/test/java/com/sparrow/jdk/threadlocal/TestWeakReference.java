package com.sparrow.jdk.threadlocal;

import com.sparrow.constant.User;
import java.lang.ref.WeakReference;

/**
 * Created by harry on 2018/4/12.
 */
public class TestWeakReference {
    static WeakReference<User> user = new WeakReference<User>(new User());

    public static void main(String[] args) {
        int i = 0;
        while (true) {
            User u=user.get();
            if (user.get() != null) {
                i++;
                System.out.println("Object is alive for " + i + " loops - ");
            } else {
                System.out.println("Object has been collected.");
                break;
            }
            //由概念可知无论内存是否足够，只要gc弱引用就会被释放。
            System.gc();
        }
    }
}