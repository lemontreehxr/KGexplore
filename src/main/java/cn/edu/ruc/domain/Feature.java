package cn.edu.ruc.domain;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class Feature {
    private Entity entity;
    private Relation relation;
    private Set<Entity> sourceEntitySet;
    private double score;
    private int rank;

    public Feature(Entity entity, Relation relation){
        setEntity(entity);
        setRelation(relation);
        setScore(1);
    }

    public Feature(Entity entity, Relation relation, double score){
        setEntity(entity);
        setRelation(relation);
        setScore(score);
    }

    @Override
    public boolean equals(Object object){
        if(!(object instanceof Feature))
            return false;
        if(object == this)
            return true;

        if(entity.getId() != ((Feature) object).getEntity().getId())
            return false;
        if (relation.getDirection() != ((Feature) object).getRelation().getDirection() || relation.getId() != ((Feature) object).getRelation().getId())
            return false;

        return true;
    }

    public int hashCode(){
        int value = entity.getId() + relation.getId() * relation.getDirection();

        return value;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Set<Entity> getSourceEntitySet() {
        return sourceEntitySet;
    }

    public void setSourceEntitySet(Set<Entity> sourceEntitySet) {
        this.sourceEntitySet = sourceEntitySet;
    }

    public void addSourceEntity(Entity sourceEntity) {
        if(sourceEntitySet == null)
            sourceEntitySet = new HashSet<>();
        sourceEntitySet.add(sourceEntity);
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        String s = "Feature{" +
                "relation=" + relation +
                ", entity=" + entity +
                ", score=" + new DecimalFormat("0.000").format(score) +
                ", rank=" + rank +
                '}';

        if(sourceEntitySet != null) {
            for (Entity sourceEntity : sourceEntitySet)
                s += "\n\t\t" + sourceEntity;
        }

        return s;
    }
}
