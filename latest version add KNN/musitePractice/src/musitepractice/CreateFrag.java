/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musitepractice;

/**
 *
 * @author qiaoyang
 */
import java.io.File;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.BigDecimal; 
import java.util.stream.IntStream; 

public class CreateFrag 
{

    static void outFrag(String inputString, int r, char k, ArrayList<String> positive, ArrayList<String> negative, Integer[] siteSet)
    {
        //find input sequence fragment's length
        int len = inputString.length();

        //System.out.println("length is " + len);
        //System.out.println("r is " + r);

        //store the K position
        ArrayList<Integer> kPos = new ArrayList<Integer>();
        //store a range of string for K
        ArrayList<String> kString = new ArrayList<String>();  
        
        //the char added when it's out of range
        char frontChar =  inputString.charAt(0);
        char endChar =  inputString.charAt(len-1);
        
        for(int i = 0; i < len; i++)
        {
            if(inputString.charAt(i) == k)
            {
                //check if it out of range
                if(i - r >= 0 && i + r < len)
                {
                    //add to the array list
                    //System.out.println("K is at " + i + " position");
                    kPos.add(i);
                    //System.out.println(inputString.substring(i - r, i + r + 1));
                    kString.add(inputString.substring(i - r, i + r + 1));
                }
                else if( i - r < 0 && i + r < len)
                {
                    String tempStr = inputString.substring(0, i + r + 1);
                    

                    for(int j = i - r; j < 0; j++)
                    {
                        tempStr = frontChar + tempStr;
                    }

                    //System.out.println(tempStr);

                    kPos.add(i);
                    kString.add(tempStr);
                }
                else if( i + r >= len && i - r >= 0)
                {
                    String tempStr = inputString.substring(i - r, len);
                    
                    for(int j = len; j <= i + r; j++)
                    {
                        tempStr = tempStr + endChar;
                    }

                    //System.out.println(tempStr);

                    kPos.add(i);
                    kString.add(tempStr);
                }
                else
                {
                    String tempStr = inputString.substring(0, len);
                    
                    for(int j = i - r; j < 0; j++)
                    {
                        tempStr = frontChar + tempStr;
                    }

                    for(int j = len; j <= i + r; j++)
                    {
                        tempStr = tempStr + endChar;
                    }

                    //System.out.println(tempStr);

                    kPos.add(i);
                    kString.add(tempStr);
                }

            }

        }


        List<Integer> list = Arrays.asList(siteSet);

        //output
        for(int i = 0; i < kPos.size(); i++)
        {
            //System.out.println("The "+k+" is at " + kPos.get(i) + "th position of the input sequence,\nthe corresponding fragment is " + kString.get(i));
            
            if(list.contains(kPos.get(i)))
            {
                positive.add(kString.get(i));
            }
            else
            {
                negative.add(kString.get(i));
            }
        }


    }
 

    
}
