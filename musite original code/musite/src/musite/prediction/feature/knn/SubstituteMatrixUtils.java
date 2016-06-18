/**
 * Musite
 * Copyright (C) 2010 Digital Biology Laboratory, University Of Missouri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package musite.prediction.feature.knn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.io.*;

import musite.prediction.feature.Instance;
import musite.prediction.feature.InstanceUtil;

/**
 *
 * @author LucasYao
 */
public class SubstituteMatrixUtils {

    public static SimilarityMatrix genSubstituteMatrix(List<Instance> instances, int windowOffset) {
        return genSubstituteMatrix(instances, windowOffset, windowOffset);
    }

    public static SimilarityMatrix genSubstituteMatrix(List<Instance> instances, int windowOffsetLeft, int windowOffsetRight) {
        List<String> peptides = new ArrayList<String>(instances.size());
        for (Instance ins : instances) {
            String pep = InstanceUtil.extractSurroundingSequence(ins, windowOffsetLeft, windowOffsetRight);
            peptides.add(pep);
        }
        return genSubstituteMatrix(peptides);
    }

    public static SimilarityMatrix[] genSubstituteMatrices(List<Instance> instances, int windowOffset) {
        return genSubstituteMatrices(instances, windowOffset, windowOffset);
    }

    public static SimilarityMatrix[] genSubstituteMatrices(List<Instance> instances, int windowOffsetLeft, int windowOffsetRight) {
        List<String> peptides = new ArrayList<String>(instances.size());
        for (Instance ins : instances) {
            String pep = InstanceUtil.extractSurroundingSequence(ins, windowOffsetLeft, windowOffsetRight);
            peptides.add(pep);
        }
        return genSubstituteMatrices(peptides);
    }

    public static void SimilarityMatrixPrint(SimilarityMatrix matrix)
    {
        String alphabet = matrix.getAlphabet();
        double[][] matrixdata = matrix.getMatrix();
        System.out.print("\t\t");
        for(int i=0;i<alphabet.length();i++)
        {
            System.out.print(alphabet.charAt(i));
            System.out.print("\t");
        }
        System.out.print("\r\n");
        for(int i=0;i<alphabet.length();i++)
        {
            System.out.print(alphabet.charAt(i));
            System.out.print("\t");
            for(int j=0;j<alphabet.length();j++)
            {
                System.out.print(matrixdata[i][j]);
                System.out.print("\t");
            }
            System.out.print("\r\n");
        }
        System.out.println("------");
    }

    public static SimilarityMatrix genSubstituteMatrix(List<String> peptides)
    {
        double[] singlefreq = new double[30];
        double[][] pairfreq = new double[30][30];
        for(int i=0;i<30;i++)
        {
            singlefreq[i]=0;
            for(int j=0;j<30;j++)
                pairfreq[i][j]=0;
        }
        HashMap<Character, Integer> alphabet = new HashMap<Character, Integer>();

        int blockwidth = peptides.get(0).length();
        int num = peptides.size();
        int mid = (blockwidth-1)/2;
        for(int i = 0;i<blockwidth; i++)
        {
            if(i!=mid)
            {
            char[] sequence= new char[num];
            for(int j=0; j<num;j++)
            {
                sequence[j] = peptides.get(j).charAt(i);
            }
            calculateFrequency(singlefreq,pairfreq,sequence, num,alphabet);
            }

        }

//        int pairnum = num*(num-1)/2*blockwidth;
        int pairnum = num*(num-1)/2*(blockwidth-1);
//        int singlenum = blockwidth*num;
        int singlenum = (blockwidth-1)*num;
        for(int i=0;i<30;i++)
        {
            singlefreq[i]=singlefreq[i]/singlenum;
            for(int j=0;j<30;j++)
                pairfreq[i][j]=pairfreq[i][j]/pairnum;
        }


        alphabet.put(new Character('*'), new Integer(alphabet.size()));


        int alphabetlen = alphabet.size();
        char [] alphabetarray = new char[alphabetlen];
        Iterator iter = alphabet.entrySet().iterator();
        while (iter.hasNext()) {
        Map.Entry<Character, Integer> entry = (Map.Entry<Character, Integer>)iter.next();
        Character key = entry.getKey();
        Integer val = entry.getValue();
        alphabetarray[val.intValue()] = key.charValue();
        }
        String alphabetstr = new String(alphabetarray);


        double[][] score = new double[alphabetlen][alphabetlen];
        for(int i=0;i<alphabetlen-1;i++)
        {
            for(int j=0;j<alphabetlen-1;j++)
            {
                score[i][j]= singlefreq[i]*singlefreq[j];
                if(i!=j)
                {
                    score[i][j] = score[i][j]*2;
                }
            }
        }

        double minscore = Double.MAX_VALUE;
        for(int i=0;i<alphabetlen-1;i++)
        {
            for(int j=0;j<alphabetlen-1;j++)
            {
                score[i][j] = 2*Math.log(pairfreq[i][j]/score[i][j])/Math.log(2);
                score[i][j] = Math.round(score[i][j]);
                if(score[i][j]<minscore) minscore  = score[i][j];
            }
        }
        for(int i=0;i<alphabetlen-1;i++)
            score[i][alphabetlen-1] = minscore;
        for(int j=0;j<alphabetlen-1;j++)
            score[alphabetlen-1][j] = minscore;
        score[alphabetlen-1][alphabetlen-1] = 0;

        SimilarityMatrix simMatrix = new SimilarityMatrixImpl(score,alphabetstr);
        return simMatrix;
    }


