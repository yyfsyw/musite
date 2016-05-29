
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

        ArrayList<Integer> kPos = new ArrayList<Integer>();
        ArrayList<String> kString = new ArrayList<String>();

        for(int i = 0; i < len; i++)
        {
            if(inputString.charAt(i) == 'k' || inputString.charAt(i) == 'K')
            {
                if(i - r > 0 && i + r <= len)
                {
                    System.out.println("K is at " + i + " position");
                    kPos.add(i);
                    System.out.println(inputString.substring(i - r, i + r + 1));
                    kString.add(inputString.substring(i - r, i + r + 1));
                }
            }


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
