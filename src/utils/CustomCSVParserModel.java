package utils;

import java.util.List;

import models.StockDataModel;
import models.StockModel;

/**
 * This interface represents all the functionalities that can
 * be done on a CSV file to fetch various types of Data for a stock
 * or list of stocks.
 */
public interface CustomCSVParserModel {
  /**
   * Helper Method to convert an input string to
   * list of {@link StockDataModel} objects.
   *
   * @param input      comma separated input string of stock
   *                   data model
   * @param stockModel stock for which data needs to be extracted.
   * @return a list of Stock Data Model from csv.
   * @throws RuntimeException if error on converting csv to list.
   */
  List<StockDataModel> toListOfStockDataModel(String input, StockModel stockModel)
          throws RuntimeException;

  /**
   * Helper Method to convert an input string to
   * list of {@link StockModel} objects.
   *
   * @param input comma separated input string of stock
   *              model
   * @return list of {@link StockModel}
   * @throws RuntimeException if the given input string is corrupted
   */
  List<StockModel> toListOfStocks(String input) throws RuntimeException;
}
