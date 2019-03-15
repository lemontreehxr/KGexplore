package cn.edu.ruc.model;

import cn.edu.ruc.domain.Entity;
import cn.edu.ruc.domain.Feature;

import java.util.List;

public class Profile {
    private Entity entity;
    private List<Feature> featureList;

    public Profile(Entity entity, List<Feature> featureList) {
        setEntity(entity);
        setFeatureList(featureList);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public List<Feature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<Feature> featureList) {
        this.featureList = featureList;
    }

    @Override
    public String toString() {
        String s = "\n\tProfile{";
        s += "\n\t\tentity=\n\t\t\t" + getEntity() + "\n\t\tfeatureList=";
        for(Feature feature: getFeatureList())
            s += "\n\t\t\t" + feature;
        s += "\n\t}";

        return s;
    }
}
