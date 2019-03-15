package cn.edu.ruc.evaluation;

import java.util.List;
import java.util.Map;

public class Metric {
    public double getRR(Map<String, Integer> standard2relevanceMap, List<String> testList){
        int count = 0;
        for(String test : testList){
            count ++;
            if(standard2relevanceMap.containsKey(test)){
                break;
            }
        }

        return count == 0 ? 0 : 1.0 / count;
    }

    public double getAPAtK(Map<String, Integer> standard2relevanceMap, List<String> testList, int k){
        double hits = 0;
        double sum = 0;
        int count = 0;
        for(String test : testList){
            if(++ count > k)
                break;

            if(standard2relevanceMap.containsKey(test)){
                sum += (++ hits) / count;
            }
        }

        return sum / (standard2relevanceMap.size() < k ? standard2relevanceMap.size() : k);
    }

    public double getAPAtR(Map<String, Integer> standard2relevanceMap, List<String> testList){
        return getAPAtK(standard2relevanceMap, testList, standard2relevanceMap.size());
    }

    public double getPrecisionAtK(Map<String, Integer> standard2relevanceMap, List<String> testList, int k){
        double hits = 0;
        int count = 0;
        for(String test : testList){
            if(++ count > k)
                break;

            if(standard2relevanceMap.containsKey(test))
                ++ hits;
        }

        return hits / (standard2relevanceMap.size() < k ? standard2relevanceMap.size() : k);
    }

    public double getPrecisionAtR(Map<String, Integer> standard2relevanceMap, List<String> testList){
        return getPrecisionAtK(standard2relevanceMap, testList, standard2relevanceMap.size());
    }
}
