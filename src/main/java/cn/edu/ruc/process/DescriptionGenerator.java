package cn.edu.ruc.process;

import cn.edu.ruc.data.ConfigManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DescriptionGenerator {
    private static Parser parser = new Parser();
    private static Map<String, Integer> entity2id = new HashMap<>();
    private static ConfigManager configManager = new ConfigManager("conf.properties");
    private static Map<Integer, String> id2image = new HashMap<>();
    private static Map<Integer, String> id2abstract = new HashMap<>();

    public static void main(String[] args){
        loadName2Id();

        translateDescription();

        writeDescription(configManager.getValue("dir") + configManager.getValue("file.description"));
    }

    private static void loadName2Id() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("file.entity")), "UTF-8"));

            String tmpString;
            while ((tmpString = bufferedReader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                String name = tokens[0];
                if(!name.startsWith("Category:")) {
                    entity2id.put(name, Integer.parseInt(tokens[1]));
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void translateDescription(){
        long beginTime = System.currentTimeMillis();

        try {
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("file.abstract")), "UTF-8"));
            String tmpString;
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                if(entity2id.containsKey(tokens[0])) {
                    id2abstract.put(entity2id.get(tokens[0]), tokens[1].substring(1, tokens[1].length() - 1));
                }
            }
            bfReader.close();

            bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("file.image")), "UTF-8"));
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                if(entity2id.containsKey(tokens[0])) {
                    id2image.put(entity2id.get(tokens[0]), tokens[1]);
                }
            }
            bfReader.close();

            bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("dir.raw") + "instance_image/images_en.ttl"), "UTF-8"));
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = parser.parse(tmpString);

                if(tokens != null && entity2id.containsKey(tokens[0])) {
                    id2image.put(entity2id.get(tokens[0]), tokens[2].substring(1, tokens[2].length() - 1));
                }
            }
            bfReader.close();

            bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(configManager.getValue("dir") + configManager.getValue("file.poster")), "UTF-8"));
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                if(entity2id.containsKey(tokens[0])) {
                    id2image.put(entity2id.get(tokens[0]), tokens[1]);
                }
            }
            bfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Translating description is done! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
    }

    private static void writeDescription(String outputPath) {
        try {
            BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8"));

            for(int id : entity2id.values()) {
                bfWriter.write(id + "\t" + id2abstract.get(id) + "\t" + id2image.get(id) + "\n");
            }

            bfWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
