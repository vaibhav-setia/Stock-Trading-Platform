package models;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements UserStockModel.
 * It contains all the users in system in a user set model.
 * It also stores the set of stocks as a stock set model.
 */
public class UserStockModelImpl implements UserStockModel {

  private final StockSetModel stockSetModel;
  private UserSetModel userSetModel;

  /**
   * Public constructor to initialise a UserStockModel class.
   */
  public UserStockModelImpl() {
    this.stockSetModel = new StockSetModelImpl(new HashMap<>());
    this.userSetModel = new UserSetModelImpl(new ArrayList<>());
  }

  /**
   * Public constructor to initialise a UserStockModel class.
   *
   * @param userSetModel  user set model to be assigned.
   * @param stockSetModel stock set model to be assigned.
   */
  public UserStockModelImpl(UserSetModel userSetModel, StockSetModel stockSetModel) {
    this.stockSetModel = stockSetModel;
    this.userSetModel = userSetModel;
  }

  @Override
  public UserSetModel getUserSetModel() {
    return this.userSetModel;
  }

  @Override
  public void setUserSetModel(UserSetModel userSetModel) {
    this.userSetModel = userSetModel;
  }

  @Override
  public StockSetModel getStockSetModel() {
    return this.stockSetModel;
  }
}
