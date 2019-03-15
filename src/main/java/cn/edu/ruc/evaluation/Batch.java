/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.evaluation;

import cn.edu.ruc.data.ConfigManager;
import cn.edu.ruc.ultity.URLComponent;

import java.io.*;
import java.util.*;

public class Batch {
    private static ConfigManager configManager = new ConfigManager("conf.properties");
    private static URLComponent urlComponent = new URLComponent("UTF-8");
    private static Set<String> userSet = new HashSet<>();
    private static Map<Integer, Map<String, Integer>> task2entity2relevanceMap = new HashMap<>();
    private static Map<Integer, Map<Integer, Map<String, List<String>>>> task2version2user2queryListMap = new HashMap<>();
    private static Map<Integer, Map<Integer, Map<String, List<String>>>> task2version2user2optionListMap = new HashMap<>();
    private static Map<Integer, Map<Integer, Map<String, Map<String, List<String>>>>> task2version2user2option2queryListMap = new HashMap<>();
    private static Map<Integer, Map<String, List<String>>> task2query2entityListMap = new HashMap<>();
    private static Map<Integer, Map<Integer, Map<String, List<Bookmark>>>> task2version2user2bookmarkListMap = new HashMap<>();
    private static Map<Integer, Map<Integer, Map<String, List<Interaction>>>> task2version2user2interactionListMap = new HashMap<>();

    public static void main(String[] args) {
        //loadUser();

        //loadRelevance();

        //loadQuery();

        //loadResult();

        //loadBookmark();

        loadInteraction2();


        //printConf();

        //printRelevance();

        //printEfficiency();
        //printRank();

        //printTime();

        //printOption();

        //printQuery();
    }

    public static void printEfficiency() {
        Metric metric = new Metric();

        for(Map.Entry<Integer, Map<Integer, Map<String, List<String>>>> task2version2user2queryListEntry : task2version2user2queryListMap.entrySet()) {
            int taskId = task2version2user2queryListEntry.getKey();
            for(Map.Entry<Integer, Map<String, List<String>>> version2user2queryListEntry : task2version2user2queryListEntry.getValue().entrySet()) {
                int versionId = version2user2queryListEntry.getKey();
                for(Map.Entry<String, List<String>> user2queryListEntry : version2user2queryListEntry.getValue().entrySet()) {
                    String userId = user2queryListEntry.getKey();
                    for(int i = 0; i < user2queryListEntry.getValue().size(); i ++) {
                        String query = user2queryListEntry.getValue().get(i);
                        String option = task2version2user2optionListMap.get(taskId).get(versionId).get(userId).get(i);
                        Map<String, Integer> standardMap = task2entity2relevanceMap.get(taskId);
                        List<String> testList = task2query2entityListMap.get(taskId).containsKey(query) ? task2query2entityListMap.get(taskId).get(query) : new ArrayList<>();

                        double pAt10 = metric.getPrecisionAtK(standardMap, testList, 10);
                        double pAt20  = metric.getPrecisionAtK(standardMap, testList, 20);
                        double RR = metric.getRR(standardMap, testList);
                        double AP = metric.getAPAtK(standardMap, testList, 20);

                        System.out.println(taskId + "\t" + versionId + "\t" + userId + "\t" + option + "\t" + query + "\t" + pAt10 + "\t" + pAt20 + "\t" + RR + "\t" + AP );
                    }
                }
                System.out.println();
            }
        }
    }

    public static void printConf() {
        System.out.println("Bookmark statistics: ");
        for(Map.Entry<Integer, Map<Integer, Map<String, List<Bookmark>>>> task2version2user2bookmarkListEntry : task2version2user2bookmarkListMap.entrySet()) {
            int taskId = task2version2user2bookmarkListEntry.getKey();
            for(Map.Entry<Integer, Map<String, List<Bookmark>>> version2user2bookmarkListEntry : task2version2user2bookmarkListEntry.getValue().entrySet()) {
                int versionId = version2user2bookmarkListEntry.getKey();
                System.out.print(taskId + "\t" + versionId);
                for(Map.Entry<String, List<Bookmark>> user2bookmarkListEntry : version2user2bookmarkListEntry.getValue().entrySet()) {
                    double bookmark_conf_sum_tmp = 0;
                    List<Bookmark> bookmarkList = user2bookmarkListEntry.getValue();
                    for (Bookmark bookmark : bookmarkList) {
                        bookmark_conf_sum_tmp += bookmark.getConf();
                    }
                    System.out.print("\t" + bookmark_conf_sum_tmp / bookmarkList.size());
                }
                System.out.println();
            }
        }
    }

