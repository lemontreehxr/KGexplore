package cn.edu.ruc.domain;

import java.util.ArrayList;
import java.util.List;

public class DocumentOfEntity {
    private String uri;
    private String context = "";
    private List<String> labelList = new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();
    private List<String> similarEntityList = new ArrayList<>();
    private List<Pair> relatedAttributeList = new ArrayList<>();
    private List<Pair> relatedEntityList = new ArrayList<>();

    public DocumentOfEntity(){

    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
    }

    public void addLabel(String label) {
        labelList.add(label);
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public void addCategory(String category) {
        categoryList.add(category);
    }

    public List<String> getSimilarEntityList() {
        return similarEntityList;
    }

    public void setSimilarEntityList(List<String> similarEntityList) {
        this.similarEntityList = similarEntityList;
    }

    public void addSimilarEntity(String similarEntity) {
        similarEntityList.add(similarEntity);
    }

    public List<Pair> getRelatedAttributeList() {
        return relatedAttributeList;
    }

    public void setRelatedAttributeList(List<Pair> relatedAttributeList) {
        this.relatedAttributeList = relatedAttributeList;
    }

    public void addRelatedAttribute(Pair relatedAttribute) {
        relatedAttributeList.add(relatedAttribute);
    }

    public List<Pair> getRelatedEntityList() {
        return relatedEntityList;
    }

    public void setRelatedEntityList(List<Pair> relatedEntityList) {
        this.relatedEntityList = relatedEntityList;
    }

    public void addRelatedEntity(Pair relatedEntity) {
        relatedEntityList.add(relatedEntity);
    }

    @Override
    public String toString() {
        return "Document{" +
                "uri='" + uri + '\'' +
                ", context='" + context + '\'' +
                ", labelList=" + labelList +
                ", categoryList=" + categoryList +
                ", similarEntityList=" + similarEntityList +
                ", relatedAttributeList=" + relatedAttributeList +
                ", relatedEntityList=" + relatedEntityList +
                '}';
    }
}
