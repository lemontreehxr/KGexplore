package cn.edu.ruc.data;

import cn.edu.ruc.ultity.URLComponent;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;


public class DictionaryManager {
	private Map<String, Integer> entity2id = new HashMap<>();
	private Map<Integer, String> id2entity = new HashMap<>();
	private Map<String, Integer> relation2id = new HashMap<>();
	private Map<Integer, String> id2relation = new HashMap<>();
	private URLComponent urlComponent = new URLComponent("UTF-8");

	public DictionaryManager(String inputPath_entity, String inputPath_relation){
		loadDictionary(inputPath_entity, entity2id, id2entity);
		loadDictionary(inputPath_relation, relation2id, id2relation);
	}

	public void loadDictionary(String inputPath, Map<String, Integer> name2id, Map<Integer, String> id2name){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
			String tmpString;
			while((tmpString = reader.readLine()) != null) {
				String[] tokens = tmpString.split("\t");

				String name = urlComponent.decode(tokens[0]);
				int id = Integer.parseInt(tokens[1]);
				name2id.put(name, id);
				id2name.put(id, name);
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String, Integer> getEntity2Id(){
		return entity2id;
	}

	public Map<String, Integer> getRelation2Id(){
		return relation2id;
	}

	public Map<Integer, String> getId2Entity(){
		return id2entity;
	}
	
	public Map<Integer, String> getId2Relation(){
		return id2relation;
	}
}
