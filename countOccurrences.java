import java.io.File;
import java.text.NumberFormat;
import java.util.*;
import java.io.*;

class EachCharCountInString
{
    static void characterCount(String inputString)
    {
        //Creating a HashMap containing char as a key and occurrences as  a value
 
        HashMap<Character, Integer> charCountMap = new HashMap<Character, Integer>();
 
        //Converting given string to char array
 
        char[] strArray = inputString.toCharArray();
 
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
        }
 
        //Printing the charCountMap
 
        System.out.println(charCountMap);
    }
 
    public static void main(String[] args) throws FileNotFoundException
    {

        File file = new File("/Users/Yidong/Documents/CS4995/github/artifica_data.txt");
        Scanner input = new Scanner(file);

        while(input.hasNext()) 
        {
            String nextToken = input.next();
            //or to process line by line
            //String nextLine = input.nextLine();

            if(nextToken.contains(">sample"))
            {
                
                continue;
            }

            System.out.println(nextToken);
            characterCount(nextToken);
        }


        input.close();    
    }
}