package cn.edu.ruc.domain;

public class Pair {
    private String relation;
    private String entity;

    public Pair(String relation, String entity) {
        this.relation = relation;
        this.entity = entity;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "relation='" + relation + '\'' +
                ", entity='" + entity + '\'' +
                '}';
    }
}
