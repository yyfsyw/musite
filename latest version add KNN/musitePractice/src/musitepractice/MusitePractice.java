/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musitepractice;
import java.io.File;
import java.util.*;
import java.io.*;
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
                    //System.out.println(sampleNo+"\t"+"Invalid Input");
                    return null;
                   
	        }
                index++;
        }
        
        return String.valueOf(strArray);
    }
    
    static void concatenateFragsFeaturesOfSamples(ArrayList<String> positive, ArrayList<String> negative, HashMap positiveFeatures, HashMap negativeFeatures)
    {

        ArrayList<Double> tempPWAAList = new ArrayList<Double>();
        ArrayList<Double> tempFreList = new ArrayList<Double>();
        ArrayList<Double> tempEBAGList = new ArrayList<Double>();
        ArrayList<Double> combined = new ArrayList<Double>();
        
        //contenate the features of each positive fragment
                            
        for(int i=0; i<positive.size();i++)
        {
            //call frequency feature
          tempFreList=Frequency.characterCount(positive.get(i));
          //System.out.println("positive tempFreList: "+tempFreList);
          //call binary coding feature(EBAG)
          tempEBAGList=EBAG.numSeq(positive.get(i));
          //System.out.println("positive tempEBAGList: "+tempEBAGList);
          //call PWAA feature
          tempPWAAList = PWAA.numSeq(positive.get(i));
          //System.out.println("positive tempPWAAList: "+tempPWAAList);
          
          //combine all the features
          combined.addAll(tempFreList);
          combined.addAll(tempEBAGList);
          combined.addAll(tempPWAAList);
          
          //put feature list into map
          positiveFeatures.put(positive.get(i),combined);
          
          //clear
          combined = new ArrayList<>();
          
        }
        //System.out.println("positive map: "+positiveFeatures);
        
        
        //contenate the features of each positive fragment

        for(int j=0; j<negative.size();j++)
        {
            //call frequency feature
            tempFreList = Frequency.characterCount(negative.get(j));
            //System.out.println("negative tempFreList: "+tempFreList);
            //call binary coding feature(EBAG)
            tempEBAGList=EBAG.numSeq(negative.get(j));
            //System.out.println("negative tempEBAGList: "+tempEBAGList);
            //call PWAA feature
            tempPWAAList = PWAA.numSeq(negative.get(j));
            //System.out.println("negative tempPWAAList: "+tempPWAAList);
            
            //combine all the features
            combined.addAll(tempFreList);
            combined.addAll(tempEBAGList);
            combined.addAll(tempPWAAList);
            
            //put feature list into map
            negativeFeatures.put(negative.get(j),combined);

            //clear
            combined = new ArrayList<Double>();
        }
        //System.out.println("negative map: "+negativeFeatures);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException{
        // TODO code application logic here
        int sampleNo=1;
        String sampleStr="";
        String validStr="";
        double classResult=0.0;
        //print the title
        //Frequency.printTitleLetter();
        
        //FILE operation
        File file = new File("artifica_data.txt");
        
        String sampleInput = "zEWGPGSDWSRGEAAGVDRGKAGLGLGGRPPPQPPREERAQQLLDAVEQRQRQLLDTIAACEEMLRQLGRRRPEPAGGGNVSAKPGAPPQPAVSARGGFPKDAGDGAAEP";
        
        sampleInput = checkSample(sampleInput,sampleNo-1);
        
        System.out.println(sampleInput);
        
        ArrayList<String> sample = new ArrayList<String>();
        ArrayList<Double> tempPWAAList = new ArrayList<Double>();
        ArrayList<Double> tempFreList = new ArrayList<Double>();
        ArrayList<Double> tempEBAGList = new ArrayList<Double>();
        ArrayList<Double> combined = new ArrayList<Double>();
        
        HashMap<Integer, ArrayList<Double>> sampleFeature = new HashMap<>();
        //ArrayList<Double> sampleFeature = new ArrayList<Double>();
        
        ArrayList<Integer> kPosition = CreateFrag.outFrag(sampleInput, 5, 'A', null, null, sample, null);
        
        for(int k = 0;  k < sample.size(); k++)
        {
            //call frequency feature
            tempFreList=Frequency.characterCount(sample.get(k));
            //System.out.println("positive tempFreList: "+tempFreList);
            //call binary coding feature(EBAG)
            tempEBAGList=EBAG.numSeq(sample.get(k));
            //System.out.println("positive tempEBAGList: "+tempEBAGList);
            //call PWAA feature
            tempPWAAList = PWAA.numSeq(sample.get(k));
            //System.out.println("positive tempPWAAList: "+tempPWAAList);
        
            //combine all the features
            combined.addAll(tempFreList);
            combined.addAll(tempEBAGList);
            combined.addAll(tempPWAAList);
            
            //System.out.println(combined);
            
            sampleFeature.put(k, combined);
            
            //System.out.println(sampleFeature.get(k));
            
            //System.out.println(sampleFeature.get(0));
            
            //clear
            combined = new ArrayList<Double>();
            
            //System.out.println(sampleFeature.get(0));
            
        }

        
        //used to store classified fragment for current sample
        Integer[] siteSet = {5, 6, 10, 11, 15, 16, 17};

        ArrayList<String> positive = new ArrayList<String>();

        ArrayList<String> negative = new ArrayList<String>();
        
        //used to store all fragments of all samples and the features of all the fragments
        
        HashMap<String, ArrayList<Double>> positiveFeatures = new HashMap<String, ArrayList<Double>>();
        HashMap<String, ArrayList<Double>> negativeFeatures = new HashMap<String, ArrayList<Double>>();
        HashMap<String, ArrayList<Double>> tempForBalance = new HashMap<String, ArrayList<Double>>();
  
        
        //Double[] sampleFeature = new Double[combined.size()];
        //sampleFeature = combined.toArray(sampleFeature);
        
        
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
                            //System.out.println(validStr);
                            CreateFrag.outFrag(validStr, 5, 'A', positive, negative, null, siteSet);
                            //System.out.println("positive: "+positive);
                            //System.out.println("negative: "+negative);
                            
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
            //System.out.println(validStr);

            if(validStr!=null)//if the input is valid
            {
                //get the positive and negative fragments list of the current sample
                //System.out.println(validStr);
                CreateFrag.outFrag(validStr, 5, 'A', positive, negative, null, siteSet);
                //System.out.println("positive: "+positive);
                //System.out.println("negative: "+negative);

                concatenateFragsFeaturesOfSamples(positive, negative, positiveFeatures, negativeFeatures);
          
            }
        }
        
        int count=1;
        if(positiveFeatures.size()<=negativeFeatures.size()){
            for (Map.Entry<String, ArrayList<Double>> entry:negativeFeatures.entrySet()) {
                
                tempForBalance.put(entry.getKey(), entry.getValue());
                count++;
                if(count>positiveFeatures.size()) break;
            }
            for(int l = 0; l < sampleFeature.size(); l++)
            {
                //System.out.println(sampleFeature.get(l));
                System.out.println("position : "+kPosition.get(l));
                classResult = KNN.KNNTest(positiveFeatures, tempForBalance, sampleFeature.get(l));
                //sampleFeatureList.clear();
                
                System.out.println(classResult);
                //System.out.println("sample feature " + l + " is ");
                //System.out.print(sampleFeature.get(l));
            }
        }
               
        else{
            for (Map.Entry<String, ArrayList<Double>> entry:positiveFeatures.entrySet()) {
                
                tempForBalance.put(entry.getKey(), entry.getValue());
                count++;
                if(count>negativeFeatures.size()) break;
            }
            
            for(int l = 0; l < sampleFeature.size(); l++)
            {
                System.out.println("position : "+kPosition.get(l));
                //sampleFeatureList.add(sampleFeature.get(l));
                classResult = KNN.KNNTest(tempForBalance, negativeFeatures, sampleFeature.get(l));
                //sampleFeatureList.clear();
                
                System.out.println(classResult);
                //System.out.println("sample feature " + l + " is ");
                //System.out.print(sampleFeature.get(l));
            }

        }

        
//        System.out.println("positive size: "+positive.size());
//        System.out.println("negative size: "+negative.size());

    }
    
}
