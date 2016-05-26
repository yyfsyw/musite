/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musitepractice;
import java.io.File;
import java.util.*;
import java.io.*;
import java.math.BigDecimal;
/**
 *
 * @author qiaoyang
 */
public class MusitePractice {

    //input a raw sample string return a valid string(without white space) or null
    static String checkSample(String strToBeChecked, int sampleNo){
        
        //a valid string can only contain the following letters
        
        final String matchString = "GAVLIFWYDHNEKQMRSTCP";
        
        //Converting given string to char array
 
        char[] strArray = strToBeChecked.toCharArray();
        
        //random
        Random rand = new Random();
	int  n;
        
        //indext of strArray
        int index=0;
        
        //checking each char of strArray
 
        for (char c : strArray)
        {
        	if (matchString.indexOf(c) != -1){
                    //do nothing, because c is in the 20 letters
	        }
	        else if(Character.isLetter(c)){
                    //convert small letter to big letter 
                    char upperChar = Character.toUpperCase(c);
                    //check if the big letter is in the 20 letters
                    if(matchString.indexOf(upperChar) != -1){
                        //It's a small letter of the 20 letters
                        strArray[index]=upperChar;
                    }
                    else{
                        //if it's a letter but not in the 20 letters, randomly repalce it with any of the 20 letters
                        n = rand.nextInt(20);
                        strArray[index] = matchString.charAt(n);
                    }  
	        }
                else if(c==' '){
                    //do nothing, ignore the whitespace
                }
	        else//invalid input, just skip the sample
	        {
                    System.out.println(sampleNo+"\t"+"Invalid Input");
                    return null;
                   
	        }
                index++;
        }
        
        return String.valueOf(strArray);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException{
        // TODO code application logic here
        int sampleNo=1;
        String sampleStr="";
        String validStr="";
        //print the title
        Frequency.printTitleLetter();
        
        //FILE operation
        File file = new File("artifica_data.txt");
        
        try (Scanner input = new Scanner(file)) {
            
            while(input.hasNext())
            {
                //it will skip white space(" ","\t")
                String nextToken = input.next();
                //or to process line by line
                //String nextLine = input.nextLine();
                
                if(nextToken.contains(">sample"))
                {
                    //print the result for last sample
                    if(sampleNo!=1){
                        //call check function
                        validStr = checkSample(sampleStr,sampleNo-1);
                        if(validStr!=null)//if the input is valid
                        {
                            //call the feature functions
                            System.out.println(validStr);
                            //call frequency feature
                            Frequency.characterCount(validStr, sampleNo-1);
                            //call binary coding feature(EBAG)
                            EBAG.numSeq(validStr);
                            //call PWAA feature
                            PWAA.numSeq(validStr);
                        }
                    }
                    sampleStr = "";
                    sampleNo++;
                    continue;
                }
                sampleStr = sampleStr + nextToken;
                
            }
            //calculate the last sample
            //call check function
            validStr = checkSample(sampleStr,sampleNo-1);
            //call frequency feature
            System.out.println(validStr);
            Frequency.characterCount(validStr, sampleNo-1);
            //call binary coding feature(EBAG)
            EBAG.numSeq(validStr);
            //call PWAA feature
            PWAA.numSeq(validStr);
        }

    }
    
}
