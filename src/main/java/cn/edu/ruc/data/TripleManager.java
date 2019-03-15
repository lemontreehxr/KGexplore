package cn.edu.ruc.data;

import cn.edu.ruc.core.DataUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TripleManager {
	private Map<Integer, Map<Integer, Map<Integer, Set<Integer>>>> direction2tripleMap = new HashMap<>();

	public TripleManager(String inputPath){
		loadTriple(inputPath);
	}

	public void loadTriple(String inputPath){
		try {
			for(int direction : DataUtil.Directions) {
				direction2tripleMap.put(direction, new HashMap<>());
			}

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), "UTF-8"));
			String tmpString;
			while((tmpString = bufferedReader.readLine()) != null /*&& count < 1000000*/) {
				String[] tokens = tmpString.split("\t");

				int subjectId = Integer.parseInt(tokens[0]), predicateId = Integer.parseInt(tokens[1]), objectId = Integer.parseInt(tokens[2]);

				//if(predicateId == 113 || predicateId == 253)
					//continue;

				for(int direction : DataUtil.Directions) {
					Map<Integer, Map<Integer, Set<Integer>>> tripleMap = direction2tripleMap.get(direction);

					if(direction < 0) {
						int tmp = objectId;
						objectId = subjectId;
						subjectId = tmp;
					}

					if(!tripleMap.containsKey(subjectId)){
						tripleMap.put(subjectId , new HashMap<>());
						tripleMap.get(subjectId).put(predicateId, new HashSet<>());
					}
					else if(!tripleMap.get(subjectId).containsKey(predicateId))
						tripleMap.get(subjectId).put(predicateId, new HashSet<>());

					tripleMap.get(subjectId).get(predicateId).add(objectId);
				}
			}

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getTripleSize(int entityId){
		int size = 0;

		for(int direction : DataUtil.Directions) {
			if(direction2tripleMap.get(direction).containsKey(entityId)) {
				for (Map.Entry<Integer, Set<Integer>> relationId2entityIdSetEntry : direction2tripleMap.get(direction).get(entityId).entrySet())
					size += relationId2entityIdSetEntry.getValue().size();
			}
		}

		return size;
	}

	public List<Integer> getRelationIdList(int entityId){
		List<Integer> relationIdList = new ArrayList<>();

		for(int direction : DataUtil.Directions) {
			if(direction2tripleMap.get(direction).containsKey(entityId)) {
				relationIdList.addAll(getRelationIdList(entityId, direction));
			}
		}

		return relationIdList;
	}

	public List<Integer> getRelationIdList(int entityId, int direction){
		List<Integer> relationIdList = new ArrayList<>();

		if(direction2tripleMap.get(direction).containsKey(entityId)) {
			for (Map.Entry<Integer, Set<Integer>> relationId2entityIdSetEntry : direction2tripleMap.get(direction).get(entityId).entrySet())
				relationIdList.add(relationId2entityIdSetEntry.getKey());
		}

		return relationIdList;
	}

	public Map<Integer, Set<Integer>> getRelationId2EntityIdSetMap(int entityId, int direction, int k) {
		Map<Integer, Set<Integer>> relationId2entityIdMap = new HashMap<>();

		if(direction2tripleMap.get(direction).containsKey(entityId)) {
			for (Map.Entry<Integer, Set<Integer>> relationId2entityIdSetEntry : direction2tripleMap.get(direction).get(entityId).entrySet()) {
				if (relationId2entityIdSetEntry.getValue().size() < k) {
					relationId2entityIdMap.put(relationId2entityIdSetEntry.getKey(), relationId2entityIdSetEntry.getValue());
				}
			}
		}

		return relationId2entityIdMap;
	}

	public Map<Integer, Set<Integer>> getRelationId2EntityIdSetMap(int entityId, int direction) {
		Map<Integer, Set<Integer>> relationId2entityIdMap = new HashMap<>();

		if(direction2tripleMap.get(direction).containsKey(entityId)) {
			for (Map.Entry<Integer, Set<Integer>> relationId2entityIdSetEntry : direction2tripleMap.get(direction).get(entityId).entrySet()) {
				relationId2entityIdMap.put(relationId2entityIdSetEntry.getKey(), relationId2entityIdSetEntry.getValue());
			}
		}

		return relationId2entityIdMap;
	}

	public Set<Integer> getEntityIdSet(int entityId, int relationId, int direction, int k) {
		Set<Integer> entityIdSet = new HashSet<>();

		if(direction2tripleMap.get(direction).containsKey(entityId)) {
			if (direction2tripleMap.get(direction).get(entityId).containsKey(relationId)) {
				if (direction2tripleMap.get(direction).get(entityId).get(relationId).size() < k)
					entityIdSet = direction2tripleMap.get(direction).get(entityId).get(relationId);
			}
		}

		return entityIdSet;
	}

	public Set<Integer> getEntityIdSet(int entityId, int relationId, int direction) {
		Set<Integer> entityIdSet = new HashSet<>();

		if(direction2tripleMap.get(direction).containsKey(entityId)) {
			if (direction2tripleMap.get(direction).get(entityId).containsKey(relationId)) {
				entityIdSet = direction2tripleMap.get(direction).get(entityId).get(relationId);
			}
		}

		return entityIdSet;
	}

	public int getEntityIdSetSize(int entityId, int relationId, int direction) {
		int size = 0;

		if(direction2tripleMap.get(direction).containsKey(entityId)) {
			if (direction2tripleMap.get(direction).get(entityId).containsKey(relationId)) {
				size = direction2tripleMap.get(direction).get(entityId).get(relationId).size();
			}
		}

		return size;
	}
}
