/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.evaluation;

public class Interaction {
    private String userId;
    private int taskId;
    private int versionId;
    private String option;
    private String target;
    private String queryContent;
    private String timestamp;

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

    public String getOption() {
        return option;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "userId='" + userId + '\'' +
                ", taskId=" + taskId +
                ", versionId=" + versionId +
                ", option='" + option + '\'' +
                ", target='" + target + '\'' +
                ", queryContent='" + queryContent + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
