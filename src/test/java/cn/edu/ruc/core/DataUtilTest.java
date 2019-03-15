package cn.edu.ruc.core;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
* DataUtil Tester. 
* 
* @author <Authors name> 
* @since <pre>May 19, 2018</pre> 
* @version 1.0 
*/
@RunWith(Parameterized.class)
public class DataUtilTest {
    private static DataUtil dataUtil;

    @Parameterized.Parameter(0)
    public String keywords;
    @Parameterized.Parameter(1)
    public String entity;
    @Parameterized.Parameter(2)
    public String relation;
    @Parameterized.Parameter(3)
    public int direction;

    @Parameterized.Parameters
    public static List<Object> getParameters() {
        return Arrays.asList(new Object[][]{
                {"JFK", "Back to the Future", "starring", 1},
                {"tom hanks", "Tom Hanks", "starring", - 1},
                {"forrest gump", "Robert Zemeckis", "film director", - 1},
        });
    }

    @BeforeClass
    public static void beforeClass() {
        dataUtil = new DataUtil();
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
    * Method: getEntity2Id(String name)
    *
    */
    @Test
    public void testGetEntity2Id() throws Exception {
    //TODO: Test goes here...
        System.out.println(DataUtil.getEntity2Id(entity));
    }

    /**
    *
    * Method: getRelation2Id(String name)
    *
    */
    @Test
    public void testGetRelation2Id() throws Exception {
        System.out.println(DataUtil.getRelation2Id(relation));
    }

    /**
    *
    * Method: getRelationId2TargetIdMap(int queryEntityId)
    *
    */
    @Test
    public void testGetRelationId2TargetIdMap() throws Exception {
        for(int direction : DataUtil.Directions) {
            for (Map.Entry<Integer, Set<Integer>> relationId2TargetIdEntry : DataUtil.getRelationId2EntityIdSetMap(DataUtil.getEntity2Id(entity), direction, DataUtil.Threshold).entrySet()) {
                System.out.println(DataUtil.getId2Relation(relationId2TargetIdEntry.getKey()));
                for (int targetId : relationId2TargetIdEntry.getValue())
                    System.out.print("\t\"" + DataUtil.getId2Entity(targetId) + "\"");
                System.out.println();
            }
        }
    }

    /**
     *
     * Method: getSourceIdSet(int queryEntityId, int queryRelationId, int queryRelationDirection)
     *
     */
    @Test
    public void testGetSourceIdSet() throws Exception {
        for(int sourceId : DataUtil.getEntityIdSet(DataUtil.getEntity2Id(entity), DataUtil.getRelation2Id(relation), direction, 5000)) {
            System.out.println(DataUtil.getId2Entity(sourceId));
        }
    }
} 
