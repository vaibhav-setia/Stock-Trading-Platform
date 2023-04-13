package models;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to write Junit tests to verify correct working of PortfolioElementModel.
 */
public class PortfolioElementModelTest {

  @Test
  public void testInitializationAndGettersWorkCorrectly() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String testDate = "2022-11-01";
    Date date = formatter.parse(testDate);

    String ipoDate = "2022-10-22";
    Date ipo = formatter.parse(ipoDate);

    PortfolioElementModel portfolioElementModel = new PortfolioElementModelImpl(
            StockModelImpl.getBuilder()
                    .stockName("Google")
                    .stockTicker("GOOG")
                    .exchangeName("NASDAQ")
                    .ipoDate(ipo)
                    .build(), 101, 10, date, new ArrayList<>());

    assertEquals("Google", portfolioElementModel.getStockModel().getStockName());
    assertEquals(101, portfolioElementModel.getStockCurrentAvgPrice(), 0.001);
    assertEquals(10, portfolioElementModel.getStockTotalQuantity(), 0.1);
    assertEquals(date, portfolioElementModel.getTransactionDate());
  }

  @Test
  public void testPortfolioElementModelBuilderWorksCorrectly() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String testDate = "2022-11-01";
    Date date = formatter.parse(testDate);

    String ipoDate = "2022-10-22";
    Date ipo = formatter.parse(ipoDate);

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl
            .getBuilder()
            .stockModel(
                    StockModelImpl.getBuilder()
                            .stockName("Google")
                            .stockTicker("GOOG")
                            .exchangeName("NASDAQ")
                            .ipoDate(ipo)
                            .build())
            .totalQuantity(10)
            .avgPrice(101)
            .transactionDate(date)
            .build();

    assertEquals("Google", portfolioElementModel.getStockModel().getStockName());
    assertEquals(101, portfolioElementModel.getStockCurrentAvgPrice(), 0.001);
    assertEquals(10, portfolioElementModel.getStockTotalQuantity(), 0.1);
    assertEquals(date, portfolioElementModel.getTransactionDate());
  }

  @Test
  public void testReadXML() throws XPathExpressionException,
          IOException, ParserConfigurationException, ParseException, SAXException {
    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl
            .getBuilder()
            .readXML("TestingHelper//UserSetModel//1000//concrete//AAL", "AAL").build();
    assertEquals("AAL", portfolioElementModel.getStockModel().getStockTicker());
    assertEquals(44, portfolioElementModel.getStockTotalQuantity(), 0.1);
    assertEquals(19.53, portfolioElementModel.getStockCurrentAvgPrice(), 0.01);
  }

  @Test
  public void testGenerateXML() throws IOException,
          ParserConfigurationException, TransformerException,
          XPathExpressionException, ParseException, SAXException {
    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl
            .getBuilder()
            .readXML("TestingHelper//UserSetModel//1000//concrete//AAL", "AAL").build();
    portfolioElementModel.generateXML("TestingHelper//Write//PortfolioElementModel");
    File file = new File("TestingHelper//Write//PortfolioElementModel");
    assertTrue(file.exists());
  }

  @Test
  public void testTransactionCreation() throws IOException,
          ParserConfigurationException,
          XPathExpressionException, ParseException, SAXException {
    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl
            .getBuilder()
            .readXML("TestingHelper//UserSetModel//1000//concrete//AAL", "AAL").build();
    assertEquals(1, portfolioElementModel.getAllTransactions().size());

    PortfolioElementModel flexiblePortfolioElementModel = PortfolioElementModelImpl
            .getBuilder()
            .readXML("TestingHelper//UserSetModel//1000//flexible//GOOG", "GOOG").build();
    assertEquals(1, flexiblePortfolioElementModel.getAllTransactions().size());

  }
}
