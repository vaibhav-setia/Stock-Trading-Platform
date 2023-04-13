package utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import models.PortfolioElementModel;
import models.PortfolioModel;
import models.StockDataModel;
import models.StockModel;
import models.StrategyModel;
import models.TransactionModel;
import models.UserModel;
import models.UserSetModel;

/**
 * Interface which contains different methods to
 * persist data of different types of objects.
 */
public interface DataPersister {
  /**
   * Method to persist all the tickers data in various places.
   *
   * @param stockModelList list of all the stock models to be written.
   * @param path           path where the object needs be persisted.
   * @throws FileNotFoundException exception if the given directory path is not found.
   */
  void writeTickersData(List<StockModel> stockModelList, String path) throws FileNotFoundException;

  /**
   * Method to persist all the time series data in various places.
   *
   * @param stockDataModelList list of all the stock data models to be written.
   * @param path               path where the object needs be persisted.
   * @throws FileNotFoundException exception if the given directory path is not found.
   */
  void writeTimeSeriesDataForTicker(List<StockDataModel> stockDataModelList, String path)
          throws FileNotFoundException;

  /**
   * Method to persist portfolio element data in various places.
   *
   * @param path                  path where the object needs be persisted.
   * @param portfolioElementModel portfolio element model to be written.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  void writePortfolioElement(String path, PortfolioElementModel portfolioElementModel)
          throws IOException, ParserConfigurationException,
          TransformerException;

  /**
   * Method to persist portfolio model data in various places.
   *
   * @param path            path where the object needs be persisted.
   * @param portfolioModel  portfolio model which needs to be persisted.
   * @param typeOfPortfolio what type of portfolio is being persisted.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  void writePortfolioModel(String path, PortfolioModel portfolioModel, String typeOfPortfolio)
          throws IOException, ParserConfigurationException,
          TransformerException;

  /**
   * Method to persist stock model data in various places.
   *
   * @param path       path where the object needs be persisted.
   * @param stockModel stock model which needs to be persisted.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  void writeStockModel(String path, StockModel stockModel) throws
          IOException, ParserConfigurationException,
          TransformerException;

  /**
   * Method to persist transaction model data in various places.
   *
   * @param path        path where the object needs be persisted.
   * @param id          transaction id of model being persisted.
   * @param transaction transaction object being persisted.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  void writeTransactionModel(String path, int id, TransactionModel transaction)
          throws IOException, ParserConfigurationException, TransformerException;

  /**
   * Method to persist user model data in various places.
   *
   * @param path      path where the object needs be persisted.
   * @param userModel user model object being persisted.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  void writeUserModel(String path, UserModel userModel)
          throws IOException, ParserConfigurationException,
          TransformerException;

  /**
   * Method to persist user set model data in various places.
   *
   * @param root         folder where all the data needs to be stored.
   * @param userSetModel user set model object being persisted.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  void writeUserSetModel(String root, UserSetModel userSetModel) throws IOException,
          ParserConfigurationException,
          TransformerException;

  void writeStrategyModel(String root, StrategyModel strategyModelList) throws IOException,
          ParserConfigurationException,
          TransformerException;
}
