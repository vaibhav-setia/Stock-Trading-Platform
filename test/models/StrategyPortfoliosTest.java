package models;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Test class to test the various functioning and methods of portfolio
 * on which strategies are applied.
 */
public class StrategyPortfoliosTest extends AbstractPortfolioTest {
  StrategyModel strategyModel1;
  Map<StockModel, Double> percentMap;

  PortfolioModel strategyPortfolio;

  Map<String, PortfolioElementModel> portfolioElementModelMap;

  @Before
  public void setUp() throws IOException {
    StockSetModel stockSetModel = StockSetModelImpl.getBuilder().build();
    stockSetModel.loadStocksFromDatabase();
    portfolioElementModelMap = new HashMap<>();
    percentMap = new HashMap<>();
    percentMap.put(stockSetModel.getStock("GOOG"), 50.0);
    percentMap.put(stockSetModel.getStock("MSFT"), 25.0);
    percentMap.put(stockSetModel.getStock("AAL"), 15.0);
    percentMap.put(stockSetModel.getStock("AAPL"), 10.0);
    strategyModel1 = DollarCostAveragingImpl.getBuilder()
            .strategyName("Strategy One")
            .brokerCommission(50.0)
            .amount(2000.0)
            .dateFrequency(50)
            .startDate(new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime())
            .endDate(new GregorianCalendar(2021, Calendar.FEBRUARY, 15).getTime())
            .stockMapPercent(percentMap)
            .build();
    strategyPortfolio = new StrategicalFlexiblePortfolioImpl.StrategicalFlexiblePortfolioBuilder()
            .portfolioName("Strategy Portfolio")
            .timestamp(new Timestamp(System.currentTimeMillis()))
            .portfolioHashMap(new HashMap<>())
            .build();
  }

  @Test
  public void testStrategyCreationOk() throws IOException {
    percentMap = new HashMap<>();
    StockSetModel stockSetModel = StockSetModelImpl.getBuilder().build();
    stockSetModel.loadStocksFromDatabase();
    percentMap.put(stockSetModel.getStock("GOOG"), 50.0);
    percentMap.put(stockSetModel.getStock("MSFT"), 25.0);
    percentMap.put(stockSetModel.getStock("AAL"), 15.0);
    percentMap.put(stockSetModel.getStock("AAPL"), 10.0);
    assertEquals("Strategy One", strategyModel1.getStrategyName());
    assertEquals(50.0, strategyModel1.getBrokerCommission(), 0.1);
    assertEquals(2000.0, strategyModel1.getAmount(), 0.1);
    assertEquals(50, strategyModel1.getDateFrequency());
    assertEquals(new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime(),
            strategyModel1.getStartDate());
    assertEquals(new GregorianCalendar(2021, Calendar.FEBRUARY, 15).getTime(),
            strategyModel1.getEndDate());
    assertEquals((Double) 50.0, percentMap.get(stockSetModel.getStock("GOOG")));
    assertEquals((Double) 25.0, percentMap.get(stockSetModel.getStock("MSFT")));
    assertEquals((Double) 15.0, percentMap.get(stockSetModel.getStock("AAL")));
    assertEquals((Double) 10.0, percentMap.get(stockSetModel.getStock("AAPL")));
  }

  @Override
  protected PortfolioModel portfolioModel(String portfolioName, Timestamp timestampAdded,
                                          Map<String,
                                                  PortfolioElementModel> portfolioElementHashMap) {
    return new StrategicalFlexiblePortfolioImpl(portfolioName, timestampAdded,
            portfolioElementHashMap, new ArrayList<>());
  }

  @Test
  public void testCreateNewStrategyPortfolioWithStrategy() {

    strategyPortfolio.addStrategy(strategyModel1);
    strategyPortfolio.runAllStrategies();
    assertEquals(8, strategyPortfolio.getPortfolioElements()
            .get("MSFT").getAllTransactions().size());
    assertEquals(8, strategyPortfolio.getPortfolioElements()
            .get("GOOG").getAllTransactions().size());
    assertEquals(8, strategyPortfolio.getPortfolioElements()
            .get("AAL").getAllTransactions().size());
    assertEquals(8, strategyPortfolio.getPortfolioElements()
            .get("AAPL").getAllTransactions().size());
    assertEquals("[MSFT, Microsoft Corporation, 18.09, 199.0, 2021-01-26][GOOG,"
                    + " Alphabet Inc - Class C, 5.0, 1519.91, 2021-01-26][AAPL,"
                    + " Apple Inc, 6.67, 179.86, 2021-01-26][AAL, American Airlines Group Inc,"
                    + " 147.67, 13.54, 2021-01-26]",
            strategyPortfolio.getPortfolioComposition(
                            new GregorianCalendar(2022, Calendar.FEBRUARY,
                                    11).getTime())
                    .stream().map(Object::toString)
                    .collect(Collectors.joining()));

  }

