package cn.edu.ruc.service.imp;

import cn.edu.ruc.core.DataUtil;
import cn.edu.ruc.core.Parser;
import cn.edu.ruc.core.Ranker;
import cn.edu.ruc.domain.Entity;
import cn.edu.ruc.domain.Explanation;
import cn.edu.ruc.domain.Feature;
import cn.edu.ruc.model.Query;
import cn.edu.ruc.model.Result;
import cn.edu.ruc.service.ExploreService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class ExploreServiceImp implements ExploreService {
	@Override
	public Result getResult(String keywords, List<String> queryEntityStringList, List<String> queryFeatureStringList) {
		long time = System.currentTimeMillis();

		List<Entity> queryEntityList = Parser.encodeEntityList(queryEntityStringList);
		List<Feature> queryFeatureList = Parser.encodeFeatureList(queryFeatureStringList);

		Query query = new Query(keywords, queryEntityList, queryFeatureList);

		List<Entity> entityList = Ranker.getEntityList(keywords, queryEntityList, queryFeatureList);
		List<Feature> featureList = Ranker.getFeatureList(entityList, queryFeatureList, true);
		Explanation explanation = Ranker.getExplanation(entityList, featureList);


		Result result = new Result(query, entityList, featureList, explanation);

		DataUtil.getLogManager().appendInfo("\nResult: " + result + "\nTime: " + (System.currentTimeMillis() - time) / 1000 + "s!");

		return result;
	}

	@Override
	public Result getProfile(List<String> queryEntityStringList, String queryEntityString) {
		long time = System.currentTimeMillis();

		List<Entity> queryEntityList = Parser.encodeEntityList(queryEntityStringList);

		Query query = new Query(null, queryEntityList, null);

		List<Entity> entityList = queryEntityList;
		List<Feature> featureList = Ranker.getFeatureList(Arrays.asList(Parser.encodeEntity(queryEntityString)), null, false);
		Explanation explanation = Ranker.getExplanation(entityList, featureList);

		Result result = new Result(query, entityList, featureList, explanation);

		DataUtil.getLogManager().appendInfo("\nResult: " + result + "\nTime: " + (System.currentTimeMillis() - time) / 1000 + "s!");

		return result;
	}
}
