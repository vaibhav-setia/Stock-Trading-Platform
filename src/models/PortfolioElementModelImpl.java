package models;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import utils.DataParser;
import utils.DataPersister;
import utils.XMLParserImpl;
import utils.XMLWriterImpl;

/**
 * This class implements Portfolio Element Model and all its functionality.
 * This class stores various details of a particular portfolio element
 * for a particular portfolio for a user. A portfolio element consists of
 * the stock, total quantity of that stock, latest transaction date and
 * the average price of the stock.
 */
public class PortfolioElementModelImpl implements PortfolioElementModel {

  protected final StockModel stockModel;

  protected double avgPrice;

  protected double totalQuantity;

  protected Date transactionDate;

  protected List<TransactionModel> transactionList;

  /**
   * Constructor to assign values to various attributes of the Portfolio Element Model.
   *
   * @param stockModel      stock which needs to be assigned to the portfolio element.
   * @param avgPrice        he average price of stock
   * @param transactionDate latest transaction date of the stock.
   * @param totalQuantity   total quantity of that stock in a particular portfolio.
   * @param transactionList list of all the transactions in a portfolio element model.
   */
  public PortfolioElementModelImpl(StockModel stockModel,
                                   double avgPrice, double totalQuantity,
                                   Date transactionDate, List<TransactionModel> transactionList) {
    this.stockModel = stockModel;
    this.avgPrice = Math.round(avgPrice * 100.0) / 100.0;
    this.totalQuantity = Math.round(totalQuantity * 100.0) / 100.0;
    this.transactionDate = transactionDate;
    this.transactionList = transactionList;
  }

  /**
   * Static class to get a builder to build
   * Portfolio Element Model class.
   *
   * @return a new builder object for Portfolio Element.
   */
  public static PortfolioElementModelBuilder getBuilder() {
    return new PortfolioElementModelBuilder();
  }

  @Override
  public StockModel getStockModel() {
    return this.stockModel;
  }

  @Override
  public double getStockCurrentAvgPrice() {
    return this.avgPrice;
  }

  @Override
  public Date getTransactionDate() {
    return this.transactionDate;
  }

  @Override
  public double getStockTotalQuantity() {
    return this.totalQuantity;
  }

  @Override
  public void generateXML(String path) throws IOException, ParserConfigurationException,
          TransformerException {

    DataPersister persister = new XMLWriterImpl();
    persister.writePortfolioElement(path, this);
  }

  @Override
  public double getPortfolioElementValueOnDate(Date date) throws IllegalArgumentException {
    if (date.compareTo(new Date()) >= 0) {
      throw new IllegalArgumentException();
    }
    double value = 0;
    double price = this.stockModel.getPriceOnADate(date);
    for (TransactionModel transactionModel : transactionList) {
      if (transactionModel.getTransactionDate().compareTo(date) <= 0) {
        value += transactionModel.getQty() * price;
      }
    }
    return Math.round(value * 100.0) / 100.0;
  }

  @Override
  public void addTransaction(TransactionModel transaction) {
    double avgPrice = ((this.avgPrice * this.totalQuantity)
            + (transaction.getPrice() * transaction.getQty()));
    this.totalQuantity = this.totalQuantity + transaction.getQty();
    this.avgPrice = this.totalQuantity == 0 ? 0 : avgPrice / this.totalQuantity;
    this.avgPrice = Math.round(this.avgPrice * 100.00) / 100.00;
    if (this.transactionDate != null) {
      this.transactionDate = transaction.getTransactionDate().compareTo(this.transactionDate)
              > 0 ? transaction.getTransactionDate() : this.transactionDate;
    } else {
      this.transactionDate = transaction.getTransactionDate();
    }
    this.transactionList.add(transaction);
  }

  @Override
  public double getPortfolioCostBasis(Date date) throws IllegalArgumentException {
    if (date.compareTo(new Date()) >= 0) {
      throw new IllegalArgumentException();
    }

    double value = 0;
    for (TransactionModel transactionModel : transactionList) {
      if (transactionModel.getTransactionDate().compareTo(date) <= 0) {
        if (transactionModel.getQty() > 0) {
          value += transactionModel.getQty() * transactionModel.getPrice();
        }
        value += transactionModel.getBrokerCommission();
      }
    }
    return Math.round(value * 100.0) / 100.0;

  }

  @Override
  public List<TransactionModel> getAllTransactions() {
    return this.transactionList;
  }

