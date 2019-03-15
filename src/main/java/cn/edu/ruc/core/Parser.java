package cn.edu.ruc.core;

import cn.edu.ruc.domain.Entity;
import cn.edu.ruc.domain.Feature;
import cn.edu.ruc.domain.Relation;
import cn.edu.ruc.ultity.URLComponent;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static URLComponent urlComponent = new URLComponent("UTF-8");

    //important: pay attention to the strings such ""
    public static Entity encodeEntity(String entityString) {
        entityString = urlComponent.decode(entityString);
        return DataUtil.getEntity2Id(entityString) == -1 ? null : new Entity(DataUtil.getEntity2Id(entityString), entityString);
    }

    public static Entity encodeCategory(String entityString) {
        entityString = urlComponent.decode(entityString);
        return DataUtil.getEntity2Id("Category:" + entityString) == -1 ? null : new Entity(DataUtil.getEntity2Id("Category:" + entityString), entityString);
    }

    public static List<Entity> encodeEntityList(List<String> entityStringList) {
        List<Entity> entityList = new ArrayList<>();

        for(String entityString : entityStringList) {
            Entity entity = encodeEntity(entityString);

            if(entity != null) {
                entityList.add(entity);
            }
        }

        return entityList;
    }

    public static Relation encodeRelation(String relationString, int direction) {
        return DataUtil.getRelation2Id(relationString) == -1 ? null : new Relation(DataUtil.getRelation2Id(relationString), direction, relationString);
    }

    public static Feature encodeFeature(String featureString) {
        featureString = urlComponent.decode(featureString);

        String[] tokens = featureString.split("##");
        String entityString = tokens[0];
        String relationString = tokens[1];
        int relationDirection = (tokens.length == 3) ? Integer.parseInt(tokens[2]) : -1;

        Entity entity;
        if(relationString.equals("subject"))
            entity = encodeCategory(entityString);
        else
            entity = encodeEntity(entityString);

        Relation relation = encodeRelation(relationString, relationDirection);

        return entity != null && relation != null ? new Feature(entity, relation) : null;
    }

    public static List<Feature> encodeFeatureList(List<String> featureStringList) {
        List<Feature> featureList = new ArrayList<>();

        for(String featureString : featureStringList) {
            Feature feature = encodeFeature(featureString);

            if(feature != null) {
                featureList.add(feature);
            }
        }

        return featureList;
    }

    //decode
    public static void decodeEntity(Entity entity){
        if(entity != null && DataUtil.getId2Entity(entity.getId()) != null) {
            entity.setName(DataUtil.getId2Entity(entity.getId()).replaceAll("Category:", ""));
        }
    }

    public static void decodeRelation(Relation relation){
        if(relation != null) {
            relation.setName(DataUtil.getId2Relation(relation.getId()));
        }
    }

    public static void decodeFeature(Feature feature){
        if(feature != null) {
            decodeEntity(feature.getEntity());
            decodeRelation(feature.getRelation());
        }
    }
}
