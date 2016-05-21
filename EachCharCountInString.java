/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package eachcharcountinstring;

import java.io.File;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;  
/**
 *
 * @author qiaoyang
 */
public class EachCharCountInString {

    static void characterCount(String inputString)
    {
        //Creating a HashMap containing char as a key and occurrences as  a value
 
 		final String matchString = "ABCDEFGHIZKLMNOPQRSTUVW";


        HashMap<Character, Integer> charCountMap = new HashMap<Character, Integer>();
 
        //Converting given string to char array
 
        char[] strArray = inputString.toCharArray();
        
        //count the number of chars in the inputString
        
        int count = 0;
        Random rand = new Random();
		int  n;
        
        //store the frequency result
        
        double[] frequency = new double[26];
        
        //checking each char of strArray
 
        for (char c : strArray)
        {
        	if (matchString.indexOf(c) != -1)
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
	        else if(Character.isLetter(c))
	        {
	        	//System.out.println(c);
	        	n = rand.nextInt(20);

				c = matchString.charAt(n);
				//System.out.println(c);
	        }
	        else
	        {
	        	System.out.println("Error");
	        	continue;
	        }
        }
        
        //calculate the frequency
        int index;//index in the frequency array
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
            BigDecimal bg = new BigDecimal(tempFrequency); 
            frequency[index]= bg.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        
        //Printing the charCountMap
 
        //System.out.println(charCountMap);
        
        //Printing the frequency
        for(int i=0; i<26; i++)
        {
            System.out.print(frequency[i]+ "\t");
        }
        System.out.println();
        
    }
 
    public static void main(String[] args) throws FileNotFoundException
    {
        int sampleNo=1;
        File file = new File("artifica_data.txt");
        //print the title
        try (Scanner input = new Scanner(file)) {
            //print the title
            System.out.print("\t");
            for(int i=(int)'A';i<'A'+26;i++)
            {
                System.out.print((char)i+"\t");
            }
            System.out.println();
            
            //print the main content--frequency
            while(input.hasNext())
            {
                String nextToken = input.next();
                //or to process line by line
                //String nextLine = input.nextLine();
                
                if(nextToken.contains(">sample"))
                {
                    
                    continue;
                }
                System.out.print(sampleNo+"\t");
                characterCount(nextToken);
                sampleNo++;
            }
        }
    }

    
}