  @Override
  public List<String> getCompositionOnDate(Date date) throws IllegalArgumentException {
    if (date.compareTo(new Date()) > 0) {
      throw new IllegalArgumentException();
    }

    List<String> composition = new ArrayList<>();
    composition.add(this.stockModel.getStockTicker());
    composition.add(this.stockModel.getStockName());
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date transactionDate = null;
    double qty = 0;
    double price = 0;
    boolean transactionFound = false;
    for (TransactionModel transactionModel : transactionList) {
      if (transactionModel.getTransactionDate().compareTo(date) <= 0) {
        transactionFound = true;
        if (transactionDate == null) {
          transactionDate = transactionModel.getTransactionDate();
        } else {
          transactionDate = transactionModel.getTransactionDate()
                  .compareTo(transactionDate) > 0 ? transactionModel
                  .getTransactionDate() : transactionDate;
        }
        double curValue = qty * price;
        if (qty + transactionModel.getQty() != 0) {
          curValue = (curValue + transactionModel.getQty()
                  * transactionModel.getPrice()) / (qty + transactionModel.getQty());
        } else {
          curValue = 0;
        }
        qty += transactionModel.getQty();
        price = curValue;
      }
    }
    composition.add(String.valueOf(Math.round(qty * 100.0) / 100.0));
    composition.add(String.valueOf(Math.round(price * 100.0) / 100.0));
    if (transactionDate != null) {
      composition.add(format.format(transactionDate));
    } else {
      composition.add("NULL");
    }
    if (transactionFound) {
      return composition;
    }
    return new ArrayList<>();
  }

  /**
   * Builder helper class to build an object of Portfolio Element Model class.
   * It contains functionality to set different fields of Portfolio Element Model.
   */
  public static class PortfolioElementModelBuilder {

    private StockModel stockModel;

    private double avgPrice;

    private double totalQuantity;

    private Date transactionDate;

    private List<TransactionModel> transactionList;

    /**
     * Initialise a new builder object with 0 price, quantity
     * and transaction date as null.
     */
    private PortfolioElementModelBuilder() {
      this.stockModel = null;
      this.avgPrice = 0;
      this.totalQuantity = 0;
      this.transactionDate = null;
      this.transactionList = new ArrayList<>();
    }

    /**
     * Set portfolio element stock model to the builder object of portfolio element model.
     *
     * @param stockModel stock model to be set to builder object.
     * @return the builder object after assigning a stock model.
     */
    public PortfolioElementModelBuilder stockModel(StockModel stockModel) {
      this.stockModel = stockModel;
      return this;
    }

    /**
     * Set avgPrice to the builder object of portfolio element model.
     *
     * @param avgPrice to be set to builder object.
     * @return the builder object after assigning avgPrice.
     */
    public PortfolioElementModelBuilder avgPrice(double avgPrice) {
      this.avgPrice = avgPrice;
      return this;
    }

    /**
     * Set totalQuantity to the builder object of portfolio element model.
     *
     * @param totalQuantity to be set to builder object.
     * @return the builder object after assigning totalQuantity.
     */
    public PortfolioElementModelBuilder totalQuantity(double totalQuantity) {
      this.totalQuantity = totalQuantity;
      return this;
    }

    /**
     * Set transactionList to the builder object of portfolio element model.
     *
     * @param transactionList transactionList to be set to builder object.
     * @return the builder object after assigning transactionList.
     */
    public PortfolioElementModelBuilder transactionList(List<TransactionModel> transactionList) {
      this.transactionList = transactionList;
      return this;
    }

    /**
     * Set transactionDate to the builder object of portfolio element model.
     *
     * @param transactionDate to be set to builder object.
     * @return the builder object after assigning transactionDate.
     */
    public PortfolioElementModelBuilder transactionDate(Date transactionDate) {
      this.transactionDate = transactionDate;
      return this;
    }

    /**
     * Constructs a Portfolio Element Model builder object by reading the contents from xml file
     * provided.
     *
     * @param path   relative path to source folder from where the xml file needs to be read.
     * @param ticker name of stock symbol to specify folder to read from.
     * @return an object of Portfolio Element Model Builder with all the attributes assigned
     *         from XML file.
     * @throws IOException                  if xml is not correctly read from file.
     * @throws ParserConfigurationException if the xml parser is configured incorrectly.
     * @throws SAXException                 exception thrown if xml violates integrity constraints.
     * @throws XPathExpressionException     if the Xpath to filter xml is incorrect.
     * @throws ParseException               if xml string is not correctly parsed as per contract.
     */
    public PortfolioElementModelBuilder readXML(String path, String ticker)
            throws XPathExpressionException, IOException, ParserConfigurationException,
            SAXException, ParseException {
      DataParser<PortfolioElementModelBuilder> dataParserPortfolioElementModel
              = new XMLParserImpl<>();
      return dataParserPortfolioElementModel.getPortfolioElementModelBuilder(path, ticker);
    }

    /**
     * Build a Portfolio Element Model builder object to get Portfolio Element Model.
     *
     * @return a new Portfolio Element Model object with stock model,
     *         avg price, transaction date and total quantity updated.
     */
    public PortfolioElementModelImpl build() {
      return new PortfolioElementModelImpl(this.stockModel, this.avgPrice,
              this.totalQuantity, this.transactionDate, new ArrayList<>(this.transactionList));
    }
  }


}
