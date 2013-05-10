package net.storyteller2.web.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static net.storyteller2.web.test.Util.*;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testIdFromUrl() {
        assertThat(idFromUrl("/st2proto/category/12"), equalTo("12"));
        assertThat(idFromUrl("/st2proto/category/12.json"), equalTo("12"));
        assertThat(idFromUrl("/st2proto/category/12.xhtml"), equalTo("12"));
        assertThat(
                idFromUrl("http://localhost:8080/st2proto/category/12.json"),
                equalTo("12"));

        assertThat(idFromUrl("/st2proto/category"), nullValue());
        assertThat(idFromUrl("http://localhost:8080/st2proto/category"),
                nullValue());
    }

}
