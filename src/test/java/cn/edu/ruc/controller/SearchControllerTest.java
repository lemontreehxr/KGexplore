/*
 * Copyright (c) 2018. by Chen Jun
 */

package cn.edu.ruc.controller;

import cn.edu.ruc.core.DataUtil;
import cn.edu.ruc.service.SearchService;
import cn.edu.ruc.service.imp.SearchServiceImp;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/** 
* SearchController Tester. 
* 
* @author <Authors name> 
* @since <pre>Mar 8, 2018</pre> 
* @version 1.0 
*/
@RunWith(Parameterized.class)
public class SearchControllerTest {
    private static DataUtil dataUtil;
    private static SearchService searchService;

    @Parameterized.Parameter(0)
    public String userId;

    @Parameterized.Parameters
    public static List<Object> getParameters() {
        return Arrays.asList(new Object[][]{
                {"0"},
                {"1"},
                {"2"},
                {"3"},
                {"4"},
                {"5"},
                {"6"},
                {"7"},
                {"8"},
                {"9"},
                {"10"},
                {"11"},
                {"12"},
                {"13"},
                {"14"},
                {"15"},
                {"16"},
                {"17"}
        });
    }

    @BeforeClass
    public static void beforeClass() {
        dataUtil = new DataUtil();
        searchService = new SearchServiceImp();
        System.out.println("@BeforeClass");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("@AfterClass");
    }

    /**
    *
    * Method: getAssess()
    *
    */
    @Test
    public void testGetAssess() throws Exception {
        System.out.println("userId:" + userId + "\n" + searchService.getAssess(userId));
    }

    /**
    *
    * Method: sendUser(@RequestParam(value = "userId") String userId)
    *
    */
    @Test
    public void testSendUser() throws Exception {
    //TODO: Test goes here...
    }

    /**
    *
    * Method: sendAssessEntity(@RequestParam(value = "id") String id, @RequestParam(value = "flag") String flag, @RequestParam(value = "queryEntity") String[] queryEntityStringList, @RequestParam(value = "relevantEntity") String relevantEntityString, @RequestParam(value = "score") double score, @RequestParam(value = "rank") int rank, @RequestParam(value = "assess") int assess, @RequestParam(value = "time") double time)
    *
    */
    @Test
    public void testSendAssessEntity() throws Exception {
    //TODO: Test goes here...
    }

    /**
    *
    * Method: sendAssessFeature(@RequestParam(value = "id") String id, @RequestParam(value = "flag") String flag, @RequestParam(value = "queryEntity") String[] queryEntityStringList, @RequestParam(value = "relevantEntity") String relevantEntityString, @RequestParam(value = "relevantFeature") String relevantFeatureString, @RequestParam(value = "assess") int assess, @RequestParam(value = "time") double time)
    *
    */
    @Test
    public void testSendAssessFeature() throws Exception {
    //TODO: Test goes here...
    }
} 
