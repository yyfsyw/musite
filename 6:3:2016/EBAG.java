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
public class EBAG {
    
    static void numSeq(String inputString){
        
        //classify 20 letters into 4 groups
        
        final String[] groups = {"AFGILMPVW", "CNQSTY", "DE", "HKR"};
        
        //variable to save binary code
        
        String numSeqStr="";
        
        //Converting given string to char array
 
        char[] strArray = inputString.toCharArray();
        
        //convert the letter string into binary string
        for(int i=0; i<4; i++){
            for(char c : strArray){
                if(groups[i].indexOf(c) == -1){
                    numSeqStr = numSeqStr + "0";
                }
                else{
                    numSeqStr = numSeqStr + "1";
                }
            }
        }
        
        System.out.println(numSeqStr);
    }
}
