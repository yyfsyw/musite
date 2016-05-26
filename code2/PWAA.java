
import java.io.File;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.BigDecimal; 
import java.util.stream.IntStream; 

public class PWAA 
{

    static void numSeq(String inputString)
    {
        int len = inputString.length();

        //System.out.println("length is " + len);

        int l = (len - 1) / 2;

        //System.out.println("l is " + l);

        int[] loca = IntStream.rangeClosed(-l, l).toArray();


 		final String oSet = "ACDEFGHIKLMNPQRSTVWY";

        //System.out.println(loca[0]);

        double[] numSeq = new double[20];
        int temsum;
        int temsign;

        for(int i = 0; i < 20; i++)
        {
            numSeq[i] = 1 / (l * (l + 1));
            temsum = 0;
            for(int j = 0; j < (2 * l + 1); j++)
            {
                if(oSet.charAt(i) == inputString.charAt(j))
                {
                    temsign = 1;
                }
                else
                {
                    temsign = 0;
                }

                temsum = temsum + temsign * (loca[j] + Math.abs(loca[j]) / l);
            }

            numSeq[i] = numSeq[i] * temsum;
        }


        for(int k = 0; k < numSeq.length; k++)
        {
            System.out.println(numSeq[k]);
        }

    }
 
    public static void main(String[] args) throws FileNotFoundException
    {
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
                numSeq(nextToken);
            }
        }
    }

    
}
