package net.malta.web.app;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GeneratedCategoryControllerTest extends StoryTellerTest {

    // Id and name of test category available to all tests
    private final static String TEST_CAT_NAME = "Testy";
    private String TEST_CAT_ID;
    private String TEST_CAT_URI;

    @After
    @Before
    public void cleanDataBeforeAll() {
        removeAll();
    }

    @Before
    public void prepare() {
        super.prepare();
        TEST_CAT_ID = create(TEST_CAT_NAME);
        TEST_CAT_URI = getResourceUri() + "/" + TEST_CAT_ID + ".json";
    }

    @Test
    public void testJsonGet() {
        expect().
            body("id", equalTo(Integer.valueOf(TEST_CAT_ID))).and().
            body("name", equalTo(TEST_CAT_NAME)).
        when().
            get(getResourceUri() + "/" + TEST_CAT_ID + ".json");
    }

    @Test
    public void testJsonCreate()  {
        final String CAT_NAME = testName("New Cat");
        
        String newCatUrl =
            given().
                body("{ \"name\" : \"" + CAT_NAME + "\"}").
                contentType("application/json").
            expect().
                statusCode(201).and().
                header("location", containsString(DEFAULT_PATH)).and().
                header("location", endsWith(".json")).
            when().
                post(getResourceUri() + ".json").getHeader("Location");
        
        // assert exists on returned Location
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
    
}
