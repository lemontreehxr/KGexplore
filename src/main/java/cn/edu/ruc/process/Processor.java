package cn.edu.ruc.process;


import cn.edu.ruc.data.ConfigManager;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Processor {
    private static Parser parser = new Parser();
    private static HashSet<String> entityHasAbstractSet = new HashSet<>();
    private static HashSet<String> relationSet = new HashSet<>();
    private static HashSet<String> entitySet = new HashSet<>();
    private static Map<Integer, Map<Integer, Set<Integer>>> sourceId2relationId2targetIdMap = new HashMap<>();
    private static Map<Integer, Map<Integer, Map<Integer, Integer>>> direction2tripleMap = new HashMap<>();
    private static Map<String, Integer> entity2id = new HashMap<>();
    private static Map<String, Integer> relation2id = new HashMap<>();
    private static ConfigManager configManager;

    public static void main(String[] args) throws IOException {
        configManager = new ConfigManager("conf.properties");
        String commonPath = configManager.getValue("dir") + configManager.getValue("dir.raw");

        String[] fileList = {
                "instance_mapping/mappingbased_properties_cleaned_en.nt",
                "instance_category/article_categories_en.ttl",
                "instance_type/instance_types_en.ttl"
        };

        /*filterAbstract(commonPath + "instance_abstract/long_abstracts_en.ttl", configManager.getValue("dir") + configManager.getValue("file.abstract"));

        for(String file : fileList)
            filterMapping(commonPath + file);

        writeName2Id(configManager.getValue("dir") + configManager.getValue("file.entity"), entitySet);
        writeName2Id(configManager.getValue("dir") + configManager.getValue("file.relation"), relationSet);

        //loadName2Id(configManager.getValue("dir") + configManager.getValue("file.entity"), entity2id);
        //loadName2Id(configManager.getValue("dir") + configManager.getValue("file.relation"), relation2id);
        BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configManager.getValue("dir") + configManager.getValue("file.triple")), "UTF-8"));
        for(String file : fileList)
            translateTriple(commonPath + file, bfWriter);
        bfWriter.close();*/

        writeStatistic(configManager.getValue("dir") + configManager.getValue("file.triple"), configManager.getValue("dir") + configManager.getValue("file.statistic"));
    }

    private static void filterAbstract(String inputPath, String outputPath){
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
            BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8"));

            String tmpString;
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);
                if (tokens != null) {
                    entityHasAbstractSet.add(tokens[0]);
                    entitySet.add(tokens[0]);
                    bfWriter.write(tokens[0] + "\t" + tokens[2] + "\n");
                }
            }
            bfReader.close();
            bfWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Filtering abstract is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    private static void filterMapping(String inputPath){
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));

            String tmpString;
            if (inputPath.endsWith("instance_mapping/mappingbased_properties_cleaned_en.nt")) {
                while ((tmpString = bfReader.readLine()) != null) {
                    String[] tokens = parser.parse(tmpString);
                    if (tokens != null) {
                        if (entityHasAbstractSet.contains(tokens[0]) && entityHasAbstractSet.contains(tokens[2])) {
                            entitySet.add(tokens[0]);
                            entitySet.add(tokens[2]);
                            relationSet.add(tokens[1]);
                        }
                    }
                }
            } else {
                while ((tmpString = bfReader.readLine()) != null) {
                    String[] tokens = parser.parse(tmpString);
                    if (tokens != null) {
                        if (entityHasAbstractSet.contains(tokens[0])) {
                            entitySet.add(tokens[0]);
                            entitySet.add(tokens[2]);
                            relationSet.add(tokens[1]);
                        }
                    }
                }
            }
            bfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Encoding entity and relation is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    private static void writeName2Id(String outputPath, HashSet<String> nameSet){
        long beginTime = System.currentTimeMillis();

        try {
            BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8"));

            int count = 0;
            for(String name : nameSet)
                bfWriter.write(name + "\t" + count ++ + "\n");

            bfWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Writing name to id is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    private static void loadName2Id(String inputPath, Map<String, Integer> name2id) {
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));

            String tmpString;
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                name2id.put(tokens[0], Integer.parseInt(tokens[1]));
            }
            bfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Load name to id is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    private static void translateTriple(String inputPath, BufferedWriter bufferedWriter){
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));

            String tmpString;
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);

                if(tokens != null && entity2id.containsKey(tokens[0]) && relation2id.containsKey(tokens[1]) && entity2id.containsKey(tokens[2])) {
                    int sourceId = entity2id.get(tokens[0]);
                    int relationId = relation2id.get(tokens[1]);
                    int targetId = entity2id.get(tokens[2]);

                    bufferedWriter.write(sourceId + "\t" + relationId + "\t" + targetId + "\n");
                }
            }
            bfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Translating triple is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    private static void writeStatistic(String inputPath, String outputPath) {
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));

            String tmpString;
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                int sourceId = Integer.parseInt(tokens[0]);
                int relationId = Integer.parseInt(tokens[1]);
                int targetId = Integer.parseInt(tokens[2]);

                for (int direction : new int[]{1, -1}) {
                    if (!direction2tripleMap.containsKey(direction))
                        direction2tripleMap.put(direction, new HashMap<>());

                    Map<Integer, Map<Integer, Integer>> tripleMap = direction2tripleMap.get(direction);
                    if (direction == -1) {
                        int tmp = sourceId;
                        sourceId = targetId;
                        targetId = tmp;
                    }

                    if (!tripleMap.containsKey(relationId)) {
                        tripleMap.put(relationId, new HashMap<>());
                    } else {
                        if (!tripleMap.get(relationId).containsKey(sourceId))
                            tripleMap.get(relationId).put(sourceId, 1);
                        else
                            tripleMap.get(relationId).put(sourceId, tripleMap.get(relationId).get(sourceId) + 1);
                    }
                }
            }
            bfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Translating triple is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );

        try {
            BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8"));

            for(Map.Entry<Integer, Map<Integer, Map<Integer, Integer>>> direction2tripleEntry: direction2tripleMap.entrySet()){
                int direction = direction2tripleEntry.getKey();
                for(Map.Entry<Integer, Map<Integer, Integer>> relation2subject2sizeEntry : direction2tripleEntry.getValue().entrySet()){
                    int relationId = relation2subject2sizeEntry.getKey();
                    for(Map.Entry<Integer, Integer> subject2sizeEntry : relation2subject2sizeEntry.getValue().entrySet())
                        bfWriter.write(direction + "\t" + relationId + "\t" + subject2sizeEntry.getKey() + "\t" + subject2sizeEntry.getValue() + "\n");
                }
            }
            bfWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
