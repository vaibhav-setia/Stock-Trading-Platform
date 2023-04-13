package models;

import java.io.IOException;
import java.util.Map;

/**
 * An interface which contains all the methods applicable on
 * a set of stocks.
 */
public interface StockSetModel {

  /**
   * Add a stock to the stock set.
   *
   * @param stockModel stock object to be added to Stock Set.
   */
  void addStock(StockModel stockModel);

  /**
   * Get a stock model from a stock list.
   *
   * @param ticker stock symbol to uniquely fetch a stock model.
   * @return a stock as a stock model object only.
   */
  StockModel getStock(String ticker);

  Map<String, StockModel> getAllStocks();

  /**
   * Loads stocks into the stock list by reading it from the
   * CSV file.
   *
   * @throws IOException if the loaded file is not read properly.
   */
  void loadStocksFromDatabase() throws IOException;
}