  @Test
  public void testCreateNewStrategyPortfolioWithMultipleStrategy() {

    strategyPortfolio.addStrategy(strategyModel1);
    strategyPortfolio.runAllStrategies();

    StrategyModel strategyModel2 = DollarCostAveragingImpl.getBuilder()
            .strategyName("Strategy two")
            .brokerCommission(50.0)
            .amount(2000.0)
            .dateFrequency(50)
            .startDate(new GregorianCalendar(2020, Calendar
                    .FEBRUARY, 11).getTime())
            .endDate(new GregorianCalendar(2021, Calendar.FEBRUARY, 15)
                    .getTime())
            .stockMapPercent(percentMap)
            .build();
    strategyPortfolio.addStrategy(strategyModel2);
    strategyPortfolio.runAllStrategies();
    assertEquals(16, strategyPortfolio.getPortfolioElements().get("MSFT")
            .getAllTransactions().size());
    assertEquals(16, strategyPortfolio.getPortfolioElements().get("GOOG")
            .getAllTransactions().size());
    assertEquals(16, strategyPortfolio.getPortfolioElements().get("AAL")
            .getAllTransactions().size());
    assertEquals(16, strategyPortfolio.getPortfolioElements().get("AAPL")
            .getAllTransactions().size());
    assertEquals("[MSFT, Microsoft Corporation, 36.18, 199.0, 2021-01-26][GOOG,"
                    + " Alphabet Inc - Class C, 10.0, 1519.91, 2021-01-26][AAPL, Apple Inc,"
                    + " 13.34, 179.86, 2021-01-26][AAL, American Airlines Group Inc, 295.33, 13.54,"
                    + " 2021-01-26]",
            strategyPortfolio.getPortfolioComposition(
                            new GregorianCalendar(2022, Calendar.FEBRUARY, 11)
                                    .getTime())
                    .stream().map(Object::toString)
                    .collect(Collectors.joining()));

  }

  @Test
  public void testcreateNewStrategyPortfolioWithSameStartEndDate() {
    StrategyModel strategyModel2 = DollarCostAveragingImpl.getBuilder()
            .strategyName("Strategy two")
            .brokerCommission(50.0)
            .amount(2000.0)
            .dateFrequency(50)
            .startDate(new GregorianCalendar(2022, Calendar.FEBRUARY, 15)
                    .getTime())
            .endDate(new GregorianCalendar(2022, Calendar.FEBRUARY, 15).getTime())
            .stockMapPercent(percentMap)
            .build();
    strategyPortfolio.addStrategy(strategyModel2);
    strategyPortfolio.runAllStrategies();
    assertEquals(1, strategyPortfolio.getPortfolioElements().get("MSFT")
            .getAllTransactions().size());
    assertEquals(1, strategyPortfolio.getPortfolioElements().get("GOOG")
            .getAllTransactions().size());
    assertEquals(1, strategyPortfolio.getPortfolioElements().get("AAL")
            .getAllTransactions().size());
    assertEquals(1, strategyPortfolio.getPortfolioElements().get("AAPL")
            .getAllTransactions().size());
    assertEquals("[MSFT, Microsoft Corporation, 1.5, 300.47, 2022-02-15]"
                    + "[GOOG, Alphabet Inc - Class C, 0.35, 2728.51, 2022-02-15]"
                    + "[AAPL, Apple Inc, 0.87, 172.79, 2022-02-15][AAL,"
                    + " American Airlines Group Inc, 13.27, 18.84, 2022-02-15]",
            strategyPortfolio.getPortfolioComposition(
                            new GregorianCalendar(2022, Calendar.FEBRUARY,
                                    16).getTime())
                    .stream().map(Object::toString)
                    .collect(Collectors.joining()));
  }

