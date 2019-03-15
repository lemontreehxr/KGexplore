package cn.edu.ruc.process;

import cn.edu.ruc.data.ConfigManager;
import cn.edu.ruc.domain.DocumentOfEntity;
import cn.edu.ruc.domain.Pair;
import com.google.gson.Gson;

import java.io.*;
import java.util.*;

public class DocumentGenerator {
    private static ConfigManager configManager = new ConfigManager("conf.properties");
    private static Parser parser = new Parser();
    private static Gson gson = new Gson();
    //private static Map<String, String> entity2labelMap = new HashMap<>();
    //private static Map<String, String> category2labelMap = new HashMap<>();
    private static Map<String, DocumentOfEntity> entity2documentMap = new HashMap<>();

    public static void main(String[] args){

        //loadEntity2Label(configManager.getValue("dir") + configManager.getValue("dir.raw") + "instance_label/labels_en.ttl");

        //loadCategory2Label(configManager.getValue("dir") + configManager.getValue("dir.raw") + "instance_label/category_labels_en.ttl");

        loadEntity2Abstract(configManager.getValue("dir") + configManager.getValue("dir.raw") + "instance_abstract/long_abstracts_en.ttl");

        loadEntity2Redirect(configManager.getValue("dir") + configManager.getValue("dir.raw") + "instance_redirect/redirects_en.ttl");

        loadEntity2Disambiguation(configManager.getValue("dir") + configManager.getValue("dir.raw") + "instance_disambiguation/disambiguations_en.ttl");

        loadEntity2Category(configManager.getValue("dir") + configManager.getValue("dir.raw") + "instance_category/article_categories_en.ttl");

        loadEntity2Triple(configManager.getValue("dir") + configManager.getValue("dir.raw") + "instance_mapping/mappingbased_properties_cleaned_en.nt");

        writeDocument(configManager.getValue("dir") + configManager.getValue("dir.document"));

        rewriteDocument();
    }

    /*public static void loadEntity2Label(String inputPath){
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
            String tmpString;
            while((tmpString = reader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);
                if(tokens != null) {
                    if(entity2labelMap.containsKey(tokens[0]))
                        System.out.println("Error:");

                    entity2labelMap.put(tokens[0], tokens[2]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Loading entity label is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    public static void loadCategory2Label(String inputPath){
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
            String tmpString;
            while((tmpString = reader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);
                if(tokens != null) {
                    if(category2labelMap.containsKey(tokens[0]))
                        System.out.println("Error:");

                    category2labelMap.put(tokens[0], tokens[2]);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Loading category label is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }*/

    private static void loadEntity2Abstract(String inputPath){
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));

            String tmpString;
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);
                if (tokens != null) {
                    DocumentOfEntity document = new DocumentOfEntity();
                    document.setUri(tokens[0]);
                    //document.setContext(tokens[2]);

                    entity2documentMap.put(tokens[0], document);
                }
            }
            bfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Loading abstract is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    public static void loadEntity2Category(String inputPath) {
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
            String tmpString;
            while((tmpString = reader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);
                if(tokens != null && entity2documentMap.containsKey(tokens[0])) {
                    entity2documentMap.get(tokens[0]).addCategory(tokens[2]);
                }
            }
            reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Loading category is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    public static void loadEntity2Redirect(String inputPath) {
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
            String tmpString;
            while((tmpString = reader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);
                if(tokens != null && entity2documentMap.containsKey(tokens[2])) {
                    entity2documentMap.get(tokens[2]).addSimilarEntity(tokens[0]);
                }
            }
            reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Loading redirect is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    public static void loadEntity2Disambiguation(String inputPath) {
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
            String tmpString;
            while((tmpString = reader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);
                if(tokens != null && entity2documentMap.containsKey(tokens[2])) {
                    entity2documentMap.get(tokens[2]).addSimilarEntity(tokens[0]);
                }
            }
            reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Loading disambiguation is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    private static void loadEntity2Triple(String inputPath){
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));

            String tmpString;
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);
                if (tokens != null) {
                    if(entity2documentMap.containsKey(tokens[0])) {
                        if(!tokens[2].startsWith("\"")) {
                            entity2documentMap.get(tokens[0]).addRelatedEntity(new Pair(tokens[1], tokens[2]));
                        } else {
                            if(tokens[1].equals("name")) {
                                entity2documentMap.get(tokens[0]).addLabel(tokens[2].substring(1, tokens[2].length() - 1));
                            } else {
                                entity2documentMap.get(tokens[0]).addRelatedAttribute(new Pair(tokens[1], tokens[2].substring(1, tokens[2].length() - 1)));
                            }
                        }
                    } else if(entity2documentMap.containsKey(tokens[2])) {
                        entity2documentMap.get(tokens[2]).addRelatedEntity(new Pair(tokens[1], tokens[0]));
                    }
                }
            }

            bfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("loading triple is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    public static void writeDocument(String outputPath) {
        try {
            File file = new File(outputPath);
            if(!file.exists()) {
                new File(outputPath).mkdirs();
            }

            Map<Integer, BufferedWriter> id2bufferedWriterMap = new HashMap<>();
            for(DocumentOfEntity document : entity2documentMap.values()) {
                int id = Math.abs(document.getUri().hashCode()) % 50;

                if(!id2bufferedWriterMap.containsKey(id)) {
                    id2bufferedWriterMap.put(id, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath + "document_" + id + ".json"), "UTF-8")));
                }
                id2bufferedWriterMap.get(id).write(gson.toJson(document) + "\n");
            }

            for(BufferedWriter bufferedWriter : id2bufferedWriterMap.values()) {
                bufferedWriter.close();
            }

            entity2documentMap.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void rewriteDocument() {
        try {
            Map<String, String> entity2contextMap = new HashMap<>();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("dir.raw") + "instance_abstract/long_abstracts_en.ttl"), "UTF-8"));

            String tmpString;
            while ((tmpString = bufferedReader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);
                if (tokens != null) {
                    entity2contextMap.put(tokens[0], tokens[2].substring(1, tokens[2].length() - 1));
                }
            }
            bufferedReader.close();

            BufferedWriter bufferedWriter;
            for(int id = 0; id < 50; id ++) {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("dir.document") + "document_" + id + ".json"), "UTF-8"));
                Map<String, DocumentOfEntity> entity2documentMap = new HashMap<>();

                while ((tmpString = bufferedReader.readLine()) != null) {
                    DocumentOfEntity document = gson.fromJson(tmpString, DocumentOfEntity.class);
                    document.setContext(entity2contextMap.get(document.getUri()));
                    entity2documentMap.put(document.getUri(), document);
                }
                bufferedReader.close();

                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configManager.getValue("dir") + configManager.getValue("dir.document") + "document_" + id + ".json"), "UTF-8"));
                for(DocumentOfEntity documentOfEntity : entity2documentMap.values()) {
                    bufferedWriter.write(gson.toJson(documentOfEntity) + "\n");
                }
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
