package models;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Class to write Junit tests to verify correct working of Stock Set Model.
 */
public class StockSetModelTest {
  @Test
  public void testInitializeStockModel() {
    StockSetModel stockSetModel = new StockSetModelImpl(new HashMap<>());
    assertNull(stockSetModel.getStock("Some ticker"));
  }

  @Test
  public void testAddAndGetStockWorksCorrectly() throws ParseException {
    String testDate = "2022-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);

    StockSetModel stockSetModel = new StockSetModelImpl(new HashMap<>());
    stockSetModel.addStock(StockModelImpl.getBuilder()
            .stockName("Google")
            .stockTicker("GOOG")
            .exchangeName("NASDAQ")
            .ipoDate(date)
            .build());

    assertEquals(stockSetModel.getStock("GOOG").getStockName(), "Google");
    assertEquals(stockSetModel.getStock("GOOG").getStockTicker(), "GOOG");
    assertEquals(stockSetModel.getStock("GOOG").getExchangeName(), "NASDAQ");
    assertEquals(stockSetModel.getStock("GOOG").getIpoDate(), date);
  }

  @Test
  public void testBuildStockModelWorksCorrectly() throws ParseException {
    String testDate = "2022-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);

    StockModel stockModel = StockModelImpl.getBuilder()
            .stockName("Google")
            .stockTicker("GOOG")
            .exchangeName("NASDAQ")
            .ipoDate(date)
            .build();

    Map<String, StockModel> stockModelList = new HashMap<>();
    stockModelList.put("GOOG", stockModel);

    StockSetModel stockSetModel = StockSetModelImpl.getBuilder()
            .stockList(stockModelList)
            .build();

    assertEquals(stockSetModel.getStock("GOOG").getStockName(), "Google");
  }
}
