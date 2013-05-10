package net.malta.web.app;

import static com.jayway.restassured.RestAssured.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import static net.storyteller2.web.test.Util.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CategoryControllerTest {

    private final static int PORT = 8080;
    private final static String CONTEXT_PATH = "/st2proto";
    private final static String BASE_URL = "http://localhost:" + PORT
            + CONTEXT_PATH;

    private final static String PATH = CONTEXT_PATH + "/category";
    private final static String TEST_CAT = "JwebtestCategory";
    private static Server server;

    @BeforeClass
    public static void prepare() throws Exception {
//        server = new Server(PORT);
//
//        WebAppContext webAppContext = new WebAppContext();
//        webAppContext.setWar("target/st2proto.war"); // from "mvn war:war"
//        webAppContext.setContextPath("/st2proto");
//        // webAppContext.setServer(server);
//
//        server.setHandler(webAppContext);
//        server.start();

        setBaseUrl(BASE_URL);
    }

    @AfterClass
    public static void stop() throws Exception {
//        server.stop();
    }

    /******************* JSON Tests - START ********************/
    // to preserve readable rest assured format - disable auto formatter:
    // @formatter:off
    @Test
    public void testJsonGet() throws Exception {
        expect().
            body("id", equalTo(1)).and().
            body("name", equalTo("Season's Special?1?")).
        when().
            get(PATH + "/1.json");
    }

    @Test
    public void testJsonCreate() throws Exception {
        final String CAT_NAME = "New Cat";
        
        // create new
        String newCatUrl =
            given().
                body("{ \"name\" : \"" + CAT_NAME + "\"}").
                contentType("application/json").
            expect().
                statusCode(201).and().
                header("location", containsString(PATH)).and().
                header("location", endsWith(".json")).
            when().
                post(PATH + ".json").getHeader("Location");
        
        // assert new name
        expect().
            body(containsString("\"name\":\"" + CAT_NAME + "\"")).
        when().
            get(newCatUrl);
    }

    @Test
    public void testJsonDelete() throws Exception {
        // new category
        String categoryUri = given().body("{ \"name\" : \"New Cat\"}")
                .post(PATH + ".json").getHeader("Location");

        // delete category
        expect().
            statusCode(200).
        when().
            delete(PATH + "/" + idFromUrl(categoryUri) + ".json");
    }
    
    @Test
    public void testJsonUpdate() throws Exception {
        final String CAT_NAME = "Test Cat";
        
        // new category
        String categoryUri = given().body("{ \"name\" : \"" + TEST_CAT + "\"}")
                .post(PATH + ".json").getHeader("Location");

        // edit category
        final String NEW_CAT_NAME = CAT_NAME + " edited";
        given().
            parameter("name", NEW_CAT_NAME).
            contentType("application/json").
        expect().
            statusCode(200).
        when().
            put(PATH + "/" + idFromUrl(categoryUri) + ".json");

        // check new name
        expect().
            body(containsString("\"name\":\"" + NEW_CAT_NAME + "\"")).
        when().
            get(categoryUri);
    }
    // @formatter:on
    /******************* JSON Tests - END ********************/

    /******************* XHTML Tests - START ********************/
    @Test
    public void testIndex() {
        beginAt("category");
        assertTitleEquals("St2 Proto");
        assertTextInTable("categories", new String[] { "1",
                "Season's Special?1?" });
        assertTextInTable("categories", new String[] { "6", "Wreath ? Goods" });
    }

    @Test
    public void testNewAndDelete() {
        beginAt("category/new");
        assertTitleEquals("New Category");

        setTextField("name", TEST_CAT);
        submit();
        assertTextPresent(TEST_CAT);

        Table catList = getTable("categories");
        assertThat(catList.getRowCount(), is(8));

        Row testRow = (Row) catList.getRows().get(catList.getRowCount() - 1);
        assertThat(testRow.getCellCount(), is(3));
        clickLinkWithText("Delete", 6);

        assertTextPresent("Delete Category");
        assertSubmitButtonPresent("Delete");
        submit();

        catList = getTable("categories");
        assertThat(catList.getRowCount(), is(7));
    }

    @Test
    public void testEdit() {
        beginAt("category/new");
        setTextField("name", TEST_CAT);
        submit();

        clickLinkWithText("Edit", 6);
        assertTitleMatch("Category.*");
        assertTextFieldEquals("name", TEST_CAT);

        final String NEW_CAT_NAME = TEST_CAT + " new";
        setTextField("name", NEW_CAT_NAME);
        submit();

        assertTextInTable("categories", NEW_CAT_NAME);

        clickLinkWithText("Delete", 6);
        submit();
    }

    @Test
    public void testShowTagsJson() {
        beginAt("category/5/showTags.json");
        final String EXPECTED = "{\"id\":5,\"tags\":[{\"tagId\":1111},{\"tagId\":2222}]}";
        assertEquals(EXPECTED, getPageSource());
    }

    @Test
    public void testShowWithProductJson() {
        beginAt("category/5/showWithProduct.json");
        final String EXPECTED = "{\"id\":5,\"name\":\"Potted Green\",\"products\":[\"1\",\"2\"],\"tags\":[]}";
        assertEquals(EXPECTED, getPageSource());
    }

    /******************* XHTML Tests - END ********************/

    /******************* showWithProduct Tests - START ********************/
    @Test
    public void testShowWithProductXml() {
        beginAt("category/5/showWithProduct.xml");
        String XML = getPageSource();
        assertMatch("<id>5</id>", XML);
        assertMatch("<name>Potted Green</name>", XML);
        assertMatch("<products class=\"list\">", XML);
        assertMatch("<string>1</string>", XML);
    }

    @Test
    public void testShowWithProductXhtml() {
        beginAt("category/5/showWithProduct.xhtml");
        assertTitleEquals("Category 5");
        assertMatch("Potted Green");
    }
    /******************* showWithProduct Tests - END ********************/

}
