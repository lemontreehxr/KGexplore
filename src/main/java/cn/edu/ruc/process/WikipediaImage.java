package cn.edu.ruc.process;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.ruc.data.ConfigManager;

public class WikipediaImage {
    private static Map<String, String> entity2image = new HashMap();
    private static Set<String> entitySet = new HashSet<>();

    private static ConfigManager configManager;
	
	public static void main(String[] args) throws IOException {
        configManager = new ConfigManager("conf.properties");

        loadEntity2Image(configManager.getValue("dir") + configManager.getValue("file.image"));

        BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configManager.getValue("dir") + configManager.getValue("file.image")), "UTF-8"));
        entity2image.entrySet().parallelStream()
                .forEach(entity2imageEntry -> {
                    try {
                        bfWriter.write(entity2imageEntry.getKey() + "\t" + entity2imageEntry.getValue() + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });


        loadEntity(configManager.getValue("dir") + configManager.getValue("file.entity"), entitySet);
        System.out.println(entitySet.size() + "\t" + entity2image.size());

        entitySet.parallelStream()
                .forEach(query -> {
                    String htmlUrl = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&pithumbsize=300&titles=" + query;
                    String json = getHtmlContent(htmlUrl);

                    if (json.contains("\"thumbnail\":{\"source\":\"")) {
                        Pattern pattern = Pattern.compile("\"thumbnail\":\\{\"source\":\"(\\S+)\",\"width\"");
                        Matcher matcher = pattern.matcher(json);
                        if (matcher.find()) {
                            try {
                                bfWriter.write(query + "\t" + matcher.group(1) + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        bfWriter.close();
	}

    private static void loadEntity2Image(String inputPath) {
        try {
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));

            String tmpString = null;
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                entity2image.put(tokens[0], tokens[1]);
            }
            bfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadEntity(String inputPath, Set<String> entitySet) {
        try {
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));

            String tmpString = null;
            while ((tmpString = bfReader.readLine()) != null) {
                String[] tokens = tmpString.split("\t");

                if(!tokens[0].startsWith("Category:") && !entity2image.containsKey(tokens[0]))
                    entitySet.add(tokens[0]);
            }
            bfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


	public static String getHtmlContent(String htmlUrl) {  
        URL url;  
        String tmpString;  
        StringBuffer sb = new StringBuffer();  
        try {  
            url = new URL(htmlUrl);
            URLConnection urlConnection = url.openConnection();

            urlConnection.setConnectTimeout(50000);
            urlConnection.setReadTimeout(50000);

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((tmpString = in.readLine()) != null) {  
                sb.append(tmpString);
            }  
            in.close();  
        } catch (final MalformedURLException me) {  
            System.out.println("error!");  
            me.getMessage();  
        } catch (final IOException e) {  
            e.printStackTrace();  
        }  
        return sb.toString();  
    }
}
