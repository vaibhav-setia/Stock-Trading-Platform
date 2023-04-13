package views;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import controller.Features;
import models.Strategy;
import utils.ChartPlot;

/**
 * Interface with all the GUI methods which can be called upon.
 */
public interface GUIView {
  /**
   * Method to add all the features.
   *
   * @param features features object with all features.
   */
  void addFeatures(Features features);

  /**
   * Method to switch layout.
   *
   * @param newLayout the new layout to switch to.
   */
  void switchLayout(String newLayout);

  /**
   * Method to pack the layout.
   */
  void packLayout();

  /**
   * Method to open the main menu.
   *
   * @param userName   username to display.
   * @param userID     userid to display.
   * @param portfolios list of portfolios to display.
   */
  void openMainMenuLayout(String userName, int userID, List<String> portfolios);

  /**
   * Method to get the userId.
   *
   * @return userId as a string only.
   */
  String getUserId();

  /**
   * Method to show wrong userId popup.
   */
  void showWrongUserIDPopup();

  /**
   * Method to show initial loading failed popup.
   */
  void showInitializationFailedPopup();

  /**
   * Method to show incorrect strategy popup.
   */
  void showIncorrectStrategyPopup();

  /**
   * Method to show no user found popup.
   */
  void showNoUserFoundPrompt();

  /**
   * Method to get the userName.
   *
   * @return the userName as a string only.
   */
  String getUserName();

  /**
   * Method to show no special characters allowed popup.
   */
  void showNoSpecialCharsAllowedPopup();

  /**
   * Method to show error message popup while storing in database.
   */
  void showDBStoreErrorMessage();

  /**
   * Method to request creation of portfolio.
   */
  void requestCreatePortfolio();

  /**
   * Method to open portfolio details.
   *
   * @param portfolioName name of portfolio to be opened.
   * @param composition   composition of portfolio as list of strings.
   */
  void openPortfolioDetails(String portfolioName, List<List<String>> composition);

  /**
   * Method to get date input.
   *
   * @return input date as string only.
   */
  String getDateInput();

  /**
   * Method to get start and end date input.
   *
   * @return start and end date as string array object only.
   */
  String[] getStartEndDateInput();

  /**
   * Method to show wrong date input popup.
   */
  void showWrongDateInputPopup();

  /**
   * Method to show value of portfolio on a date.
   *
   * @param date  date on which value to be displayed.
   * @param value value to be shown.
   */
  void showValueOnADate(Date date, double value);

  /**
   * Method to show cost basis of portfolio on a date.
   *
   * @param date  date on which value to be displayed.
   * @param value value to be shown.
   */
  void showCostBasis(Date date, double value);

  /**
   * Method to show composition of portfolio.
   *
   * @param composition list of string lists with all composition details.
   */
  void showComposition(List<List<String>> composition);

  /**
   * Method to show incorrect quantity popup.
   */
  void showIncorrectQuantityPopup();

  /**
   * Method to show the bar chart.
   *
   * @param startDate     start date of bar chart.
   * @param endDate       end date of bar chart.
   * @param portfolioName name of portfolio.
   * @param chartPlot     chartPlot of the graph.
   */
  void showBarChart(String startDate, String endDate, String portfolioName, ChartPlot chartPlot);

  /**
   * Method to show incorrect weightage popup.
   */
  void showIncorrectWeightPopup();

  /**
   * Method to show incorrect commission popup.
   */
  void showIncorrectCommissionPopup();

  /**
   * Method to show incorrect amount popup.
   */
  void showIncorrectAmountPopup();

  /**
   * Method to show incorrect frequency popup.
   */
  void showIncorrectFrequencyPopup();

  /**
   * Method to request more stock input.
   */
  void requestMoreStockInput();

  /**
   * Method to request more stock input for strategy.
   */
  void requestMoreStockInputForStrategy();

  /**
   * Method to show only alphanumeric portfolio name allowed popup.
   */
  void showPortfolioNameShouldOnlyBeAlphaNumericPopup();

  /**
   * Method to show only alphanumeric strategy name allowed popup.
   */
  void showStrategyNameShouldOnlyBeAlphaNumericPopup();

  /**
   * Method to show popup portfolio name already exists.
   */
  void showPortfolioNameAlreadyExists();

  /**
   * Method to show closeAddFlexiblePortfolioWithoutStrategyPopup.
   *
   * @param portfolios list of portfolios.
   */
  void closeAddFlexiblePortfolioWithoutStrategyPopup(List<String> portfolios);

  /**
   * Method to show modify portfolio popup.
   *
   * @param portfolioName     name of portfolio.
   * @param portfolioElements map of portfolio elements.
   * @param stocks            list of stocks.
   */
  void showModifyPortfolioPopup(String portfolioName, Map<String,
          Double> portfolioElements, List<String> stocks);

  /**
   * Method to show no price popup.
   *
   * @param stockName name of stock.
   */
  void showNoPriceOnDatePopup(String stockName);

  /**
   * Method to show close add strategy popup.
   */
  void closeAddStrategyPopup();

  /**
   * Method to close modify portfolio popup.
   */
  void closeModifyPortfolioPopup();

  /**
   * Method to show download portfolio popup.
   *
   * @param path path where file has been downloaded.
   */
  void showDownloadPortfolioPopup(String path);

  /**
   * Method to show failed to save portfolio popup.
   */
  void showFailedToSavePortfolio();

  /**
   * Method to show strategy name already exists popup.
   */
  void showSameStrategyNameAlreadyExist();

  /**
   * Method to show processing popup.
   */
  void showProcessingPopup();

  /**
   * Method to close processing popup.
   */
  void closeProcessingPopup();

  /**
   * Method to request create flexible portfolio without strategy.
   *
   * @param stocks list of stocks available.
   */
  void requestCreateFlexiblePortfolioWithoutStrategy(List<String> stocks);

  /**
   * Method to show request create flexible portfolio with strategy.
   *
   * @param stocks        list of stocks available.
   * @param strategyTypes list of strategies available.
   */
  void requestCreateFlexiblePortfolioWithStrategy(List<String> stocks,
                                                  List<Strategy> strategyTypes);

  /**
   * Method to show request upload portfolio via xml.
   */
  void requestCreateFlexiblePortfolioWithXML();

  /**
   * Method to show popup for upload xml.
   *
   * @param portfolioNames list of portfolio names.
   */
  void closeAddFlexiblePortfolioWithUploadXMLPopup(List<String> portfolioNames);

  /**
   * Method to show incorrect upload popup.
   */
  void showIncorrectFileUploadPopup();

  /**
   * Method to set the selected file.
   *
   * @param file file to be set.
   */
  void setSelectedFile(File file);

  /**
   * Method to close portfolio popup.
   *
   * @param portfolioNames list of portfolio names.
   */
  void closeAddFlexiblePortfolioWithStrategyPopup(List<String> portfolioNames);

  /**
   * Method to clear login page inputs.
   */
  void clearLoginPageInputs();

  /**
   * Method to clear portfolio details page.
   */
  void clearPortfolioDetailsPage();

  /**
   * Method to modify portfolio success popup.
   */
  void showModifyPortfolioSuccessPopup();

  /**
   * Method to show add strategy success popup.
   */
  void showAddStrategySuccessPopup();

  /**
   * Method to add request strategy.
   *
   * @param portfolioName name of portfolio.
   * @param stocks        list of stocks.
   * @param strategyTypes list of strategies.
   */
  void requestAddStrategy(String portfolioName, List<String> stocks, List<Strategy> strategyTypes);
}
