/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.core;

import cn.edu.ruc.domain.Task;

import java.util.ArrayList;
import java.util.List;

public class Assessor {
    public static List<Task> getTaskList(int id) {
        List<Task> taskList = new ArrayList<>();
        for(int i = 0; i < DataUtil.Task_Size; i ++) {
            Task task = DataUtil.getTask((id + i) % DataUtil.Task_Size + 1);
            int versionId = (id / DataUtil.Task_Size + i) % DataUtil.Version_Size + 1;
            task.setVersionId(versionId);
            taskList.add(task);
        }

        return taskList;
    }
}
