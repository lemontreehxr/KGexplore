package cn.edu.ruc.model;

import cn.edu.ruc.domain.Entity;
import cn.edu.ruc.domain.Explanation;
import cn.edu.ruc.domain.Feature;

import java.util.List;

public class Result {
    private Query query;
    private List<Entity> entityList;
    private List<Feature> featureList;
    private Explanation explanation;

    public Result(Query query, List<Entity> entityList, List<Feature> featureList, Explanation explanation) {
        setQuery(query);
        setEntityList(entityList);
        setFeatureList(featureList);
        setExplanation(explanation);
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
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

    public Explanation getExplanation() {
        return explanation;
    }

    public void setExplanation(Explanation explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        String s = "\nResult{";

        s += "\n\tquery=" + query;

        if(entityList != null) {
            s += "\n\tentityList=";
            for (Entity entity : entityList)
                s += "\n\t\t" + entity;
        }

        if(featureList != null) {
            s += "\n\tfeatureList=";
            for (Feature feature : featureList)
                s += "\n\t\t" + feature;
        }

        if(explanation != null) {
            s += "\n\texplanation=";
            s += "\n\t\t" + explanation;
        }

        s += "\n}";

        return s;
    }
}
