package models;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import utils.CustomCSVParserModelImpl;

import static org.junit.Assert.assertEquals;

/**
 * Test Class to write junits to ensure CSV Parser Model class is working correctly.
 */
public class CustomCSVParserModelTest {
  @Test
  public void testToListOfStockDataModel() throws ParseException {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    String testDate = "2022-11-01";
    Date date = formatter.parse(testDate);

    String input = "2022-10-28,92.5300,96.8600,92.3225,96.5800,35696925\r\n"
            + "2022-10-27,94.3100,95.1700,91.9000,92.6000,54036485\r\n"
            + "2022-10-26,96.7600,98.5400,94.5700,94.8200,7167181";

    StockModel stockModel = StockModelImpl.getBuilder()
            .stockTicker("GOOG")
            .exchangeName("NASDAQ")
            .stockName("Google")
            .ipoDate(date)
            .build();

    List<StockDataModel> stockDataModelList = new CustomCSVParserModelImpl()
            .toListOfStockDataModel(input, stockModel);

    assertEquals(2, stockDataModelList.size());
    assertEquals(92.6, stockDataModelList.get(0).getClosePrice(), 0.001);
    assertEquals(formatter.parse("2022-10-27"), stockDataModelList.get(0).getDate());

    assertEquals(94.82, stockDataModelList.get(1).getClosePrice(), 0.001);
    assertEquals(formatter.parse("2022-10-26"), stockDataModelList.get(1).getDate());

  }

  @Test
  public void testToListOfStocks() {
    String input = "A,Agilent Technologies Inc,NYSE,Stock,2020-08-19,Stock,Active\r\n"
            + "AA,Alcoa Corp,NYSE,Stock,2020-08-19,Stock,Active";

    List<StockModel> stockModelList = new CustomCSVParserModelImpl().toListOfStocks(input);

    assertEquals("AA", stockModelList.get(0).getStockTicker());
  }
}
