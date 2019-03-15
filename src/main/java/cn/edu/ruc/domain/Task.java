/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.domain;

public class Task {
    private int id;
    private String description;
    private int size;
    private int versionId;

    public Task(int id, String description, int size) {
        setId(id);
        setDescription(description);
        setSize(size);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", versionId=" + versionId +
                '}';
    }
}
