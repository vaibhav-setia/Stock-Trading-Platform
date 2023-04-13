package models;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Abstract class to test the functioning of flexible and inflexible portfolio.
 */
public abstract class AbstractPortfolioTest {

  protected abstract PortfolioModel
      portfolioModel(String portfolioName,
                     Timestamp timestampAdded,
                     Map<String, PortfolioElementModel> portfolioElementHashMap);

  @Test
  public void testZeroStockPortfolioCreation() {
    PortfolioModel portfolioModel = portfolioModel("TEST",
            new Timestamp(System.currentTimeMillis()), new HashMap<>());
    assertEquals("TEST", portfolioModel.getName());
    assertEquals(0, portfolioModel.getPortfolioElements().size());
  }

  @Test
  public void testCompositionPortfolio() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String ipoDate = "2019-10-22";
    Date ipo = formatter.parse(ipoDate);
    TransactionModel t1 = TransactionModelImpl.getBuilder()
            .date(new GregorianCalendar(2020, Calendar.NOVEMBER, 3).getTime())
            .qty(100)
            .price(100)
            .exchange("NASDAQ")
            .brokerCommission(0).build();
    TransactionModel t2 = TransactionModelImpl.getBuilder()
            .date(new GregorianCalendar(2021, Calendar.NOVEMBER, 1).getTime())
            .qty(200)
            .price(100)
            .exchange("NASDAQ")
            .brokerCommission(0).build();
    PortfolioElementModel portfolioElementModel1 = PortfolioElementModelImpl.getBuilder()
            .stockModel(StockModelImpl.getBuilder()
                    .stockTicker("GOOG")
                    .stockName("Alphabet Inc - Class C")
                    .ipoDate(ipo)
                    .exchangeName("NASDAQ")
                    .build())
            .build();
    ipo = formatter.parse("2005-09-27");
    PortfolioElementModel portfolioElementModel2 = PortfolioElementModelImpl.getBuilder()
            .stockModel(StockModelImpl.getBuilder()
                    .stockTicker("AAL")
                    .stockName("American Airlines Group Inc")
                    .ipoDate(ipo)
                    .exchangeName("NASDAQ")
                    .build())
            .build();
    portfolioElementModel1.addTransaction(t1);
    portfolioElementModel2.addTransaction(t2);
    Map<String, PortfolioElementModel> portfolioElementHashMap = new HashMap<>();
    portfolioElementHashMap.put("GOOG", portfolioElementModel1);
    portfolioElementHashMap.put("AAL", portfolioElementModel2);
    PortfolioModel portfolioModel = portfolioModel("TEST",
            new Timestamp(System.currentTimeMillis()), portfolioElementHashMap);
    assertEquals("TEST", portfolioModel.getName());
    assertEquals("",
            portfolioModel
                    .getPortfolioComposition(new GregorianCalendar(2019, Calendar.NOVEMBER, 3)
                            .getTime()).stream()
                    .map(Object::toString).reduce("", String::concat));

    assertEquals("[GOOG, Alphabet Inc - Class C, 100.0, 100.0, 2020-11-03]",
            portfolioModel
                    .getPortfolioComposition(new GregorianCalendar(2021, Calendar.JANUARY, 3)
                            .getTime()).stream()
                    .map(Object::toString).reduce("", String::concat));
    assertEquals("[GOOG, Alphabet Inc - Class C, 100.0, 100.0, 2020-11-03]"
                    + "[AAL, American Airlines Group Inc, 200.0, 100.0, 2021-11-01]",
            portfolioModel
                    .getPortfolioComposition(new GregorianCalendar(2022, Calendar.NOVEMBER, 3)
                            .getTime()).stream()
                    .map(Object::toString).reduce("", String::concat));
  }

  @Test
  public void checkCostBasis() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String ipoDate = "2019-10-22";
    Date ipo = formatter.parse(ipoDate);
    TransactionModel t1 = TransactionModelImpl.getBuilder()
            .date(new GregorianCalendar(2020, Calendar.NOVEMBER, 3).getTime())
            .qty(100)
            .price(100)
            .exchange("NASDAQ")
            .brokerCommission(0).build();
    TransactionModel t2 = TransactionModelImpl.getBuilder()
            .date(new GregorianCalendar(2021, Calendar.NOVEMBER, 1).getTime())
            .qty(200)
            .price(100)
            .exchange("NASDAQ")
            .brokerCommission(0).build();
    PortfolioElementModel portfolioElementModel1 = PortfolioElementModelImpl.getBuilder()
            .stockModel(StockModelImpl.getBuilder()
                    .stockTicker("GOOG")
                    .stockName("Alphabet Inc - Class C")
                    .ipoDate(ipo)
                    .exchangeName("NASDAQ")
                    .build())
            .build();
    ipo = formatter.parse("2005-09-27");
    PortfolioElementModel portfolioElementModel2 = PortfolioElementModelImpl.getBuilder()
            .stockModel(StockModelImpl.getBuilder()
                    .stockTicker("AAL")
                    .stockName("American Airlines Group Inc")
                    .ipoDate(ipo)
                    .exchangeName("NASDAQ")
                    .build())
            .build();
    portfolioElementModel1.addTransaction(t1);
    portfolioElementModel2.addTransaction(t2);
    Map<String, PortfolioElementModel> portfolioElementHashMap = new HashMap<>();
    portfolioElementHashMap.put("GOOG", portfolioElementModel1);
    portfolioElementHashMap.put("AAL", portfolioElementModel2);
    PortfolioModel portfolioModel = portfolioModel("TEST",
            new Timestamp(System.currentTimeMillis()), portfolioElementHashMap);

    assertEquals(10000.0,
            portfolioModel
                    .getPortfolioCostBasis(new GregorianCalendar(2021, Calendar.JANUARY, 3)
                            .getTime()), 0.1);
    assertEquals(0.0,
            portfolioModel
                    .getPortfolioCostBasis(new GregorianCalendar(2018, Calendar.JANUARY, 3)
                            .getTime()), 0.1);
    assertEquals(30000.0,
            portfolioModel
                    .getPortfolioCostBasis(new GregorianCalendar(2022, Calendar.JANUARY, 3)
                            .getTime()), 0.1);

  }

}
