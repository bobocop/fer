package opjj.hw2;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class MoneyToTextConverterTest {

    private MoneyToTextConverter c = new MoneyToTextConverter();

    @Test(expected = IllegalArgumentException.class)
    public void convertShouldThrowAnExceptionOnNullArgument() {
        c.convert(null);
    }

    @Test
    public void textualRepresentationOfAPositiveSum() {
        assertConversion(0,  "nula kuna i nula lipa");
        assertConversion(1,  "jedna kuna i nula lipa");
        assertConversion(2,  "dvije kune i nula lipa");
        assertConversion(3,  "tri kune i nula lipa");
        assertConversion(4,  "četiri kune i nula lipa");
        assertConversion(5,  "pet kuna i nula lipa");
        assertConversion(6,  "šest kuna i nula lipa");
        assertConversion(7,  "sedam kuna i nula lipa");
        assertConversion(8,  "osam kuna i nula lipa");
        assertConversion(9,  "devet kuna i nula lipa");
        assertConversion(10, "deset kuna i nula lipa");
        assertConversion(11, "jedanaest kuna i nula lipa");
        assertConversion(12, "dvanaest kuna i nula lipa");
        assertConversion(13, "trinaest kuna i nula lipa");
        assertConversion(14, "četrnaest kuna i nula lipa");
        assertConversion(15, "petnaest kuna i nula lipa");
        assertConversion(16, "šesnaest kuna i nula lipa");
        assertConversion(17, "sedamnaest kuna i nula lipa");
        assertConversion(18, "osamnaest kuna i nula lipa");
        assertConversion(19, "devetnaest kuna i nula lipa");
        assertConversion(20, "dvadeset kuna i nula lipa");
        assertConversion(21, "dvadeset jedna kuna i nula lipa");
        assertConversion(22, "dvadeset dvije kune i nula lipa");
        assertConversion(29, "dvadeset devet kuna i nula lipa");
        assertConversion(30, "trideset kuna i nula lipa");
        assertConversion(40, "četrdeset kuna i nula lipa");
        assertConversion(50, "pedeset kuna i nula lipa");
        assertConversion(60, "šezdeset kuna i nula lipa");
        assertConversion(70, "sedamdeset kuna i nula lipa");
        assertConversion(80, "osamdeset kuna i nula lipa");
        assertConversion(90, "devedeset kuna i nula lipa");
        assertConversion(95, "devedeset pet kuna i nula lipa");
        assertConversion(100,    "jedna stotina kuna i nula lipa");
        assertConversion(111,    "jedna stotina jedanaest kuna i nula lipa");
        assertConversion(157,    "jedna stotina pedeset sedam kuna i nula lipa");
        assertConversion(200,    "dvije stotine kuna i nula lipa");
        assertConversion(300,    "tri stotine kuna i nula lipa");
        assertConversion(567,    "pet stotina šezdeset sedam kuna i nula lipa");
        assertConversion(729,    "sedam stotina dvadeset devet kuna i nula lipa");
        assertConversion(1000,   "jedna tisuća kuna i nula lipa");
        assertConversion(1024,   "jedna tisuća dvadeset četiri kune i nula lipa");
        assertConversion(3000,   "tri tisuće kuna i nula lipa");
        assertConversion(5000,   "pet tisuća kuna i nula lipa");
        assertConversion(14000,  "četrnaest tisuća kuna i nula lipa");
        assertConversion(50001,  "pedeset tisuća jedna kuna i nula lipa");
        assertConversion(148345, "jedna stotina četrdeset osam tisuća tri stotine četrdeset pet kuna i nula lipa");
        assertConversion(190853, "jedna stotina devedeset tisuća osam stotina pedeset tri kune i nula lipa");
        assertConversion(234692, "dvije stotine trideset četiri tisuće šest stotina devedeset dvije kune i nula lipa");
        assertConversion(571460, "pet stotina sedamdeset jedna tisuća četiri stotine šezdeset kuna i nula lipa");
        assertConversion(3.0, "tri kune i nula lipa");
        assertConversion(15.03, "petnaest kuna i tri lipe");
        assertConversion(3.29, "tri kune i dvadeset devet lipa");
        assertConversion(1000000, "jedan milijun kuna i nula lipa");
        assertConversion(2000000, "dva milijuna kuna i nula lipa");
        assertConversion(57000000, "pedeset sedam milijuna kuna i nula lipa");
        assertConversion(800000000.01, "osam stotina milijuna kuna i jedna lipa");
        assertConversion(42110023.24, "četrdeset dva milijuna jedna stotina deset tisuća dvadeset tri kune i dvadeset četiri lipe");
        assertConversion(1234567.88, "jedan milijun dvije stotine trideset četiri tisuće pet stotina šezdeset sedam kuna i osamdeset osam lipa");
        assertConversion(991_992_999.03, "devet stotina devedeset jedan milijun " +
        		"devet stotina devedeset dvije tisuće devet stotina devedeset devet kuna i tri lipe");
    }

    @Test
    public void textualRepresentationOfANegativeSum() {
        assertConversion(7,    "sedam kuna i nula lipa");
        assertConversion(-15,  "petnaest kuna i nula lipa");
        assertConversion(-239, "dvije stotine trideset devet kuna i nula lipa");
    }

    private void assertConversion(double n, String text) {
        assertEquals(text, c.convert(new BigDecimal(n)));
    }

}
