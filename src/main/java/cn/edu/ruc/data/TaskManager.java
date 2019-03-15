/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.data;

import cn.edu.ruc.domain.Task;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    public Map<Integer, Task> taskMap = new HashMap<>();

    public TaskManager(String inputPath){
        loadTask(inputPath);
    }

    public void loadTask(String inputPath){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
            String tmpString;
            while((tmpString = reader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                taskMap.put(Integer.parseInt(tokens[0]), new Task(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2])));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }
}
