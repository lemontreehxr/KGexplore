/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.evaluation;

import cn.edu.ruc.core.DataUtil;
import cn.edu.ruc.core.Parser;
import cn.edu.ruc.core.Ranker;
import cn.edu.ruc.data.ConfigManager;
import cn.edu.ruc.domain.Entity;
import cn.edu.ruc.domain.Feature;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class Rank {
    private static ConfigManager configManager = new ConfigManager("conf.properties");
    private static DataUtil dataUtil = new DataUtil();


    public static void main(String[] args) {
        getResult();
    }

    public static void getResult(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("file.queries")), "UTF-8"));
            String tmpString;
            Map<Integer, Set<String>> task2queryContentSetMap = new HashMap<>();
            while ((tmpString = reader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");
                int taskId = Integer.parseInt(tokens[0]);
                String queryContent = tokens[3];

                if(!task2queryContentSetMap.containsKey(taskId))
                    task2queryContentSetMap.put(taskId, new HashSet<>());

                task2queryContentSetMap.get(taskId).add(queryContent);
            }
            reader.close();

            for(Map.Entry<Integer, Set<String>> task2queryContentSetEntry : task2queryContentSetMap.entrySet()) {
                int taskId = task2queryContentSetEntry.getKey();
                for(String queryContent : task2queryContentSetEntry.getValue()) {
                    for (Entity entity : getEntityList(queryContent)) {
                        System.out.println(taskId + "\t" + queryContent + "\t" + entity.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Entity> getEntityList(String queryContent)  {
        String keywords = "";
        List<String> queryEntityStringList = new ArrayList<>();
        List<String> queryFeatureStringList = new ArrayList<>();

        for(String parameter : queryContent.substring(1, queryContent.length() - 1).split("&")) {
            String[] tokens = parameter.split("=");
            try {
                if(tokens[0].equals("keywords") && tokens.length == 2) {
                    keywords = URLDecoder.decode(tokens[1], "UTF-8");
                }
                else if(tokens[0].equals("queryEntities") && tokens.length == 2) {
                    queryEntityStringList.add(URLDecoder.decode(tokens[1], "UTF-8"));
                }
                else if(tokens[0].equals("queryFeatures") && tokens.length == 2) {
                    queryFeatureStringList.add(URLDecoder.decode(tokens[1], "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        List<Entity> queryEntityList = Parser.encodeEntityList(queryEntityStringList);
        List<Feature> queryFeatureList = Parser.encodeFeatureList(queryFeatureStringList);

        return Ranker.getEntityList(keywords, queryEntityList, queryFeatureList);
    }
}
