/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.core;

import cn.edu.ruc.domain.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

//important: pay attention to the thread-safe situation!
public class Ranker {
	public static List<Entity> getEntityList(String keywords, List<Entity> queryEntityList, List<Feature> queryFeatureList) {
		Map<Integer, Entity> entityMap = new ConcurrentHashMap<>();

		ArrayList<Entity> entityList = new ArrayList<>();
		entityList.addAll(getEntityListByKeyword(keywords));
		entityList.addAll(getEntityListByEntityAndFeature(queryEntityList, queryFeatureList));

		entityList.stream()
				.forEach(entity -> {
					if (!entityMap.containsKey(entity.getId())) {
						entityMap.put(entity.getId(), entity);
					} else {
						entityMap.get(entity.getId()).setScore(entityMap.get(entity.getId()).getScore() + entity.getScore());
					}
				});

		List<Entity> relevantEntityList = sortEntityList(new ArrayList<>(entityMap.values()),  DataUtil.Output_Entity_Size);
		for(int i = 0; i < relevantEntityList.size(); i ++) {
			relevantEntityList.get(i).setRank(i + 1);
			relevantEntityList.get(i).setDescription(DataUtil.getDescription(relevantEntityList.get(i).getId()));
			Parser.decodeEntity(relevantEntityList.get(i));
		}

		return relevantEntityList;
	}

