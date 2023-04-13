package models;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import utils.DataParser;
import utils.DataPersister;
import utils.XMLParserImpl;
import utils.XMLWriterImpl;

/**
 * Transaction model class which contains all the data regarding a transaction.
 * It implements all the methods of transaction model. A negative quantity means
 * there has been a sell transaction.
 */
public class TransactionModelImpl implements TransactionModel {

  private final Date transactionDate;

  private final double brokerCommission;

  private final double qty;

  private final double price;

  private final String exchange;

  private final String transactionSource;

  /**
   * Public constructor to assign transaction details to a transaction object.
   *
   * @param transactionDate   date on which transaction occurred.
   * @param brokerCommission  commission given to broker for a transaction.
   * @param qty               quantity of stock involved in transaction.
   * @param price             price on which stock was traded.
   * @param exchange          stock exchange name where transaction took place.
   * @param transactionSource source of creating this transaction.
   */
  public TransactionModelImpl(Date transactionDate, double brokerCommission,
                              double qty, double price, String exchange, String transactionSource) {
    this.transactionDate = transactionDate;
    this.brokerCommission = brokerCommission;
    this.qty = qty;
    this.price = price;
    this.exchange = exchange;
    this.transactionSource = transactionSource;
  }

  /**
   * Static class to get a builder to build
   * Transaction Model class.
   *
   * @return a new builder object for Transaction Model.
   */
  public static TransactionBuilder getBuilder() {
    return new TransactionBuilder();
  }

  @Override
  public Date getTransactionDate() {
    return transactionDate;
  }

  @Override
  public double getBrokerCommission() {
    return brokerCommission;
  }

  @Override
  public double getQty() {
    return qty;
  }

  @Override
  public double getPrice() {
    return price;
  }

  @Override
  public String getExchange() {
    return exchange;
  }

  @Override
  public String getTransactionSource() {
    return transactionSource;
  }


  @Override
  public void generateXML(String path, int id) throws IOException,
          ParserConfigurationException, TransformerException {
    DataPersister persister = new XMLWriterImpl();
    persister.writeTransactionModel(path, id, this);
  }

  /**
   * Builder helper class to build an object of Transaction Model class.
   * It contains functionality to set different fields of transaction Model.
   */
  public static class TransactionBuilder {
    private Date transactionDate;

    private double brokerCommission;

    private double qty;

    private double price;

    private String exchange;

    private String transactionSource;

    /**
     * Initialise a new builder object with
     * empty fields for a transaction.
     */
    private TransactionBuilder() {
      this.transactionDate = null;
      this.brokerCommission = 0;
      this.qty = 0;
      this.price = 0;
      this.exchange = "";
      this.transactionSource = "Manual";
    }

    /**
     * Set transaction date to the builder object of transaction model.
     *
     * @param transactionDate transaction date to be set to builder object.
     * @return the builder object after assigning a transaction date.
     */
    public TransactionBuilder date(Date transactionDate) {
      this.transactionDate = transactionDate;
      return this;
    }

    /**
     * Set transactionSource to the builder object of transaction model.
     *
     * @param transactionSource transactionSource to be set to builder object.
     * @return the builder object after assigning a transactionSource.
     */
    public TransactionBuilder transactionSource(String transactionSource) {
      this.transactionSource = transactionSource;
      return this;
    }

    /**
     * Set brokerCommission to the builder object of transaction model.
     *
     * @param brokerCommission brokerCommission to be set to builder object.
     * @return the builder object after assigning a brokerCommission.
     */
    public TransactionBuilder brokerCommission(double brokerCommission) {
      this.brokerCommission = brokerCommission;
      return this;
    }

    /**
     * Set quantity to the builder object of transaction model.
     *
     * @param qty quantity to be set to builder object.
     * @return the builder object after assigning a quantity.
     */
    public TransactionBuilder qty(double qty) {
      this.qty = qty;
      return this;
    }

    /**
     * Set price to the builder object of transaction model.
     *
     * @param price price to be set to builder object.
     * @return the builder object after assigning a price.
     */
    public TransactionBuilder price(double price) {
      this.price = price;
      return this;
    }

    /**
     * Set exchange to the builder object of transaction model.
     *
     * @param exchange exchange to be set to builder object.
     * @return the builder object after assigning a exchange.
     */
    public TransactionBuilder exchange(String exchange) {
      this.exchange = exchange;
      return this;
    }

    /**
     * Constructs a transaction model builder object by reading the contents from xml file
     * provided.
     *
     * @param path relative path to source folder from where the xml file needs to be read.
     * @param id   transaction id to specify folder to read from.
     * @return an object of PortfolioModelBuilder with all the attributes assigned from XML file.
     * @throws IOException                  if xml is not correctly read from file.
     * @throws ParserConfigurationException if the xml parser is configured incorrectly.
     * @throws SAXException                 exception thrown if xml violates integrity constraints.
     * @throws XPathExpressionException     if the Xpath to filter xml is incorrect.
     * @throws ParseException               if xml string is not correctly parsed as per contract.
     */
    public TransactionBuilder readXML(String path, int id) throws XPathExpressionException,
            IOException, ParserConfigurationException, SAXException, ParseException {
      DataParser<TransactionBuilder> dataParserTransaction = new XMLParserImpl<>();
      return dataParserTransaction.getTransactionBuilder(path, id);
    }

    /**
     * Build a Transaction Model builder object to get Transaction Model.
     *
     * @return a new Transaction Model object with all the transaction details.
     */
    public TransactionModelImpl build() {
      return new TransactionModelImpl(this.transactionDate, this.brokerCommission,
              this.qty, this.price, this.exchange, this.transactionSource);
    }
  }
}
