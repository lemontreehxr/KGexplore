package cn.edu.ruc.controller;

import cn.edu.ruc.model.Assess;
import cn.edu.ruc.model.Dropdown;
import cn.edu.ruc.model.Profile;
import cn.edu.ruc.model.Query;
import cn.edu.ruc.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@Controller
@RequestMapping("/")
@Scope("prototype")
public class SearchController {
	@Autowired
	SearchService searchService;

	//localhost:8080/controller/getAssess?userId=
	@ResponseBody
	@RequestMapping(value = "getAssess", method = RequestMethod.GET)
	public Assess getAssess(@RequestParam(value = "userId") String userId){
		return searchService.getAssess(userId);
	}

	//localhost:8080/controller/sendUser?userId=
	@ResponseBody
	@RequestMapping(value="sendUser", method=RequestMethod.GET)
	public void sendUser(@RequestParam(value = "userId") String userId){
		searchService.sendUser(userId);
	}

	//localhost:8080/controller/sendBookmark?userId=test&taskId=1&versionId=1&relevantEntities=Cast Away_1_7000_timestamp__Forrest Gump_Tom Hanks%23%23Actor%23%23-1
	@ResponseBody
	@RequestMapping(value="sendBookmark", method=RequestMethod.GET)
	public void sendBookmark(@RequestParam(value = "userId") String userId, @RequestParam(value = "taskId") int taskId, @RequestParam(value = "versionId") int versionId, @RequestParam(value = "entityString") String entityString, @RequestParam(value = "relevance") int relevance){
		searchService.sendBookmark(userId, taskId, versionId, entityString, relevance);
	}

	//localhost:8080/controller/sendInteraction?userId=test&taskId=1&versionId=1&area=query&option=search&content="entity[], feature[]"&timestamp=1000
	@ResponseBody
	@RequestMapping(value="sendInteraction", method=RequestMethod.GET)
	public void sendInteraction(@RequestParam(value = "userId") String userId, @RequestParam(value = "taskId") int taskId, @RequestParam(value = "versionId") int versionId, @RequestParam(value = "option") String option, @RequestParam(value = "target") String target, @RequestParam(value = "queryContent") String queryContent, @RequestParam(value = "timestamp") String timestamp){
		searchService.sendInteraction(userId, taskId, versionId, option, target, queryContent, timestamp);
	}
}
