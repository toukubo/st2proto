package net.malta.web.app;

import static com.jayway.restassured.RestAssured.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import static net.storyteller2.web.test.Util.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.jwebunit.html.Cell;
import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.json.config.JsonPathConfig;

public class CategoryControllerTest {

    // Web Server Paths
    private final static String CONTEXT_PATH = "/st2proto";
    private final static String DEFAULT_PATH = CONTEXT_PATH + "/category";
    private static int PORT = 8080;

    // All test names should have this prefix. Ensures removeAll can clean up
    // test data.
    private final static String TEST_CAT_PREFIX = "TESTCAT";

    // Id and name of test category available to all tests
    private final static String TEST_CAT_NAME = catName("Testy");
    private String TEST_CAT_ID;
    private String TEST_CAT_URI;

    static {
        JsonPath.config = new JsonPathConfig("UTF-8");

        if (System.getProperty("appserver.port") != null)
            PORT = Integer.parseInt(System.getProperty("appserver.port"));

        RestAssured.port = PORT;
    }

    @BeforeClass
    public static void cleanDataBeforeAll() {
        removeAll();
    }

    @After
    public void cleanDataAfterEach() {
        removeAll();
    }

    @Before
    public void prepare() {
        setBaseUrl("http://localhost:" + PORT + CONTEXT_PATH);

        // create one category that can be used by the tests
        // tests requiring more data can create it themselves
        TEST_CAT_ID = createCategory(TEST_CAT_NAME);
        TEST_CAT_URI = DEFAULT_PATH + "/" + TEST_CAT_ID + ".json";
    }

    /******************* JSON Tests - START ********************/
    // to preserve readable rest assured format - disable auto formatter:
    // @formatter:off
    @Test
    public void testJsonGet() {
        expect().
            body("id", equalTo(Integer.valueOf(TEST_CAT_ID))).and().
            body("name", equalTo(TEST_CAT_NAME)).
        when().
            get(DEFAULT_PATH + "/" + TEST_CAT_ID + ".json");
    }

    @Test
    public void testJsonCreate()  {
        final String CAT_NAME = catName("New Cat");
        
        // create new
        String newCatUrl =
            given().
                body("{ \"name\" : \"" + CAT_NAME + "\"}").
                contentType("application/json").
            expect().
                statusCode(201).and().
                header("location", containsString(DEFAULT_PATH)).and().
                header("location", endsWith(".json")).
            when().
                post(DEFAULT_PATH + ".json").getHeader("Location");
        
        // assert new name
        expect().
            body(containsString("\"name\":\"" + CAT_NAME + "\"")).
        when().
            get(newCatUrl);
    }

    @Test
    public void testJsonDelete()  {
        // delete category
        expect().
            statusCode(200).
        when().
            delete(TEST_CAT_URI);
    }
    
    @Test
    public void testJsonUpdate()  {
        // edit category
        final String NEW_CAT_NAME = TEST_CAT_NAME + " edited";
        given().
            parameter("name", NEW_CAT_NAME).
            contentType("application/json").
        expect().
            statusCode(200).
        when().
            put(TEST_CAT_URI);

        // check new name
        expect().
            body(containsString("\"name\":\"" + NEW_CAT_NAME + "\"")).
        when().
            get(TEST_CAT_URI);
    }
    
    @Test
    public void testJsonShowTags() {
        beginAt("category/5/showTags.json");
        final String EXPECTED = "{\"id\":5,\"tags\":[{\"tagId\":1111},{\"tagId\":2222}]}";
        assertEquals(EXPECTED, getPageSource());
    }

    // @formatter:on
    /******************* JSON Tests - END ********************/

    /******************* XHTML Tests - START ********************/
    @Test
    public void testXHtmlIndex() {
        final String CAT1 = catName("chocolate");
        final String CAT2 = catName("beer");
        final String ID1 = createCategory(CAT1);
        final String ID2 = createCategory(CAT2);
        beginAt("category");
        assertTitleEquals("St2 Proto");
        assertTextInTable("categories", new String[] { ID1, CAT1 });
        assertTextInTable("categories", new String[] { ID2, CAT2 });
    }

    @Test
    public void testXHtmlNew() {
        final String NEW_CAT_NAME = catName("NewCat");

        beginAt("category/new");
        assertTitleEquals("New Category");

        setTextField("name", NEW_CAT_NAME);
        submit();
        assertTextPresent(NEW_CAT_NAME);
    }

