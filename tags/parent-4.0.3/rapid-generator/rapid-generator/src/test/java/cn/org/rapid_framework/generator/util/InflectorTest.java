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
    
//    @Test 
//    public void test() {
//        Inflector inflector = Inflector.getInstance();
//        for (int i = 0; i < DATA.length; i++) {
//            String singular = DATA[i][0];
//            String plural = DATA[i][1];
//            assertEquals(plural, inflector.pluralize(singular));
//        }
//    }
//    
//    private static final String[][] DATA = {
//                { "biglietto", "biglietti" },
//                { "supermercato", "supermercati" },
//                { "fratello", "fratelli" },
//                { "figlio", "figli" },
//                { "bacio", "baci" },
//                { "zio", "zii" },
//                { "parco", "parchi" },
//                { "lago", "laghi" },
//                { "amico", "amici" },
//                { "asparago", "asparagi" },
//
//                { "birra", "birre" },
//                { "macchina", "macchine" },
//                { "figlia", "figlie" },
//                { "barca", "barche" },
//                { "casalinga", "casalinghe" },
//                { "spiaggia", "spiagge" },
//                { "arancia", "arance" },
//                { "farmacia", "farmacie" },
//                { "valigia", "valigie" },
//                
//                { "cane", "cani" },
//                { "carne", "carni" },
//                { "nome", "nomi" },
//                { "notte", "notti" },
//                { "valore", "valori" },
//                { "valle", "valli" },
//                
//                { "radio", "radio" },       
//                { "foto", "foto" },     
//                { "moto", "moto" },
//                { "mano", "mani" },
//                
//                { "problema", "problemi" },     
//                { "programma", "programmi" },       
//                { "aroma", "aromi" },
//                { "cinema", "cinema" },
//                { "clima", "clima" },
//                { "vittima", "vittime" },
//                
//                { "citt\u00e0", "citt\u00e0" },
//                { "difficolt\u00e0", "difficolt\u00e0" },
//                { "caff\u00e8", "caff\u00e8" },
//                { "tass\u00ec", "tass\u00ec" },
//                { "virt\u00f9", "virt\u00f9" },
//                
//                { "brindisi", "brindisi" },
//                { "crisi", "crisi" },
//                { "specie", "specie" },
//                { "moglie", "mogli" },
//                
//                { "computer", "computer" },
//                { "chef", "chef" },
//                { "hostess", "hostess" },
//
//                { "uovo", "uova" },
//                { "lenzuolo", "lenzuola" },
//                { "paio", "paia" },
//                { "braccio", "braccia" },
//                { "dito", "dita" },
//                { "centinaio", "centinaia" },
//                { "uomo", "uomini" },
//                { "dio", "dei" },
//                
//                { "turista", "turisti" }, // assume male form
//                { "giornalista", "giornalisti" }, // assume male form
//                { "collega", "colleghi" }, // assume male form
//                { "atleta", "atleti" }, // assume male form
//                
//            };
}