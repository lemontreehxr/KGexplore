package cn.edu.ruc.service;

import cn.edu.ruc.model.Assess;

import java.util.List;

public interface SearchService {
	//get assess
	Assess getAssess(String userId);

	//send user
	void sendUser(String userId);

	//send entity
	void sendBookmark(String userId, int taskId, int versionId, String entityString, int relevance);

	//send interaction
	void sendInteraction(String userId, int taskId, int versionId, String option, String target, String queryContent, String timestamp);
}