    @Test
    public void testXHtmlDelete() {
        beginAt("category");

        Table table = getTable("categories");
        final int initalRowCount = table.getRowCount();
        int catLinksIdx = getTestCatTableIdx(table);
        clickLinkWithText("Delete", catLinksIdx);
        assertTextPresent("Delete Category");
        assertSubmitButtonPresent("Delete");

        submit();
        assertThat(getTable("categories").getRowCount(), is(initalRowCount - 1));
    }

    @Test
    public void testXHtmlEdit() {
        beginAt("category");

        int catLinksIdx = getTestCatTableIdx(getTable("categories"));
        clickLinkWithText("Edit", catLinksIdx);
        assertTitleMatch("Category " + TEST_CAT_ID);
        assertTextFieldEquals("name", TEST_CAT_NAME);

        final String NEW_CAT_NAME = TEST_CAT_NAME + " new";
        setTextField("name", NEW_CAT_NAME);
        submit();
        assertTextInTable("categories", NEW_CAT_NAME);
    }

    /******************* XHTML Tests - END ********************/

    /******************* showWithProduct Tests - START ********************/
    @Test
    public void testJsonShowWithProduct() {
        beginAt("category/" + TEST_CAT_ID + "/showWithProduct.json");
        final String EXPECTED = "{\"id\":" + TEST_CAT_ID + ",\"name\":\"" + TEST_CAT_NAME + "\",\"products\":[\"1\",\"2\"],\"tags\":[]}";
        assertEquals(EXPECTED, getPageSource());
    }

    @Test
    public void testXmlShowWithProduct() {
        beginAt("category/" + TEST_CAT_ID + "/showWithProduct.xml");
        String XML = getPageSource();
        assertMatch("<id>" + TEST_CAT_ID + "</id>", XML);
        assertMatch("<name>" + TEST_CAT_NAME + "</name>", XML);
        assertMatch("<products class=\"list\">", XML);
        assertMatch("<string>1</string>", XML);
    }

    @Test
    public void testXHtmlShowWithProduct() {
        beginAt("category/" + TEST_CAT_ID + "/showWithProduct.xhtml");
        assertTitleEquals("Category " + TEST_CAT_ID);
        assertMatch(TEST_CAT_NAME);
    }

    /******************* showWithProduct Tests - END ********************/

    /***************** PRIVATE Helper routines - START ******************/

    /*
     * Make a test cat name. Always prefex TEST_CAT to cat names so we clean up.
     * 
     * @param name Category name to add the prefix to
     */
    private static String catName(final String name) {
        return TEST_CAT_PREFIX + name;
    }

    /*
     * A helper to create a category for a name and check return code
     * 
     * @param name CategoryName
     * 
     * @return id Id of created category
     */
    private static String createCategory(final String name) {
        assert (name.startsWith(TEST_CAT_PREFIX));
        String uri = given().body("{ \"name\" : \"" + name + "\"}")
                .contentType("application/json").expect().statusCode(201)
                .when().post(DEFAULT_PATH + ".json").getHeader("Location");
        return idFromUrl(uri);
    }

    /*
     * Remove all categories with a name that starts with our prefix
     * TEST_CAT_PREFIX by doing an HTTP DELETE
     */
    private static void removeAll() {
        List<Map<String,Object>> categories = get(DEFAULT_PATH + ".json").jsonPath().getList(
                "");
        for (Map<String,Object> cat : categories) {
            boolean isTestCat = ((String) cat.get("name"))
                    .startsWith(TEST_CAT_PREFIX);
            if (isTestCat) {
                delete(DEFAULT_PATH + "/" + cat.get("id").toString());
            }
        }
    }

    /*
     * Find testcat in the Html Table and return the index.
     * This is index is used to lookup nth link in the table so subtract 1 for the <th> row.
     * @param table JWebunit Table
     * @return idx for the nth set of links for the Test category.
     */
    private int getTestCatTableIdx(Table table) {
        @SuppressWarnings("unchecked")
        ArrayList<Row> rows = table.getRows();
        for (int idx = 0; idx < rows.size(); idx++) {
            Row row = rows.get(idx);
            Cell cell = (Cell) row.getCells().get(0);
            String idStr = cell.getValue();
            if (idStr.equals(TEST_CAT_ID)) {
                return idx - 1; 
            }
        }
        return -1;
    }
    
    /***************** PRIVATE Helper routines - END ******************/

}
