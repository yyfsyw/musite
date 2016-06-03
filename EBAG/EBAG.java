
import java.io.File;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.BigDecimal; 
import java.util.stream.IntStream; 

public class EBAG 
{
    
    static ArrayList<Double> numSeq(String inputString)
    {
        
        //classify 20 letters into 4 groups
        
        final String[] groups = {"AFGILMPVW", "CNQSTY", "DE", "HKR"};
        
        //variable to save binary code
        
        ArrayList<Double> numSeqStr = new ArrayList<Double>();
        
        //Converting given string to char array
 
        char[] strArray = inputString.toCharArray();
        
        //convert the letter string into binary string
        for(int i=0; i<4; i++){
            for(char c : strArray){
                if(groups[i].indexOf(c) == -1)
                {
                    numSeqStr.add(0.0);// = numSeqStr + "0";
                }
                else
                {
                    numSeqStr.add(1.0);// = numSeqStr + "1";
                }
            }
        }
        
        //System.out.println(numSeqStr);

        return (numSeqStr);
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
                List<Double> myList = numSeq(nextToken);

                        //print out the result
                for(int k = 0; k < myList.size(); k++)
                {
                    System.out.println(myList.get(k));
                }
            }
        }
    }
}
