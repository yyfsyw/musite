/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musitepractice;
import java.io.File;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.BigDecimal; 
import java.util.stream.IntStream; 
/**
 *
 * @author qiaoyang
 */
public class PWAA {
    
     static ArrayList<Double> numSeq(String inputString)
    {
        //find input sequence fragment's length
        int len = inputString.length();

        //System.out.println("length is " + len);

        int l = (len - 1) / 2;

        //System.out.println("l is " + l);

        //find the range
        int[] loca = IntStream.rangeClosed(-l, l).toArray();


 	final String oSet = "ACDEFGHIKLMNPQRSTVWY";

        //System.out.println(loca[0]);

        //find number sequence
        ArrayList<Double> numSeq = new ArrayList<Double>();
        int temsum;
        int temsign;

        /*
        for i=1:20
        Numseq(i)=1/(L*(L+1));
        temsum=0;
        for j=1:(2*L+1)
        temsign=(OSet(i)==P_SeqFrag(j));
        temsum=temsum+temsign*(Loca(j)+abs(Loca(j))/L);
        end
        Numseq(i)=Numseq(i)*temsum;
        end
        return
        */
        
        for(int i = 0; i < 20; i++)
        {
            numSeq.add (1.0 / (l * (l + 1.0)));

            temsum = 0;

            for(int j = 0; j < (2 * l + 1); j++)
            {
                if(oSet.charAt(i) == inputString.charAt(j))
                {
                    temsign = 1;
                }
                else
                {
                    temsign = 0;
                }

                temsum = temsum + temsign * (loca[j] + Math.abs(loca[j]) / l);
            }

            numSeq.set(i, numSeq.get(i) * temsum);
        }

        return (numSeq);
        //print out the result
//        for(int k = 0; k < numSeq.size(); k++)
//        {
//            System.out.println(numSeq.get(k));
//        }

    }


}
