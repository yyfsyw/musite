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
    
    static void concatenateFragsFeaturesOfSamples(ArrayList<String> positive, ArrayList<String> negative, HashMap positiveFeatures, HashMap negativeFeatures)
    {
        //contenate the features of each positive fragment
                            
        for(int i=0; i<positive.size();i++)
        {
            //call frequency feature
          Frequency.characterCount(positive.get(i));
          //call binary coding feature(EBAG)
          EBAG.numSeq(positive.get(i));
          //call PWAA feature
          PWAA.numSeq(positive.get(i));
        }

        //contenate the features of each positive fragment

        for(int j=0; j<negative.size();j++)
        {
            //call frequency feature
            Frequency.characterCount(negative.get(j));
            //call binary coding feature(EBAG)
            EBAG.numSeq(negative.get(j));
            //call PWAA feature
            PWAA.numSeq(negative.get(j));
        }
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
        
        //used to store classified fragment for current sample
        Integer[] siteSet = {5, 6, 10, 11, 15, 16, 17};

        ArrayList<String> positive = new ArrayList<String>();

        ArrayList<String> negative = new ArrayList<String>();
        
        //used to store all fragments of all samples and the features of all the fragments
        
        HashMap<String, double[]> positiveFeatures = new HashMap<String, double[]>();
        HashMap<String, double[]> negativeFeatures = new HashMap<String, double[]>();
  
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
                        //call check function for current sample
                        validStr = checkSample(sampleStr,sampleNo-1);
                        if(validStr!=null)//if the input is valid
                        {
                            //get the positive and negative fragments list of the current sample
                            System.out.println(validStr);
                            CreateFrag.outFrag(validStr, 5, 'A',positive,negative,siteSet);
                            System.out.println("positive: "+positive);
                            System.out.println("negative: "+negative);
                            
                            concatenateFragsFeaturesOfSamples(positive, negative, positiveFeatures, negativeFeatures);
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
            System.out.println(validStr);

            if(validStr!=null)//if the input is valid
            {
                //get the positive and negative fragments list of the current sample
                System.out.println(validStr);
                CreateFrag.outFrag(validStr, 5, 'A',positive,negative,siteSet);
                System.out.println("positive: "+positive);
                System.out.println("negative: "+negative);

                concatenateFragsFeaturesOfSamples(positive, negative, positiveFeatures, negativeFeatures);
            }
        }

    }
    
}
