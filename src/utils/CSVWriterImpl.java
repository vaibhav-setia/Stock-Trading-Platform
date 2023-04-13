package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * Class to write the objetc of any class in a csv format.
 * This class implements all the methods of DataPersister class.
 */
public class CSVWriterImpl implements DataPersister {
  @Override
  public void writeTickersData(List<StockModel> stockModelList, String path)
          throws FileNotFoundException {
    File csvOutputFile = new File(path);
    PrintWriter printWriter = new PrintWriter(csvOutputFile);
    printWriter.println(getCSVStringFromStockModel(stockModelList));
    printWriter.flush();
  }

  @Override
  public void writeTimeSeriesDataForTicker(List<StockDataModel> stockDataModelList, String path)
          throws FileNotFoundException {
    File csvOutputFile = new File(path);
    PrintWriter printWriter = new PrintWriter(csvOutputFile);
    printWriter.println(getCSVStringFromStockDataModel(stockDataModelList));
    printWriter.flush();
  }

  /**
   * Placeholder method to persist portfolio element in csv format if required in future.
   *
   * @param path                  path where the object needs be persisted.
   * @param portfolioElementModel portfolio element model to be written.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  @Override
  public void writePortfolioElement(String path,
                                    PortfolioElementModel portfolioElementModel)
          throws IOException, ParserConfigurationException, TransformerException {
    return;
  }

  /**
   * Placeholder method to persist portfolio model in csv format if required in future.
   *
   * @param path            path where the object needs be persisted.
   * @param portfolioModel  portfolio model which needs to be persisted.
   * @param typeOfPortfolio what type of portfolio is being persisted.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  @Override
  public void writePortfolioModel(String path,
                                  PortfolioModel portfolioModel,
                                  String typeOfPortfolio)
          throws IOException, ParserConfigurationException, TransformerException {
    return;
  }

  /**
   * Placeholder method to persist stock model in csv format if required in future.
   *
   * @param path       path where the object needs be persisted.
   * @param stockModel stock model which needs to be persisted.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  @Override
  public void writeStockModel(String path, StockModel stockModel)
          throws IOException, ParserConfigurationException,
          TransformerException {
    return;
  }

  /**
   * Placeholder method to persist transaction model in csv format if required in future.
   *
   * @param path        path where the object needs be persisted.
   * @param id          transaction id of model being persisted.
   * @param transaction transaction object being persisted.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  @Override
  public void writeTransactionModel(String path, int id, TransactionModel transaction)
          throws IOException, ParserConfigurationException, TransformerException {
    return;
  }

  /**
   * Placeholder method to persist user model in csv format if required in future.
   *
   * @param path      path where the object needs be persisted.
   * @param userModel user model object being persisted.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  @Override
  public void writeUserModel(String path, UserModel userModel)
          throws IOException, ParserConfigurationException, TransformerException {
    return;
  }

  /**
   * Placeholder method to persist user set model in csv format if required in future.
   *
   * @param root         folder where all the data needs to be stored.
   * @param userSetModel user set model object being persisted.
   * @throws IOException                  exception while writing the file.
   * @throws ParserConfigurationException if the data parser is configured incorrectly.
   * @throws TransformerException         if the data object could not be converted to file.
   */
  @Override
  public void writeUserSetModel(String root, UserSetModel userSetModel)
          throws IOException, ParserConfigurationException, TransformerException {
    return;
  }

  @Override
  public void writeStrategyModel(String root, StrategyModel strategyModelList)
          throws IOException, ParserConfigurationException, TransformerException {
    return;
  }

  private String getCSVStringFromStockModel(List<StockModel> data) {
    String pattern = "yyyy-MM-dd";
    DateFormat dateFormat = new SimpleDateFormat(pattern);

    StringBuilder builder = new StringBuilder();
    builder.append("symbol,name,exchange,assetType,ipoDate,delistingDate,status\r\n");
    for (StockModel stockModel : data) {
      builder.append(stockModel.getStockTicker())
              .append(",")
              .append(stockModel.getStockName().replace("/", ""))
              .append(",")
              .append(stockModel.getExchangeName())
              .append(",")
              .append(stockModel.getAssetType())
              .append(",")
              .append(stockModel.getIpoDate() != null
                      ? dateFormat.format(stockModel.getIpoDate()) : "null")
              .append(",")
              .append(stockModel.getDelistingDate() != null
                      ? dateFormat.format(stockModel.getDelistingDate()) : "null")
              .append(",")
              .append(stockModel.getStatus())
              .append("\r\n");
    }
    return builder.toString();
  }

  private String getCSVStringFromStockDataModel(List<StockDataModel> data) {
    String pattern = "yyyy-MM-dd";
    DateFormat dateFormat = new SimpleDateFormat(pattern);

    StringBuilder builder = new StringBuilder();
    builder.append("timestamp,open,high,low,close,volume\r\n");
    for (StockDataModel stockDataModel : data) {
      builder.append(stockDataModel.getDate() != null
                      ? dateFormat.format(stockDataModel.getDate()) : "null")
              .append(",")
              .append(stockDataModel.getOpenPrice())
              .append(",")
              .append(stockDataModel.getHighPrice())
              .append(",")
              .append(stockDataModel.getLowPrice())
              .append(",")
              .append(stockDataModel.getClosePrice())
              .append(",")
              .append(stockDataModel.getQuantity())
              .append("\r\n");
    }

    return builder.toString();
  }
}
