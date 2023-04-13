package models;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to write Junit tests to verify correct working of StockModel.
 */
public class StockModelTest {

  @Test
  public void testStockModelGettersWorkCorrectly() throws ParseException {
    String testDate = "2022-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);

    StockModel stockModel = new StockModelImpl("GOOG", "Google",
            "NASDAQ", date);

    assertEquals(stockModel.getStockTicker(), "GOOG");
    assertEquals(stockModel.getStockName(), "Google");
    assertEquals(stockModel.getExchangeName(), "NASDAQ");
    assertEquals(stockModel.getIpoDate(), date);
  }

  @Test
  public void testGetPriceWorksCorrectlyWhenDataAvailable() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String testIpoDate = "2012-11-01";
    Date ipoDate = formatter.parse(testIpoDate);

    String testPriceDate = "2022-10-27";
    Date priceDate = formatter.parse(testPriceDate);

    StockModel stockModel = new StockModelImpl("GOOG", "Google",
            "NASDAQ", ipoDate);

    assertEquals(92.6, stockModel.getPriceOnADate(priceDate), 0.001);
  }

  @Test
  public void testGetPriceWorksCorrectlyWhenDataIsUnAvailable() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String testIpoDate = "2012-11-01";
    Date ipoDate = formatter.parse(testIpoDate);

    String testPriceDate = "2022-10-29";
    Date priceDate = formatter.parse(testPriceDate);

    StockModel stockModel = new StockModelImpl("GOOG", "Google",
            "NASDAQ", ipoDate);

    assertEquals(96.58, stockModel.getPriceOnADate(priceDate), 0.001);
  }

  @Test
  public void testBuilderWorksCorrectly() throws ParseException {
    String testDate = "2022-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);

    StockModel stockModel = StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .ipoDate(date)
            .stockName("Google")
            .exchangeName("NASDAQ")
            .build();

    assertEquals(stockModel.getStockTicker(), "GOOG");
    assertEquals(stockModel.getStockName(), "Google");
    assertEquals(stockModel.getExchangeName(), "NASDAQ");
    assertEquals(stockModel.getIpoDate(), date);
  }

  @Test
  public void testReadXML() throws XPathExpressionException, IOException,
          ParserConfigurationException, ParseException, SAXException {
    StockModel stockModel = StockModelImpl.getBuilder()
            .readXML("TestingHelper//UserSetModel//"
                            + "1000//concrete//AAL//American Airlines Group Inc",
                    "American Airlines Group Inc").build();
    assertEquals("AAL", stockModel.getStockTicker());
    assertEquals("American Airlines Group Inc", stockModel.getStockName());
    assertEquals("NASDAQ", stockModel.getExchangeName());
  }

  @Test
  public void testGenerateXML() throws IOException, ParserConfigurationException,
          TransformerException, XPathExpressionException, ParseException, SAXException {
    StockModel stockModel = StockModelImpl.getBuilder()
            .readXML("TestingHelper//UserSetModel//"
                            + "1000//concrete//AAL//American Airlines Group Inc",
                    "American Airlines Group Inc").build();
    stockModel.generateXML("TestingHelper//Write//StockModel");
    File file = new File("TestingHelper//Write//StockModel");
    assertTrue(file.exists());
  }
}
