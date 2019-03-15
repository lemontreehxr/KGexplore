package cn.edu.ruc.controller;

import cn.edu.ruc.model.Result;
import cn.edu.ruc.service.ExploreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/")
@Scope("prototype")
public class ExploreController {
	@Autowired
	ExploreService exploreService;

	//localhost:8080/controller/getResult?keywords=Forrest&queryEntities=Forrest Gump&queryFeatures=Tom Hanks%23%23Actor%23%23-1
	@ResponseBody
	@RequestMapping(value="getResult", method=RequestMethod.GET)
	public Result explore(@RequestParam(required = false, value = "keywords") String keywords, @RequestParam(required = false, value = "queryEntities") String[] queryEntityStringList, @RequestParam(required = false, value = "queryFeatures") String[] queryFeatureStringList){
		return exploreService.getResult(keywords, Arrays.asList(queryEntityStringList), Arrays.asList(queryFeatureStringList));
	}

	//localhost:8080/controller/getProfile?queryEntity=Forrest Gump
	@ResponseBody
	@RequestMapping(value="getProfile", method=RequestMethod.GET)
	public Result search(@RequestParam(required = false, value = "queryEntities") String[] queryEntityStringList, @RequestParam(required = false, value = "queryEntity") String queryEntityString){
		return exploreService.getProfile(Arrays.asList(queryEntityStringList), queryEntityString);
	}
}
