package cn.edu.ruc.core;

import cn.edu.ruc.domain.Entity;

import java.util.List;

public class Computer {
    //compute score in the embedding space
    public static double getEmbeddingScore(int entityId1, int entityId2) {
        double sum=0;

        return (2 - Math.sqrt(sum))/ 2;
    }

    public static double getEmbeddingsScore(int entityId1, int relationId, int direction, int entityId2){

        double sum=0;


        return (3 - Math.sqrt(sum))/ 3;
    }

    public static double getEmbeddingsScore(int entityId1, int relationId, int direction, double[] entityVector2){
        double sum = 0;


        return (3 - Math.sqrt(sum))/ 3;
    }
}
