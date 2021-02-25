package RefundPackage;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RefundTest extends Refund {

    @Test
    void testReplaceAllPrices1() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertEquals(234.00, replaceAllPrices(list, 3));
    }

    @Test
    void testReplaceAllPrices2() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertEquals(90.00, replaceAllPrices(list, 6));
    }

    @Test
    void testReplaceAllPrices3() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertEquals(125.00, replaceAllPrices(list, 9));
    }

    @Test
    void testValidClient1() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertEquals("100323", list.get(0));
    }

    @Test
    void testValidClient2() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertNotEquals("10032", list.get(0));
    }

    @Test
    void testValidClient3() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertNotEquals("1003230", list.get(0));
    }

    @Test
    void testValidClient4() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertNotEquals("100a32", list.get(0));
    }

    @Test
    void testValidClient5() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertNotEquals("a00323", list.get(0));
    }

    @Test
    void testValidContract1() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertEquals("A", list.get(1));
    }

    @Test
    void testValidContract2() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertNotEquals("K", list.get(1));
    }

    @Test
    void testValidTypeOfCare1() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        boolean TypeOfCare = Refund.validTypeOfCare(list);
        assertEquals(true, Refund.validTypeOfCare(list));
    }

    @Test
    void testValidTypeOfCare2() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        boolean TypeOfCare = Refund.validTypeOfCare(list);
        assertNotEquals(false, Refund.validTypeOfCare(list));
    }

    @Test
    void testValidDates1() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertEquals(true, Refund.validDates(list));
    }

    @Test
    void testValidDates2() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertNotEquals(false, Refund.validDates(list));
    }


    @Test
    void testValidMonths1() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertEquals(true, Refund.validMonths(list));
    }

    @Test
    void testValidMonths2() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertNotEquals(false, Refund.validMonths(list));
    }

    @Test
    void testValidBetweenMonths1() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertEquals(true, Refund.validBetweenMonths(list));
    }

    @Test
    void testValidBetweenMonths2() throws IOException, ParseException {
        List<String>  list = Refund.CreateListe();
        assertNotEquals(false, Refund.validBetweenMonths(list));
    }
}