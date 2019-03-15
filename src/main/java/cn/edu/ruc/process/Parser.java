package cn.edu.ruc.process;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public Parser(){

    }

    public String[] tokenLize(String s){
        if(s.startsWith("#"))
            return null;

        Pattern pattern = Pattern.compile("(\\S+)\\s(\\S+)\\s(.*)\\s(.)");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find( ))
            return new String[]{matcher.group(1), matcher.group(2), matcher.group(3)};

        //System.out.println("Exceptional line: " + s);
        return null;
    }

    public String[] parse(String s){
        String[] tokens = tokenLize(s);

        if(tokens == null || tokens.length < 3)
            return null;

        tokens[0] = replace_entity(tokens[0]);
        if(tokens[0] == null)
            return null;

        tokens[1] = replace_ontology(tokens[1]);
        if(tokens[1] == null)
            return null;

        if(tokens[2].startsWith("<http://dbpedia.org/resource/"))
            tokens[2] = replace_entity(tokens[2]);
        else if(tokens[2].startsWith("<http://dbpedia.org/ontology/"))
            tokens[2] = replace_ontology(tokens[2]);
        else
            tokens[2] = replace_literal(tokens[2]);
        if(tokens[2] == null)
            return null;

        return tokens;
    }

    public String replace_entity(String s){
        if(s.startsWith("<http://dbpedia.org/resource/"))
            return s.replaceAll("<http://dbpedia.org/resource/", "").replaceAll(">", "");
        else
            //System.out.println("Exceptional entity: " + s);

        return null;
    }

    public String replace_ontology(String s){
        if(s.equals("<http://www.w3.org/2000/01/rdf-schema#label>"))
            return "label";
        else if(s.equals("<http://xmlns.com/foaf/0.1/name>"))
            return "name";
        else if(s.equals("<http://purl.org/dc/terms/subject>"))
            return "subject";
        else if(s.equals("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>"))
            return "ontology";
        else if(s.startsWith("<http://dbpedia.org/ontology/"))
            return s.replaceAll("<http://dbpedia.org/ontology/", "").replaceAll(">", "");
        else
            //System.out.println("Exceptional ontology: " + s);

        return null;
    }

    public String replace_literal(String s){
        if(s.endsWith("@en"))
            return s.substring(0, s.indexOf("@en"));
        else if(s.startsWith("<") && s.endsWith(">"))
            return s;
        //else if(s.contains("^^<"))
            //return s.substring(s.indexOf("\""), s.indexOf("^^<") - 1);
        else
            //System.out.println("Exceptional literal: " + s);

        return null;
    }
}
