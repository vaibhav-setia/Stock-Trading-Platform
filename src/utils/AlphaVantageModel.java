package utils;

import java.util.List;

import models.StockDataModel;
import models.StockModel;

/**
 * This class implements the Stock Data API Model.
 * It has methods which call Alpha Vantage API to extract
 * the requested data for particular stock.
 */
public class AlphaVantageModel implements StockDataAPIModel {
  private final String apiKey = "WMDORK6BEVBQ7K7S";
  private final HttpRequestModel httpRequestModel;

  /**
   * Public constructor to initialize the alpha vantage model.
   */
  public AlphaVantageModel() {
    httpRequestModel = new HttpRequestModelImpl();
  }

  @Override
  public List<StockDataModel> getTimeSeriesDataForAStock(StockModel stockModel)
          throws RuntimeException {
    String result = httpRequestModel.fetchUrl(generateUrlForTimeSeriesData(stockModel));
    return new CustomCSVParserModelImpl().toListOfStockDataModel(result, stockModel);
  }

  @Override
  public List<StockModel> getSupportedStocks() {
    String result = httpRequestModel.fetchUrl(generateUrlForStocksListData());
    return new CustomCSVParserModelImpl().toListOfStocks(result);
  }

  /**
   * Helper method to generate the URL alphaVantage API.
   *
   * @param stockModel stock for which data needs to requested.
   * @return a URL string of AlphaVantageApi
   */
  private String generateUrlForTimeSeriesData(StockModel stockModel) {
    return "https://www.alphavantage"
            + ".co/query?function=TIME_SERIES_DAILY"
            + "&outputsize=full"
            + "&symbol"
            + "="
            + stockModel.getStockTicker()
            + "&apikey="
            + this.apiKey
            + "&datatype=csv";
  }

  private String generateUrlForStocksListData() {
    return "https://www.alphavantage"
            + ".co/query?function=LISTING_STATUS"
            + "&apikey="
            + this.apiKey;
  }
}