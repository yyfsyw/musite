
import java.io.File;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.BigDecimal; 
import java.util.stream.IntStream; 

public class output 
{

    static void outFrag(String inputString, int r, char k, ArrayList<String> positive, ArrayList<String> negative, Integer[] siteSet)
    {
        //find input sequence fragment's length
        int len = inputString.length();

        System.out.println("length is " + len);
        //System.out.println("r is " + r);

        //store the K position
        ArrayList<Integer> kPos = new ArrayList<Integer>();
        //store a range of string for K
        ArrayList<String> kString = new ArrayList<String>();  
        
        //the char added when it's out of range
        char frontChar =  inputString.charAt(0);
        char endChar =  inputString.charAt(len-1);
        
        for(int i = 0; i < len; i++)
        {
            if(inputString.charAt(i) == k)
            {
                //check if it out of range
                if(i - r >= 0 && i + r <= len)
                {
                    //add to the array list
                    //System.out.println("K is at " + i + " position");
                    kPos.add(i);
                    //System.out.println(inputString.substring(i - r, i + r + 1));
                    kString.add(inputString.substring(i - r, i + r + 1));
                }
                else if( i - r < 0 && i + r <= len)
                {
                    String tempStr = inputString.substring(0, i + r + 1);
                    

                    for(int j = i - r; j < 0; j++)
                    {
                        tempStr = frontChar + tempStr;
                    }

                    //System.out.println(tempStr);

                    kPos.add(i);
                    kString.add(tempStr);
                }
                else if( i + r > len && i - r >= 0)
                {
                    String tempStr = inputString.substring(i - r, len);
                    
                    for(int j = len; j <= i + r; j++)
                    {
                        tempStr = tempStr + endChar;
                    }

                    //System.out.println(tempStr);

                    kPos.add(i);
                    kString.add(tempStr);
                }
                else
                {
                    String tempStr = inputString.substring(0, len);
                    
                    for(int j = i - r; j < 0; j++)
                    {
                        tempStr = frontChar + tempStr;
                    }

                    for(int j = len; j <= i + r; j++)
                    {
                        tempStr = tempStr + endChar;
                    }

                    //System.out.println(tempStr);

                    kPos.add(i);
                    kString.add(tempStr);
                }

            }

        }

        List<Integer> list = Arrays.asList(siteSet);

        //output
        for(int i = 0; i < kPos.size(); i++)
        {
            //System.out.println("The "+k+" is at " + kPos.get(i) + "th position of the input sequence,\nthe corresponding fragment is " + kString.get(i));
            
            if(list.contains(kPos.get(i)))
            {
                positive.add(kString.get(i));
            }
            else
            {
                negative.add(kString.get(i));
            }
        }

    }
 
    public static void main(String[] args) throws FileNotFoundException
    {
        //read file
        File file = new File("artifica_data.txt");

        Integer[] siteSet = {5, 6, 10, 11, 15, 16, 17};

        ArrayList<String> positive = new ArrayList<String>();

        ArrayList<String> negative = new ArrayList<String>();

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

                Scanner reader = new Scanner(System.in);  // Reading from System.in
                System.out.println("Enter a range for k: ");
                int r = reader.nextInt();
                System.out.println("range is " + r);

                outFrag(nextToken, r, 'K', positive, negative, siteSet);

                //print the positive fragment.
                System.out.println("positive fragment are");
                for (int i = 0; i < positive.size(); i++) 
                {
                    System.out.println(positive.get(i));
                }

                System.out.println();

                //print the negative fragment.
                System.out.println("negative fragment are");
                for (int j = 0; j < negative.size(); j++) 
                {
                    System.out.println(negative.get(j));
                }
            }
        }
    }

    
}