    public static SimilarityMatrix[] genSubstituteMatrices(List<String> peptides)
    {
        double[] overallsinglefreq = new double[30];
        HashMap<Character, Integer> overallalphabet = new HashMap<Character, Integer>();
        calculateSingleFrequency(overallsinglefreq,peptides,overallalphabet);
        int overallsinglenum = (peptides.get(0).length()-1)*peptides.size();
        for(int i=0;i<30;i++)
        {
            overallsinglefreq[i]=overallsinglefreq[i]/overallsinglenum;

        }



       int blockwidth = peptides.get(0).length();
       int num = peptides.size();
       int mid = (blockwidth-1)/2;
       SimilarityMatrix[] simMatrices = new SimilarityMatrix[blockwidth-1];
       for(int ii = 0;ii<blockwidth; ii++)
       {
        if(ii!=mid)
        {
        double[] singlefreq = new double[30];
        double[][] pairfreq = new double[30][30];
        for(int i=0;i<30;i++)
        {
            singlefreq[i]=0;
            for(int j=0;j<30;j++)
                pairfreq[i][j]=0;
        }
        HashMap<Character, Integer> alphabet = new HashMap<Character, Integer>();






            char[] sequence= new char[num];
            for(int j=0; j<num;j++)
            {
                sequence[j] = peptides.get(j).charAt(ii);
            }
            calculateFrequency(singlefreq,pairfreq,sequence, num,alphabet);




        int pairnum = num*(num-1)/2;
        int singlenum = num;
        for(int i=0;i<30;i++)
        {
            singlefreq[i]=singlefreq[i]/singlenum;
            for(int j=0;j<30;j++)
                pairfreq[i][j]=pairfreq[i][j]/pairnum;
        }





        int alphabetlen = alphabet.size();
        char [] alphabetarray = new char[alphabetlen];
        Iterator iter = alphabet.entrySet().iterator();
        while (iter.hasNext()) {
        Map.Entry<Character, Integer> entry = (Map.Entry<Character, Integer>)iter.next();
        Character key = entry.getKey();
        Integer val = entry.getValue();
        alphabetarray[val.intValue()] = key.charValue();
        }
        String alphabetstr = new String(alphabetarray);


        double[][] score = new double[alphabetlen][alphabetlen];
        for(int i=0;i<alphabetlen;i++)
        {
            for(int j=0;j<alphabetlen;j++)
            {
     //           score[i][j]= singlefreq[i]*singlefreq[j];
                score[i][j]= overallsinglefreq[i]*overallsinglefreq[j];
                if(i!=j)
                {
                    score[i][j] = score[i][j]*2;
                }
            }
        }

        double minscore = Double.MAX_VALUE;
        for(int i=0;i<alphabetlen;i++)
        {
            for(int j=0;j<alphabetlen;j++)
            {
                score[i][j] = 2*Math.log(pairfreq[i][j]/score[i][j])/Math.log(2);
//                score[i][j] = pairfreq[i][j]/score[i][j];
                score[i][j] = Math.round(score[i][j]);
                if(score[i][j]<minscore) minscore  = score[i][j];
            }
        }

 /*
        int currentalphabetlen = alphabet.size();
        if(!alphabet.containsKey('*'))
        {
             alphabet.put(new Character('*'), new Integer(currentalphabetlen));
        }
        for(int i=0;i<alphabetlen-1;i++)
            score[i][alphabetlen-1] = minscore;
        for(int j=0;j<alphabetlen-1;j++)
            score[alphabetlen-1][j] = minscore;
        score[alphabetlen-1][alphabetlen-1] = 0;

 */

        if(ii<mid)
            simMatrices[ii] = new SimilarityMatrixImpl(score,alphabetstr);
        else
            simMatrices[ii-1] = new SimilarityMatrixImpl(score,alphabetstr);
       }
      }
        return simMatrices;
    }

    private static void calculateSingleFrequency(double[] singlefreq,List<String> peptides,HashMap<Character, Integer> alphabet)
    {
       int blockwidth = peptides.get(0).length();
       int num = peptides.size();
       int mid = (blockwidth-1)/2;
       for(int i=0; i<blockwidth; i++)
       {
           if(i!=mid)
           {
               for(int j=0;j<num;j++)
               {
                   char c = peptides.get(j).charAt(i);
                               int index1;
                    if(alphabet.containsKey(c))
                    {
                        index1 = alphabet.get(c).intValue();
                    }
                    else
                    {
                        index1 = alphabet.size();
                        alphabet.put(new Character(c), new Integer(index1));
                    }
                    singlefreq[index1]++;
               }
           }
       }
    }

    private static void calculateFrequency(double[] singlefreq, double[][] pairfreq, char[] peptide, int peptidelen, HashMap<Character, Integer> alphabet)
    {
        int n = peptidelen;
        

        for(int i = 0; i< n; i++)
        {
            
            char c1 = peptide[i];
            int index1;
            if(alphabet.containsKey(c1))
            {
                index1 = alphabet.get(c1).intValue();
            }
            else
            {
                index1 = alphabet.size();
                alphabet.put(new Character(c1), new Integer(index1));
            }
            singlefreq[index1]++;
            for(int j = i+1; j<n; j++)
            {
                char c2 = peptide[j];
                int index2;
                if(alphabet.containsKey(c2))
                {
                    index2 = alphabet.get(c2).intValue();
                }
                else
                {
                    index2 = alphabet.size();
                    alphabet.put(new Character(c2), new Integer(index2));
                }

                pairfreq[index1][index2]++;
                if(index1!=index2)
                pairfreq[index2][index1]++;

            }

        }
    }


}
