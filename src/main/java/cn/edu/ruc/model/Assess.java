/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.model;

import cn.edu.ruc.domain.Task;

import java.util.List;

public class Assess {
    private List<Task> taskList;

    public Assess(List<Task> taskList) {
        setTaskList(taskList);
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public String toString() {
        return "Assess{" +
                "\n\ttaskList=" + taskList +
                '}';
    }
}
