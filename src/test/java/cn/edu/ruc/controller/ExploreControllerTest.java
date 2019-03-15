package cn.edu.ruc.controller;

import cn.edu.ruc.core.DataUtil;
import cn.edu.ruc.service.ExploreService;
import cn.edu.ruc.service.SearchService;
import cn.edu.ruc.service.imp.ExploreServiceImp;
import cn.edu.ruc.service.imp.SearchServiceImp;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

/** 
* ExploreController Tester. 
* 
* @author <Authors name> 
* @since <pre>Feb 10, 2018</pre> 
* @version 1.0 
*/
@RunWith(Parameterized.class)
public class ExploreControllerTest {
    private static DataUtil dataUtil;
    private static SearchService searchService;
    private static ExploreService exploreService;

    @Parameterized.Parameter(0)
    public String keywords;
    @Parameterized.Parameter(1)
    public String queryEntityString;
    @Parameterized.Parameter(2)
    public List<String> queryEntityStringList;
    @Parameterized.Parameter(3)
    public List<String> queryFeatureStringList;
    @Parameterized.Parameter(4)
    public int versionId;

    @Parameterized.Parameters
    public static List<Object> getParameters() {
        return Arrays.asList(new Object[][]{
                {"forrest", "Forrest_Gump", Arrays.asList(new String[]{"Forrest_Gump", "JFK_(film)"}), Arrays.asList(new String[]{}), 1},
                {"tom hanks", "Forrest_Gump", Arrays.asList(new String[]{}), Arrays.asList(new String[]{}), 2},
                {"jfk", "Forrest_Gump", Arrays.asList(new String[]{}), Arrays.asList(new String[]{"Tom_Hanks##starring##-1"}), 3},
                {"forrest gump", "Forrest_Gump", Arrays.asList(new String[]{}), Arrays.asList(new String[]{"Tom_Hanks##starring##-1"}), 3},
                {"tom hanks film", "Forrest_Gump", Arrays.asList(new String[]{}), Arrays.asList(new String[]{"Tom_Hanks##starring##-1"}), 3}
        });
    }

    @BeforeClass
    public static void beforeClass() {
        dataUtil = new DataUtil();
        searchService = new SearchServiceImp();
        exploreService = new ExploreServiceImp();
        System.out.println("@BeforeClass");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("@AfterClass");
    }

    @Before
    public void before() throws Exception {
        System.out.println("@Before");
    }

    @After
    public void after() throws Exception {
        System.out.println("@After");
    }

    /**
    *
    * Method: getResult(@RequestParam(required = false, value = "entities") String[] entityStringList, @RequestParam(required = false, value = "features") String[] featureStringList)
    *
    */
    @Test
    public void testGetResult() throws Exception {
        System.out.println("@Test(getResult(@RequestParam(required = false, value = \"entities\") String[] entityStringList, @RequestParam(required = false, value = \"features\") String[] featureStringList))");

        exploreService.getResult(keywords, queryEntityStringList, queryFeatureStringList);
    }


    /**
     *
     * Method: getResult(@RequestParam(required = false, value = "entities") String[] entityStringList, @RequestParam(required = false, value = "features") String[] featureStringList)
     *
     */
    /*@Test
    public void testGetProfile() throws Exception {
        System.out.println("@Test(getResult(@RequestParam(required = false, value = \"entities\") String[] entityStringList, @RequestParam(required = false, value = \"features\") String[] featureStringList))");

        exploreService.getProfile(queryEntityStringList, queryEntityStringList.get(0));
    }*/
} 