  @Test
  public void testCreateNewStrategyPortfolioWithNoEndDate() {
    StrategyModel strategyModel2 = DollarCostAveragingImpl.getBuilder()
            .strategyName("Strategy two")
            .brokerCommission(50.0)
            .amount(2000.0)
            .dateFrequency(50)
            .startDate(new GregorianCalendar(2022, Calendar.FEBRUARY,
                    15).getTime())
            .stockMapPercent(percentMap)
            .build();
    strategyPortfolio.addStrategy(strategyModel2);
    strategyPortfolio.runAllStrategies();
    assertEquals(6, strategyPortfolio.getPortfolioElements().get("MSFT")
            .getAllTransactions().size());
    assertEquals(6, strategyPortfolio.getPortfolioElements().get("GOOG")
            .getAllTransactions().size());
    assertEquals(6, strategyPortfolio.getPortfolioElements().get("AAL")
            .getAllTransactions().size());
    assertEquals(6, strategyPortfolio.getPortfolioElements().get("AAPL")
            .getAllTransactions().size());
    assertEquals("[MSFT, Microsoft Corporation, 1.5, 300.47, 2022-02-15]"
                    + "[GOOG, Alphabet Inc - Class C, 0.35, 2728.51, 2022-02-15]"
                    + "[AAPL, Apple Inc, 0.87, 172.79, 2022-02-15]"
                    + "[AAL, American Airlines Group Inc, 13.27, 18.84, 2022-02-15]",
            strategyPortfolio.getPortfolioComposition(
                            new GregorianCalendar(2022, Calendar.FEBRUARY,
                                    16).getTime())
                    .stream().map(Object::toString)
                    .collect(Collectors.joining()));
    assertEquals(0.0, strategyPortfolio
            .getPortfolioCostBasis(new GregorianCalendar(2022,
                    Calendar.FEBRUARY, 12).getTime()), 0.1);
    assertEquals(0.0, strategyPortfolio
            .getPortfolioCostBasis(new GregorianCalendar(2022,
                    Calendar.FEBRUARY, 12).getTime()), 0.1);

  }

  @Test
  public void testModifyNewStrategyPortfolioWithMultipleStrategy() throws IOException {
    strategyPortfolio.addStrategy(strategyModel1);
    strategyPortfolio.runAllStrategies();

    StrategyModel strategyModel2 = DollarCostAveragingImpl.getBuilder()
            .strategyName("Strategy two")
            .brokerCommission(50.0)
            .amount(2000.0)
            .dateFrequency(50)
            .startDate(new GregorianCalendar(2020, Calendar.FEBRUARY,
                    11).getTime())
            .endDate(new GregorianCalendar(2021, Calendar.FEBRUARY, 15).getTime())
            .stockMapPercent(percentMap)
            .build();
    strategyPortfolio.addStrategy(strategyModel2);
    strategyPortfolio.runAllStrategies();
    assertEquals(16, strategyPortfolio.getPortfolioElements().get("MSFT")
            .getAllTransactions().size());
    assertEquals(16, strategyPortfolio.getPortfolioElements().get("GOOG")
            .getAllTransactions().size());
    assertEquals(16, strategyPortfolio.getPortfolioElements().get("AAL")
            .getAllTransactions().size());
    assertEquals(16, strategyPortfolio.getPortfolioElements().get("AAPL")
            .getAllTransactions().size());
    assertEquals("[MSFT, Microsoft Corporation, 36.18, 199.0, 2021-01-26]"
                    + "[GOOG, Alphabet Inc - Class C, 10.0, 1519.91, 2021-01-26]"
                    + "[AAPL, Apple Inc, 13.34, 179.86, 2021-01-26]"
                    + "[AAL, American Airlines Group Inc, 295.33, 13.54, 2021-01-26]",
            strategyPortfolio.getPortfolioComposition(
                            new GregorianCalendar(2022, Calendar.FEBRUARY, 11)
                                    .getTime())
                    .stream().map(Object::toString)
                    .collect(Collectors.joining()));
    StockSetModel stockSetModel = StockSetModelImpl.getBuilder().build();
    stockSetModel.loadStocksFromDatabase();
    TransactionModel transactionModel = TransactionModelImpl.getBuilder()
            .qty(50.0)
            .date(new GregorianCalendar(2021, Calendar.FEBRUARY, 15).getTime())
            .exchange("NASDAQ")
            .price(stockSetModel.getStock("GOOG")
                    .getPriceOnADate(new GregorianCalendar(2021,
                            Calendar.FEBRUARY, 15).getTime()))
            .brokerCommission(50)
            .build();
    strategyPortfolio.modifyPortfolio(stockSetModel.getStock("GOOG"), transactionModel);
    assertEquals(17, strategyPortfolio.getPortfolioElements().get("GOOG")
            .getAllTransactions().size());
    assertEquals("[MSFT, Microsoft Corporation, 36.18, 199.0, 2021-01-26]"
                    + "[GOOG, Alphabet Inc - Class C, 60.0, 2006.74, 2021-02-15]"
                    + "[AAPL, Apple Inc, 13.34, 179.86, 2021-01-26]"
                    + "[AAL, American Airlines Group Inc, 295.33, 13.54, 2021-01-26]",
            strategyPortfolio.getPortfolioComposition(
                            new GregorianCalendar(2022, Calendar.FEBRUARY, 11).getTime())
                    .stream().map(Object::toString)
                    .collect(Collectors.joining()));

  }

