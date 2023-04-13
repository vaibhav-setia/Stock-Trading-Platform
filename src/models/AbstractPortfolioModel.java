package models;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import utils.ChartPlot;
import utils.ChartPlotCalculatorUtil;
import utils.DateRange;
import utils.DateRangeCalculatorUtil;

/**
 * Abstract class for portfolio model.
 * This class is inherited by both flexible and inflexible portfolios and
 * contains all the common abstracted methods.
 */
public abstract class AbstractPortfolioModel implements PortfolioModel {
  protected final String portfolioName;

  protected final Timestamp timestampAdded;

  protected final Map<String, PortfolioElementModel> portfolioElementHashMap;

  /**
   * Public constructor to initialize a portfolio.
   *
   * @param portfolioName           name of portfolio
   * @param timestampAdded          creation timestamp of portfolio
   * @param portfolioElementHashMap map of the different elements of a portfolio
   */
  public AbstractPortfolioModel(String portfolioName, Timestamp timestampAdded,
                                Map<String, PortfolioElementModel> portfolioElementHashMap) {
    this.portfolioName = portfolioName;
    this.timestampAdded = timestampAdded;
    this.portfolioElementHashMap = portfolioElementHashMap;
  }

  protected static TransactionModel addTransactionToElement(StockSetModel stockSetModel,
                                                            SimpleDateFormat format,
                                                            List<String> stock)
          throws ParseException {
    return TransactionModelImpl.getBuilder()
            .qty(Integer.parseInt(stock.get(1).trim()))
            .date(format.parse(stock.get(2)))
            .price(stockSetModel.getStock(stock.get(0).trim())
                    .getPriceOnADate(format.parse(stock.get(2))))
            .brokerCommission(Double.parseDouble(stock.get(3).trim()))
            .exchange(stockSetModel.getStock(stock.get(0).trim()).getExchangeName()).build();
  }

  protected static void addElementToMap(StockSetModel stockSetModel,
                                        SimpleDateFormat format, List<String> stock,
                                        Map<String, PortfolioElementModel> portfolioElementHashMap)
          throws ParseException {
    if (!stockSetModel.getStock(stock.get(0).trim())
            .doesPriceExistOnADate(format.parse(stock.get(2)))) {
      throw new IllegalArgumentException("Incorrect Date for: "
              + stock.get(0).trim());
    }
    PortfolioElementModel portfolioElement;
    if (portfolioElementHashMap.containsKey(stock.get(0).trim())) {
      portfolioElement = portfolioElementHashMap.get(stock.get(0).trim());
    } else {
      portfolioElement = PortfolioElementModelImpl.getBuilder()
              .stockModel(stockSetModel.getStock(stock.get(0).trim())).build();
    }
    portfolioElement.addTransaction(addTransactionToElement(stockSetModel, format, stock));
    portfolioElementHashMap.put(stock.get(0).trim(), portfolioElement);
  }

  protected static void checkShort(Map<String,
          PortfolioElementModel> portfolioElementHashMap) throws IllegalArgumentException {
    for (String ticker : portfolioElementHashMap.keySet()) {
      PortfolioElementModel tickerElement = portfolioElementHashMap.get(ticker);
      List<TransactionModel> tickerTransactions = tickerElement.getAllTransactions();
      tickerTransactions.sort(Comparator.comparing(TransactionModel::getTransactionDate));
      double qty = 0;
      for (TransactionModel tickerTransaction : tickerTransactions) {
        qty += tickerTransaction.getQty();
        if (qty < 0) {
          throw new IllegalArgumentException();
        }
      }
    }
  }

  @Override
  public String getName() {
    return this.portfolioName;
  }

  @Override
  public void runAllStrategies() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<StrategyModel> getAllStrategies() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Timestamp getTimestamp() {
    return this.timestampAdded;
  }

