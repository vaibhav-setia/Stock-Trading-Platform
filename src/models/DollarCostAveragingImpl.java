package models;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import utils.DataParser;
import utils.DataPersister;
import utils.XMLParserImpl;
import utils.XMLWriterImpl;

/**
 * Dollar Cost Averaging class implements strategy model.
 * This strategy is used to invest for a range of time period
 * by specifying the start, end date, amount to be invested and
 * where and how much to invest.
 */
public class DollarCostAveragingImpl implements StrategyModel {

  private final String strategyName;
  private final double amount;

  private final Date startDate;

  private final Date endDate;

  private final int dateFrequency;


  private final Map<StockModel, Double> stockMapPercent;

  private final double brokerCommission;

  /**
   * Public constructor to assign values to a dollar
   * cost averaging implementation.
   *
   * @param strategyName     name of strategy.
   * @param amount           amount to be invested.
   * @param startDate        starting date of the strategy.
   * @param endDate          ending date of the strategy.
   * @param dateFrequency    frequency of investment.
   * @param brokerCommission broker commission on each transaction investment.
   * @param stockMapPercent  percent of each stock to be invested.
   * @throws IllegalArgumentException if the end date is lesser than start date or null.
   */
  public DollarCostAveragingImpl(String strategyName, double amount, Date startDate, Date endDate,
                                 int dateFrequency, double brokerCommission,
                                 Map<StockModel, Double> stockMapPercent)
          throws IllegalArgumentException {
    if (endDate != null && endDate.compareTo(startDate) < 0) {
      throw new IllegalArgumentException();
    }
    this.strategyName = strategyName;
    this.amount = amount;
    this.startDate = startDate;
    this.endDate = endDate;
    this.dateFrequency = dateFrequency;
    this.stockMapPercent = stockMapPercent;
    this.brokerCommission = brokerCommission;
  }

  /**
   * Method to get builder object of strategy.
   *
   * @return a dollar cost averaging builder object only.
   */
  public static DollarCostAveragingBuilder getBuilder() {
    return new DollarCostAveragingBuilder();
  }

  @Override
  public String getStrategyName() {
    return strategyName;
  }

  @Override
  public int getDateFrequency() {
    return this.dateFrequency;
  }

  @Override
  public Date getStartDate() {
    return startDate;
  }

  @Override
  public Date getEndDate() {
    return endDate;
  }

  @Override
  public double getAmount() {
    return amount;
  }

  @Override
  public void generateXML(String path) throws
          IOException, ParserConfigurationException, TransformerException {
    DataPersister persister = new XMLWriterImpl();
    persister.writeStrategyModel(path, this);
  }

  private Map<StockModel, Map<Date, Map<Integer, Double>>>
      getPricesDatesList(Set<Date> applicableDates, StrategyModel strategyModel, Date maxDate) {
    Map<StockModel, Map<Date, Map<Integer, Double>>> soln = new HashMap<>();
    for (StockModel stockModel : strategyModel.getStockMapPercent().keySet()) {
      soln.put(stockModel, stockModel.getPriceOnMultipleDates(applicableDates, maxDate));
    }
    return soln;
  }

  private Date getLastRunDate(String source, PortfolioModel portfolioModel) {
    Date maxRunDate = null;
    for (int i = 0; i < portfolioModel.getAllTransactions().size(); i++) {
      TransactionModel transactionModel = portfolioModel.getAllTransactions().get(i);
      if (maxRunDate == null && transactionModel.getTransactionSource().equals(source)) {
        maxRunDate = transactionModel.getTransactionDate();
        continue;
      }
      if (transactionModel.getTransactionSource().equals(source)
              && transactionModel.getTransactionDate().compareTo(maxRunDate) > 0) {
        maxRunDate = transactionModel.getTransactionDate();
      }
    }

    return maxRunDate;
  }

