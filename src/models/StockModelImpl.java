package models;

import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import utils.AlphaVantageModel;
import utils.CSVWriterImpl;
import utils.CustomCSVParserModelImpl;
import utils.DataParser;
import utils.DataPersister;
import utils.FileHandlerModel;
import utils.FileHandlerModelImpl;
import utils.XMLParserImpl;
import utils.XMLWriterImpl;

/**
 * This class implements StockModel and all its functionality.
 * This class stores all the information for a {@link StockModel}.
 */
public class StockModelImpl implements StockModel {

  private final String stockTicker;

  private final String stockName;

  private final String exchangeName;

  private final Date ipoDate;

  private final String assetType;
  private final Date delistingDate;
  private final String status;

  /**
   * Constructor to assign values to attributes of Stock Model.
   *
   * @param stockTicker  stock ticker symbol to be assigned.
   * @param stockName    stock ticker name to be assigned.
   * @param exchangeName exchange name where stock is traded.
   * @param ipoDate      date when stock was made public.
   */
  public StockModelImpl(String stockTicker, String stockName, String exchangeName, Date ipoDate) {
    this.stockTicker = stockTicker;
    this.stockName = stockName;
    this.exchangeName = exchangeName;
    this.ipoDate = ipoDate;
    this.assetType = "Stock";
    this.delistingDate = null;
    this.status = "Active";
  }

  /**
   * Static class to get a builder to build
   * Stock Model class.
   *
   * @return a new builder object for Stock Model.
   */
  public static StockModelBuilder getBuilder() {
    return new StockModelBuilder();
  }

  @Override
  public String getStockTicker() {
    return this.stockTicker;
  }

  @Override
  public String getStockName() {
    return this.stockName;
  }

  @Override
  public String getExchangeName() {
    return this.exchangeName;
  }

  @Override
  public Date getIpoDate() {
    return this.ipoDate;
  }

  @Override
  public Date getDelistingDate() {
    return this.delistingDate;
  }

  @Override
  public String getStatus() {
    return this.status;
  }

  @Override
  public String getAssetType() {
    return this.assetType;
  }

  private List<StockDataModel> getStockDataModelListFromTimeSeriesData(FileHandlerModel handler)
          throws IOException {
    String prices = null;
    prices = handler.readFileDataAsString(handler.getFilePathForATicker(this.getStockTicker()));
    return new CustomCSVParserModelImpl()
            .toListOfStockDataModel(prices, this);
  }

  private boolean toFetchTimeSeriesData(Date date, FileHandlerModel handler) {
    boolean fileExists = handler.checkFileExists(
            handler.getFilePathForATicker(this.stockTicker));

    boolean dateInRange = false;
    if (fileExists) {
      try {
        List<StockDataModel> stockDataModelList = getStockDataModelListFromTimeSeriesData(handler);
        dateInRange = (stockDataModelList.get(0).getDate().compareTo(date) > 0);
      } catch (IOException e) {
        return true;
      }
    }

    return !fileExists || !dateInRange;
  }

  private List<StockDataModel> getStockDate(Date date) {
    FileHandlerModel handler = new FileHandlerModelImpl();

    if (toFetchTimeSeriesData(date, handler)) {
      List<StockDataModel> stockDataModels = new AlphaVantageModel()
              .getTimeSeriesDataForAStock(this);
      DataPersister persister = new CSVWriterImpl();
      try {
        persister.writeTimeSeriesDataForTicker(stockDataModels,
                handler.getFilePathForATicker(this.stockTicker));
      } catch (FileNotFoundException e) {
        return null;
      }
    }

    List<StockDataModel> stockDataModels = null;
    try {
      stockDataModels = getStockDataModelListFromTimeSeriesData(handler);
    } catch (IOException e) {
      return null;
    }

    return stockDataModels;
  }

  @Override
  public double getPriceOnADate(Date date) {
    List<StockDataModel> stockDataModels = getStockDate(date);
    if (stockDataModels == null) {
      return 0;
    }

    double price = 0;
    double lastPrice = 0;
    Collections.reverse(stockDataModels);
    for (StockDataModel stockDataModel : stockDataModels) {
      if (stockDataModel.getDate().equals(date)) {
        price = stockDataModel.getClosePrice();
        break;
      } else if (stockDataModel.getDate().compareTo(date) > 0) {
        price = lastPrice;
        break;
      }
      lastPrice = stockDataModel.getClosePrice();
    }
    return price;
  }