	public static List<Entity> getEntityListByKeyword(String keywords) {
		List<Entity> relevantEntityList = new ArrayList<>();

		if(keywords != null && keywords != "") {
			try {
				IndexSearcher searcher = new IndexSearcher(DataUtil.getDirectoryReader());
				searcher.setSimilarity(new LMDirichletSimilarity(2500.0f));

				String fields[] = {"context", "labels", "similarEntities", "categories", "relatedAttributes", "relatedEntities"};
				for(String filed : fields) {
					QueryParser parser = new QueryParser(filed, new StandardAnalyzer());
					Query query = parser.parse(keywords.trim().toLowerCase());
					TopDocs topDocs = searcher.search(query, 2 * DataUtil.Output_Entity_Size);
					double topScore = topDocs.getMaxScore();
					double lambda = 1;
					for (int i = 0; i < topDocs.scoreDocs.length; i++) {
						Document document = searcher.doc(topDocs.scoreDocs[i].doc);

						if(DataUtil.hasDescription(DataUtil.getEntity2Id(document.get("uri")))) {
							relevantEntityList.add(new Entity(DataUtil.getEntity2Id(document.get("uri")), topDocs.scoreDocs[i].score * lambda / (topScore * 100)));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return relevantEntityList;
	}

	public static List<Entity> getEntityListByEntityAndFeature(List<Entity> queryEntityList, List<Feature> queryFeatureList) {
		Map<Integer, Entity> entityMap = new ConcurrentHashMap<>();

		getFeatureList(queryEntityList, queryFeatureList, true).stream()
				.forEach(relevantFeature -> DataUtil.getEntityIdSet(relevantFeature.getEntity().getId(), relevantFeature.getRelation().getId(), relevantFeature.getRelation().getDirection(), DataUtil.Threshold).parallelStream()
						.filter(sourceId -> DataUtil.hasDescription(sourceId))
						.forEach(sourceId -> {
							double score = relevantFeature.getScore();

							if (!entityMap.containsKey(sourceId)) {
								entityMap.put(sourceId, new Entity(sourceId, score));
							} else {
								entityMap.get(sourceId).setScore(entityMap.get(sourceId).getScore() + score);
							}
						})
				);

		return sortEntityList(new ArrayList<>(entityMap.values()), 2 * DataUtil.Output_Entity_Size);
	}

	/*public static List<Entity> getEntityListByFeature(List<Feature> queryFeatureList) {
		Map<Integer, Entity> entityMap = new ConcurrentHashMap<>();

		if(queryFeatureList != null && !queryFeatureList.isEmpty()) {
			getFeatureListByFeature(queryFeatureList).stream()
					.forEach(relevantFeature -> DataUtil.getEntityIdSet(relevantFeature.getEntity().getId(), relevantFeature.getRelation().getId(), relevantFeature.getRelation().getDirection(), DataUtil.Threshold).parallelStream()
							.forEach(sourceId -> {
								double score = relevantFeature.getScore();

								if (!entityMap.containsKey(sourceId)) {
									entityMap.put(sourceId, new Entity(sourceId, score));
								} else {
									entityMap.get(sourceId).setScore(entityMap.get(sourceId).getScore() + score);
								}
							})
					);
		}

		return sortEntityList(new ArrayList<>(entityMap.values()), 2 * DataUtil.Output_Entity_Size);
	}*/

	/*public static List<Feature> getFeatureList(List<Entity> queryEntityList, List<Feature> queryFeatureList) {
		Map<FeatureKey, Feature> featureKey2featureMap = new ConcurrentHashMap<>();

		ArrayList<Feature> featureList = new ArrayList<>();
		featureList.addAll(getFeatureList(queryEntityList, queryFeatureList));

		Map<Integer, Double> relation2score = new ConcurrentHashMap<>();
		Map<Integer, Integer> relation2frequency = new ConcurrentHashMap<>();

		featureList.stream()
				.forEach(feature -> {
					if(!relation2score.containsKey(feature.getRelation().getId()))
						relation2score.put(feature.getRelation().getId(), feature.getEntity().getScore());
					else
						relation2score.put(feature.getRelation().getId(), relation2score.get(feature.getRelation().getId()) + feature.getEntity().getScore());

					if(!relation2frequency.containsKey(feature.getRelation().getId()))
						relation2frequency.put(feature.getRelation().getId(), 1);
					else
						relation2frequency.put(feature.getRelation().getId(), relation2frequency.get(feature.getRelation().getId()) + 1);
				});

		featureList.stream()
				.forEach(feature -> {
					feature.getRelation().setScore(relation2score.get(feature.getRelation().getId()) / relation2frequency.get(feature.getRelation().getId()));
					feature.setScore(feature.getRelation().getScore() * feature.getEntity().getScore());
				});

		List<Feature> relevantFeatureList = sortFeatureList(new ArrayList<>(featureKey2featureMap.values()), DataUtil.Output_Feature_Size);

		for(int i = 0; i < relevantFeatureList.size(); i ++) {
			relevantFeatureList.get(i).setRank(i + 1);
			Parser.decodeFeature(relevantFeatureList.get(i));
		}

		return relevantFeatureList;
	}*/

	public static List<Feature> getFeatureList(List<Entity> queryEntityList, List<Feature> queryFeatureList, boolean doFilter) {
		Map<FeatureKey, Feature> featureKey2featureMap = new ConcurrentHashMap<>();

		if(queryFeatureList != null && !queryFeatureList.isEmpty()) {
			queryFeatureList.parallelStream()
					.forEach(queryFeature -> {
						FeatureKey featureKey = new FeatureKey(queryFeature.getEntity().getId(), queryFeature.getRelation().getId(), queryFeature.getRelation().getDirection());

						featureKey2featureMap.put(featureKey, queryFeature);
					});
		}

		DataUtil.Directions.parallelStream()
				.forEach(direction -> queryEntityList.stream()
						.forEach(queryEntity -> DataUtil.getRelationId2EntityIdSetMap(queryEntity.getId(), direction, DataUtil.Threshold).entrySet().parallelStream()
								.filter(relationId2TargetIdEntry -> relationId2TargetIdEntry.getKey() != 113)
								.forEach(relationId2TargetIdEntry -> {
										int relationId = relationId2TargetIdEntry.getKey();
										relationId2TargetIdEntry.getValue().parallelStream()
												.filter(targetId -> !doFilter || (DataUtil.getEntityIdSetSize(targetId, relationId, - direction) > 1))
												.forEach(targetId -> {
													int size = DataUtil.getEntityIdSetSize(targetId, relationId, - direction);

													double score = (1.0 / queryEntityList.size()) * (1.0 / Math.log(size + 1));

													FeatureKey featureKey = new FeatureKey(targetId, relationId, - direction);

													if (!featureKey2featureMap.containsKey(featureKey)) {
														Feature feature = new Feature(new Entity(targetId), new Relation(relationId2TargetIdEntry.getKey(), - direction), score);
														featureKey2featureMap.put(featureKey, feature);
													} else {
														featureKey2featureMap.get(featureKey).setScore(featureKey2featureMap.get(featureKey).getScore() + score);
													}
												});
									})
						)
				);


		/*
		//group by relation
		Map<Integer, Double> relation2score = new ConcurrentHashMap<>();
		Map<Integer, Integer> relation2frequency = new ConcurrentHashMap<>();

		featureKey2featureMap.values().stream()
				.forEach(feature -> {
					if(!relation2score.containsKey(feature.getRelation().getId()))
						relation2score.put(feature.getRelation().getId(), feature.getEntity().getScore());
					else
						relation2score.put(feature.getRelation().getId(), relation2score.get(feature.getRelation().getId()) + feature.getEntity().getScore());

					if(!relation2frequency.containsKey(feature.getRelation().getId()))
						relation2frequency.put(feature.getRelation().getId(), 1);
					else
						relation2frequency.put(feature.getRelation().getId(), relation2frequency.get(feature.getRelation().getId()) + 1);
				});

		featureKey2featureMap.values().stream()
				.forEach(feature -> {
					feature.getRelation().setScore(relation2score.get(feature.getRelation().getId()) / relation2frequency.get(feature.getRelation().getId()));
					feature.setScore(feature.getRelation().getScore() * feature.getEntity().getScore());
				});*/

		List<Feature> relevantFeatureList = sortFeatureList(new ArrayList<>(featureKey2featureMap.values()), DataUtil.Output_Feature_Size);

		for(int i = 0; i < relevantFeatureList.size(); i ++) {
			relevantFeatureList.get(i).setRank(i + 1);
			Parser.decodeFeature(relevantFeatureList.get(i));
			//System.out.println(relevantFeatureList.get(i));
		}

		return relevantFeatureList;
	}

	/*
	//get relevant relation of an entity list by accumulating their information gain
	private static List<Relation> getRelation(List<Entity> queryEntityList, List<Feature> queryFeatureList){
		//important: hash map is not thread-safe.
		Map<Integer, Relation> relationMap = new ConcurrentHashMap<>();

		queryEntityList.stream() //can not apply parallel steam here, or may occur un-consistence of write and read, consider the lock in the future
				.forEach(queryEntity -> getRelevantRelation(queryEntity).parallelStream() //can apply parallel steam here, since a query entity can not produce two same relation via relation2entityMap
						.forEach(relation -> {
							if(relationMap.containsKey(relation.getId())) {
								relationMap.get(relation.getId()).setScore(relationMap.get(relation.getId()).getScore() + relation.getScore());
							}
							else {
								relationMap.put(relation.getId(), relation);
							}
						})
				);

		relationMap.values().parallelStream()
				.forEach(relation -> relation.setScore(relation.getScore() / queryEntityList.size()));

		queryFeatureList.stream()
				.forEach(queryFeature -> {
							if(relationMap.containsKey(queryFeature.getRelation().getId())) {
								relationMap.get(queryFeature.getRelation().getId()).setScore(relationMap.get(queryFeature.getRelation().getId()).getScore() + queryFeature.getRelation().getScore());
							}
							else {
								relationMap.put(queryFeature.getRelation().getId(), queryFeature.getRelation());
							}
						});

		return sortRelationList(new ArrayList<>(relationMap.values()), DataUtil.Output_Relation_Size);
	}

	//get relevant relation of an entity by information gain
	private static List<Relation> getRelevantRelation(Entity queryEntity){
		int min_size = Integer.MAX_VALUE, max_size = 0;
		for(int direction : DataUtil.Directions){
			for(Map.Entry<Integer, Set<Integer>> relation2entityEntry : DataUtil.getRelationId2EntityIdSetMap(queryEntity.getId(), direction).entrySet()){
				int num = relation2entityEntry.getValue().size();
				min_size = min_size > num ? num : min_size;
				max_size = max_size < num ? num : max_size;
			}
		}

		List<Relation> relationList = new ArrayList<>();
		for(int direction : DataUtil.Directions){
			for(Map.Entry<Integer, Set<Integer>> relation2entityEntry : DataUtil.getRelationId2EntityIdSetMap((queryEntity.getId(), direction).entrySet()){
				double pro = (double) relation2entityEntry.getValue().size() / (min_size + max_size);
				double score = (- pro * Math.log(pro)) * queryEntity.getScore();

				relationList.add(new Relation(relation2entityEntry.getKey(), direction, score));
			}
		}

		return sortRelationList(relationList, DataUtil.Output_Relation_Size);
	}*/

	/*
	public static List<Feature> getFeatureListByFeature(List<Feature> queryFeatureList) {
		List<Feature> relevantFeatureList = new ArrayList<>();

		if(queryFeatureList != null && !queryFeatureList.isEmpty()) {
			relevantFeatureList = queryFeatureList.parallelStream()
					.map(queryFeature -> {
						//int size = DataUtil.getEntityIdSetSize(queryFeature.getEntity().getId(), queryFeature.getRelation().getId(), queryFeature.getRelation().getDirection());
						//double score = 1;

						return new Feature(queryFeature.getEntity(), queryFeature.getRelation());
					})
					.collect(Collectors.toList());
		}

		return relevantFeatureList;
	}*/

	public static Explanation getExplanation(List<Entity> entityList, List<Feature> featureList) {
		List<List<Double>> scoreListList = new ArrayList<>();

		double base = Math.log(1.0 / DataUtil.getEntityIdSet(DataUtil.getEntity2Id("Thing"), DataUtil.getRelation2Id("ontology"), - 1).size());

		for(Feature feature : featureList) {
			List<Double> scoreList = new ArrayList<>();
			Set<Integer> sourceIdSet = DataUtil.getEntityIdSet(feature.getEntity().getId(), feature.getRelation().getId(), feature.getRelation().getDirection());
			for(Entity entity : entityList) {
				if(sourceIdSet.contains(entity.getId())) {
					scoreList.add(1.0);
				}
				else {
					scoreList.add(1 - Math.log(getProbability(sourceIdSet, entity)) / base);
				}
			}
			scoreListList.add(scoreList);
		}

		return new Explanation(scoreListList);
	}

	private static double getProbability(Set<Integer> sourceIdSet, Entity entity) {
		Entity type = getType(sourceIdSet, entity);

		double score = (double) sourceIdSet.size() / DataUtil.getEntityIdSet(type.getId(), DataUtil.getRelation2Id("ontology"), - 1).size();

		/*(if(type.getId() == DataUtil.getEntity2Id("Thing"))
			score = (double) sourceIdSet.size() / DataUtil.getEntityIdSet(type.getId(), DataUtil.getRelation2Id("ontology"), - 1).size();
		else
			score = getJaccardSimilarity(sourceIdSet, DataUtil.getEntityIdSet(type.getId(), DataUtil.getRelation2Id("ontology"), - 1));
		*/

		return score;
	}

	private static Entity getType(Set<Integer> sourceIdSet, Entity entity) {
		Map<Integer, Set<Integer>> type2entityIdSetMap = new ConcurrentHashMap<>();

		DataUtil.getEntityIdSet(entity.getId(), DataUtil.getRelation2Id("ontology"), 1).parallelStream()
				.forEach(typeId -> {
					if(!type2entityIdSetMap.containsKey(typeId)) {
						type2entityIdSetMap.put(typeId, DataUtil.getEntityIdSet(typeId, DataUtil.getRelation2Id("ontology"), - 1));
					}
				});

		List<Entity> typeList = type2entityIdSetMap.entrySet().parallelStream()
				.map(type2entityIdSetEntry_c -> {
					Set<Integer> entityIdSet_c = type2entityIdSetEntry_c.getValue();

					double score_1 = getJaccardSimilarity(entityIdSet_c, sourceIdSet);
					double score_2 = 1.0 / entityIdSet_c.size();
					/*double score_2 = 0;

					for(Map.Entry<Integer, Set<Integer>> type2entityIdSetEntry_c_i : type2entityIdSetMap.entrySet()) {
						if(type2entityIdSetEntry_c_i.getKey() != type2entityIdSetEntry_c.getKey()) {
							Set<Integer> entityIdSet_c_i = type2entityIdSetEntry_c_i.getValue();

							double i_1 = (double) entityIdSet_c_i.size() / DataUtil.getWholeEntityId().size();
							double i_2 = getJaccardSimilarity(entityIdSet_c_i, entityIdSet_c);

							score_2 += getJaccardSimilarity(entityIdSet_c_i, sourceIdSet) * (- Math.log(i_1) + Math.log(i_2));
						}
					}*/

					return new Entity(type2entityIdSetEntry_c.getKey(), score_1 * score_2);
				}).collect(Collectors.toList());

		if(typeList.isEmpty()) {
			return new Entity(DataUtil.getEntity2Id("Thing"));
		} else {
			return sortEntityList(typeList, typeList.size()).get(0);
		}
	}

	/*private static double getJaccardSimilarity(Set<Integer> leftEntityIdSet, Set<Integer> rightEntityIdSet) {
		return (double) Math.min(leftEntityIdSet.size(), rightEntityIdSet.size()) / rightEntityIdSet.size();
	}*/

	private static double getJaccardSimilarity(Set<Integer> leftEntityIdSet, Set<Integer> rightEntityIdSet) {
		int top, bottom = rightEntityIdSet.size();

		if(leftEntityIdSet.size() < rightEntityIdSet.size()) {
			top = leftEntityIdSet.parallelStream()
					.filter(leftEntityId -> rightEntityIdSet.contains(leftEntityId))
					.collect(Collectors.toList()).size();
		} else {
			top = rightEntityIdSet.parallelStream()
					.filter(rightEntityId -> leftEntityIdSet.contains(rightEntityId))
					.collect(Collectors.toList()).size();
		}

		return (double) top / bottom;
	}

	//return top-k
	private static List<Entity> sortEntityList(List<Entity> entityList, int k) {
		return entityList.parallelStream()
				.sorted((a, b) -> a.getScore() == b.getScore() ? 0 : (a.getScore() < b.getScore() ? 1 : -1))
				.limit(entityList.size() < k ? entityList.size() : k)
				.collect(Collectors.toList());
	}

	public static List<Relation> sortRelationList(List<Relation> relationList, int k){
		return relationList.parallelStream()
				.sorted((a, b) -> a.getScore() == b.getScore() ? 0 : (a.getScore() < b.getScore() ? 1 : -1))
				.limit(relationList.size() < k ? relationList.size() : k)
				.collect(Collectors.toList());
	}

	private static List<Feature> sortFeatureList(List<Feature> featureList, int k){
		return featureList.parallelStream()
				.sorted((a, b) -> a.getScore() == b.getScore() ? 0 : (a.getScore() < b.getScore() ? 1 : -1))
				.limit(featureList.size() < k ? featureList.size() : k)
				.collect(Collectors.toList());
	}

	/*private static List<Feature> sortFeatureList(List<Feature> featureList, int k){
		return featureList.parallelStream()
				.sorted((a, b) -> a.getRelation().getScore() == b.getRelation().getScore() ? (a.getEntity().getScore() == b.getEntity().getScore() ? 0 : (a.getEntity().getScore() < b.getEntity().getScore() ? 1 : -1)) : (a.getRelation().getScore() < b.getRelation().getScore() ? 1 : -1))
				.limit(featureList.size() < k ? featureList.size() : k)
				.collect(Collectors.toList());
	}*/
}	
