package views;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import models.PortfolioElementModel;
import models.PortfolioElementModelImpl;
import models.PortfolioModel;
import models.PortfolioModelImpl;
import models.StockModelImpl;
import models.UserModel;
import models.UserModelImpl;
import utils.ChartPlot;
import utils.DateRangeType;

import static org.junit.Assert.assertEquals;

/**
 * Unit test class to ensure views are
 * working correctly.
 */
public class TextBasedViewUnitTest {
  private TextBasedView textBasedView;
  private ByteArrayOutputStream actionBytes;
  private ByteArrayOutputStream menuBytes;

  @Before
  public void initializeTest() {
    actionBytes = new ByteArrayOutputStream();
    menuBytes = new ByteArrayOutputStream();
    PrintStream actionOut = new PrintStream(actionBytes);
    PrintStream menuOut = new PrintStream(menuBytes);
    textBasedView = new TextBasedViewImpl(actionOut, menuOut);
  }

  @Test
  public void testShowWelcomeDialog() {
    String desiredOutput = "Hello!"
            + "\nSelect one of the three options"
            + "\n[1] Create User"
            + "\n[2] Login User"
            + "\n[3] Quit"
            + "\n";

    textBasedView.showWelcomeDialog();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowCreateUserDialog() {
    String desiredOutput = "Enter User Name\n";
    textBasedView.showCreateUserDialog();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowMainMenu() {
    String desiredOutput = "\n"
            + "Select one of the below:\n"
            + "[1] Add new portfolio\n"
            + "[2] View a portfolio\n"
            + "[3] Modify a portfolio\n"
            + "[4] Analyze cost basis of a portfolio\n"
            + "[5] Show portfolio performance over time\n"
            + "[6] Determine a portfolio for a date\n"
            + "[7] Download a portfolio\n"
            + "[8] Logout\n";
    textBasedView.showMainMenu();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowAddPortfolioNamePrompt() {
    String desiredOutput = "Enter portfolio name\n";
    textBasedView.showAddPortfolioNamePrompt();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowAddPortfolioOptions() {
    String desiredOutput = "Select one of the below"
            + "\n[1] Enter data manually"
            + "\n[2] Load data from XML file\n";
    textBasedView.showAddPortfolioOptions();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowTotalNumberOfStocksToBeAddedPrompt() {
    String desiredOutput = "Enter number of stock entries you would like to make.\n";
    textBasedView.showTotalNumberOfStocksToBeAddedPrompt();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowEnterStockTickerPrompt() {
    String desiredOutput = "Enter ticker symbol\n";
    textBasedView.showEnterStockTickerPrompt();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowEnterQuantityPrompt() {
    String desiredOutput = "Enter quantity in whole number\n";
    textBasedView.showEnterQuantityPrompt();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowEnterFilePathPrompt() {
    String desiredOutput = "Enter file Name with its extension."
            + "\nPlease ensure file is located in same location as the program.\n";
    textBasedView.showEnterFilePathPrompt();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowNoPortfoliosPrompt() {
    String desiredOutput = "No portfolios found\n";
    textBasedView.showNoPortfoliosPrompt();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void welcomeUserPrompt() {
    UserModel userModel = UserModelImpl.getBuilder()
            .userId(1234)
            .name("Test User")
            .build();

    String desiredOutput = String.format("Welcome %s (userId: %d)\n",
            userModel.getUserName(), userModel.getUserId());

    textBasedView.welcomeUserPrompt(userModel.getUserName(), userModel.getUserId());
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void noSpecialCharactersAllowedPrompt() {
    String desiredOutput = "No special character allowed."
            + " Please input only alphanumeric values and try again.\n";
    textBasedView.noSpecialCharactersAllowedPrompt();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowNoDataFoundForDatePrompt() {
    String desiredOutput = "No data found for the date."
            + " Please input a valid date on which the stock was actually traded\n";
    textBasedView.showNoDataFoundForDatePrompt();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void savePortfolioSuccessMessage() {
    String path = "Some path";
    String desiredOutput = "Saved portfolio at path : " + path + "\n";
    textBasedView.savePortfolioSuccessMessage(path);
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowPortfolioValueForDate() {
    Date date = new Date();
    double value = 69.420;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String desiredOutput = "Portfolio value for "
            + df.format(date)
            + " as of close is "
            + value
            + "\n";
    textBasedView.showPortfolioValueForDate(date, value);
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowSelectPortfolioPrompt() {
    List<String> portfolios = new ArrayList<>();
    portfolios.add("Portfolio 1");
    portfolios.add("Portfolio 2");

    String desiredOutput = "Select a portfolio using Id no. from below:\n"
            + "1. Portfolio 1\n"
            + "2. Portfolio 2\n";

    textBasedView.showSelectPortfolioPrompt(portfolios);
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowPortfolioDetails() throws ParseException {
    String testDate = "2022-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);

    String desiredOutput = "Portfolio Details:\n"
            + "Portfolio Name: Portfolio 1\n"
            + "Ticker Symbol Name            "
            + "Stock Name                    "
            + "Total Quantity                "
            + "Average Price                 "
            + "Latest transaction Date       \n"
            + "GOOG                          "
            + "Google                        "
            + "10.0                          "
            + "101.0                         "
            + "2022-11-01                    \n"
            + "AAL                           "
            + "Apple                         "
            + "69.0                          "
            + "420.0                         "
            + "2022-11-01                    \n"
            + "\n"
            + "\n"
            + "\n";

    Map<String, PortfolioElementModel> portfolioElementModels = new HashMap<>();

    portfolioElementModels.put("GOOG", PortfolioElementModelImpl.getBuilder()
            .avgPrice(101)
            .totalQuantity(10)
            .transactionDate(date)
            .stockModel(new StockModelImpl("GOOG", "Google",
                    "NASDAQ", date))
            .build());

    portfolioElementModels.put("AAL", PortfolioElementModelImpl.getBuilder()
            .avgPrice(420)
            .totalQuantity(69)
            .transactionDate(date)
            .stockModel(new StockModelImpl("AAL", "Apple",
                    "NASDAQ", date))
            .build());

    PortfolioModel portfolioModel = PortfolioModelImpl.getBuilder()
            .portfolioName("Portfolio 1")
            .portfolioHashMap(portfolioElementModels)
            .build();
    List<List<String>> portfolioElement = new ArrayList<>();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    for (Map.Entry<String, PortfolioElementModel> entry :
            portfolioModel.getPortfolioElements().entrySet()) {
      List<String> element = new ArrayList<>();
      element.add(entry.getValue().getStockModel().getStockTicker());
      element.add(entry.getValue().getStockModel().getStockName());
      element.add(String.valueOf(entry.getValue().getStockTotalQuantity()));
      element.add(String.valueOf(entry.getValue().getStockCurrentAvgPrice()));
      element.add(format.format(entry.getValue().getTransactionDate()));
      portfolioElement.add(element);
    }
    textBasedView.showPortfolioDetails(portfolioModel.getName(), portfolioElement);
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowWrongInputDialog() {
    String desiredOutput = "Wrong input entered, try again!\n";
    textBasedView.showWrongInputDialog();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowPortfolioNameAlreadyExistsPrompt() {
    String desiredOutput = "Portfolio Name already exists."
            + " Please enter a different portfolio name.\n";
    textBasedView.showPortfolioNameAlreadyExistsPrompt();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowNoDataAvailableForOneOrMoreStockForTheDataEntered() {
    String desiredOutput = "No data available for one or more stocks"
            + " for the date entered. Please try again.\n";
    textBasedView.showNoDataAvailableForOneOrMoreStockForTheDataEntered();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowEnterUserIDDialog() {
    String desiredOutput = "Enter userId to login to\n";
    textBasedView.showEnterUserIDDialog();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowEnterDatePrompt() {
    String desiredOutput = "Enter date (YYYY-MM-dd format)\n";
    textBasedView.showEnterDatePrompt();
    assertEquals(desiredOutput, menuBytes.toString());
  }

  @Test
  public void testShowNoUserFoundPrompt() {
    String desiredOutput = "No user found!"
            + "\nYou might want to create a new user.\n";
    textBasedView.showNoUserFoundPrompt();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowInitializationFailedPrompt() {
    String desiredOutput = "Failed to initialize the program."
            + "\nYou might want to check the resources and fix any incorrectly formatted files."
            + "\nQuitting....\n";
    textBasedView.showInitializationFailedPrompt();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowPortfolioAddSuccessMessage() {
    String portfolioName = "Portfolio 1";
    String desiredOutput = "Portfolio 1 added successfully\n";
    textBasedView.showPortfolioAddSuccessMessage(portfolioName);
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowAbortingPrompt() {
    String desiredOutput = "No price information found for the date. Aborting operation"
            + "\nPlease provide correct information this time\n";
    textBasedView.showAbortingPrompt();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowLoginSuccessPrompt() {
    String desiredOutput = "Log In Success\n";
    textBasedView.showLoginSuccessPrompt();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowUserCreateSuccessPrompt() {
    String desiredOutput = "User create success\n";
    textBasedView.showUserCreateSuccessPrompt();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowDBStoreErrorMessage() {
    String desiredOutput = "Entered data could not be stored. Please try again!\n";
    textBasedView.showDBStoreErrorMessage();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowSavePortfolioErrorMessage() {
    String portfolioName = "Portfolio 1";
    String desiredOutput = "Portfolio 1 could not be saved. Please try again!\n";
    textBasedView.showSavePortfolioErrorMessage(portfolioName);
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void testShowUploadPortfolioByXMLError() {
    String desiredOutput = "Error uploading XML file to portfolio. Please try again!\n";
    textBasedView.showUploadPortfolioByXMLError();
    assertEquals(desiredOutput, actionBytes.toString());
  }

  @Test
  public void printAndAddToAppendable() {
    String desiredOutput = "Test";
    textBasedView.printAndAddToAppendable(desiredOutput);
    assertEquals(desiredOutput + "\n", actionBytes.toString());
  }

  @Test
  public void testShowEnterCommissionPrompt() {
    String desiredOutput = "Enter commission";
    textBasedView.showEnterCommissionPrompt();
    assertEquals(desiredOutput + "\n", menuBytes.toString());
  }

  @Test
  public void testShowDifferentAddPortfolioTypeOptions() {
    String desiredOutput = "Which type of portfolio do you want to create?"
            + "\n[1] Flexible Portfolio"
            + "\n[2] Inflexible Portfolio";
    textBasedView.showDifferentAddPortfolioTypeOptions();
    assertEquals(desiredOutput + "\n", menuBytes.toString());
  }

  @Test
  public void testShowPortfolioModifySuccessMessage() {
    String desiredOutput = "Portfolio modified successfully.";
    textBasedView.showPortfolioModifySuccessMessage();
    assertEquals(desiredOutput + "\n", actionBytes.toString());
  }

  @Test
  public void testShowCostBasisForPortfolio() throws ParseException {
    String testDate = "2022-11-01";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date date = formatter.parse(testDate);
    double value = 10.1;
    String expectedResult = "Portfolio cost basis for "
            + testDate
            + " is "
            + value;
    textBasedView.showCostBasisForPortfolio(date, value);
    assertEquals(expectedResult + "\n", actionBytes.toString());
  }

  @Test
  public void testShowRangeStart() {
    String expectedResult = "For start of the range";
    textBasedView.showRangeStart();
    assertEquals(expectedResult + "\n", menuBytes.toString());
  }

  @Test
  public void testShowRangeEnd() {
    String expectedResult = "For end of the range";
    textBasedView.showRangeEnd();
    assertEquals(expectedResult + "\n", menuBytes.toString());
  }

  @Test
  public void testShowDateCannotBeInFutureDialog() {
    String expectedResult = "Date cannot be in future. Please try again";
    textBasedView.showDateCannotBeInFutureDialog();
    assertEquals(expectedResult + "\n", actionBytes.toString());
  }

  @Test
  public void testShowInvalidTransactionAsQuantityNegative() {
    String expectedResult =
            "Invalid input as resultant quantity for the stock turns out to be negative";
    textBasedView.showInvalidTransactionAsQuantityNegative();
    assertEquals(expectedResult + "\n", actionBytes.toString());
  }

  @Test
  public void testShowInputFileViolatesPortfolioConstraints() {
    String expectedResult = "Input file violates portfolio constraints."
            + "Please check all the inputs and try again.";
    textBasedView.showInputFileViolatesPortfolioConstraints();
    assertEquals(expectedResult + "\n", actionBytes.toString());
  }

  @Test
  public void testShowEndDateCannotBeBeforeStartDate() {
    String expectedResult = "End date cannot be before start date.";
    textBasedView.showEndDateCannotBeBeforeStartDate();
    assertEquals(expectedResult + "\n", actionBytes.toString());
  }

  @Test
  public void testShowBarChart() {
    String expectedResult = "Performance of portfolio Portfolio 1 from 2022-10-11 to 2022-10-16\n"
            + "2022-10-11: *\n"
            + "2022-10-12: **\n"
            + "2022-10-13: ***\n"
            + "2022-10-14: ****\n"
            + "2022-10-15: *****\n"
            + "2022-10-16: ******\n"
            + "Relative scale: First * = 100 and following * = 1.5\n";

    String startDate = "2022-10-11";
    String endDate = "2022-10-16";
    String portfolioName = "Portfolio 1";
    int start = 100;
    double scale = 1.5;
    DateRangeType dateRangeType = DateRangeType.DAILY;
    Map<String, Integer> plots = new LinkedHashMap<>();
    plots.put("2022-10-11", 1);
    plots.put("2022-10-12", 2);
    plots.put("2022-10-13", 3);
    plots.put("2022-10-14", 4);
    plots.put("2022-10-15", 5);
    plots.put("2022-10-16", 6);
    ChartPlot chartPlot = new ChartPlot(plots, start, scale, dateRangeType);
    textBasedView.showBarChart(startDate, endDate, portfolioName, chartPlot);
    assertEquals(expectedResult + "\n", actionBytes.toString());
  }

}
