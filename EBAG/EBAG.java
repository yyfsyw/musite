
import java.io.File;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.BigDecimal; 
import java.util.stream.IntStream; 

public class EBAG 
{
    
    static Double numSeq(String inputString)
    {
        
        //classify 20 letters into 4 groups
        
        final String[] groups = {"AFGILMPVW", "CNQSTY", "DE", "HKR"};
        
        //variable to save binary code
        
        String numSeqStr = "";
        
        //Converting given string to char array
 
        char[] strArray = inputString.toCharArray();
        
        //convert the letter string into binary string
        for(int i = 0; i < 4; i++){
            for(char c : strArray){
                if(groups[i].indexOf(c) == -1)
                {
                    numSeqStr = numSeqStr + "0";
                }
                else
                {
                    numSeqStr = numSeqStr + "1";
                }
            }
        }

        
        System.out.println(numSeqStr);
        Integer returnVal = Integer.parseInt(numSeqStr, 2);
        return returnVal * 1.0;
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        //read file
        File file = new File("artifica_data.txt");
        //print the title
        try (Scanner input = new Scanner(file)) 
        {
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
                Double myNum = numSeq(nextToken);

                        //print out the result
                System.out.println(myNum);
            }
        }
    }
}
