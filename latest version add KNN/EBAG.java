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
public class EBAG {
    
    static ArrayList<Double> numSeq(String inputString){
        
        //classify 20 letters into 4 groups
        
        final String[] groups = {"AFGILMPVW", "CNQSTY", "DE", "HKR"};
        
        //variable to save binary code
        
        ArrayList<Double> numSeqList = new ArrayList<Double>();

        
        //Converting given string to char array
 
        char[] strArray = inputString.toCharArray();
        
        //convert the letter string into binary string
        for(int i=0; i<4; i++){
            for(char c : strArray){
                if(groups[i].indexOf(c) == -1){
                    numSeqList.add(0.0);// = numSeqStr + "0";
                }
                else{
                    numSeqList.add(1.0);// = numSeqStr + "1";
                }
            }
        }
        
        //System.out.println(numSeqStr);
        return numSeqList;
    }
}