    public static void printRelevance() {
        for(Map.Entry<Integer, Map<Integer, Map<String, List<Bookmark>>>> task2version2user2bookmarkListEntry : task2version2user2bookmarkListMap.entrySet()) {
            int taskId = task2version2user2bookmarkListEntry.getKey();
            for(Map.Entry<Integer, Map<String, List<Bookmark>>> version2user2bookmarkListEntry : task2version2user2bookmarkListEntry.getValue().entrySet()) {
                int versionId = version2user2bookmarkListEntry.getKey();
                System.out.print(taskId + "\t" + versionId);
                for(Map.Entry<String, List<Bookmark>> user2bookmarkListEntry : version2user2bookmarkListEntry.getValue().entrySet()) {
                    double bookmark_rel_sum_tmp = 0;
                    List<Bookmark> bookmarkList = user2bookmarkListEntry.getValue();
                    for (Bookmark bookmark : bookmarkList) {
                        if(task2entity2relevanceMap.get(taskId).containsKey(bookmark.getEntity())) {
                            bookmark_rel_sum_tmp += 1;
                        }
                    }
                    System.out.print("\t" + bookmark_rel_sum_tmp / bookmarkList.size());
                }
                System.out.println();
            }
        }
    }

    public static void printRank() {
        for(Map.Entry<Integer, Map<Integer, Map<String, List<Interaction>>>> task2version2user2interactionListEntry : task2version2user2interactionListMap.entrySet()) {
            int taskId = task2version2user2interactionListEntry.getKey();
            for(Map.Entry<Integer, Map<String, List<Interaction>>> version2user2interactionListEntry : task2version2user2interactionListEntry.getValue().entrySet()) {
                int versionId = version2user2interactionListEntry.getKey();
                System.out.print(taskId + "\t" + versionId);
                for(Map.Entry<String, List<Interaction>> user2interactionListEntry : version2user2interactionListEntry.getValue().entrySet()) {
                    List<Interaction> interactionList = user2interactionListEntry.getValue();
                    double rank_sum = 0;
                    for(Interaction interaction : interactionList) {
                        if(interaction.getOption().equals("Add-to-bookmark")) {
                            int count = 0;
                            if (task2query2entityListMap.get(taskId).containsKey(interaction.getQueryContent())){
                                for (String entity : task2query2entityListMap.get(taskId).get(interaction.getQueryContent())) {
                                    count++;
                                    if (entity.equals(interaction.getTarget().substring(1, interaction.getTarget().length() - 1)))
                                        break;
                                }
                            }
                            else {
                                //System.out.println(interaction.getQueryContent());
                                count = 20;
                            }
                            rank_sum += count;
                        }
                        else if(interaction.getOption().equals("Remove-from-bookmark")){
                            int count = 0;
                            for(String entity : task2query2entityListMap.get(taskId).get(interaction.getQueryContent())) {
                                count ++;
                                if(entity.equals(interaction.getTarget().substring(1, interaction.getTarget().length() - 1)))
                                    break;
                            }
                            rank_sum -= count;
                        }
                    }
                    System.out.print("\t" + rank_sum / 5);
                }
                System.out.println();
            }
        }
    }

    public static void printTime() {
        System.out.println("Interaction statistics: ");
        for(Map.Entry<Integer, Map<Integer, Map<String, List<Interaction>>>> task2version2user2interactionListEntry : task2version2user2interactionListMap.entrySet()) {
            int taskId = task2version2user2interactionListEntry.getKey();
            for(Map.Entry<Integer, Map<String, List<Interaction>>> version2user2interactionListEntry : task2version2user2interactionListEntry.getValue().entrySet()) {
                int versionId = version2user2interactionListEntry.getKey();
                System.out.print(taskId + "\t" + versionId);
                for(Map.Entry<String, List<Interaction>> user2interactionListEntry : version2user2interactionListEntry.getValue().entrySet()) {
                    List<Interaction> interactionList = user2interactionListEntry.getValue();

                    double task_time_tmp = (Long.parseLong(interactionList.get(interactionList.size() - 1).getTimestamp()) - Long.parseLong(interactionList.get(0).getTimestamp())) / 1000;
                    System.out.print("\t" + task_time_tmp);
                }
                System.out.println();
            }
        }
    }

