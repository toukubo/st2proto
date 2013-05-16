package net.malta.web.app;

import static com.jayway.restassured.RestAssured.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import static net.storyteller2.web.test.Util.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.json.config.JsonPathConfig;

public abstract class StoryTellerTest {

    private static int PORT = 8080;
    private final static String CONTEXT_PATH = "/st2proto";

    private final static String TEST_DATA_PREFIX = "TEST_";

    static {
        JsonPath.config = new JsonPathConfig("UTF-8");

        if (System.getProperty("appserver.port") != null)
            PORT = Integer.parseInt(System.getProperty("appserver.port"));

        RestAssured.port = PORT;
    }

    @Before
    public void prepare() {
        setBaseUrl("http://localhost:" + PORT + CONTEXT_PATH);
    }

    /**
     * Name by which the resource is accessed in a URI. Conventions are as
     * follows:
     * <ul>
     * <li>/st2proto/category => 'category'</li>
     * <li>CategoryControllerTest => 'category'</li>
     * </ul>
     * 
     * @return Resource name in lowercase
     */
    protected String getResourceName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    /**
     * Get Resource Uri for the controller under test CategoryControllerTest =>
     * '/context/category'
     * 
     * @return URI for the entity being tested by this class
     */
    protected String getResourceUri() {
        return CONTEXT_PATH + "/" + getResourceName();
    }

    /*
     * A helper to create a category for a name and check return code
     * 
     * @param name CategoryName
     * 
     * @return id Id of created category
     */
    protected String create(String name) {
        String uri = given().body("{ \"name\" : \"" + name + "\"}")
                .contentType("application/json").expect().statusCode(201)
                .when().post(getResourceUri() + ".json").getHeader("Location");
        return idFromUrl(uri);
    }

    /**
     * Assumes entity has a name and an id.
     */
    protected void removeAll() {
        List<Map<String, Object>> entities = get(getResourceUri() + ".json")
                .jsonPath().getList("");
        for (Map<String, Object> entity : entities) {
            boolean isTestCat = ((String) entity.get("name"))
                    .startsWith(TEST_DATA_PREFIX);
            if (isTestCat) {
                delete(getResourceUri() + "/" + entity.get("id").toString());
            }
        }
    }

    /**
     * Return passed name with a prefix added on. Current mechanism for cleaning
     * the database is to create and delete all data with this prefix.
     * 
     * TODO: Much better would be to use Spring transactions so nothing gets
     * committed.
     * 
     * @param name
     * @return test prefix + name
     */
    protected String testName(String name) {
        return TEST_DATA_PREFIX + name;
    }

}
