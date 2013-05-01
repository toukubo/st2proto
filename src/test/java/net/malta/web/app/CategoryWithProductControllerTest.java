package net.malta.web.app;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CategoryWithProductControllerTest {

	private final static int PORT = 8080;
	private final static String CONTEXT_PATH = "/st2proto";
	private final static String BASE_URL = "http://localhost:" + PORT
			+ CONTEXT_PATH;

	@Before
	public void prepare() throws Exception {
		setBaseUrl(BASE_URL);
	}

	@Test
	public void testShowWithProductJson() {
		beginAt("category/showWithProduct/5.json");
		final String EXPECTED = "{\"id\":5,\"name\":\"Potted Green\",\"products\":[\"1\",\"2\"]}";
		assertEquals(EXPECTED, getPageSource());
	}

    @Test
    public void testShowWithProductXml() {
        beginAt("category/showWithProduct/5.xml");
        String XML = getPageSource();
        assertMatch("<id>5</id>", XML);
        assertMatch("<name>Potted Green</name>", XML);
        assertMatch("<products class=\"list\">", XML);
        assertMatch("<string>1</string>", XML);
    }

    @Test
    public void testShowWithProductXhtml() {
        beginAt("category/showWithProduct/5.xhtml");
        assertTitleEquals("Category 5");
        assertMatch("Potted Green");
    }

}
