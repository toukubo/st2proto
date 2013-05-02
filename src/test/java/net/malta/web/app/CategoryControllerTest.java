package net.malta.web.app;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;

import org.junit.Before;
import org.junit.Test;
import static org.junit.matchers.JUnitMatchers.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class CategoryControllerTest {

	private final static int PORT = 8080;
	private final static String CONTEXT_PATH = "/st2proto";
	private final static String BASE_URL = "http://localhost:" + PORT
			+ CONTEXT_PATH;

	@Before
	public void prepare() throws Exception {

// TODO: run embedded Jetty for zero setup test runs...
		
//		server = new Server(PORT);
//		server.setHandler(new WebAppContext("src/main/webapp", CONTEXT_PATH));
//		server.start();

		setBaseUrl(BASE_URL);
	}

	@Test
	public void testIndex() {
		beginAt("category");
		assertTitleEquals("St2 Proto");
        assertTextInTable("categories", new String[] {"1", "Season's Special?1?"});
        assertTextInTable("categories", new String[] {"6", "Wreath ? Goods"});
	}
	
	@Test
	public void testNewAndDelete() {
		beginAt("category/new");
		assertTitleEquals("New Category");
		
		final String TEST_CAT = "JwebtestCategory"; 
		setTextField("name", TEST_CAT);
		submit();
		
		assertTextPresent(TEST_CAT);
		Table catList = getTable("categories");
		assertThat(catList.getRowCount(), is(8));
		
		Row testRow = (Row) catList.getRows().get(catList.getRowCount()-1);
		assertThat(testRow.getCellCount(), is(3));
		clickLinkWithText("Delete", 6);
		
		assertTextPresent("Delete Category");
		assertSubmitButtonPresent("Delete");
		submit();
		
		catList = getTable("categories");
		assertThat(catList.getRowCount(), is(7));
	}

    @Test
    public void testShowTagsJson() {
        beginAt("category/5/showTags.json");
        final String EXPECTED = 
                "{\"id\":5,\"tags\":[{\"tagId\":1111},{\"tagId\":2222}]}";
        assertEquals(EXPECTED, getPageSource());
    }

    @Test
    public void testShowWithProductJson() {
        beginAt("category/5/showWithProduct.json");
        final String EXPECTED = "{\"id\":5,\"name\":\"Potted Green\",\"products\":[\"1\",\"2\"],\"tags\":[]}";
        assertEquals(EXPECTED, getPageSource());
    }

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
    
}
