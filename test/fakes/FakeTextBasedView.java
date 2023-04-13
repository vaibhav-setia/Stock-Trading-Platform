package fakes;

import java.util.Date;
import java.util.List;

import utils.ChartPlot;
import views.TextBasedView;

/**
 * A Fake Text Based View created only for the purpose of testing. This fake text based view just
 * logs the calls made to its method. This implementation is helpful to mock any real
 * implementation.
 */
public class FakeTextBasedView implements TextBasedView {
  private final StringBuilder log;

  /**
   * Construct a fake text based view given a string builder for logging.
   *
   * @param log the string builder used for logging the method calls.
   */
  public FakeTextBasedView(StringBuilder log) {
    this.log = log;
  }

  /**
   * A fake welcome dialog method which just logs the call made.
   */
  @Override
  public void showWelcomeDialog() {
    log.append("[Text Based View]Show welcome dialog\n");
  }

  /**
   * A fake create user dialog method which just logs the call made.
   */
  @Override
  public void showCreateUserDialog() {
    log.append("[Text Based View]Show create user dialog\n");
  }

  /**
   * A fake show main menu dialog method which just logs the call made.
   */
  @Override
  public void showMainMenu() {
    log.append("[Text Based View]Show main menu").append("\n");
  }

  /**
   * A fake create show portfolio name method which just logs the call made.
   */
  @Override
  public void showAddPortfolioNamePrompt() {
    log.append("[Text Based View]Show add portfolio name prompt\n");
  }

  /**
   * A fake add portfolio options dialog method which just logs the call made.
   */
  @Override
  public void showAddPortfolioOptions() {
    log.append("[Text Based View]Show portfolio options\n");
  }

  /**
   * A fake show total number of stocks to be added prompt method which just logs the call made.
   */
  @Override
  public void showTotalNumberOfStocksToBeAddedPrompt() {
    log.append("[Text Based View]Show total number of stocks to be added prompt\n");
  }

  /**
   * A fake show enter stock ticker prompt dialog method which just logs the call made.
   */
  @Override
  public void showEnterStockTickerPrompt() {
    log.append("[Text Based View]Show enter stock ticker prompt\n");
  }

  /**
   * A fake show enter quantity prompt method which just logs the call made.
   */
  @Override
  public void showEnterQuantityPrompt() {
    log.append("[Text Based View]Show enter quantity prompt\n");
  }

  @Override
  public void showEnterCommissionPrompt() {
    log.append("[Text Based View]Show enter commission prompt\n");
  }

  /**
   * A fake enter file path prompt method which just logs the call made.
   */
  @Override
  public void showEnterFilePathPrompt() {
    log.append("[Text Based View]Show enter file path prompt\n");
  }

  /**
   * A fake show no portfolios dialog method which just logs the call made.
   */
  @Override
  public void showNoPortfoliosPrompt() {
    log.append("[Text Based View]Show no portfolios prompt\n");
  }

  /**
   * A fake welcome user dialog method which just logs the call made.
   */
  @Override
  public void welcomeUserPrompt(String userName, int userId) {
    log.append("[Text Based View]Show welcome user prompt: ")
            .append(userName).append("\n");
  }

  /**
   * A fake no special characters allowed dialog method which just logs the call made.
   */
  @Override
  public void noSpecialCharactersAllowedPrompt() {
    log.append("[Text Based View]Show no special characters allowed prompt\n");
  }

  /**
   * A fake show no data found dialog method which just logs the call made.
   */
  @Override
  public void showNoDataFoundForDatePrompt() {
    log.append("[Text Based View]Show no data for date prompt\n");
  }

  /**
   * A fake save portfolio success dialog method which just logs the call made.
   */
  @Override
  public void savePortfolioSuccessMessage(String path) {
    log.append("[Text Based View]Show save portfolio success message prompt")
            .append("\n");
  }

  /**
   * A fake show portfolio value dialog method which just logs the call made.
   */
  @Override
  public void showPortfolioValueForDate(Date date, double value) {
    log.append("[Text Based View]Show portfolio value for date prompt: ")
            .append(date.toString()).append(" ").append(value).append("\n");
  }

  /**
   * A fake show select portfolio dialog method which just logs the call made.
   */
  @Override
  public void showSelectPortfolioPrompt(List<String> portfolioModelList) {
    log.append("[Text Based View]Show select portfolio prompt: ")
            .append(portfolioModelList.size()).append("\n");
  }

  /**
   * A fake show portfolio details dialog method which just logs the call made.
   */
  @Override
  public void showPortfolioDetails(String portfolioModelName, List<List<String>> portfolioElement) {
    log.append("[Text Based View]Show portfolio details prompt: ")
            .append(portfolioModelName).append("\n");
  }

  /**
   * A fake wrong input dialog method which just logs the call made.
   */
  @Override
  public void showWrongInputDialog() {
    log.append("[Text Based View]Show wrong input prompt\n");
  }

  /**
   * A fake show portfolio name dialog method which just logs the call made.
   */
  @Override
  public void showPortfolioNameAlreadyExistsPrompt() {
    log.append("[Text Based View]Show portfolio name already exists prompt\n");
  }

