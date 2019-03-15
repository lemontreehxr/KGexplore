package cn.edu.ruc.domain;

import java.util.List;

public class Explanation {
    private List<Entity> entityList;
    private List<Feature> featureList;
    private List<List<Double>> scoreListList;

    public Explanation(List<Entity> entityList, List<Feature> featureList, List<Double> scoreList) {
        setEntityList(entityList);
        setFeatureList(featureList);
        setScoreListList(scoreListList);
    }

    public Explanation(List<List<Double>> scoreListList) {
        setScoreListList(scoreListList);
    }

    public List<Entity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<Entity> entityList) {
        this.entityList = entityList;
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<Feature> featureList) {
        this.featureList = featureList;
    }

    public List<List<Double>> getScoreListList() {
        return scoreListList;
    }

    public void setScoreListList(List<List<Double>> scoreListList) {
        this.scoreListList = scoreListList;
    }

    @Override
    public String toString() {
        String s = "Explanation{";
        if(entityList != null) {
            s += "\n\t\tentityList=";
            for (Entity entity : entityList)
                s += "\n\t\t\t" + entity;
        }

        if(featureList != null) {
            s += "\n\t\tfeatureList=";
            for (Feature feature : featureList)
                s += "\n\t\t\t" + feature;
        }

        if(scoreListList != null) {
            s += "\n\t\tscoreListList=";
            for(List<Double> scoreList : scoreListList) {
                s += "\t\t\t";
                for (double score : scoreList)
                    s += "[" + score + "]";
                s += "\n";
            }
        }

        s += '}';

        return s;
    }
}