  @Test
  public void testInvestOnHoliday() {
    StrategyModel strategyModel2 = DollarCostAveragingImpl.getBuilder()
            .strategyName("Strategy two")
            .brokerCommission(50.0)
            .amount(2000.0)
            .dateFrequency(50)
            .startDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 20)
                    .getTime())
            .endDate(new GregorianCalendar(2022, Calendar.NOVEMBER, 20).getTime())
            .stockMapPercent(percentMap)
            .build();
    strategyPortfolio.addStrategy(strategyModel2);
    strategyPortfolio.runAllStrategies();
    assertEquals(1, strategyPortfolio.getPortfolioElements().get("MSFT")
            .getAllTransactions().size());
    assertEquals(1, strategyPortfolio.getPortfolioElements().get("GOOG")
            .getAllTransactions().size());
    assertEquals(1, strategyPortfolio.getPortfolioElements().get("AAL")
            .getAllTransactions().size());
    assertEquals(1, strategyPortfolio.getPortfolioElements().get("AAPL")
            .getAllTransactions().size());
    assertEquals("", strategyPortfolio.getPortfolioComposition(
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 11).getTime())
            .stream().map(Object::toString)
            .collect(Collectors.joining()));
    assertEquals("[MSFT, Microsoft Corporation, 1.86, 242.05, 2022-11-21][GOOG,"
                    + " Alphabet Inc - Class C, 9.91, 95.83, 2022-11-21][AAPL,"
                    + " Apple Inc, 1.01, 148.01,"
                    + " 2022-11-21][AAL, American Airlines Group Inc, 18.05, 13.85, 2022-11-21]",
            strategyPortfolio.getPortfolioComposition(
                            new GregorianCalendar(2022, Calendar.NOVEMBER, 21).getTime())
                    .stream().map(Object::toString)
                    .collect(Collectors.joining()));
    System.out.println();

  }

  @Test
  public void testMultipleHolidayInvestments() {
    StrategyModel strategyModel2 = DollarCostAveragingImpl.getBuilder()
            .strategyName("Strategy two")
            .brokerCommission(50.0)
            .amount(2000.0)
            .dateFrequency(7)
            .startDate(new GregorianCalendar(2021, Calendar.NOVEMBER,
                    20).getTime())
            .endDate(new GregorianCalendar(2021, Calendar.DECEMBER, 10).getTime())
            .stockMapPercent(percentMap)
            .build();
    strategyPortfolio.addStrategy(strategyModel2);
    strategyPortfolio.runAllStrategies();
    assertEquals(3, strategyPortfolio.getPortfolioElements().get("MSFT")
            .getAllTransactions().size());
    assertEquals(3, strategyPortfolio.getPortfolioElements().get("GOOG")
            .getAllTransactions().size());
    assertEquals(3, strategyPortfolio.getPortfolioElements().get("AAL")
            .getAllTransactions().size());
    assertEquals(3, strategyPortfolio.getPortfolioElements().get("AAPL")
            .getAllTransactions().size());
    assertEquals("[MSFT, Microsoft Corporation, 4.04, 334.11, 2021-12-06]"
            + "[GOOG, Alphabet Inc - Class C, 0.98, 2913.0, 2021-12-06][AAPL,"
            + " Apple Inc, 2.77, 162.16, 2021-12-06][AAL, American Airlines Group Inc,"
            + " 40.77, 18.4, 2021-12-06]", strategyPortfolio.getPortfolioComposition(
                    new GregorianCalendar(2022, Calendar.FEBRUARY, 11).getTime())
            .stream().map(Object::toString)
            .collect(Collectors.joining()));
    assertEquals("[MSFT, Microsoft Corporation, 4.04, 334.11, 2021-12-06]"
                    + "[GOOG, Alphabet Inc - Class C, 0.98, 2913.0, 2021-12-06]"
                    + "[AAPL, Apple Inc, 2.77, 162.16, 2021-12-06][AAL,"
                    + " American Airlines Group Inc, 40.77, 18.4, 2021-12-06]",
            strategyPortfolio.getPortfolioComposition(
                            new GregorianCalendar(2022, Calendar.NOVEMBER, 21).getTime())
                    .stream().map(Object::toString)
                    .collect(Collectors.joining()));
    System.out.println();

  }

  @Test
  public void testPortfolioValueOnDate() {
    strategyPortfolio.addStrategy(strategyModel1);
    strategyPortfolio.runAllStrategies();
    assertEquals(0.00, strategyPortfolio
            .getPortfolioValueByDate(new GregorianCalendar(2019, Calendar.JANUARY,
                    01).getTime()), 0.1);
    assertEquals(22247.71, strategyPortfolio
            .getPortfolioValueByDate(new GregorianCalendar(2022, Calendar.MARCH,
                    01).getTime()), 0.1);
    assertEquals(18674.69, strategyPortfolio
            .getPortfolioValueByDate(new GregorianCalendar(2021, Calendar.MARCH,
                    01).getTime()), 0.1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEndDateBeforeFirstDate() {
    StrategyModel strategyModel2 = DollarCostAveragingImpl.getBuilder()
            .strategyName("Strategy two")
            .brokerCommission(50.0)
            .amount(2000.0)
            .dateFrequency(7)
            .startDate(new GregorianCalendar(2021, Calendar.NOVEMBER,
                    20).getTime())
            .endDate(new GregorianCalendar(2020, Calendar.DECEMBER, 10).getTime())
            .stockMapPercent(percentMap)
            .build();
    strategyPortfolio.addStrategy(strategyModel2);
    strategyPortfolio.runAllStrategies();


  }

  @Test
  public void stocksWithIPODateMoreThanStartDate() throws IOException {
    StockSetModel stockSetModel = StockSetModelImpl.getBuilder().build();
    stockSetModel.loadStocksFromDatabase();
    percentMap = new HashMap<>();
    percentMap.put(stockSetModel.getStock("AAC-U"), 100.0);
    StrategyModel strategyModel2 = DollarCostAveragingImpl.getBuilder()
            .strategyName("Strategy two")
            .brokerCommission(50.0)
            .amount(2000.0)
            .dateFrequency(7)
            .startDate(new GregorianCalendar(2021, Calendar.JANUARY, 01).getTime())
            .endDate(new GregorianCalendar(2021, Calendar.FEBRUARY, 28).getTime())
            .stockMapPercent(percentMap)
            .build();
    strategyPortfolio.addStrategy(strategyModel2);
    strategyPortfolio.runAllStrategies();
    assertEquals(5, strategyPortfolio.getPortfolioElements().get("AAC-U")
            .getAllTransactions().size());
  }

  @Test
  public void testCostBasisOnStrategyPortfolio() {
    StrategyModel strategyModel2 = DollarCostAveragingImpl.getBuilder()
            .strategyName("Strategy One")
            .brokerCommission(50.0)
            .amount(25000.0)
            .dateFrequency(50)
            .startDate(new GregorianCalendar(2020, Calendar.FEBRUARY, 11).getTime())
            .endDate(new GregorianCalendar(2021, Calendar.FEBRUARY, 15).getTime())
            .stockMapPercent(percentMap)
            .build();
    strategyPortfolio.addStrategy(strategyModel2);
    strategyPortfolio.runAllStrategies();
    assertEquals(0.00, strategyPortfolio
            .getPortfolioCostBasis(new GregorianCalendar(2019, Calendar.JANUARY,
                    01).getTime()), 0.1);
    assertEquals(175000.0, strategyPortfolio
            .getPortfolioCostBasis(new GregorianCalendar(2021, Calendar.JANUARY,
                    01).getTime()), 0.1);
    assertEquals(200000.0, strategyPortfolio
            .getPortfolioCostBasis(new GregorianCalendar(2022, Calendar.SEPTEMBER,
                    01).getTime()), 0.1);

  }

}
