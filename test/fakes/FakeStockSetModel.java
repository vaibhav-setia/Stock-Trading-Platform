package fakes;

import java.util.HashMap;
import java.util.Map;

import models.StockModel;
import models.StockSetModel;

/**
 * A Fake Stock Set model containing logging functionality to be used for testing ONLY.
 * It also provides functionality to return a particular Stock Model instance as desired by its
 * functions.
 */
public class FakeStockSetModel implements StockSetModel {
  private final StringBuilder log;
  private StockModel getStockReturnValue;
  private StockModel getAlternateStockReturnValue;

  private int count;

  /**
   * Construct a fake Stock Set model from a string builder.
   *
   * @param log The string builder used to write logs.
   */
  public FakeStockSetModel(StringBuilder log) {
    this.log = log;
  }

  /**
   * Construct a fake Stock Set model from a string builder and a Stock Model to be returned.
   *
   * @param log                 the string builder used to write logs.
   * @param getStockReturnValue the Stock Model to be returned during various function calls.
   */
  public FakeStockSetModel(StringBuilder log, StockModel getStockReturnValue) {
    this.log = log;
    this.getStockReturnValue = getStockReturnValue;
    this.getAlternateStockReturnValue = null;
  }

  /**
   * Construct a Fake Stock Set Model from a string builder and two stock models to be returned.
   *
   * @param log                     the string builder used for keeping track of logs.
   * @param getStockReturnValue     the Stock Model to be returned during first call.
   * @param getAlternateReturnValue the Stock Model to be returned in consecutive calls.
   */
  public FakeStockSetModel(StringBuilder log, StockModel getStockReturnValue,
                           StockModel getAlternateReturnValue) {
    this.log = log;
    this.getStockReturnValue = getStockReturnValue;
    this.getAlternateStockReturnValue = getStockReturnValue;
  }

  /**
   * Fake Add Stock model which just logs in the string builder.
   *
   * @param stockModel stock object to be added to Stock Set.
   */
  @Override
  public void addStock(StockModel stockModel) {
    log.append("[Stock Set Model]Add stock: ").append(stockModel.getStockName()).append("\n");
  }

  /**
   * Fake get stock model which logs and uses the stock model passed in the constructor.
   *
   * @param ticker stock symbol to uniquely fetch a stock model.
   * @return the stock model passed in the constructor and alternate stock model in next calls.
   */
  @Override
  public StockModel getStock(String ticker) {
    log.append("[Stock Set Model]Get stock: ").append(ticker).append("\n");
    count++;
    if (count == 1 || getAlternateStockReturnValue == null) {
      return getStockReturnValue;
    } else {
      return getAlternateStockReturnValue;
    }
  }

  @Override
  public Map<String, StockModel> getAllStocks() {
    log.append("[Stock Set Model]Get all stocks called.");
    return new HashMap<>();
  }

  /**
   * Fake Load Stocks from database which just logs in the call.
   */
  @Override
  public void loadStocksFromDatabase() {
    log.append("[Stock Set Model]Load stocks from database\n");
  }
}