    public static void printOption() {
        System.out.println("Interaction statistics: ");
        for(Map.Entry<Integer, Map<Integer, Map<String, List<Interaction>>>> task2version2user2interactionListEntry : task2version2user2interactionListMap.entrySet()) {
            int taskId = task2version2user2interactionListEntry.getKey();
            for(Map.Entry<Integer, Map<String, List<Interaction>>> version2user2interactionListEntry : task2version2user2interactionListEntry.getValue().entrySet()) {
                int versionId = version2user2interactionListEntry.getKey();
                String query_fre_s = "", keywords_fre_s = "", entity_input_fre_s = "", feature_input_fre_s = "", lookup_fre_s = "", revisit_s = "", time_s = "", query_per_min_s = "", query_fre_bookmark = "";
                for(Map.Entry<String, List<Interaction>> user2interactionListEntry : version2user2interactionListEntry.getValue().entrySet()) {
                    List<Interaction> interactionList = user2interactionListEntry.getValue();

                    List<String> queryContentList = new ArrayList<>();
                    Set<String> queryContentSet = new HashSet<>();
                    Set<String> queryContentSet_has_bookmark = new HashSet<>();
                    List<String> optionList = new ArrayList<>();
                    //Map<String, List<String>> bookmark2queryMap = new LinkedHashMap<>();
                    int keywords_fre = 0, entity_add_fre = 0, entity_delete_fre = 0, feature_add_fre = 0, feature_delete_fre = 0, lookup_fre = 0;
                    for(Interaction interaction : interactionList) {
                        if(interaction.getOption().equals("Update-keywords")){
                            keywords_fre ++;

                            queryContentList.add(interaction.getQueryContent());
                            optionList.add(interaction.getOption());
                        }
                        else if(interaction.getOption().equals("Add-an-entity")){
                            entity_add_fre ++;

                            queryContentList.add(interaction.getQueryContent());
                            optionList.add(interaction.getOption());
                        }
                        else if(interaction.getOption().equals("Delete-an-entity")){
                            entity_delete_fre ++;

                            queryContentList.add(interaction.getQueryContent());
                            optionList.add(interaction.getOption());
                        }
                        else if(interaction.getOption().equals("Add-a-feature")){
                            feature_add_fre ++;

                            queryContentList.add(interaction.getQueryContent());
                            optionList.add(interaction.getOption());
                        }
                        else if(interaction.getOption().equals("Delete-a-feature")){
                            feature_delete_fre ++;

                            queryContentList.add(interaction.getQueryContent());
                            optionList.add(interaction.getOption());
                        }

                        else if(interaction.getOption().equals("Lookup-profile")){
                            lookup_fre ++;
                        }
                        else if(interaction.getOption().equals("Add-to-bookmark")){
                            queryContentSet_has_bookmark.add(interaction.getQueryContent());
                        }
                        else if(interaction.getOption().equals("Remove-from-bookmark")){
                            queryContentSet_has_bookmark.remove(interaction.getQueryContent());
                        }
                        queryContentSet.add(interaction.getQueryContent());
                        /*
                        else if(interaction.getOption().equals("Recall-a-historical-query")){
                            recall_fre ++;

                            query_fre ++;
                            queryContentList.add(interaction.getQueryContent());
                            optionList.add(interaction.getOption());
                        }*/

                    }

                    double time = (double)(Long.parseLong(interactionList.get(interactionList.size() - 1).getTimestamp()) - Long.parseLong(interactionList.get(0).getTimestamp())) / (1000 * 60);

                    query_fre_s += "\t" + queryContentList.size();
                    keywords_fre_s += "\t" + keywords_fre;
                    entity_input_fre_s += "\t" + (entity_add_fre + entity_delete_fre);
                    feature_input_fre_s += "\t" + (feature_add_fre + feature_delete_fre);
                    lookup_fre_s += "\t" + lookup_fre;
                    revisit_s += "\t" + (queryContentList.size() - queryContentSet.size());
                    time_s += "\t" + time;
                    query_per_min_s += "\t" + (double) queryContentList.size() / time;
                    query_fre_bookmark += "\t" + (double) queryContentSet_has_bookmark.size();
                }

                System.out.println(taskId + "\t" + versionId + "\tquery fre" + query_fre_s);
                System.out.println(taskId + "\t" + versionId + "\trevisit" + revisit_s);
                System.out.println(taskId + "\t" + versionId + "\ttext input fre" + keywords_fre_s);
                System.out.println(taskId + "\t" + versionId + "\tentity input fre" + entity_input_fre_s);
                System.out.println(taskId + "\t" + versionId + "\tfeature input fre" + feature_input_fre_s);
                System.out.println(taskId + "\t" + versionId + "\tlookup fre" + lookup_fre_s);
                System.out.println(taskId + "\t" + versionId + "\ttime(min)" + time_s);
                System.out.println(taskId + "\t" + versionId + "\tquery/time" + query_per_min_s);
                System.out.println(taskId + "\t" + versionId + "\tquery fre has bookmark" + query_fre_bookmark);
            }
        }
    }

