
import java.io.File;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.BigDecimal; 
import java.util.stream.IntStream; 

public class output 
{

    static void outFrag(String inputString, int r)
    {
        //find input sequence fragment's length
        int len = inputString.length();

        System.out.println("length is " + len);
        //System.out.println("r is " + r);

        //store the K position
        ArrayList<Integer> kPos = new ArrayList<Integer>();
        //store a range of string for K
        ArrayList<String> kString = new ArrayList<String>();

        for(int i = 0; i < len; i++)
        {
            if(inputString.charAt(i) == 'k' || inputString.charAt(i) == 'K')
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
                        tempStr = "X" + tempStr;
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
                        tempStr = tempStr + "X";
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
                        tempStr = "X" + tempStr;
                    }

                    for(int j = len; j <= i + r; j++)
                    {
                        tempStr = tempStr + "X";
                    }

                    //System.out.println(tempStr);

                    kPos.add(i);
                    kString.add(tempStr);
                }

            }

        }


        //output
        for(int k = 0; k < kPos.size(); k++)
        {
            System.out.println("The K is at " + kPos.get(k) + "th position of the input sequence,\nthe corresponding fragment is " + kString.get(k));
        }


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

                Scanner reader = new Scanner(System.in);  // Reading from System.in
                System.out.println("Enter a range for k: ");
                int r = reader.nextInt();
                System.out.println("range is " + r);

                outFrag(nextToken, r);
            }
        }
    }

    
}
