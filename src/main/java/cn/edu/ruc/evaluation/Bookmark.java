/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.evaluation;


public class Bookmark {
    private String userId;
    private int taskId;
    private int versionId;
    private String entity;
    private int conf;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public int getConf() {
        return conf;
    }

    public void setConf(int conf) {
        this.conf = conf;
    }

    @Override
    public String toString() {
        return "Bookmark{" +
                "userId='" + userId + '\'' +
                ", taskId=" + taskId +
                ", versionId=" + versionId +
                ", entity='" + entity + '\'' +
                ", conf=" + conf +
                '}';
    }
}
