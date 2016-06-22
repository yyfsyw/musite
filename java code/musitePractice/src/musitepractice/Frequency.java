/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musitepractice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author qiaoyang
 */
public class Frequency {
    
    static void printTitleLetter(){
        //print the title
            System.out.print("\t");
            for(int i=(int)'A';i<'A'+26;i++)
            {
                System.out.print((char)i+"\t");
            }
            System.out.println();
    }
    
    static ArrayList<Double> characterCount(String inputString)
    {
        //Creating a HashMap containing char as a key and occurrences as  a value
        
        HashMap<Character, Integer> charCountMap = new HashMap<Character, Integer>();
 
        //Converting given string to char array
 
        char[] strArray = inputString.toCharArray();
        
        //count the number of chars in the inputString
        
        int count = 0;
        
        //store the frequency result for 26 letters
        
        double[] frequency = new double[26];
        
        //store valid index
        
        int[] validIndex = {0,2,3,4,5,6,7,8,10,11,12,13,15,16,17,18,19,21,22,24};
        
        //store the frequency result for 20 valid letters
        ArrayList<Double> validFrequency = new ArrayList<Double>();
        
        //checking each char of strArray
 
        for (char c : strArray)
        {
            if(charCountMap.containsKey(c))
            {
                //If char is present in charCountMap, incrementing it's count by 1
 
                charCountMap.put(c, charCountMap.get(c)+1);
            }
            else
            {
                //If char is not present in charCountMap,
                //putting this char to charCountMap with 1 as it's value
 
                charCountMap.put(c, 1);
            }
            count++;
        }
        
        //calculate the frequency
        int index;
        //index in the frequency array
        double tempFrequency;
        Iterator iter = charCountMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            char keyChar = (Character)key;
            index = keyChar-'A';
            Object value = entry.getValue();
            int valueInt = (Integer)value;
            tempFrequency =(valueInt*1.0)/count;
            //round up and keep 5 digits after decimal point
            BigDecimal bg = new BigDecimal(tempFrequency); 
            frequency[index]= bg.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();  
            
        }
        
        for(int i =0; i<20; i++)
            {
                int indexTemp = validIndex[i];
                double temp = frequency[indexTemp];
                validFrequency.add(temp);
            }
        return validFrequency;

//        //print the title
//        System.out.print(sampleNo+"\t");
        //Printing the frequency
//        for(int i=0; i<26; i++)
//        {
//            System.out.print(frequency[i]+ "\t");
//        }
//        System.out.println();
        
        
    }
}