  @Override
  public double getPortfolioValueByDate(Date date) throws IllegalArgumentException {
    double sum = 0;
    if (date.compareTo(new Date()) >= 0) {
      throw new IllegalArgumentException();
    }
    for (String stock : portfolioElementHashMap.keySet()) {
      sum += portfolioElementHashMap.get(stock).getPortfolioElementValueOnDate(date);
    }
    return Math.round(sum * 100.0) / 100.0;
  }

  @Override
  public Timestamp getPortfolioAddDateTime() {
    return this.timestampAdded;
  }

  @Override
  public List<List<String>> getPortfolioComposition(Date date) {
    List<List<String>> compositionResult = new ArrayList<>();
    for (String tick : portfolioElementHashMap.keySet()) {
      List<String> elementResult = portfolioElementHashMap.get(tick).getCompositionOnDate(date);
      if (elementResult.size() > 0) {
        compositionResult.add(elementResult);
      }
    }
    return compositionResult;
  }

  @Override
  public Map<String, PortfolioElementModel> getPortfolioElements() {
    return this.portfolioElementHashMap;
  }

  @Override
  public PortfolioModel getConcretePortfolio() {
    return null;
  }

  @Override
  public PortfolioModel getFlexiblePortfolio() {
    return null;
  }

  @Override
  public PortfolioModel getStrategyFlexiblePortfolio() {
    return null;
  }


  @Override
  public abstract void generateXML(String path)
          throws IOException, ParserConfigurationException, TransformerException;


  @Override
  public void modifyPortfolio(StockModel stockModel,
                              TransactionModel transaction) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addPortfolioElement(PortfolioElementModel portfolioElement)
          throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }

  @Override
  public double getPortfolioCostBasis(Date date) {
    double sum = 0;
    for (String stock : portfolioElementHashMap.keySet()) {
      sum += portfolioElementHashMap.get(stock).getPortfolioCostBasis(date);
    }
    return Math.round(sum * 100.0) / 100.0;
  }

  @Override
  public ChartPlot getPerformanceForPortfolio(Date startDate, Date endDate) {
    DateRange dateRange = DateRangeCalculatorUtil.getDateRange(startDate, endDate);
    Map<Date, Double> datePriceMap = getPricesForDates(dateRange.getDateList(), true);
    return ChartPlotCalculatorUtil.buildChartPlot(datePriceMap,
            dateRange.getDateRangeType(), 50);
  }

  private Map<Date, Double> getPricesForDates(List<Date> dates, boolean lookBack) {
    Map<Date, Double> datePricesMap = new LinkedHashMap<>();
    for (Date date : dates) {
      double price = this.getPortfolioValueByDate(date);
      if (price == 0 && lookBack) {
        date = getPreviousWorkingDateFromPortfolioData(date);
        price = this.getPortfolioValueByDate(date);
      }
      datePricesMap.put(date, price);
    }
    return datePricesMap;
  }

  protected Date getPreviousWorkingDateFromPortfolioData(Date date) {
    if (this.getPortfolioValueByDate(date) == 0) {
      Date currentDate = date;
      int totalLookBack = 10;
      while (this.getPortfolioValueByDate(date) == 0 && totalLookBack != 0) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        localDate = localDate.minusDays(1);
        date = Date.from(localDate.atStartOfDay(ZoneId.of("America/New_York")).toInstant());
        totalLookBack--;
      }
      if (totalLookBack == 0) {
        return currentDate;
      }
      return date;
    }
    return date;
  }


  @Override
  public List<TransactionModel> getAllTransactions() {
    List<TransactionModel> transactionModelList = new ArrayList<>();
    for (Map.Entry<String, PortfolioElementModel> entry : this.portfolioElementHashMap.entrySet()) {
      transactionModelList.addAll(entry.getValue().getAllTransactions());
    }
    return transactionModelList;
  }

  @Override
  public void addStrategy(StrategyModel strategyModel) throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
  }
}
