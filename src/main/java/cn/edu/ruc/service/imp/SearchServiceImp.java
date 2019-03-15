package cn.edu.ruc.service.imp;

import cn.edu.ruc.core.DataUtil;
import cn.edu.ruc.model.Assess;
import cn.edu.ruc.service.SearchService;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImp implements SearchService {
	@Override
	public Assess getAssess(String userId) {
		return DataUtil.getAssess(userId);
	}

	@Override
	public void sendUser(String userId) {
		DataUtil.writeUser(userId);
	}

	@Override
	public void sendBookmark(String userId, int taskId, int versionId, String entityString, int relevance) {
		DataUtil.writeBookmark(userId, taskId, versionId, entityString, relevance);
	}

	@Override
	public void sendInteraction(String userId, int taskId, int versionId, String option, String target, String queryContent, String timestamp) {
		DataUtil.writeInteraction(userId, taskId, versionId, option, target, queryContent, timestamp);
	}
}
