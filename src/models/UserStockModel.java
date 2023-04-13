package models;

/**
 * Interface methods applicable to different models being further used and interacted
 * by the controller indirectly.
 */
public interface UserStockModel {
  /**
   * Get the user set model object which consists a list of all the user models.
   *
   * @return a user set model object only.
   */
  UserSetModel getUserSetModel();

  /**
   * Initial setup of the user set model.
   *
   * @param userSetModel user set model object to be assigned.
   */
  void setUserSetModel(UserSetModel userSetModel);

  /**
   * Get the stock set model object which consists of set of all the stocks available.
   *
   * @return a stock set model object only.
   */
  StockSetModel getStockSetModel();
}
