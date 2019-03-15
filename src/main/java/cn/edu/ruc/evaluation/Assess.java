/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.evaluation;

import cn.edu.ruc.data.ConfigManager;

import java.io.*;
import java.util.*;

public class Assess {
    private static ConfigManager configManager = new ConfigManager("conf.properties");
    private static Set<String> userSet = new HashSet<>();
    private static Map<Integer, Set<String>> task2entityMap = new HashMap<>();
    private static Map<Integer, Set<String>> task2entityMap_has_assess = new HashMap<>();

    public static void main(String[] args) {
        //loadUser();

        //loadBookmark();

        loadAssess();

        loadResult();

        printTask2Result();
    }

    public static void loadAssess(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("file.assess")), "UTF-8"));
            String tmpString;
            while ((tmpString = reader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                int taskId = Integer.parseInt(tokens[0]);
                String entity = tokens[1];

                if(!task2entityMap_has_assess.containsKey(taskId))
                    task2entityMap_has_assess.put(taskId, new HashSet<>());

                task2entityMap_has_assess.get(taskId).add(entity);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadResult(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("file.result")), "UTF-8"));
            String tmpString;
            while ((tmpString = reader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                int taskId = Integer.parseInt(tokens[0]);
                String entity = tokens[2];

                if(!task2entityMap.containsKey(taskId))
                    task2entityMap.put(taskId, new HashSet<>());

                if(!task2entityMap_has_assess.containsKey(entity))
                    task2entityMap.get(taskId).add(entity);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printTask2Result() {
        for(Map.Entry<Integer, Set<String>> task2entityEntry : task2entityMap.entrySet()) {
            for(String entity : task2entityEntry.getValue())
                System.out.println(task2entityEntry.getKey() + "\t" + entity + "\t" + "https://en.wikipedia.org/wiki/" + entity + "\t");
        }
    }

    public static void loadUser(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("dir.log") + configManager.getValue("file.user.log")), "UTF-8"));
            String tmpString;
            while((tmpString = reader.readLine()) != null) {
                userSet.add(tmpString);
            }
            reader.close();
            System.out.println("User size: " + userSet.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBookmark(){
        try {
            for(String user : userSet) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("dir.log") + user + "/" + configManager.getValue("file.bookmark.log")), "UTF-8"));
                String tmpString;
                while ((tmpString = reader.readLine()) != null) {
                    String[] tokens = tmpString.split("\t");
                    if (userSet.contains(tokens[0])) {
                        if(!task2entityMap.containsKey(Integer.parseInt(tokens[1])))
                            task2entityMap.put(Integer.parseInt(tokens[1]), new HashSet<>());

                        task2entityMap.get(Integer.parseInt(tokens[1])).add(tokens[3]);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
