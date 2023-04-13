package models;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to write Junit tests to verify correct working of TransactionModel.
 */
public class TransactionModelTest {

  @Test
  public void testGenerateTransaction() {
    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime())
            .qty(50)
            .exchange("NASDAQ")
            .price(123.4)
            .build();

    assertEquals(50, transaction.getQty(), 0.1);
    assertEquals(new GregorianCalendar(2020, Calendar.FEBRUARY, 11)
            .getTime(), transaction.getTransactionDate());
    assertEquals("NASDAQ", transaction.getExchange());
    assertEquals(123.4, transaction.getPrice(), 0.1);
  }

  @Test
  public void testReadXML() throws XPathExpressionException, IOException,
          ParserConfigurationException, ParseException, SAXException {
    TransactionModel transactionModel = TransactionModelImpl.getBuilder()
            .readXML("TestingHelper//UserSetModel//1000//flexible//GOOG//Transactions", 1)
            .build();
    assertEquals("NASDAQ", transactionModel.getExchange());
    assertEquals(new GregorianCalendar(2021, Calendar.OCTOBER, 13)
            .getTime(), transactionModel.getTransactionDate());
    assertEquals(22.0, transactionModel.getQty(), 0.1);
    assertEquals(2758.0, transactionModel.getPrice(), 0.1);
    assertEquals(12.0, transactionModel.getBrokerCommission(), 0.1);
  }

  @Test
  public void testGenerateXML() throws IOException, ParserConfigurationException,
          TransformerException, XPathExpressionException, ParseException, SAXException {
    TransactionModel transactionModel = TransactionModelImpl.getBuilder()
            .readXML("TestingHelper//UserSetModel//1000//flexible//GOOG//Transactions", 1)
            .build();
    transactionModel.generateXML("TestingHelper//UserSetModel//"
            + "1000//flexible//GOOG//Transactions", 1);
    File file = new File("TestingHelper//UserSetModel//1000//"
            + "flexible//GOOG//Transactions//1.xml");
    assertTrue(file.exists());
  }
}
