package com.sparrow.lesson.lesson1;

/**
 * @author zhanglz
 */
public class HelloWorld {

  /**
   * 快键键 psvm 智能提示自动生成main 方法
   */
  public static void main(String[] args) {
    //sout 回车，自动生成输出行
    System.out.println("Hello world!");

    /**
     * 一个数如果要指明它采用八进制，必须在它前面加上一个0(零)，如：123是十进制，但0123则表示采用八进制。这就是八进制数的表达方法。
     *   现在，对于同样一个数，比如是100，我们在代码中可以用平常的10进制表达，例如在变量初始化时：
     *   int   a   =   100;
     *   我们也可以这样写：
     *   int   a   =   0144;   //0144是八进制的100；一个10进制数如何转成8进制，我们后面会学到。
     *   千万记住，用八进制表达时，你不能少了最前的那个0。否则计算机会通通当成10进制。
     *   不过，有一个地方使用八进制数时，却不能使用加0.
     */
    int i = 0144;
    System.out.println(i);

    int b = 0B1;
    System.out.println(~b);
    System.out.println(Integer.toBinaryString(~b));

    int c=b|i;
    b|=i;
    System.out.println(b==c);
  }
}
