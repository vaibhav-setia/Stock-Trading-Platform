package models;


import java.util.Date;

/**
 * This class implements StockDataModel and all its functionality.
 * This class stores all the price data for a {@link StockModel} class
 * for a particular date.
 */
public class StockDataModelImpl implements StockDataModel {

  private final StockModel stockModel;
  private final Date date;
  private final double closePrice;
  private final double openPrice;
  private final double highPrice;
  private final double lowPrice;
  private final long quantity;

  /**
   * Public constructor to assign a Stock Data Model
   * values to a Stock Set Data Model.
   *
   * @param stockModel stock for which all data is being stored.
   * @param date       date for which the data is being stored.
   * @param openPrice  opening price of the stock in market.
   * @param highPrice  highest price on that date of stock.
   * @param lowPrice   lowest price of stock on that day.
   * @param closePrice closing price of stock on that day.
   * @param quantity   quantity of stock traded that day.
   */
  public StockDataModelImpl(StockModel stockModel, Date date, double openPrice,
                            double highPrice, double lowPrice, double closePrice, long quantity) {
    this.stockModel = stockModel;
    this.date = date;
    this.closePrice = closePrice;
    this.lowPrice = lowPrice;
    this.openPrice = openPrice;
    this.highPrice = highPrice;
    this.quantity = quantity;
  }

  /**
   * Static class to get a builder to build
   * Stock Data Model class.
   *
   * @return a new builder object for Stock Data Model.
   */
  public static StockDataModelBuilder getBuilder() {
    return new StockDataModelBuilder();
  }

  @Override
  public StockModel getStockModel() {
    return this.stockModel;
  }

  @Override
  public double getClosePrice() {
    return this.closePrice;
  }

  @Override
  public double getHighPrice() {
    return this.highPrice;
  }

  @Override
  public double getLowPrice() {
    return this.lowPrice;
  }

  @Override
  public double getOpenPrice() {
    return this.openPrice;
  }

  @Override
  public long getQuantity() {
    return this.quantity;
  }

  @Override
  public Date getDate() {
    return this.date;
  }

  /**
   * Builder helper class to build an object of Stock Data Model class.
   * It contains functionality to set different fields of Stock Data Model.
   */
  public static class StockDataModelBuilder {
    private StockModel stockModel;
    private Date date;
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double closePrice;
    private long quantity;

    /**
     * Initialise a new builder object with no data.
     */
    private StockDataModelBuilder() {
      this.stockModel = null;
      this.date = null;
      this.openPrice = 0;
      this.closePrice = 0;
      this.highPrice = 0;
      this.lowPrice = 0;
      this.quantity = 0;
    }

    /**
     * Set stockModel of builder object to be assigned
     * to Stock Data Model.
     *
     * @param stockModel stock to be assigned.
     * @return the resulting builder object with stockModel assigned.
     */
    public StockDataModelBuilder stockModel(StockModel stockModel) {
      this.stockModel = stockModel;
      return this;
    }

    /**
     * Set timestamp of builder object to be assigned
     * to Stock Data Model.
     *
     * @param timestamp timestamp to be assigned.
     * @return the resulting builder object with timestamp assigned.
     */
    public StockDataModelBuilder timestamp(Date timestamp) {
      this.date = timestamp;
      return this;
    }

    /**
     * Set openPrice of builder object to be assigned
     * to Stock Data Model.
     *
     * @param openPrice openPrice to be assigned.
     * @return the resulting builder object with openPrice assigned.
     */
    public StockDataModelBuilder openPrice(double openPrice) {
      this.openPrice = openPrice;
      return this;
    }

    /**
     * Set lowPrice of builder object to be assigned
     * to Stock Data Model.
     *
     * @param lowPrice openPrice to be assigned.
     * @return the resulting builder object with lowPrice assigned.
     */
    public StockDataModelBuilder lowPrice(double lowPrice) {
      this.lowPrice = lowPrice;
      return this;
    }

    /**
     * Set closePrice of builder object to be assigned
     * to Stock Data Model.
     *
     * @param closePrice closePrice to be assigned.
     * @return the resulting builder object with closePrice assigned.
     */
    public StockDataModelBuilder closePrice(double closePrice) {
      this.closePrice = closePrice;
      return this;
    }

    /**
     * Set highPrice of builder object to be assigned
     * to Stock Data Model.
     *
     * @param highPrice highPrice to be assigned.
     * @return the resulting builder object with highPrice assigned.
     */
    public StockDataModelBuilder highPrice(double highPrice) {
      this.highPrice = highPrice;
      return this;
    }

    /**
     * Set quantity of builder object to be assigned
     * to Stock Data Model.
     *
     * @param quantity quantity to be assigned.
     * @return the resulting builder object with quantity assigned.
     */
    public StockDataModelBuilder quantity(long quantity) {
      this.quantity = quantity;
      return this;
    }

    /**
     * Set date of builder object to be assigned
     * to Stock Data Model.
     *
     * @param date date to be assigned.
     * @return the resulting builder object with date assigned.
     */
    public StockDataModelBuilder date(Date date) {
      this.date = date;
      return this;
    }

    /**
     * Build a Stock Data Model builder object to get Stock Data Model.
     *
     * @return a new Stock Data Model object with all the attributes updated.
     */
    public StockDataModelImpl build() {
      return new StockDataModelImpl(this.stockModel, this.date, this.openPrice,
              this.highPrice, this.lowPrice, this.closePrice, this.quantity);
    }


  }


}
