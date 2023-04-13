package models;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Class to write Junit tests to verify correct working of StockDataModel.
 */
public class StockDataModelTest {

  @Test
  public void testStockDataModelInitializesAndGettersWorkProperly() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String testDate = "2022-11-01";
    Date date = formatter.parse(testDate);

    String ipoDate = "2022-10-27";
    Date ipo = formatter.parse(ipoDate);

    StockDataModel stockDataModel = new StockDataModelImpl(
            StockModelImpl.getBuilder()
                    .stockTicker("GOOG")
                    .stockName("Google")
                    .exchangeName("NASDAQ")
                    .ipoDate(ipo)
                    .build(),
            date,
            101,
            110,
            98,
            104,
            101
    );

    assertEquals("GOOG", stockDataModel.getStockModel().getStockTicker());
    assertEquals(104, stockDataModel.getClosePrice(), 0.001);
  }

  @Test
  public void testStockDataModelCreatesCorrectlyWithBuilder() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String testDate = "2022-11-01";
    Date date = formatter.parse(testDate);

    String ipoDate = "2022-10-27";
    Date ipo = formatter.parse(ipoDate);

    StockDataModel stockDataModel = StockDataModelImpl
            .getBuilder()
            .stockModel(StockModelImpl.getBuilder()
                    .stockTicker("GOOG")
                    .stockName("Google")
                    .exchangeName("NASDAQ")
                    .ipoDate(ipo)
                    .build())
            .date(date)
            .openPrice(101)
            .highPrice(110)
            .lowPrice(98)
            .closePrice(104)
            .quantity(101)
            .build();

    assertEquals("GOOG", stockDataModel.getStockModel().getStockTicker());
    assertEquals(104, stockDataModel.getClosePrice(), 0.001);
  }
}
