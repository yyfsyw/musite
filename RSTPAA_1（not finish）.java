package com.company;
//import java.lang.Math;
//import java.util.*;
import java.lang.*;
//import java.io.Serializable;
//import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public class RSTPAA_1 (String Seri, int k, int w){
        //% MDHHP=mean(dataHHP(:,1:3));
        //如果是一个矩阵的列都是随机变量和行都是意见， S是一个行向量包含对应于每个列的标准偏差
        //S = std(dataHHP,w)指定任何以前的语法加权方案。当w = 0 （默认值）， S归一化的N-1。
        // 当w = 1， S通过观察， N次数恢复正常。w也可以包含非负元素的权重向量。在这种情况下， w的长度必须等于std经营的维度的长度。
        //未完成 stdm=[std(dataHHP(:,1),1),std(dataHHP(:,2),1),std(dataHHP(:,3),1)];
        //double sum.stdm;
        for(int i=1;i<3;i++)
        //0,1,2 three times
        {
        for(j=0;j<6;j++)
        {
        sum+=dataHHP[j][1];
        }//end of j loop
        stdm+=getStandardDeviation(sum);//apache
        // http://commons.apache.org/proper/commons-math/javadocs/api-3.2/org/apache/commons/math3/stat/descriptive/DescriptiveStatistics.html#getStandardDeviation()
        }//end of i loop

        int Len=length(Seri);
        for(int i=1;i<Len;i++)
        {
        for(int j=1;j<20;j++)
        {
        if Seri[i]==OSet[j]//已经完成 array in java
        P[j]=P[j]+1;
        }
        }
        // 未完成 P=P/Len; //让矩阵中的每一个元素整除Length;
        //for loop O(n)
        //可能会有更好的办法????

        for(int i=1;i<21,i++)
        {
        P[i]=P[i]/Len;
        }

        if(k==0)
        return P；
        else
        {
        for(int i=1;i<k;i++)
        {
        tao[i]=0;//前面先定义下tao是int array
        for(int j=1;j>Len-i;j++)
        {
        //前面得先定义下num& numk 是 string类型的array or string?(mutable, 不行)
        //num=find(OSet==Seri(j));
        // find(X),X 为矩阵,按竖行计算出每个不为0的元素的Index,在这里是找出和Seri(j)不同的index位置,应该类似于IndexOf在java里面
        //所以 num和 numk必须是mutable的元素集合,在java里可以使用 array或者指针
        //numk=find(OSet==Seri(j+i));
        if sum(num)~=0&sum(numk)~=0
        {
        H1=[dataHHP(num,1),dataHHP(num,2),dataHHP(num,3)];
        H1k=[dataHHP(numk,1),dataHHP(numk,2),dataHHP(numk,3)];
        H1=H1./stdm;
        H1k=H1k./stdm;
        tranJ=sum((H1k-H1).^2)/3;
        }
        else
        tranJ=0; //未完成 tranJ 的definition

        tao[i]=tao[i]+tranJ;


        }//j loop

        tao[i]=tao[i]/(Len-i);

        }// i loop

            /*

            tempaa=zeros(1,20+k);
            sumtao=sum(tao)*w;
            for i=1:20+k
            if i<21
                tempaa(i)=P(i)/(1+sumtao);
            else
                tempaa(i)=w*tao(i-20)/(1+sumtao);
            end
            end
            f=tempaa;

            * */

        }//else loop
}
    public static void main(String[] args) {
        //Seri,k,w三个变量为已知,已给定参数

        double[][] dataHHP={
                {0.6200, -0.5000, 15.0000, 2.3500, 9.8700, 6.1100},
                {0.2900, -1.0000, 47.0000, 1.7100, 10.7800, 5.0200},
                {-0.9000, 3.0000, 59.0000, 1.8800, 9.6000, 2.9800},
                {-0.7400, 3.0000, 73.0000, 2.1900, 9.6700, 3.0800},
                {1.1900, -2.5000, 91.0000, 2.5800, 9.2400, 5.9100},
                {0.4800, 0, 1.0000, 2.3400, 9.6000, 6.0600},
                {-0.4000, -0.5000, 82.0000, 1.7800, 8.9700, 7.6400},
                {1.3800, -1.8000, 57.0000, 2.3200, 9.7600, 6.0400},
                {-1.5000, 3.0000, 73.0000, 2.2000, 8.9000, 9.4700},
                {1.0600, -1.8000, 57.0000, 2.3600, 9.6000, 6.0400},
                {0.6400, -1.3000, 75.0000, 2.2800, 9.2100, 5.7400},
                {-0.7800, 0.2000, 58.0000, 2.1800, 9.0900, 10.7600},
                {0.1200, 0, 42.0000, 1.9900, 10.6000, 6.3000},
                {-0.8500, 0.2000, 72.0000, 2.1700, 9.1300, 5.6500},
                {-2.5300, 3.0000, 101.0000, 2.1800, 9.0900, 10.7600},
                {-0.1800, 0.3000, 31.0000, 2.2100, 9.1500, 5.6800},
                {-0.0500, -0.4000, 45.0000, 2.1500, 9.1200, 5.6000},
                {1.0800, -1.5000, 43.0000, 2.2900, 9.7400, 6.0200},
                {0.8100, -3.4000, 130.0000, 2.3800, 9.3900, 5.8800},
                {0.2600, -2.3000, 107.0000, 2.2000, 9.1100, 5.6300}
        };
        //initial data
        double[] tao = new double [k];
        String OSet= "ACDEFGHIKLMNPQRSTVWY";
        int[] P = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //0 to 21 (but we only need 1-20)
        Seri ="12323435";
        int k=3;
        int w=5;
        double[][] f=RSTPAA_1(Seri,k,w)//function f=RSTPAA_1(Seri,k,w)??????????

    }//static void main end







