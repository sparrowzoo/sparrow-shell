package com.sparrow.lesson.ref.type;

/**
 * @author zhanglz
 */

public class RefOperator {

  public static void main(String[] args) {
    Move move = new Movement();
    move.move(1, 2);
    if (move instanceof Move) {
      System.out.println("is instanceof Move");
    }

    if (move instanceof Movement) {
      System.out.println("is instanceof Movement");
    }
  }
}

class Movement implements Move {

  @Override
  public void move(int deltax, int deltay) {
    System.out.println("deltax:" + deltax + ",deltay:" + deltay);
  }
}
