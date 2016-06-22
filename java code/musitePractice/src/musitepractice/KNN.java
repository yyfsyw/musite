/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musitepractice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 *
 * @author qiaoyang
 */
public class KNN {
    
    
    
    static double KNNTest(HashMap positiveFeatures, HashMap negativeFeatures, ArrayList<Double> sampleFeature){
        
        ArrayList<Double> positiveDis = new ArrayList<>();
        ArrayList<Double> negativeDis = new ArrayList<>();
        ArrayList<Integer> nearestKDis = new ArrayList<>();//1 positive,0 negative
        
        //calculate the distance from the sample vector to all the test vectors
        //get a dis array of positiveDis
        
         getDisArray(positiveFeatures, positiveDis, sampleFeature);
         
        //get a dis array of negativeDis
        
         getDisArray(negativeFeatures, negativeDis, sampleFeature);
        
        //find nearest k dis's class from positiveDis and negativeDis
        
         findNearestKDisClass(15, positiveDis, negativeDis, nearestKDis);
        
        //calculate the frequecy of positive and negative and get the class of sample vector
         
        return getClassify(nearestKDis);
    }
    
    
    static void getDisArray(HashMap featureMap, ArrayList<Double> resultDis, ArrayList<Double> sampleFeature){
        double dist,sum=0.0;  
        Iterator iter = featureMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object value = entry.getValue();
            ArrayList<Double> tempFeature = (ArrayList<Double>)value;
            
            //calculate the distance between tempFeature and sampleFeature
            for(int i=0;i<tempFeature.size();i++)  
            {  
                sum+=(tempFeature.get(i)-sampleFeature.get(i))*(tempFeature.get(i)-sampleFeature.get(i));  
            }  
            dist=Math.sqrt(sum); 
            resultDis.add(dist);
            sum = 0.0; 
        }
    }
    
    static void findNearestKDisClass(int k, ArrayList<Double> positiveDis, ArrayList<Double> negativeDis, ArrayList<Integer> nearestKDis){
        int posIndex=0;
        int negIndex=0;

        Collections.sort(positiveDis);
        Collections.sort(negativeDis);
        
//        System.out.println("~~~~~~~!!!!!!!!!!!!!************positiveDis:");
//        System.out.println(positiveDis);
//        System.out.println("~~~~~~~!!!!!!!!!!!!!************positiveDis end");
//        System.out.println("~~~~~~~!!!!!!!!!!!!!!***********negativeDis:");
//        System.out.println(negativeDis);
//        System.out.println("~~~~~~~!!!!!!!!!!!!!!***********negativeDis end");
        
        //sort positiveDis, sort negativeDis
        for(int i=0; i<k; i++){

            if(!positiveDis.isEmpty()&&negativeDis.get(negIndex)>=positiveDis.get(posIndex)){
                nearestKDis.add(1);
                posIndex++;
                
            }
            else{
                if(!negativeDis.isEmpty()){
                    nearestKDis.add(0);
                    negIndex++;
                }
                    
                
            }
//            System.out.println("************nearestKDis:");
//                System.out.println(nearestKDis);    
        }
    }
    
    static double getClassify(ArrayList<Integer> nearestKDis){
        
        int posTimes=0;
        int negTimes=0;
        //System.out.println(nearestKDis);
        for(int i=0;i<nearestKDis.size();i++){
            if(nearestKDis.get(i)==1){
                posTimes++;
            }
            else{
                negTimes++;
            }
        }
        
        
        return posTimes*(1.0)/(posTimes+negTimes);
    }
    
}
