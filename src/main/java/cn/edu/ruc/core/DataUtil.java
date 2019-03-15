package cn.edu.ruc.core;

import cn.edu.ruc.data.*;
import cn.edu.ruc.data.TripleManager;
import cn.edu.ruc.data.DescriptionManager;
import cn.edu.ruc.domain.Description;
import cn.edu.ruc.domain.Task;
import cn.edu.ruc.model.Assess;
import org.apache.lucene.index.DirectoryReader;

import javax.servlet.http.HttpServlet;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataUtil extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static int Output_Auto_Size;
	public static int Output_Entity_Size;
	public static int Output_Relation_Size;
	public static int Output_Feature_Size;
	public static int Threshold;
	public static List<Integer> Directions;

	public static int Task_Size;
	public static int Version_Size;

	private static ConfigManager configManager;
	private static LogManager logManager;
	private static DictionaryManager dictionaryManager;
	private static TripleManager tripleManager;
	private static DescriptionManager descriptionManager;
	private static IndexManager indexManager;
	private static TaskManager taskManager;

	public DataUtil(){
		System.out.println("------------------------------------------------------");

		loadConfiguration();
		loadParameter();
		loadData();

		System.out.println("------------------------------------------------------");
	}

	private void loadConfiguration(){
		configManager = new ConfigManager("conf.properties");
		logManager = new LogManager();
		System.out.println("Configurations are loaded!");
	}

	private void loadParameter(){
		Output_Auto_Size = Integer.parseInt(configManager.getValue("output.auto.size"));
		Output_Entity_Size = Integer.parseInt(configManager.getValue("output.entity.size"));
		Output_Relation_Size = Integer.parseInt(configManager.getValue("output.relation.size"));
		Output_Feature_Size = Integer.parseInt(configManager.getValue("output.feature.size"));
		Threshold = Integer.parseInt(configManager.getValue("threshold"));

		Directions = new ArrayList<>();
		Directions.add(Integer.parseInt(configManager.getValue("direction.forward")));
		Directions.add(Integer.parseInt(configManager.getValue("direction.backward")));

		Task_Size = Integer.parseInt(configManager.getValue("task.size"));
		Version_Size = Integer.parseInt(configManager.getValue("version.size"));
		System.out.println("Parameters are loaded!");
	}

	private void loadData(){
		long beginTime;

		beginTime = System.currentTimeMillis();
		dictionaryManager = new DictionaryManager(configManager.getValue("dir") + configManager.getValue("file.entity"), configManager.getValue("dir") + configManager.getValue("file.relation"));
		System.out.println("Dictionaries are loaded! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );

		beginTime = System.currentTimeMillis();
		tripleManager = new TripleManager(configManager.getValue("dir") + configManager.getValue("file.triple"));
		System.out.println("Triples are loaded! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );

		beginTime = System.currentTimeMillis();
		descriptionManager = new DescriptionManager(configManager.getValue("dir") + configManager.getValue("file.description"));
		System.out.println("Descriptions are loaded! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );

		beginTime = System.currentTimeMillis();
		indexManager = new IndexManager(configManager.getValue("dir") + configManager.getValue("dir.document"), configManager.getValue("dir") + configManager.getValue("dir.index"));
		System.out.println("Indexes are loaded! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );

		beginTime = System.currentTimeMillis();
		taskManager = new TaskManager(configManager.getValue("dir") + configManager.getValue("file.task"));
		System.out.println("Tasks are loaded! Time cost: " + (System.currentTimeMillis() - beginTime)/1000 );
	}

	public static LogManager getLogManager () {
		return logManager;
	}

	public static DirectoryReader getDirectoryReader(){
		return indexManager.getDirectoryReader();
	}

	public static String getId2Entity(int id){
		return dictionaryManager.getId2Entity().get(id);
	}

	public static int getEntity2Id(String name){
		return dictionaryManager.getEntity2Id().containsKey(name) ? dictionaryManager.getEntity2Id().get(name) : -1;
	}

	public static Set<Integer> getWholeEntityId() {
		return dictionaryManager.getId2Entity().keySet();
	}

	public static String getId2Relation(int id){
		return dictionaryManager.getId2Relation().get(id);
	}

	public static int getRelation2Id(String name){
		return dictionaryManager.getRelation2Id().containsKey(name) ? dictionaryManager.getRelation2Id().get(name) : -1;
	}

	public static List<Integer> getRelationIdList(int queryEntityId) {
		return tripleManager.getRelationIdList(queryEntityId);
	}

	public static List<Integer> getRelationIdList(int queryEntityId, int direction) {
		return tripleManager.getRelationIdList(queryEntityId, direction);
	}

	public static Map<Integer, Set<Integer>> getRelationId2EntityIdSetMap(int queryEntityId, int direction, int k) {
		return tripleManager.getRelationId2EntityIdSetMap(queryEntityId, direction, k);
	}

	public static Map<Integer, Set<Integer>> getRelationId2EntityIdSetMap(int queryEntityId, int direction) {
		return tripleManager.getRelationId2EntityIdSetMap(queryEntityId, direction);
	}

	public static Set<Integer> getEntityIdSet(int queryEntityId, int relationId, int direction, int k) {
		return tripleManager.getEntityIdSet(queryEntityId, relationId, direction, k);
	}

	public static Set<Integer> getEntityIdSet(int queryEntityId, int relationId, int direction) {
		return tripleManager.getEntityIdSet(queryEntityId, relationId, direction);
	}

	public static int getEntityIdSetSize(int queryEntityId, int queryRelationId, int direction) {
		return tripleManager.getEntityIdSetSize(queryEntityId, queryRelationId, direction);
	}

	public static Description getDescription(int entityId) {
		return descriptionManager.getDescription(entityId);
	}

	public static boolean hasDescription(int entityId) {
		return descriptionManager.hasDescription(entityId);
	}

	public static Task getTask(int id) {
		return taskManager.getTaskMap().get(id);
	}

	public static Assess getAssess(String userId) {
		Assess assess = null;
		try {
			File logDir = new File(configManager.getValue("dir") + configManager.getValue("dir.log"));
			if (!logDir.exists()) {
				new File(configManager.getValue("dir") + configManager.getValue("dir.log")).mkdirs();
			}

			int assessId = 0;
			File userDir = new File(configManager.getValue("dir") + configManager.getValue("dir.log") + userId);
			if (!userDir.exists()) {
				for (File file : logDir.listFiles()) {
					if (file.isDirectory())
						assessId ++;
				}
				new File(configManager.getValue("dir") + configManager.getValue("dir.log") + userId).mkdirs();

				writeAssessId(userId, assessId);
			} else {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(userDir + "/" + configManager.getValue("file.assess.log")), "UTF-8"));

				String tmpString = bufferedReader.readLine();
				assessId = Integer.parseInt(tmpString.split("\t")[1]);

				bufferedReader.close();
			}

			assess = new Assess(new Assessor().getTaskList(assessId));

			/*
			File file = new File(configManager.getValue("dir") + configManager.getValue("dir.log") + configManager.getValue("file.user.log"));
			if(!file.exists()) {
				new File(configManager.getValue("dir") + configManager.getValue("dir.log")).mkdirs();
			}

			int assessId = 0;
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String tmpString;
			while ((tmpString = reader.readLine()) != null) {
				if(!tmpString.equals(userId))
					assessId ++;
				else
					break;
			}
			reader.close();

			new File(configManager.getValue("dir") + configManager.getValue("dir.log") + userId).mkdirs();
			assess = new Assess(new Assessor().getTaskList(assessId));*/

		} catch (Exception e) {
			e.printStackTrace();
		}

		return assess;
	}

	public static void writeAssessId(String userId, int assessId) {
		try {
			File userDir = new File(configManager.getValue("dir") + configManager.getValue("dir.log") + userId);

			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userDir + "/" + configManager.getValue("file.assess.log")), "UTF-8"));
			bufferedWriter.write(userId + "\t" + assessId);

			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeUser(String userId) {
		try {
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(configManager.getValue("dir") + configManager.getValue("dir.log") + configManager.getValue("file.user.log"), true)));

			printWriter.println(userId);

			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeBookmark(String userId, int taskId, int versionId, String entityString, int relevance) {
		try {
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(configManager.getValue("dir") + configManager.getValue("dir.log")  + userId + '/'+ configManager.getValue("file.bookmark.log"), true)));

			printWriter.println(userId + "\t" + taskId + "\t" + versionId + "\t" + entityString + "\t" + relevance);

			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeInteraction(String userId, int taskId, int versionId, String option, String target, String queryContent, String timestamp) {
		try {
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(configManager.getValue("dir") + configManager.getValue("dir.log")  + userId + '/'+ configManager.getValue("file.interaction.log"), true)));

			printWriter.println(userId + "\t" + taskId + "\t" + versionId + "\t" + option + "\t" + target + "\t" + queryContent + "\t" + timestamp);

			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