    public static void printQuery() {
        for(Map.Entry<Integer, Map<Integer, Map<String, List<Interaction>>>> task2version2user2interactionListEntry : task2version2user2interactionListMap.entrySet()) {
            int taskId = task2version2user2interactionListEntry.getKey();
            for(Map.Entry<Integer, Map<String, List<Interaction>>> version2user2interactionListEntry : task2version2user2interactionListEntry.getValue().entrySet()) {
                int versionId = version2user2interactionListEntry.getKey();
                for(Map.Entry<String, List<Interaction>> user2interactionListEntry : version2user2interactionListEntry.getValue().entrySet()) {
                    List<Interaction> interactionList = user2interactionListEntry.getValue();
                    for(Interaction interaction : interactionList) {
                        if (interaction.getOption().equals("Update-keywords") || interaction.getOption().equals("Add-an-entity") || interaction.getOption().equals("Delete-an-entity") || interaction.getOption().equals("Add-a-feature") || interaction.getOption().equals("Delete-a-feature")) {
                            System.out.println(taskId + "\t" + versionId + "\t" + interaction.getUserId() + "\t" + interaction.getOption() + "\t" + interaction.getQueryContent());
                        }
                    }
                }
            }
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

    public static void loadRelevance(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("file.assess")), "UTF-8"));
            String tmpString;
            while ((tmpString = reader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                if(!task2entity2relevanceMap.containsKey(Integer.parseInt(tokens[0])))
                    task2entity2relevanceMap.put(Integer.parseInt(tokens[0]), new HashMap<>());

                if(tokens.length == 4 && Integer.parseInt(tokens[3]) > 1)
                    task2entity2relevanceMap.get(Integer.parseInt(tokens[0])).put(urlComponent.decode(tokens[1]), Integer.parseInt(tokens[3]));
            }
            reader.close();
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
                        Bookmark bookmark = new Bookmark();
                        bookmark.setUserId(tokens[0]);
                        bookmark.setTaskId(Integer.parseInt(tokens[1]));
                        bookmark.setVersionId(Integer.parseInt(tokens[2]));
                        bookmark.setEntity(urlComponent.decode(tokens[3]));
                        bookmark.setConf(Integer.parseInt(tokens[4]));

                        if (!task2version2user2bookmarkListMap.containsKey(bookmark.getTaskId()))
                            task2version2user2bookmarkListMap.put(bookmark.getTaskId(), new HashMap<>());
                        if (!task2version2user2bookmarkListMap.get(bookmark.getTaskId()).containsKey(bookmark.getVersionId()))
                            task2version2user2bookmarkListMap.get(bookmark.getTaskId()).put(bookmark.getVersionId(), new HashMap<>());
                        if (!task2version2user2bookmarkListMap.get(bookmark.getTaskId()).get(bookmark.getVersionId()).containsKey(bookmark.getUserId()))
                            task2version2user2bookmarkListMap.get(bookmark.getTaskId()).get(bookmark.getVersionId()).put(bookmark.getUserId(), new ArrayList<>());

                        task2version2user2bookmarkListMap.get(bookmark.getTaskId()).get(bookmark.getVersionId()).get(bookmark.getUserId()).add(bookmark);
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadInteraction2(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + "interaction.txt"), "UTF-8"));
            String tmpString;
            while ((tmpString = reader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                int taskId = Integer.parseInt(tokens[0]);
                int versionId = Integer.parseInt(tokens[1]);
                String userId = tokens[2];
                String option = tokens[3];
                String query = tokens[4];

                if (!task2version2user2option2queryListMap.containsKey(taskId))
                    task2version2user2option2queryListMap.put(taskId, new HashMap<>());
                if (!task2version2user2option2queryListMap.get(taskId).containsKey(versionId))
                    task2version2user2option2queryListMap.get(taskId).put(versionId, new HashMap<>());
                if (!task2version2user2option2queryListMap.get(taskId).get(versionId).containsKey(userId))
                    task2version2user2option2queryListMap.get(taskId).get(versionId).put(userId, new HashMap<>());
                if (!task2version2user2option2queryListMap.get(taskId).get(versionId).get(userId).containsKey(option))
                    task2version2user2option2queryListMap.get(taskId).get(versionId).get(userId).put(option, new ArrayList<>());

                task2version2user2option2queryListMap.get(taskId).get(versionId).get(userId).get(option).add(query);
            }
            reader.close();

            for(Map.Entry<Integer, Map<Integer, Map<String, Map<String, List<String>>>>> task2version2user2option2queryListMapEntry : task2version2user2option2queryListMap.entrySet()) {
                int taskId = task2version2user2option2queryListMapEntry.getKey();
                for(Map.Entry<Integer, Map<String, Map<String, List<String>>>> version2user2option2queryListMapEntry : task2version2user2option2queryListMapEntry.getValue().entrySet()) {
                    int versionId = version2user2option2queryListMapEntry.getKey();
                    String query_fre_s = "", keywords_fre_s = "", entity_input_fre_s = "", feature_input_fre_s = "", revisit_s = "";
                    for(Map.Entry<String, Map<String, List<String>>> user2option2queryListMapEntry : version2user2option2queryListMapEntry.getValue().entrySet()) {
                        Set<String> queryContentSet = new HashSet<>();
                        int keywords_fre = 0, entity_add_fre = 0, entity_delete_fre = 0, feature_add_fre = 0, feature_delete_fre = 0;
                        for(Map.Entry<String, List<String>> option2queryListMapEntry: user2option2queryListMapEntry.getValue().entrySet()) {
                            if(option2queryListMapEntry.getKey().equals("Update-keywords")){
                                for(String query : option2queryListMapEntry.getValue()) {
                                    keywords_fre ++;
                                    queryContentSet.add(query);
                                }
                            }
                            else if(option2queryListMapEntry.getKey().equals("Add-an-entity")){
                                for(String query : option2queryListMapEntry.getValue()) {
                                    entity_add_fre ++;
                                    queryContentSet.add(query);
                                }
                            }
                            else if(option2queryListMapEntry.getKey().equals("Delete-an-entity")){
                                for(String query : option2queryListMapEntry.getValue()) {
                                    entity_delete_fre ++;
                                    queryContentSet.add(query);
                                }
                            }
                            else if(option2queryListMapEntry.getKey().equals("Add-a-feature")){
                                for(String query : option2queryListMapEntry.getValue()) {
                                    feature_add_fre ++;
                                    queryContentSet.add(query);
                                }
                            }
                            else if(option2queryListMapEntry.getKey().equals("Delete-a-feature")){
                                for(String query : option2queryListMapEntry.getValue()) {
                                    feature_delete_fre ++;
                                    queryContentSet.add(query);
                                }
                            }
                        }
                        query_fre_s += "\t" + (keywords_fre + entity_add_fre + entity_delete_fre + feature_add_fre + feature_delete_fre);
                        keywords_fre_s += "\t" + keywords_fre;
                        entity_input_fre_s += "\t" + (entity_add_fre + entity_delete_fre);
                        feature_input_fre_s += "\t" + (feature_add_fre + feature_delete_fre);
                        revisit_s += "\t" + (keywords_fre + entity_add_fre + entity_delete_fre + feature_add_fre + feature_delete_fre - queryContentSet.size());
                    }

                    System.out.println(taskId + "\t" + versionId + "\tquery fre" + query_fre_s);
                    System.out.println(taskId + "\t" + versionId + "\trevisit" + revisit_s);
                    System.out.println(taskId + "\t" + versionId + "\ttext input fre" + keywords_fre_s);
                    System.out.println(taskId + "\t" + versionId + "\tentity input fre" + entity_input_fre_s);
                    System.out.println(taskId + "\t" + versionId + "\tfeature input fre" + feature_input_fre_s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadInteraction(){
        try {
            for(String user : userSet) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("dir.log") + user + "/" + configManager.getValue("file.interaction.log")), "UTF-8"));
                String tmpString;
                while ((tmpString = reader.readLine()) != null) {
                    String[] tokens = tmpString.split("\t");
                    if (userSet.contains(tokens[0])) {
                        Interaction interaction = new Interaction();
                        interaction.setUserId(tokens[0]);
                        interaction.setTaskId(Integer.parseInt(tokens[1]));
                        interaction.setVersionId(Integer.parseInt(tokens[2]));
                        interaction.setOption(tokens[3]);
                        interaction.setTarget(urlComponent.decode(tokens[4]));
                        interaction.setQueryContent(tokens[5]);
                        interaction.setTimestamp(tokens[6]);

                        if (!task2version2user2interactionListMap.containsKey(interaction.getTaskId()))
                            task2version2user2interactionListMap.put(interaction.getTaskId(), new HashMap<>());
                        if (!task2version2user2interactionListMap.get(interaction.getTaskId()).containsKey(interaction.getVersionId()))
                            task2version2user2interactionListMap.get(interaction.getTaskId()).put(interaction.getVersionId(), new HashMap<>());
                        if (!task2version2user2interactionListMap.get(interaction.getTaskId()).get(interaction.getVersionId()).containsKey(interaction.getUserId()))
                            task2version2user2interactionListMap.get(interaction.getTaskId()).get(interaction.getVersionId()).put(interaction.getUserId(), new ArrayList<>());

                        task2version2user2interactionListMap.get(interaction.getTaskId()).get(interaction.getVersionId()).get(interaction.getUserId()).add(interaction);
                    }
                }
                reader.close();
            }
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
                String query = tokens[1];
                String entity = tokens[2];

                if(!task2query2entityListMap.containsKey(taskId))
                    task2query2entityListMap.put(taskId, new HashMap<>());
                if(!task2query2entityListMap.get(taskId).containsKey(query))
                    task2query2entityListMap.get(taskId).put(query, new ArrayList<>());

                task2query2entityListMap.get(taskId).get(query).add(entity);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadQuery(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("file.queries")), "UTF-8"));
            String tmpString;
            while ((tmpString = reader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                int taskId = Integer.parseInt(tokens[0]);
                int versionId = Integer.parseInt(tokens[1]);
                String userId = tokens[2];
                String option = tokens[3];
                String query = tokens[4];

                if (!task2version2user2queryListMap.containsKey(taskId))
                    task2version2user2queryListMap.put(taskId, new HashMap<>());
                if (!task2version2user2queryListMap.get(taskId).containsKey(versionId))
                    task2version2user2queryListMap.get(taskId).put(versionId, new HashMap<>());
                if (!task2version2user2queryListMap.get(taskId).get(versionId).containsKey(userId))
                    task2version2user2queryListMap.get(taskId).get(versionId).put(userId, new ArrayList<>());

                task2version2user2queryListMap.get(taskId).get(versionId).get(userId).add(query);

                if (!task2version2user2optionListMap.containsKey(taskId))
                    task2version2user2optionListMap.put(taskId, new HashMap<>());
                if (!task2version2user2optionListMap.get(taskId).containsKey(versionId))
                    task2version2user2optionListMap.get(taskId).put(versionId, new HashMap<>());
                if (!task2version2user2optionListMap.get(taskId).get(versionId).containsKey(userId))
                    task2version2user2optionListMap.get(taskId).get(versionId).put(userId, new ArrayList<>());

                task2version2user2optionListMap.get(taskId).get(versionId).get(userId).add(option);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
