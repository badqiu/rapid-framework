package cn.org.rapid_framework.generator.util;
import org.junit.Assert;
import org.junit.Test;

public class InflectorTest extends Assert {
    
    @Test
    public void testPluralization() {
        Inflector i = Inflector.getInstance();
        assertEquals("quizzes", i.pluralize("quiz"));
        assertEquals("QUIZzes", i.pluralize("QUIZ"));
        assertEquals("matrices", i.pluralize("matrix"));
        assertEquals("people", i.pluralize("person"));
        assertEquals("kids", i.pluralize("kid"));
        assertEquals("bashes", i.pluralize("bash"));
    }
    @Test
    public void testSingularization() {
        Inflector i = Inflector.getInstance();
        assertEquals("matrix", i.singularize("matrices"));
        assertEquals("quiz", i.singularize("quizzes"));
        assertEquals("person", i.singularize("people"));
        assertEquals("kid", i.singularize("kids"));
        assertEquals("bash", i.singularize("bashes"));
    }
}