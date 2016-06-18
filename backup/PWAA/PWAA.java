
import java.io.File;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.BigDecimal; 
import java.util.stream.IntStream; 

public class PWAA 
{

    static ArrayList<Double> numSeq(String inputString)
    {
        //find input sequence fragment's length
        int len = inputString.length();

        //System.out.println("length is " + len);

        int l = (len - 1) / 2;

        //System.out.println("l is " + l);

        //find the range
        int[] loca = IntStream.rangeClosed(-l, l).toArray();


 		final String oSet = "ACDEFGHIKLMNPQRSTVWY";

        //System.out.println(loca[0]);

        //find number sequence
        ArrayList<Double> numSeq = new ArrayList<Double>();
        int temsum;
        int temsign;

        /*
        for i=1:20
        Numseq(i)=1/(L*(L+1));
        temsum=0;
        for j=1:(2*L+1)
        temsign=(OSet(i)==P_SeqFrag(j));
        temsum=temsum+temsign*(Loca(j)+abs(Loca(j))/L);
        end
        Numseq(i)=Numseq(i)*temsum;
        end
        return
        */
        
        for(int i = 0; i < 20; i++)
        {
            numSeq.add (1.0 / (l * (l + 1.0)));

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

            numSeq.set(i, numSeq.get(i) * temsum);
        }

        return (numSeq);

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