  /**
   * A fake show no data available for date dialog method which just logs the call made.
   */
  @Override
  public void showNoDataAvailableForOneOrMoreStockForTheDataEntered() {
    log.append("[Text Based View]Show no data available for one or more"
            + " stocks for the date entered prompt\n");
  }

  /**
   * A fake show enter user id dialog method which just logs the call made.
   */
  @Override
  public void showEnterUserIDDialog() {
    log.append("[Text Based View]Show enter user id prompt\n");
  }

  /**
   * A fake enter date dialog method which just logs the call made.
   */
  @Override
  public void showEnterDatePrompt() {
    log.append("[Text Based View]Show enter date prompt\n");
  }

  /**
   * A fake no user found dialog method which just logs the call made.
   */
  @Override
  public void showNoUserFoundPrompt() {
    log.append("[Text Based View]Show no user found prompt\n");
  }

  /**
   * A fake initialization failed prompt dialog method which just logs the call made.
   */
  @Override
  public void showInitializationFailedPrompt() {
    log.append("[Text Based View]Show initialization failed prompt\n");
  }

  /**
   * A fake add success message dialog method which just logs the call made.
   */
  @Override
  public void showPortfolioAddSuccessMessage(String portfolioName) {
    log.append("[Text Based View]Show portfolio add success message prompt: ")
            .append(portfolioName).append("\n");
  }

  /**
   * A fake aborting dialog method which just logs the call made.
   */
  @Override
  public void showAbortingPrompt() {
    log.append("[Text Based View]Show aborting prompt\n");
  }

  /**
   * A fake login success dialog method which just logs the call made.
   */
  @Override
  public void showLoginSuccessPrompt() {
    log.append("[Text Based View]Show login success prompt\n");
  }

  /**
   * A fake user create success dialog method which just logs the call made.
   */
  @Override
  public void showUserCreateSuccessPrompt() {
    log.append("[Text Based View]Show user create success prompt\n");
  }

  /**
   * A fake db store error dialog method which just logs the call made.
   */
  @Override
  public void showDBStoreErrorMessage() {
    log.append("[Text Based View]Show db store error message prompt\n");
  }

  /**
   * A fake save portfolio error dialog method which just logs the call made.
   */
  @Override
  public void showSavePortfolioErrorMessage(String portfolio) {
    log.append("[Text Based View]Show save portfolio error message prompt: ")
            .append(portfolio).append("\n");
  }

  /**
   * A fake upload portfolio by xml dialog method which just logs the call made.
   */
  @Override
  public void showUploadPortfolioByXMLError() {
    log.append("[Text Based View]Show upload portfolio by xml error prompt\n");
  }

  /**
   * A fake print and add to appendable dialog method which just logs the call made.
   */
  @Override
  public void printAndAddToAppendable(String toPrint) {
    log.append("[Text Based View]Show print and add to appendable prompt: ")
            .append(toPrint).append("\n");
  }

  @Override
  public void showDifferentAddPortfolioTypeOptions() {
    log.append("[Text Based View]Show Different Add Portfolio type options called.\n");
  }

  @Override
  public void showPortfolioModifySuccessMessage() {
    log.append("[Text Based View]Show Portfolio Modify Success Message called.\n");
  }

  @Override
  public void showCostBasisForPortfolio(Date date, double value) {
    log.append("[Text Based View]Show Cost Basis For Portfolio called.\n");
  }

  @Override
  public void showRangeStart() {
    log.append("[Text Based View]Show Range Start called.\n");
  }

  @Override
  public void showRangeEnd() {
    log.append("[Text Based View]Show Range End called.\n");
  }

  @Override
  public void showDateCannotBeInFutureDialog() {
    log.append("[Text Based View]Show Date cannot be in future called.\n");
  }

  @Override
  public void showInvalidTransactionAsQuantityNegative() {
    log.append("[Text Based View]Show Invalid transaction as quantity negative called.\n");
  }

  @Override
  public void showInputFileViolatesPortfolioConstraints() {
    log.append("[Text Based View]Show input file violates portfolio constraints called.\n");
  }

  @Override
  public void showEndDateCannotBeBeforeStartDate() {
    log.append("[Text Based View]End date cannot be before start date.\n");
  }

  @Override
  public void showBarChart(String startDate, String endDate,
                           String portfolioName, ChartPlot chartPlot) {
    log.append("[Text Based View]Show Bar Chart Called.\n")
            .append("Start date: ")
            .append(startDate)
            .append(" ")
            .append("End date: ")
            .append(endDate)
            .append(" ")
            .append("Portfolio name: ")
            .append(portfolioName)
            .append(" ")
            .append("scale: ")
            .append(chartPlot.getScale())
            .append(" ")
            .append("start: ")
            .append(chartPlot.getStart())
            .append(" ")
            .append("date range: ")
            .append(chartPlot.getDateRangeType())
            .append(" ")
            .append("chart plot size: ")
            .append(chartPlot.getPlots().size())
            .append("\n");
  }
}