  /**
   * Method to create all transactions from a strategy to later persis them.
   * Transactions are only created if there is enough amount to buy some quantity.
   *
   * @param stockAllDatePrices map of all the models and their prices on transaction dates.
   * @param strategyModel      strategy to be applied while creating transactions.
   * @param portfolioModel     portfolioModel on which transactions will be added.
   */
  private void createTransactions(Map<StockModel, Map<Date, Map<Integer, Double>>>
                                          stockAllDatePrices,
                                  StrategyModel strategyModel, PortfolioModel portfolioModel) {
    for (StockModel stockModel : stockAllDatePrices.keySet()) {
      for (Date date : stockAllDatePrices.get(stockModel).keySet()) {
        for (int dateCount : stockAllDatePrices.get(stockModel).get(date).keySet()) {
          for (int i = 0; i < dateCount; i++) {
            if (stockAllDatePrices.get(stockModel).get(date) == null) {
              continue;
            }
            if (strategyModel.getStockMapPercent().get(stockModel) == 0) {
              continue;
            }
            if (stockAllDatePrices.get(stockModel).get(date).get(dateCount) == null) {
              continue;
            }
            double amountAvailable = ((strategyModel.getStockMapPercent()
                    .get(stockModel)) * (0.01)) * (strategyModel.getAmount());
            amountAvailable -= strategyModel.getBrokerCommission();
            if (amountAvailable < 0) {
              continue;
            }
            TransactionModel transaction = TransactionModelImpl.getBuilder()
                    .date(date)
                    .brokerCommission(strategyModel.getBrokerCommission())
                    .exchange(stockModel.getExchangeName())
                    .price(stockAllDatePrices.get(stockModel).get(date).get(dateCount))
                    .qty(amountAvailable / stockAllDatePrices.get(stockModel).get(date)
                            .get(dateCount))
                    .transactionSource(strategyModel.getStrategyName())
                    .build();
            if (!portfolioModel.getPortfolioElements().containsKey(stockModel.getStockTicker())) {
              portfolioModel.addPortfolioElement(PortfolioElementModelImpl.getBuilder()
                      .stockModel(stockModel).build());
            }
            portfolioModel.modifyPortfolio(stockModel, transaction);
          }
        }
      }

    }
  }

  @Override
  public void run(PortfolioModel portfolioModel) {
    Set<Date> applicableDates = new HashSet<>();
    Date lastRunDate = getLastRunDate(this.getStrategyName(), portfolioModel);
    if (lastRunDate == null) {
      applicableDates.add(this.getStartDate());
    }
    lastRunDate = lastRunDate == null ? this.getStartDate() : lastRunDate;
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(lastRunDate);
    calendar.add(Calendar.DATE, this.getDateFrequency());

    Date maxDate = lastRunDate;
    while (calendar.getTime().compareTo(
            new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)) <= 0) {
      if (this.getEndDate() != null
              && calendar.getTime().compareTo(this.getEndDate()) > 0) {
        break;
      }
      applicableDates.add(calendar.getTime());
      if (calendar.getTime().compareTo(maxDate) > 0) {
        maxDate = calendar.getTime();
      }
      calendar.add(Calendar.DATE, this.getDateFrequency());
    }
    if (applicableDates.size() > 0) {
      createTransactions(getPricesDatesList(applicableDates,
              this, maxDate), this, portfolioModel);
    }
  }


  @Override
  public double getBrokerCommission() {
    return brokerCommission;
  }

  @Override
  public Map<StockModel, Double> getStockMapPercent() {
    return this.stockMapPercent;
  }

  /**
   * Builder class to build an object of dollar cost averaging model.
   */
  public static class DollarCostAveragingBuilder {

    private String strategyName;
    private double amount;

    private Date startDate;

    private Date endDate;

    private int dateFrequency;

    private double brokerCommission;

    private Map<StockModel, Double> stockMapPercent;

    private DollarCostAveragingBuilder() {
      this.strategyName = "";
      this.amount = 0;
      this.startDate = null;
      this.endDate = null;
      this.dateFrequency = 0;
      this.stockMapPercent = new HashMap<>();
      this.brokerCommission = 0.0;

    }

    public DollarCostAveragingBuilder amount(Double amount) {
      this.amount = amount;
      return this;
    }

    public DollarCostAveragingBuilder brokerCommission(Double brokerCommission) {
      this.brokerCommission = brokerCommission;
      return this;
    }

    public DollarCostAveragingBuilder strategyName(String strategyName) {
      this.strategyName = strategyName;
      return this;
    }

    public DollarCostAveragingBuilder startDate(Date startDate) {
      this.startDate = startDate;
      return this;
    }

    public DollarCostAveragingBuilder endDate(Date endDate) {
      this.endDate = endDate;
      return this;
    }

    public DollarCostAveragingBuilder dateFrequency(int dateFrequency) {
      this.dateFrequency = dateFrequency;
      return this;
    }

    public DollarCostAveragingBuilder stockMapPercent(Map<StockModel, Double> stockMapPercent) {
      this.stockMapPercent = stockMapPercent;
      return this;
    }

    public DollarCostAveragingBuilder readXML(String path, String strategyName)
            throws XPathExpressionException,
            IOException, ParserConfigurationException, SAXException, ParseException {
      DataParser<DollarCostAveragingBuilder> dataParserTransaction = new XMLParserImpl<>();
      return dataParserTransaction.getDollarCostAveragingBuilder(path, strategyName);
    }

    /**
     * Method to return a dollar cost averaging object, using the builder object.
     * @return dollar cost averaging impl object only.
     */
    public DollarCostAveragingImpl build() {
      return new DollarCostAveragingImpl(this.strategyName, this.amount,
              this.startDate, this.endDate,
              this.dateFrequency, this.brokerCommission, this.stockMapPercent);
    }

  }


}
