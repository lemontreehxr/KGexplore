package cn.edu.ruc.model;

import cn.edu.ruc.domain.Entity;
import cn.edu.ruc.domain.Feature;

import java.util.List;

public class Query {
    private String keywords;
    private List<Entity> entityList;
    private List<Feature> featureList;

    public Query(String keywords, List<Entity> entityList, List<Feature> featureList) {
        setKeywords(keywords);
        setEntityList(entityList);
        setFeatureList(featureList);
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

    @Override
    public String toString() {
        String s = "\n\tQuery{";

        if(keywords != "" && keywords != null) {
            s += "\n\t\tkeywords=" + keywords;
        }
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
        s += "\n\t}";

        return s;
    }
}