  @Override
  public boolean doesPriceExistOnADate(Date date) {
    List<StockDataModel> stockDataModels = getStockDate(date);
    if (stockDataModels == null) {
      return false;
    }

    for (StockDataModel stockDataModel : stockDataModels) {
      if (stockDataModel.getDate().equals(date)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void generateXML(String path) throws IOException, ParserConfigurationException,
          TransformerException {
    DataPersister persister = new XMLWriterImpl();
    persister.writeStockModel(path, this);
  }

  @Override
  public Map<Date, Map<Integer, Double>> getPriceOnMultipleDates(Set<Date> dateSet, Date maxDate) {
    List<StockDataModel> stockDataModels = getStockDate(maxDate);
    if (stockDataModels == null) {
      return new HashMap<>();
    }
    Map<Date, Double> stockModelDatePriceMap = new HashMap<>();
    for (StockDataModel stockDataModel : stockDataModels) {
      stockModelDatePriceMap.put(stockDataModel.getDate(), stockDataModel.getClosePrice());
    }
    Map<Date, Map<Integer, Double>> datePriceMap = new HashMap<>();
    for (Date date : dateSet) {
      Date dateToCheck = date;
      int count = 0;
      while (!stockModelDatePriceMap.containsKey(dateToCheck) && count < 10) {
        count++;
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(dateToCheck);
        gregorianCalendar.add(Calendar.DATE, 1);
        dateToCheck = gregorianCalendar.getTime();
      }
      Map<Integer, Double> dateCountMap;
      if (datePriceMap.containsKey(dateToCheck)) {
        dateCountMap = datePriceMap.get(dateToCheck);
        for (Integer key : dateCountMap.keySet()) {
          Double priceVal = dateCountMap.get(key);
          dateCountMap.remove(key);
          dateCountMap.put(key + 1, priceVal);
        }
      } else {
        dateCountMap = new HashMap<>();
        dateCountMap.put(1, stockModelDatePriceMap.get(dateToCheck));
      }
      datePriceMap.put(dateToCheck, dateCountMap);
    }
    return datePriceMap;
  }

  /**
   * Builder helper class to build an object of Stock Data Model class.
   * It contains functionality to set different fields of Stock Data Model.
   */
  public static class StockModelBuilder {
    private String stockTicker;

    private String stockName;

    private String exchangeName;

    private Date ipoDate;

    /**
     * Initialises a stock model builder with initial empty values.
     */
    private StockModelBuilder() {
      this.stockTicker = "";
      this.stockName = "";
      this.exchangeName = "";
      this.ipoDate = null;
    }

    /**
     * Set stock ticker symbol of builder object to be further assigned
     * to User Set Model.
     *
     * @param stockTicker stock symbol to be assigned.
     * @return the resulting builder object with stock symbol assigned.
     */
    public StockModelBuilder stockTicker(String stockTicker) {
      this.stockTicker = stockTicker;
      return this;
    }

    /**
     * Set stockName of builder object to be further assigned
     * to User Set Model.
     *
     * @param stockName stock name to be assigned.
     * @return the resulting builder object with stock name assigned.
     */
    public StockModelBuilder stockName(String stockName) {
      this.stockName = stockName;
      return this;
    }

    /**
     * Set exchangeName of builder object to be further assigned
     * to User Set Model.
     *
     * @param exchangeName exchange name to be assigned.
     * @return the resulting builder object with exchange name assigned.
     */
    public StockModelBuilder exchangeName(String exchangeName) {
      this.exchangeName = exchangeName;
      return this;
    }

    /**
     * Set ipoDate of builder object to be further assigned
     * to User Set Model.
     *
     * @param ipoDate ipoDate to be assigned.
     * @return the resulting builder object with ipoDate assigned.
     */
    public StockModelBuilder ipoDate(Date ipoDate) {
      this.ipoDate = ipoDate;
      return this;
    }

    /**
     * Read and build a Stock Model object from the xml file.
     *
     * @param path   relative path to source folder from where the xml file needs to be read.
     * @param ticker stock symbol to read a particular stock from XML.
     * @return an object of StockModelBuilder with all the attributes assigned from XML file.
     * @throws IOException                  if xml is not correctly read from file.
     * @throws ParserConfigurationException if the xml parser is configured incorrectly.
     * @throws SAXException                 exception thrown if xml violates integrity constraints.
     * @throws XPathExpressionException     if the Xpath to filter xml is incorrect.
     * @throws ParseException               if xml string is not correctly parsed as per contract.
     */
    public StockModelBuilder readXML(String path, String ticker)
            throws XPathExpressionException, IOException, ParserConfigurationException,
            SAXException, ParseException {
      DataParser<StockModelBuilder> dataParserStockModel = new XMLParserImpl<>();
      return dataParserStockModel.getStockModelBuilder(path, ticker);
    }

    /**
     * Build a Stock Model builder object to get Stock Model.
     *
     * @return a new Stock Model object with all users appended
     *         in the list.
     */
    public StockModelImpl build() {
      return new StockModelImpl(this.stockTicker, this.stockName, this.exchangeName, this.ipoDate);
    }

  }

}
