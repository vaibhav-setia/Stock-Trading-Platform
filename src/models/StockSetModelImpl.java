package models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.AlphaVantageModel;
import utils.CSVWriterImpl;
import utils.CustomCSVParserModelImpl;
import utils.DataPersister;
import utils.FileHandlerModel;
import utils.FileHandlerModelImpl;

/**
 * This class implements the StockSetModel interface.
 * it stores all the stocks in a map as a StockModel using their ticker symbol.
 * It provides a build class to create a StockSetModel object.
 */
public class StockSetModelImpl implements StockSetModel {
  private Map<String, StockModel> stockList;

  public StockSetModelImpl(Map<String, StockModel> stockList) {
    this.stockList = stockList;
  }

  public static StockSetModelBuilder getBuilder() {
    return new StockSetModelBuilder();
  }

  @Override
  public void addStock(StockModel stockModel) {
    this.stockList.put(stockModel.getStockTicker(), stockModel);
  }

  @Override
  public StockModel getStock(String ticker) {
    return stockList.get(ticker);
  }

  @Override
  public Map<String, StockModel> getAllStocks() {
    return stockList;
  }

  @Override
  public void loadStocksFromDatabase() throws IOException {
    FileHandlerModel fileHandlerModel = new FileHandlerModelImpl();
    Files.createDirectories(Paths.get("StockSetModel//Tickers"));

    if (!fileHandlerModel.checkFileExists(fileHandlerModel.getFilePathForTickerList())) {
      List<StockModel> stocks = new AlphaVantageModel().getSupportedStocks();
      DataPersister persister = new CSVWriterImpl();
      persister.writeTickersData(stocks, fileHandlerModel.getFilePathForTickerList());
    }

    String stocksListString = fileHandlerModel
            .readFileDataAsString(fileHandlerModel.getFilePathForTickerList());
    List<StockModel> stocks = new CustomCSVParserModelImpl().toListOfStocks(stocksListString);
    Map<String, StockModel> stockMap = new HashMap<>();
    for (StockModel stockModel : stocks) {
      stockMap.put(stockModel.getStockTicker(), stockModel);
    }
    this.stockList = stockMap;
  }

  /**
   * Builder helper class to build an object of Stock Set Model class.
   * It contains functionality to set different fields of Stock Set Model.
   */
  public static class StockSetModelBuilder {
    private Map<String, StockModel> stockList;

    private StockSetModelBuilder() {
      this.stockList = new HashMap<>();

    }

    /**
     * Set stockList of builder object to be further assigned
     * to Stock Set Model.
     *
     * @param stockList stockList to be assigned.
     * @return the resulting builder object with stockList assigned.
     */
    public StockSetModelBuilder stockList(Map<String, StockModel> stockList) {
      this.stockList = stockList;
      return this;
    }

    /**
     * Build a Stock Set Model builder object to get Stock Set Model.
     *
     * @return a new Stock Set Model object with all stocks added
     *         in the map.
     */
    public StockSetModel build() {
      return new StockSetModelImpl(this.stockList);
    }
  }


}
