package models;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import utils.ChartPlot;
import utils.DateRangeType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class to write Junit tests to verify correct working of FlexiblePortfolioModel.
 */
public class FlexiblePortfolioModelTest extends AbstractPortfolioTest {

  protected PortfolioModel portfolioModel(
          String portfolioName,
          Timestamp timestampAdded,
          Map<String, PortfolioElementModel> portfolioElementHashMap) {
    return new FlexiblePortfolioModelImpl(portfolioName, timestampAdded, portfolioElementHashMap);
  }

  @Before
  public void fileWriteUp() throws IOException {
    Files.createDirectories(Paths.get("TestingHelper//UserSetModel//1000//concrete"));
    Files.createDirectories(Paths.get("TestingHelper//UserSetModel//1000//concrete//AAL"));
    Files.createDirectories(Paths.get("TestingHelper//UserSetModel//1000//flexible"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//flexible//GOOG"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//flexible//GOOG//Transactions"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//concrete//AAL//Transactions"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//concrete//AAL//American Airlines Group Inc"));
    Files.createDirectories(Paths
            .get("TestingHelper//UserSetModel//1000//flexible//GOOG//Alphabet Inc - Class C"));

    String userSetModel = "<UserSetModel>\n"
            + "<UserModel id=\"1000\"/>\n"
            + "</UserSetModel>";
    java.io.FileWriter fw = new java.io.FileWriter("TestingHelper//UserSetModel.xml");
    fw.write(userSetModel);
    fw.close();
    String concrete = "<PortfolioModel>\n"
            + "<portfolioName>concrete</portfolioName>\n"
            + "<portfolioType>Inflexible</portfolioType>\n"
            + "<timestamp>2022-11-12 05:38:54.164</timestamp>\n"
            + "<PortfolioElementModel id=\"AAL\"/>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//concrete//concrete.xml");
    fw.write(concrete);
    fw.close();
    String flexible = "<PortfolioModel>\n"
            + "<portfolioName>flexible</portfolioName>\n"
            + "<portfolioType>Flexible</portfolioType>\n"
            + "<timestamp>2022-11-12 05:38:03.911</timestamp>\n"
            + "<PortfolioElementModel id=\"GOOG\"/>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//flexible//flexible.xml");
    fw.write(flexible);
    fw.close();
    String stockAAL = "<PortfolioElementModel>\n"
            + "<avgPrice>19.53</avgPrice>\n"
            + "<totalQuantity>44.0</totalQuantity>\n"
            + "<transactionDate>Tue Oct 19 00:00:00 EDT 2021</transactionDate>\n"
            + "<StockModel id=\"American Airlines Group Inc\"/>\n"
            + "<Transaction id=\"1\"/>\n"
            + "</PortfolioElementModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//concrete//AAL//AAL.xml");
    fw.write(stockAAL);
    fw.close();
    String stockGOOG = "<PortfolioElementModel>\n"
            + "<avgPrice>2758.0</avgPrice>\n"
            + "<totalQuantity>22.0</totalQuantity>\n"
            + "<transactionDate>Wed Oct 13 00:00:00 EDT 2021</transactionDate>\n"
            + "<StockModel id=\"Alphabet Inc - Class C\"/>\n"
            + "<Transaction id=\"1\"/>\n"
            + "</PortfolioElementModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//flexible//GOOG//GOOG.xml");
    fw.write(stockGOOG);
    fw.close();
    String stockAAL1 = "<StockModel>\n"
            + "<stockTicker>AAL</stockTicker>\n"
            + "<stockName>American Airlines Group Inc</stockName>\n"
            + "<exchangeName>NASDAQ</exchangeName>\n"
            + "<ipoDate>Tue Sep 27 00:00:00 EDT 2005</ipoDate>\n"
            + "</StockModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//"
            + "concrete//AAL//American Airlines Group Inc//American Airlines Group Inc.xml");
    fw.write(stockAAL1);
    fw.close();
    String stockGOOG1 = "<StockModel>\n"
            + "<stockTicker>GOOG</stockTicker>\n"
            + "<stockName>Alphabet Inc - Class C</stockName>\n"
            + "<exchangeName>NASDAQ</exchangeName>\n"
            + "<ipoDate>Thu Mar 27 00:00:00 EDT 2014</ipoDate>\n"
            + "</StockModel>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//1000//"
            + "flexible//GOOG//Alphabet Inc - Class C//Alphabet Inc - Class C.xml");
    fw.write(stockGOOG1);
    fw.close();
    String stockGOOG2 = "<Transaction>\n"
            + "<transactionDate>Wed Oct 13 00:00:00 EDT 2021</transactionDate>\n"
            + "<brokerCommission>12.0</brokerCommission>\n"
            + "<qty>22.0</qty>\n"
            + "<price>2758.0</price>\n"
            + "<exchange>NASDAQ</exchange>\n"
            + "<transactionSource>Manual</transactionSource>\n"
            + "</Transaction>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//"
            + "1000//flexible//GOOG//Transactions//1.xml");
    fw.write(stockGOOG2);
    fw.close();
    String stockAAL2 = "<Transaction>\n"
            + "<transactionDate>Tue Oct 19 00:00:00 EDT 2021</transactionDate>\n"
            + "<brokerCommission>0.0</brokerCommission>\n"
            + "<qty>44.0</qty>\n"
            + "<price>19.53</price>\n"
            + "<exchange>NASDAQ</exchange>\n"
            + "<transactionSource>Manual</transactionSource>\n"
            + "</Transaction>";
    fw = new java.io.FileWriter("TestingHelper//UserSetModel//"
            + "1000//concrete//AAL//Transactions//1.xml");
    fw.write(stockAAL2);
    fw.close();
    String shortStock = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>100</totalQuantity>\n"
            + "<Date>2022-09-22</Date>\n"
            + "<BrokerCommission>50</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>-200</totalQuantity>\n"
            + "<Date>2021-09-22</Date>\n"
            + "<BrokerCommission>50</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>MSFT</StockTicker>\n"
            + "<totalQuantity>10</totalQuantity>\n"
            + "<Date>2022-09-15</Date>\n"
            + "<BrokerCommission>50</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//shortFlexStock.xml");
    fw.write(shortStock);
    fw.close();
    String buyOnClose = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>100</totalQuantity>\n"
            + "<Date>2022-09-22</Date>\n"
            + "<BrokerCommission>50</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>200</totalQuantity>\n"
            + "<Date>2021-07-04</Date>\n"
            + "<BrokerCommission>50</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>MSFT</StockTicker>\n"
            + "<totalQuantity>10</totalQuantity>\n"
            + "<Date>2022-09-15</Date>\n"
            + "<BrokerCommission>50</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//buyOnClose.xml");
    fw.write(buyOnClose);
    fw.close();

    String sellOnClose = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>100</totalQuantity>\n"
            + "<Date>2022-09-22</Date>\n"
            + "<BrokerCommission>50</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>-20</totalQuantity>\n"
            + "<Date>2021-07-04</Date>\n"
            + "<BrokerCommission>50</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>MSFT</StockTicker>\n"
            + "<totalQuantity>10</totalQuantity>\n"
            + "<Date>2022-09-15</Date>\n"
            + "<BrokerCommission>50</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//sellOnClose.xml");
    fw.write(sellOnClose);
    fw.close();
    String flexUpload = "<PortfolioModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>100</totalQuantity>\n"
            + "<Date>2022-09-22</Date>\n"
            + "<BrokerCommission> 50.9</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>GOOG</StockTicker>\n"
            + "<totalQuantity>200</totalQuantity>\n"
            + "<Date>2021-09-22</Date>\n"
            + "<BrokerCommission> 100.9</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "<PortfolioElementModel>\n"
            + "<StockTicker>MSFT</StockTicker>\n"
            + "<totalQuantity>10</totalQuantity>\n"
            + "<Date>2022-09-15</Date>\n"
            + "<BrokerCommission>70.0</BrokerCommission>\n"
            + "</PortfolioElementModel>\n"
            + "</PortfolioModel>";
    fw = new java.io.FileWriter("TestingHelper//flexUpload.xml");
    fw.write(flexUpload);
    fw.close();
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForSingleStockWithYearlyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("Dec 2015", 0);
    expectedPlotMap.put("Dec 2016", 0);
    expectedPlotMap.put("Dec 2017", 0);
    expectedPlotMap.put("Dec 2018", 0);
    expectedPlotMap.put("Dec 2019", 0);
    expectedPlotMap.put("Dec 2020", 29);
    expectedPlotMap.put("Dec 2021", 49);
    expectedPlotMap.put("Oct 2022", 1);

    double expectedScale = 582.25;
    double expectedStart = 987.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.modifyPortfolio(googleStock, transaction);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter
                    .parse("2015-01-10"),
            formatter.parse("2022-10-10"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.YEARLY, chartPlot.getDateRangeType());
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForSingleStockWithMonthlyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("Jan 2022", 47);
    expectedPlotMap.put("Feb 2022", 47);
    expectedPlotMap.put("Mar 2022", 49);
    expectedPlotMap.put("Apr 2022", 40);
    expectedPlotMap.put("May 2022", 39);
    expectedPlotMap.put("Jun 2022", 38);
    expectedPlotMap.put("Jul 2022", 1);
    expectedPlotMap.put("Aug 2022", 1);
    expectedPlotMap.put("Sep 2022", 1);
    expectedPlotMap.put("Oct 2022", 1);

    double expectedScale = 561.8333333333334;
    double expectedStart = 961.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.modifyPortfolio(googleStock, transaction);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter
                    .parse("2022-01-10"),
            formatter.parse("2022-10-10"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.MONTHLY, chartPlot.getDateRangeType());
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForSingleStockWithWeeklyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("12 Aug 2022", 49);
    expectedPlotMap.put("19 Aug 2022", 40);
    expectedPlotMap.put("26 Aug 2022", 28);
    expectedPlotMap.put("02 Sep 2022", 23);
    expectedPlotMap.put("09 Sep 2022", 29);
    expectedPlotMap.put("16 Sep 2022", 14);
    expectedPlotMap.put("23 Sep 2022", 6);
    expectedPlotMap.put("30 Sep 2022", 1);
    expectedPlotMap.put("07 Oct 2022", 7);
    expectedPlotMap.put("10 Oct 2022", 5);

    double expectedScale = 5.520833333333333;
    double expectedStart = 961.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.modifyPortfolio(googleStock, transaction);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter
                    .parse("2022-08-10"),
            formatter.parse("2022-10-10"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.DAILY, chartPlot.getDateRangeType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void buyOnMarketClose() throws ParseException, XPathExpressionException,
          IOException, ParserConfigurationException, SAXException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, StockModel> stockModelMap = new HashMap<>();
    StockModel stockModel1 = StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .stockName("GOOGLE")
            .ipoDate(formatter.parse("2010-01-10"))
            .exchangeName("NASDAQ")
            .build();
    StockModel stockModel2 = StockModelImpl.getBuilder()
            .stockTicker("MSFT")
            .stockName("MICROSOFT")
            .ipoDate(formatter.parse("2011-01-10"))
            .exchangeName("NASDAQ")
            .build();
    stockModelMap.put("GOOG", stockModel1);
    stockModelMap.put("MSFT", stockModel2);
    StockSetModel stockSetModel = StockSetModelImpl.getBuilder()
            .stockList(stockModelMap)
            .build();
    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl
            .getBuilder()
            .buildPortfolioFromXML("TestingHelper//buyOnClose.xml",
                    "buyClose", stockSetModel)
            .build();
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForSingleStockWithDailyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("10 Oct 2022", 24);
    expectedPlotMap.put("11 Oct 2022", 22);
    expectedPlotMap.put("12 Oct 2022", 23);
    expectedPlotMap.put("13 Oct 2022", 28);
    expectedPlotMap.put("14 Oct 2022", 18);
    expectedPlotMap.put("17 Oct 2022", 32);
    expectedPlotMap.put("18 Oct 2022", 35);
    expectedPlotMap.put("19 Oct 2022", 31);
    expectedPlotMap.put("20 Oct 2022", 31);
    expectedPlotMap.put("21 Oct 2022", 35);
    expectedPlotMap.put("24 Oct 2022", 41);
    expectedPlotMap.put("25 Oct 2022", 49);
    expectedPlotMap.put("26 Oct 2022", 9);
    expectedPlotMap.put("27 Oct 2022", 1);
    expectedPlotMap.put("28 Oct 2022", 16);

    double expectedScale = 2.5625;
    double expectedStart = 926.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.modifyPortfolio(googleStock, transaction);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter.parse("2022-10-10"),
            formatter.parse("2022-10-30"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.DAILY, chartPlot.getDateRangeType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void sellOnMarketClose() throws ParseException, XPathExpressionException,
          IOException, ParserConfigurationException, SAXException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, StockModel> stockModelMap = new HashMap<>();
    StockModel stockModel1 = StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .stockName("GOOGLE")
            .ipoDate(formatter.parse("2010-01-10"))
            .exchangeName("NASDAQ")
            .build();
    StockModel stockModel2 = StockModelImpl.getBuilder()
            .stockTicker("MSFT")
            .stockName("MICROSOFT")
            .ipoDate(formatter.parse("2011-01-10"))
            .exchangeName("NASDAQ")
            .build();
    stockModelMap.put("GOOG", stockModel1);
    stockModelMap.put("MSFT", stockModel2);
    StockSetModel stockSetModel = StockSetModelImpl.getBuilder()
            .stockList(stockModelMap)
            .build();
    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl
            .getBuilder()
            .buildPortfolioFromXML("TestingHelper//sellOnClose.xml",
                    "sellClose", stockSetModel)
            .build();
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForMultipleStockWithYearlyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("Dec 2015", 0);
    expectedPlotMap.put("Dec 2016", 0);
    expectedPlotMap.put("Dec 2017", 0);
    expectedPlotMap.put("Dec 2018", 0);
    expectedPlotMap.put("Dec 2019", 0);
    expectedPlotMap.put("Dec 2020", 23);
    expectedPlotMap.put("Dec 2021", 49);
    expectedPlotMap.put("Oct 2022", 1);

    double expectedScale = 597.75;
    double expectedStart = 3795.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl.getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter.parse("2015-01-10"),
            formatter.parse("2022-10-10"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.YEARLY, chartPlot.getDateRangeType());
  }

  @Test
  public void testFlexUpload() throws ParseException, XPathExpressionException,
          IOException, ParserConfigurationException, SAXException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, StockModel> stockModelMap = new HashMap<>();
    StockModel stockModel1 = StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .stockName("GOOGLE")
            .ipoDate(formatter.parse("2010-01-10"))
            .exchangeName("NASDAQ")
            .build();
    StockModel stockModel2 = StockModelImpl.getBuilder()
            .stockTicker("MSFT")
            .stockName("MICROSOFT")
            .ipoDate(formatter.parse("2011-01-10"))
            .exchangeName("NASDAQ")
            .build();
    stockModelMap.put("GOOG", stockModel1);
    stockModelMap.put("MSFT", stockModel2);
    StockSetModel stockSetModel = StockSetModelImpl.getBuilder()
            .stockList(stockModelMap)
            .build();
    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl
            .getBuilder()
            .buildPortfolioFromXML("TestingHelper//flexUpload.xml",
                    "flexUpload", stockSetModel)
            .build();
    assertEquals(2, portfolioModel.getPortfolioElements().size());
    assertEquals("flexUpload", portfolioModel.getName());
    assertEquals("flexUpload", portfolioModel.getFlexiblePortfolio().getName());
    assertEquals("[MSFT, MICROSOFT, 10.0, 245.38, 2022-09-15]"
                   + "[GOOG, GOOGLE, 300.0, 1912.7, 2022-09-22]",
            portfolioModel.getPortfolioComposition(formatter.parse("2022-09-22")).stream()
                    .map(Object::toString).reduce("", String::concat));
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForMultipleStockWithMonthlyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("Jan 2022", 47);
    expectedPlotMap.put("Feb 2022", 47);
    expectedPlotMap.put("Mar 2022", 49);
    expectedPlotMap.put("Apr 2022", 39);
    expectedPlotMap.put("May 2022", 39);
    expectedPlotMap.put("Jun 2022", 37);
    expectedPlotMap.put("Jul 2022", 2);
    expectedPlotMap.put("Aug 2022", 1);
    expectedPlotMap.put("Sep 2022", 1);
    expectedPlotMap.put("Oct 2022", 1);

    double expectedScale = 577.0208333333334;
    double expectedStart = 3725.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl.getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter.parse("2022-01-10"),
            formatter.parse("2022-10-10"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.MONTHLY, chartPlot.getDateRangeType());
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForMultipleStockWithWeeklyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("12 Aug 2022", 49);
    expectedPlotMap.put("19 Aug 2022", 46);
    expectedPlotMap.put("26 Aug 2022", 34);
    expectedPlotMap.put("02 Sep 2022", 25);
    expectedPlotMap.put("09 Sep 2022", 28);
    expectedPlotMap.put("16 Sep 2022", 17);
    expectedPlotMap.put("23 Sep 2022", 15);
    expectedPlotMap.put("30 Sep 2022", 1);
    expectedPlotMap.put("07 Oct 2022", 4);
    expectedPlotMap.put("10 Oct 2022", 4);

    double expectedScale = 19.645833333333332;
    double expectedStart = 3725.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl.getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter.parse("2022-08-10"),
            formatter.parse("2022-10-10"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.DAILY, chartPlot.getDateRangeType());
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForMultipleStockWithDailyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("10 Oct 2022", 8);
    expectedPlotMap.put("11 Oct 2022", 3);
    expectedPlotMap.put("12 Oct 2022", 2);
    expectedPlotMap.put("13 Oct 2022", 16);
    expectedPlotMap.put("14 Oct 2022", 1);
    expectedPlotMap.put("17 Oct 2022", 16);
    expectedPlotMap.put("18 Oct 2022", 21);
    expectedPlotMap.put("19 Oct 2022", 19);
    expectedPlotMap.put("20 Oct 2022", 19);
    expectedPlotMap.put("21 Oct 2022", 30);
    expectedPlotMap.put("24 Oct 2022", 38);
    expectedPlotMap.put("25 Oct 2022", 49);
    expectedPlotMap.put("26 Oct 2022", 27);
    expectedPlotMap.put("27 Oct 2022", 12);
    expectedPlotMap.put("28 Oct 2022", 46);

    double expectedScale = 7.4375;
    double expectedStart = 3739.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl
            .getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter
                    .parse("2022-10-10"),
            formatter.parse("2022-10-30"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.DAILY, chartPlot.getDateRangeType());
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForMultipleStockWithSellWithYearlyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("Dec 2015", 0);
    expectedPlotMap.put("Dec 2016", 0);
    expectedPlotMap.put("Dec 2017", 0);
    expectedPlotMap.put("Dec 2018", 0);
    expectedPlotMap.put("Dec 2019", 0);
    expectedPlotMap.put("Dec 2020", 47);
    expectedPlotMap.put("Dec 2021", 49);
    expectedPlotMap.put("Oct 2022", 1);

    double expectedScale = 306.625;
    double expectedStart = 3301.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl
            .getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionThird = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-11-10"))
            .price(95)
            .qty(-5)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);
    portfolioModel.modifyPortfolio(googleStock, transactionThird);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter
                    .parse("2015-01-10"),
            formatter.parse("2022-10-10"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.YEARLY, chartPlot.getDateRangeType());
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForMultipleStockWithSellWithMonthlyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("Jan 2022", 47);
    expectedPlotMap.put("Feb 2022", 46);
    expectedPlotMap.put("Mar 2022", 49);
    expectedPlotMap.put("Apr 2022", 39);
    expectedPlotMap.put("May 2022", 38);
    expectedPlotMap.put("Jun 2022", 36);
    expectedPlotMap.put("Jul 2022", 2);
    expectedPlotMap.put("Aug 2022", 2);
    expectedPlotMap.put("Sep 2022", 1);
    expectedPlotMap.put("Oct 2022", 1);

    double expectedScale = 296.1041666666667;
    double expectedStart = 3244.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl
            .getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionThird = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-11-10"))
            .price(95)
            .qty(-5)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);
    portfolioModel.modifyPortfolio(googleStock, transactionThird);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter
                    .parse("2022-01-10"),
            formatter.parse("2022-10-10"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.MONTHLY, chartPlot.getDateRangeType());
  }

  @Test
  public void testGetPortfolioPerformanceOverTimeForMultipleStockWithSellWithWeeklyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("12 Aug 2022", 49);
    expectedPlotMap.put("19 Aug 2022", 46);
    expectedPlotMap.put("26 Aug 2022", 35);
    expectedPlotMap.put("02 Sep 2022", 25);
    expectedPlotMap.put("09 Sep 2022", 28);
    expectedPlotMap.put("16 Sep 2022", 18);
    expectedPlotMap.put("23 Sep 2022", 16);
    expectedPlotMap.put("30 Sep 2022", 1);
    expectedPlotMap.put("07 Oct 2022", 4);
    expectedPlotMap.put("10 Oct 2022", 4);

    double expectedScale = 16.895833333333332;
    double expectedStart = 3244.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl
            .getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionThird = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-11-10"))
            .price(95)
            .qty(-5)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);
    portfolioModel.modifyPortfolio(googleStock, transactionThird);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter
                    .parse("2022-08-10"),
            formatter.parse("2022-10-10"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.DAILY, chartPlot.getDateRangeType());
  }

  @Test
  public void testPortfolioModelInitializationAndGettersWorkCorrectly() throws
          ParseException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String testDate = "2022-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);

    Map<String, PortfolioElementModel> portfolioElementModelMap = new HashMap<>();

    portfolioElementModelMap.put("Portfolio 1", PortfolioElementModelImpl.getBuilder()
            .avgPrice(101)
            .transactionDate(date)
            .stockModel(new StockModelImpl("GOOG", "Google",
                    "NASDAQ", date)).build());

    PortfolioModel portfolioModel = new FlexiblePortfolioModelImpl("Portfolio 1",
            timestamp,
            portfolioElementModelMap);

    assertEquals(portfolioModel.getName(), "Portfolio 1");
    assertEquals(portfolioModel.getPortfolioElements().size(), 1);
  }

  @Test
  public void testPortfolioValueByDateWorksCorrectly() throws ParseException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String testDate = "2021-11-03";
    Date date = formatter.parse(testDate);

    String priceDate = "2022-10-27";
    Date pricingDate = formatter.parse(priceDate);

    Map<String, PortfolioElementModel> portfolioElementModelMap = new HashMap<>();
    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .qty(10)
            .price(101)
            .brokerCommission(0)
            .exchange("NASDAQ")
            .date(date)
            .build();
    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(new StockModelImpl("GOOG", "Google",
                    "NASDAQ", date))
            .build();
    portfolioElementModel.addTransaction(transaction);
    portfolioElementModelMap.put("Portfolio 1", portfolioElementModel);

    PortfolioModel portfolioModel = new FlexiblePortfolioModelImpl("Portfolio 1",
            timestamp,
            portfolioElementModelMap);

    assertEquals(926.0, portfolioModel.getPortfolioValueByDate(pricingDate),
            0.001);
  }

  @Test
  public void testPortfolioModelCreationByBuilder() throws ParseException {
    String testDate = "2022-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);

    Map<String, PortfolioElementModel> portfolioElementModelMap = new HashMap<>();
    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(new StockModelImpl("GOOG", "Google",
                    "NASDAQ", date)).build();
    portfolioElementModel.addTransaction(TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(10)
            .price(101)
            .build());
    portfolioElementModelMap.put("Portfolio 1", portfolioElementModel);

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioHashMap(portfolioElementModelMap)
            .portfolioName("Portfolio 1")
            .build();

    assertEquals(portfolioModel.getName(), "Portfolio 1");
    assertEquals(portfolioModel.getPortfolioElements().size(), 1);
  }

  @Test
  public void testReadXML() throws XPathExpressionException, IOException,
          ParserConfigurationException, ParseException, SAXException {
    PortfolioModel portfolioModel = PortfolioModelImpl.getBuilder()
            .readXML("TestingHelper//UserSetModel//1000//flexible",
                    "flexible").build();
    assertEquals("flexible", portfolioModel.getName());
    assertEquals(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")
            .parse("2022-11-12 05:38:03.911"), portfolioModel.getPortfolioAddDateTime());
    assertEquals(1, portfolioModel.getPortfolioElements().size());
  }


  @Test
  public void testGenerateXML() throws IOException, ParserConfigurationException,
          TransformerException, XPathExpressionException, ParseException, SAXException {
    PortfolioModel portfolioModel = PortfolioModelImpl.getBuilder()
            .readXML("TestingHelper//UserSetModel//1000//flexible",
                    "flexible")
            .build();
    portfolioModel.generateXML("TestingHelper//Write//PortfolioModel");
    File file = new File("TestingHelper//Write//PortfolioModel");
    assertTrue(file.exists());
  }

  @Test
  public void testPortfolioModelCreationWhenMultipleSameStocksAdded()
          throws ParseException {
    String testDate = "2022-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(new StockModelImpl("GOOG", "Google",
                    "NASDAQ", date)).build();
    portfolioElementModel.addTransaction(TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(10)
            .price(101)
            .build());

    Map<String, PortfolioElementModel> mapEle = new HashMap<>();
    mapEle.put("GOOG", portfolioElementModel);

    PortfolioModel flexiblePortfolioModel = FlexiblePortfolioModelImpl
            .getBuilder()
            .portfolioName("Portfolio 1")
            .portfolioHashMap(mapEle)
            .build();

    Map<String, StockModel> stocks = new HashMap<>();
    stocks.put("GOOG", StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .exchangeName("NASDAQ")
            .stockName("Google")
            .ipoDate(date)
            .build());


    assertEquals(flexiblePortfolioModel.getName(), "Portfolio 1");
    assertEquals(flexiblePortfolioModel.getPortfolioElements().size(), 1);
    assertEquals(10, flexiblePortfolioModel.getPortfolioElements().get("GOOG")
            .getStockTotalQuantity(), 0.1);
  }

  /**
   * Test class to test cost basis of a portfolio
   * for past, current and future dates. It checks
   * costs basis after adding a buy and sell transaction
   * as well.
   *
   * @throws ParseException if date is not correctly parsed.
   */
  @Test
  public void testCostBasis() throws ParseException {
    String testDate = "2021-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);
    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(new StockModelImpl("GOOG", "Google",
                    "NASDAQ", date)).build();
    portfolioElementModel.addTransaction(TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(10)
            .price(101)
            .brokerCommission(50)
            .build());
    Map<String, PortfolioElementModel> mapEle = new HashMap<>();
    mapEle.put("GOOG", portfolioElementModel);
    PortfolioModel flexiblePortfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .portfolioHashMap(mapEle)
            .build();
    Map<String, StockModel> stocks = new HashMap<>();
    stocks.put("GOOG", StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .exchangeName("NASDAQ")
            .stockName("Google")
            .ipoDate(date)
            .build());
    assertEquals(0, flexiblePortfolioModel.getPortfolioCostBasis(
            new GregorianCalendar(2020, Calendar.NOVEMBER, 1)
                    .getTime()), 0.1);
    assertEquals(1060.0, flexiblePortfolioModel.getPortfolioCostBasis(
            new GregorianCalendar(2021, Calendar.NOVEMBER, 1)
                    .getTime()), 0.1);
    assertEquals(1060.0, flexiblePortfolioModel.getPortfolioCostBasis(
            new GregorianCalendar(2022, Calendar.NOVEMBER, 3)
                    .getTime()), 0.1);
    flexiblePortfolioModel.modifyPortfolio(stocks.get("GOOG"),
            TransactionModelImpl.getBuilder()
                    .date(date)
                    .exchange("NASDAQ")
                    .qty(10)
                    .price(101)
                    .brokerCommission(50)
                    .build());
    assertEquals(0, flexiblePortfolioModel.getPortfolioCostBasis(
            new GregorianCalendar(2020, Calendar.NOVEMBER, 1)
                    .getTime()), 0.1);
    assertEquals(2120.0, flexiblePortfolioModel.getPortfolioCostBasis(
            new GregorianCalendar(2021, Calendar.NOVEMBER, 1)
                    .getTime()), 0.1);
    assertEquals(2120.0, flexiblePortfolioModel.getPortfolioCostBasis(
            new GregorianCalendar(2022, Calendar.NOVEMBER, 3)
                    .getTime()), 0.1);
    flexiblePortfolioModel.modifyPortfolio(stocks.get("GOOG"),
            TransactionModelImpl.getBuilder()
                    .date(date)
                    .exchange("NASDAQ")
                    .qty(-5)
                    .price(101)
                    .brokerCommission(50)
                    .build());
    assertEquals(0, flexiblePortfolioModel.getPortfolioCostBasis(
            new GregorianCalendar(2020, Calendar.NOVEMBER, 1)
                    .getTime()), 0.1);
    assertEquals(2170.0, flexiblePortfolioModel.getPortfolioCostBasis(
            new GregorianCalendar(2021, Calendar.NOVEMBER, 1)
                    .getTime()), 0.1);
    assertEquals(2170.0, flexiblePortfolioModel.getPortfolioCostBasis(
            new GregorianCalendar(2022, Calendar.NOVEMBER, 3)
                    .getTime()), 0.1);
  }

  /**
   * Test class to test portfolio value of date
   * for past, current and future dates. It checks
   * cost value after adding a buy and sell transaction
   * as well.
   *
   * @throws ParseException if date is not correctly parsed.
   */
  @Test
  public void testGetPortfolioValueByDate() throws ParseException {
    String testDate = "2021-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);
    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(new StockModelImpl("GOOG", "Google",
                    "NASDAQ", date)).build();
    portfolioElementModel.addTransaction(TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(10)
            .price(101)
            .brokerCommission(50)
            .build());
    Map<String, PortfolioElementModel> mapEle = new HashMap<>();
    mapEle.put("GOOG", portfolioElementModel);
    PortfolioModel flexiblePortfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .portfolioHashMap(mapEle)
            .build();
    Map<String, StockModel> stocks = new HashMap<>();
    stocks.put("GOOG", StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .exchangeName("NASDAQ")
            .stockName("Google")
            .ipoDate(date)
            .build());
    assertEquals(0, flexiblePortfolioModel.getPortfolioValueByDate(
                    new GregorianCalendar(2020, Calendar.NOVEMBER, 1).getTime()),
            0.1);
    assertEquals(28754.8, flexiblePortfolioModel.getPortfolioValueByDate(
                    new GregorianCalendar(2021, Calendar.NOVEMBER, 1).getTime()),
            0.1);
    assertEquals(834.9, flexiblePortfolioModel.getPortfolioValueByDate(
                    new GregorianCalendar(2022, Calendar.NOVEMBER, 3).getTime()),
            0.1);
    date = formatter.parse("2021-12-01");
    flexiblePortfolioModel.modifyPortfolio(stocks.get("GOOG"), TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(10)
            .price(101)
            .brokerCommission(50)
            .build());
    assertEquals(29877.6, flexiblePortfolioModel.getPortfolioValueByDate(
                    new GregorianCalendar(2021, Calendar.NOVEMBER, 15).getTime()),
            0.1);
    assertEquals(56647.2, flexiblePortfolioModel.getPortfolioValueByDate(
                    new GregorianCalendar(2021, Calendar.DECEMBER, 1).getTime()),
            0.1);
    assertEquals(1669.8, flexiblePortfolioModel.getPortfolioValueByDate(
                    new GregorianCalendar(2022, Calendar.NOVEMBER, 3).getTime()),
            0.1);
    date = formatter.parse("2022-06-07");
    flexiblePortfolioModel.modifyPortfolio(stocks.get("GOOG"), TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(-5)
            .price(101)
            .brokerCommission(50)
            .build());
    assertEquals(59470.0, flexiblePortfolioModel.getPortfolioValueByDate(
                    new GregorianCalendar(2021, Calendar.DECEMBER, 10).getTime()),
            0.1);
    assertEquals(35168.8, flexiblePortfolioModel.getPortfolioValueByDate(
                    new GregorianCalendar(2022, Calendar.JUNE, 7).getTime()),
            0.1);
    assertEquals(1252.35, flexiblePortfolioModel.getPortfolioValueByDate(
                    new GregorianCalendar(2022, Calendar.NOVEMBER, 3).getTime()),
            0.1);
  }

  @Test
  public void sellTransactionsValidity() throws ParseException {
    String testDate = "2021-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);
    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(new StockModelImpl("GOOG", "Google",
                    "NASDAQ", date)).build();
    portfolioElementModel.addTransaction(TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(10)
            .price(101)
            .brokerCommission(50)
            .build());
    Map<String, PortfolioElementModel> mapEle = new HashMap<>();
    mapEle.put("GOOG", portfolioElementModel);
    PortfolioModel flexiblePortfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .portfolioHashMap(mapEle)
            .build();
    Map<String, StockModel> stocks = new HashMap<>();
    stocks.put("GOOG", StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .exchangeName("NASDAQ")
            .stockName("Google")
            .ipoDate(date)
            .build());
    assertEquals(10, flexiblePortfolioModel.getPortfolioElements().get("GOOG")
            .getStockTotalQuantity(), 0.1);
    flexiblePortfolioModel.modifyPortfolio(stocks.get("GOOG"), TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(-5)
            .price(101)
            .brokerCommission(50)
            .build());
    assertEquals(5, flexiblePortfolioModel.getPortfolioElements().get("GOOG")
            .getStockTotalQuantity(), 0.1);

    flexiblePortfolioModel.modifyPortfolio(stocks.get("GOOG"), TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(-5)
            .price(101)
            .brokerCommission(50)
            .build());
    assertEquals(0, flexiblePortfolioModel.getPortfolioElements().get("GOOG")
            .getStockTotalQuantity(), 0.1);
  }

  @Test
  public void addTransactionsValidity() throws ParseException {
    String testDate = "2021-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);
    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(new StockModelImpl("GOOG", "Google",
                    "NASDAQ", date)).build();
    portfolioElementModel.addTransaction(TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(10)
            .price(101)
            .brokerCommission(50)
            .build());
    Map<String, PortfolioElementModel> mapEle = new HashMap<>();
    mapEle.put("GOOG", portfolioElementModel);
    PortfolioModel flexiblePortfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .portfolioHashMap(mapEle)
            .build();
    Map<String, StockModel> stocks = new HashMap<>();
    stocks.put("GOOG", StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .exchangeName("NASDAQ")
            .stockName("Google")
            .ipoDate(date)
            .build());
    assertEquals(10, flexiblePortfolioModel.getPortfolioElements().get("GOOG")
            .getStockTotalQuantity(), 0.1);
    flexiblePortfolioModel.modifyPortfolio(stocks.get("GOOG"), TransactionModelImpl.getBuilder()
            .date(date)
            .exchange("NASDAQ")
            .qty(5)
            .price(101)
            .brokerCommission(50)
            .build());
    assertEquals(15, flexiblePortfolioModel.getPortfolioElements().get("GOOG")
            .getStockTotalQuantity(), 0.1);
  }


  @Test
  public void testGetPortfolioPerformanceOverTimeForMultipleStockWithSellWithDailyRange()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("10 Oct 2022", 7);
    expectedPlotMap.put("11 Oct 2022", 3);
    expectedPlotMap.put("12 Oct 2022", 1);
    expectedPlotMap.put("13 Oct 2022", 15);
    expectedPlotMap.put("14 Oct 2022", 1);
    expectedPlotMap.put("17 Oct 2022", 14);
    expectedPlotMap.put("18 Oct 2022", 18);
    expectedPlotMap.put("19 Oct 2022", 18);
    expectedPlotMap.put("20 Oct 2022", 17);
    expectedPlotMap.put("21 Oct 2022", 28);
    expectedPlotMap.put("24 Oct 2022", 36);
    expectedPlotMap.put("25 Oct 2022", 45);
    expectedPlotMap.put("26 Oct 2022", 30);
    expectedPlotMap.put("27 Oct 2022", 15);
    expectedPlotMap.put("28 Oct 2022", 49);

    double expectedScale = 7.166666666666667;
    double expectedStart = 3253.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl.getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionThird = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-11-10"))
            .price(95)
            .qty(-5)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);
    portfolioModel.modifyPortfolio(googleStock, transactionThird);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter
                    .parse("2022-10-10"),
            formatter.parse("2022-10-30"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.DAILY, chartPlot.getDateRangeType());
  }

  @Test
  public void testGetFlexiblePortfolioPerformanceOverTimeForMultipleStockWithDailyRangeSingleLines()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("10 Oct 2022", 1);

    double expectedScale = 1.0;
    double expectedStart = 3795.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl.getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(formatter
                    .parse("2022-10-10"),
            formatter.parse("2022-10-10"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.DAILY, chartPlot.getDateRangeType());
  }

  @Test
  public void testGetFlexiblePortfolioPerformanceOverTimeForMultipleStockWithDailyRangeFiveLines()
          throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("30 Sep 2022", 1);
    expectedPlotMap.put("03 Oct 2022", 25);
    expectedPlotMap.put("04 Oct 2022", 48);
    expectedPlotMap.put("05 Oct 2022", 49);
    expectedPlotMap.put("06 Oct 2022", 44);

    double expectedScale = 4.6875;
    double expectedStart = 3725.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl.getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(
            formatter.parse("2022-10-01"),
            formatter.parse("2022-10-06"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.DAILY, chartPlot.getDateRangeType());
  }

  @Test
  public void
      testGetFlexiblePortfolioPerformanceOverTimerMultipleStockWithYearlyRange30Lines()
              throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Map<String, Integer> expectedPlotMap = new LinkedHashMap<>();
    expectedPlotMap.put("Dec 1993", 0);
    expectedPlotMap.put("Dec 1994", 0);
    expectedPlotMap.put("Dec 1995", 0);
    expectedPlotMap.put("Dec 1996", 0);
    expectedPlotMap.put("Dec 1997", 0);
    expectedPlotMap.put("Dec 1998", 0);
    expectedPlotMap.put("Dec 1999", 0);
    expectedPlotMap.put("Dec 2000", 0);
    expectedPlotMap.put("Dec 2001", 0);
    expectedPlotMap.put("Dec 2002", 0);
    expectedPlotMap.put("Dec 2003", 0);
    expectedPlotMap.put("Dec 2004", 0);
    expectedPlotMap.put("Dec 2005", 0);
    expectedPlotMap.put("Dec 2006", 0);
    expectedPlotMap.put("Dec 2007", 0);
    expectedPlotMap.put("Dec 2008", 0);
    expectedPlotMap.put("Dec 2009", 0);
    expectedPlotMap.put("Dec 2010", 0);
    expectedPlotMap.put("Dec 2011", 0);
    expectedPlotMap.put("Dec 2012", 0);
    expectedPlotMap.put("Dec 2013", 0);
    expectedPlotMap.put("Dec 2014", 0);
    expectedPlotMap.put("Dec 2015", 0);
    expectedPlotMap.put("Dec 2016", 0);
    expectedPlotMap.put("Dec 2017", 0);
    expectedPlotMap.put("Dec 2018", 0);
    expectedPlotMap.put("Dec 2019", 0);
    expectedPlotMap.put("Dec 2020", 23);
    expectedPlotMap.put("Dec 2021", 49);
    expectedPlotMap.put("Oct 2022", 1);

    double expectedScale = 591.8125;
    double expectedStart = 4080.0;

    StockModel googleStock = new StockModelImpl("GOOG", "Google",
            "NASDAQ", formatter.parse("2001-01-01"));

    StockModel appleStock = new StockModelImpl("AAPL", "Apple",
            "NASDAQ", formatter.parse("2001-01-01"));

    PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
            .stockModel(googleStock).build();

    PortfolioElementModel portfolioElementModelSecond = PortfolioElementModelImpl.getBuilder()
            .stockModel(appleStock).build();

    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .build();

    TransactionModel transaction = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2020-01-10"))
            .price(101)
            .qty(10)
            .brokerCommission(10)
            .exchange(googleStock.getExchangeName())
            .build();

    TransactionModel transactionSecond = TransactionModelImpl.getBuilder()
            .date(formatter.parse("2021-01-10"))
            .price(95)
            .qty(20)
            .brokerCommission(11)
            .exchange(googleStock.getExchangeName())
            .build();

    portfolioModel.addPortfolioElement(portfolioElementModel);
    portfolioModel.addPortfolioElement(portfolioElementModelSecond);
    portfolioModel.modifyPortfolio(googleStock, transaction);
    portfolioModel.modifyPortfolio(appleStock, transactionSecond);

    ChartPlot chartPlot = portfolioModel.getPerformanceForPortfolio(
            formatter.parse("1993-10-01"),
            formatter.parse("2022-10-29"));
    assertEquals(expectedPlotMap, chartPlot.getPlots());
    assertEquals(expectedScale, chartPlot.getScale(), 0.01);
    assertEquals(expectedStart, chartPlot.getStart(), 0.01);
    assertEquals(DateRangeType.YEARLY, chartPlot.getDateRangeType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuySellInvalidity() throws ParseException, XPathExpressionException,
          IOException, ParserConfigurationException, SAXException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Map<String, StockModel> stockModelMap = new HashMap<>();
    StockModel stockModel1 = StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .stockName("GOOGLE")
            .ipoDate(formatter.parse("2010-01-10"))
            .exchangeName("NASDAQ")
            .build();
    StockModel stockModel2 = StockModelImpl.getBuilder()
            .stockTicker("MSFT")
            .stockName("MICROSOFT")
            .ipoDate(formatter.parse("2011-01-10"))
            .exchangeName("NASDAQ")
            .build();
    stockModelMap.put("GOOG", stockModel1);
    stockModelMap.put("MSFT", stockModel2);
    StockSetModel stockSetModel = StockSetModelImpl.getBuilder()
            .stockList(stockModelMap)
            .build();
    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl
            .getBuilder()
            .buildPortfolioFromXML("TestingHelper//shortFlexStock.xml",
                    "SHORT", stockSetModel)
            .build();
  }
}
