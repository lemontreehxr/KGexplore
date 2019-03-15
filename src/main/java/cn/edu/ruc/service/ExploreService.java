package cn.edu.ruc.service;

import cn.edu.ruc.model.Result;

import java.util.List;

public interface ExploreService {
	//get result
	Result getResult(String keywords, List<String> queryEntityStringList, List<String> queryFeatureStringList);

	//get profile
	Result getProfile(List<String> queryEntityStringList, String queryEntityString);
}
