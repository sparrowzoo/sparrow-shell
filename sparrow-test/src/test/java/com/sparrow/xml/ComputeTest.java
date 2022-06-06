package com.sparrow.xml;

/**
 * @author by harry
 */
public class ComputeTest {
    public static void main(String[] args) {
        System.out.println();

        String a="12345678";
        String b="345678";

        char[] arraya=a.toCharArray();
        char[] arrayb=b.toCharArray();

        char[][] maxMin=computeMaxMin(arraya,arrayb);

        char[]minAarry=fill(maxMin[1],maxMin[0].length);

        char []result =new char[minAarry.length+1];
        char[] charResult=null;
        int jinWei=0;
        for(int i=maxMin[0].length-1;i>=0;i--){
            charResult=((Integer.parseInt(maxMin[0][i]+"")+Integer.valueOf(maxMin[1][i]+"")+jinWei)+"").toCharArray();

            if(charResult.length==2) {
                jinWei = Integer.parseInt(charResult[0] + "");
                result[i] = charResult[1];
            }
            else
            {
                jinWei=0;
                result[i]=charResult[0];
            }

        }
        if(jinWei>0){
            result[maxMin[0].length]=(char) jinWei;
        }

    }

    private static char[] fill(char[] array,int length){

        if(array.length<=length){
            char[] destArray=new char[length];
            for(int i=0;i<length-array.length;i++){
                destArray[i]=0;
            }
            System.arraycopy(array,0,destArray,length-array.length,array.length);
            return destArray;
        }
        return array;
    }

    private static char[][]computeMaxMin(char []arraya,char []arrayb){
        char[][] maxMin=new char[2][];
        if(arraya.length>arrayb.length){
            maxMin[0]=arraya;
            maxMin[1]=arrayb;
            return maxMin;
        }
        maxMin[0]=arrayb;
        maxMin[1]=arraya;
        return maxMin;
    }
}
