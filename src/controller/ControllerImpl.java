package controller;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import models.FlexiblePortfolioModelImpl;
import models.PortfolioElementModel;
import models.PortfolioElementModelImpl;
import models.PortfolioModel;
import models.PortfolioModelImpl;
import models.StockModel;
import models.StockSetModel;
import models.TransactionModel;
import models.TransactionModelImpl;
import models.UserModel;
import models.UserModelImpl;
import models.UserStockModel;
import utils.ChartPlot;
import utils.ChartPlotCalculatorUtil;
import views.TextBasedView;

/**
 * Controller class to dictate the flow of program.
 * This class decides what inputs to take and accordingly sends
 * instructions to both view and model.
 */
public class ControllerImpl implements Controller {
  private final UserStockModel userStockModel;
  private final TextBasedView textBasedView;
  private final InputStream in;
  private final String root;
  private Scanner inputScanner;

  /**
   * Public constructor to initialise the portfolio system setup.
   *
   * @param userStockModel Set up the users and stock models.
   * @param textBasedView  Set up the text views.
   * @param in             Set up input stream to take input.
   * @param root           Root directory folder where all data will be stored/read.
   */
  public ControllerImpl(UserStockModel userStockModel,
                        TextBasedView textBasedView, InputStream in, String root) {
    this.textBasedView = textBasedView;
    this.userStockModel = userStockModel;
    this.in = in;
    this.root = root;
  }

  /**
   * Method to start the system and show user the available options.
   * It also reads from files for existing database and users.
   */
  @Override
  public void start() {
    inputScanner = new Scanner(in);
    try {
      userStockModel.getStockSetModel().loadStocksFromDatabase();
      this.userStockModel.setUserSetModel(userStockModel.getUserSetModel()
              .loadUserSetFromDatabase(this.root));
    } catch (ParseException | XPathExpressionException | IOException
             | ParserConfigurationException | SAXException e) {
      textBasedView.showInitializationFailedPrompt();
      System.exit(0);
    }
    startProgram();
  }

  private void startProgram() {
    textBasedView.showWelcomeDialog();
    String option = inputScanner.nextLine();
    switch (option) {
      case "1":
        createUser();
        break;
      case "2":
        loginUser();
        break;
      case "3":
        break;
      default:
        textBasedView.showWrongInputDialog();
        startProgram();
        break;
    }
  }

  private String inputNonSpecialCharString() {
    String input = inputScanner.nextLine();
    if (!isStringAcceptable(input)) {
      textBasedView.noSpecialCharactersAllowedPrompt();
      return inputNonSpecialCharString();
    }
    return input;
  }

  private void createUser() {
    textBasedView.showCreateUserDialog();
    String userName = inputNonSpecialCharString();
    UserModel userModel = UserModelImpl.getBuilder().userId(userStockModel.getUserSetModel()
                    .getListSize() + 1000)
            .name(userName)
            .build();
    saveToDB(userModel);
    textBasedView.showUserCreateSuccessPrompt();
    textBasedView.welcomeUserPrompt(userModel.getUserName(), userModel.getUserId());
    showMainMenu(userModel);
  }

  private void loginUser() {
    textBasedView.showEnterUserIDDialog();
    int userId = 0;
    try {
      userId = Integer.parseInt(inputScanner.nextLine());
    } catch (Exception e) {
      textBasedView.showWrongInputDialog();
      loginUser();
      return;
    }
    if (userId < 1000) {
      textBasedView.showNoUserFoundPrompt();
      startProgram();
      return;
    }
    if (userStockModel.getUserSetModel().getUserList().size() > userId - 1000) {
      UserModel userModel = userStockModel.getUserSetModel().getUserList().get(userId - 1000);
      textBasedView.showLoginSuccessPrompt();
      textBasedView.welcomeUserPrompt(userModel.getUserName(), userModel.getUserId());
      showMainMenu(userModel);
    } else {
      textBasedView.showNoUserFoundPrompt();
      startProgram();
    }
  }

  private void showMainMenu(UserModel userModel) {
    textBasedView.showMainMenu();
    String option = inputScanner.nextLine();
    switch (option) {
      case "1":
        showDifferentAddPortfolioTypeOptions(userModel);
        break;
      case "2":
        viewAPortfolio(userModel);
        break;
      case "3":
        modifyAPortfolio(userModel);
        break;
      case "4":
        analyzeCostBasisOfPortfolio(userModel);
        break;
      case "5":
        showPortfolioPerformance(userModel);
        break;
      case "6":
        determinePortfolioForADate(userModel);
        break;
      case "7":
        savePortfolio(userModel);
        break;
      case "8":
        startProgram();
        break;
      default:
        textBasedView.showWrongInputDialog();
        showMainMenu(userModel);
        break;
    }
  }

  private void modifyAPortfolio(UserModel userModel) {
    List<String> portfolioList = new ArrayList<>(userModel.getAllFlexiblePortfolios().keySet());
    if (checkIfNoPortfoliosPresent(userModel, portfolioList)) {
      return;
    }

    int selection = selectPortfolioWrapper(userModel, portfolioList);
    if (selection <= 0) {
      modifyAPortfolio(userModel);
      return;
    }

    String portfolio = portfolioList.get(selection - 1);

    String tickerName = inputTickerName();
    StockModel stockModel = userStockModel.getStockSetModel().getStock(tickerName);
    TransactionModel transactionModel = inputTransaction(stockModel,
            userModel.getPortfolio(portfolio));
    userModel.modifyPortfolio(portfolio, stockModel, transactionModel);
    saveToDB(userModel);
    textBasedView.showPortfolioModifySuccessMessage();
    showMainMenu(userModel);
  }

  private double inputQuantityForTransaction(double quantitySoFar) {
    int quantity = inputQuantity();
    if (quantity + quantitySoFar < 0) {
      {
        textBasedView.showInvalidTransactionAsQuantityNegative();
      }
      return inputQuantityForTransaction(quantitySoFar);
    }
    return quantity;
  }

  private TransactionModel inputTransaction(StockModel stockModel, PortfolioModel portfolioModel) {
    Date date;
    double price = 0;
    while (true) {
      date = inputDate();
      if (stockModel.doesPriceExistOnADate(date)) {
        price = stockModel.getPriceOnADate(date);
        break;
      } else {
        textBasedView.showNoDataFoundForDatePrompt();
      }
    }

    double quantitySoFar = 0;
    PortfolioElementModel portfolioElementModel = portfolioModel.getPortfolioElements()
            .get(stockModel.getStockTicker());
    if (portfolioElementModel != null) {
      quantitySoFar = portfolioElementModel.getStockTotalQuantity();
    }

    double quantity = inputQuantityForTransaction(quantitySoFar);

    double commission = inputCommission();
    return TransactionModelImpl.getBuilder()
            .date(date)
            .price(price)
            .qty(quantity)
            .brokerCommission(commission)
            .exchange(stockModel.getExchangeName())
            .build();
  }

  Date[] inputDateRange() {
    textBasedView.showRangeStart();
    Date startDate = inputDate();
    textBasedView.showRangeEnd();
    Date endDate = inputDate();
    return new Date[]{startDate, endDate};
  }

  private void showPortfolioPerformance(UserModel userModel) {
    List<String> portfolioList = new ArrayList<>(userModel.getAllPortfolios().keySet());
    if (checkIfNoPortfoliosPresent(userModel, portfolioList)) {
      return;
    }

    int selection = selectPortfolioWrapper(userModel, portfolioList);
    if (selection <= 0) {
      showPortfolioPerformance(userModel);
      return;
    }
    showPortfolioPerformanceOverTime(userModel, portfolioList.get(selection - 1));
  }

  private void showPortfolioPerformanceOverTime(UserModel userModel, String portfolioName) {
    Date[] dates = inputDateRange();
    Date startDate = dates[0];
    Date endDate = dates[1];

    if (endDate.compareTo(startDate) < 0) {
      textBasedView.showEndDateCannotBeBeforeStartDate();
      showPortfolioPerformanceOverTime(userModel, portfolioName);
      return;
    }

    List<TransactionModel> transactionModels = userModel
            .getPortfolio(portfolioName).getAllTransactions();
    Date firstTransactionDate = new Date();
    for (TransactionModel transactionModel : transactionModels) {
      if (transactionModel.getTransactionDate().compareTo(firstTransactionDate) < 0) {
        firstTransactionDate = transactionModel.getTransactionDate();
      }
    }

    if (endDate.compareTo(new Date()) > 0) {
      textBasedView.showDateCannotBeInFutureDialog();
      showPortfolioPerformanceOverTime(userModel, portfolioName);
      return;
    }

    ChartPlot chartPlot = userModel.getPerformanceForPortfolio(startDate, endDate,
            portfolioName);
    textBasedView.showBarChart(ChartPlotCalculatorUtil.getDateLabel(startDate,
                    chartPlot.getDateRangeType()),
            ChartPlotCalculatorUtil.getDateLabel(endDate, chartPlot.getDateRangeType()),
            portfolioName,
            chartPlot);
    showMainMenu(userModel);
  }

  private void analyzeCostBasisOfPortfolio(UserModel userModel) {
    List<String> portfolioList = new ArrayList<>(userModel.getAllPortfolios().keySet());
    if (checkIfNoPortfoliosPresent(userModel, portfolioList)) {
      return;
    }

    int selection = selectPortfolioWrapper(userModel, portfolioList);
    if (selection <= 0) {
      analyzeCostBasisOfPortfolio(userModel);
      return;
    }

    Date date = inputDate();

    double cost = userModel.getPortfolioCostBasis(portfolioList.get(selection - 1), date);
    textBasedView.showCostBasisForPortfolio(date, cost);
    showMainMenu(userModel);
  }

  private Date inputDate() {
    Date date = null;
    try {
      textBasedView.showEnterDatePrompt();
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      format.setLenient(false);
      date = format.parse(inputScanner.nextLine());
    } catch (ParseException | IllegalArgumentException e) {
      textBasedView.showWrongInputDialog();
      return inputDate();
    }
    return date;
  }

  private boolean isStringAcceptable(String input) {
    Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(input);
    return !m.find() && input.length() != 0;
  }

  private String inputTickerName() {
    textBasedView.showEnterStockTickerPrompt();
    String tickerName = inputScanner.nextLine().toUpperCase();
    StockModel stockModel = userStockModel.getStockSetModel().getStock(tickerName);
    if (stockModel == null) {
      textBasedView.showWrongInputDialog();
      return inputTickerName();
    }
    return tickerName;
  }

  private void addNewPortfolioOptions(UserModel userModel, String portfolioName,
                                      PortfolioType portfolioType) {
    textBasedView.showAddPortfolioOptions();
    String option = inputScanner.nextLine();
    switch (option) {
      case "1":
        if (portfolioType == PortfolioType.INFLEXIBLE) {
          addPortfolioDataManually(userModel, portfolioName);
        } else {
          addPortfolioDataManuallyForFlexiblePortfolio(userModel, portfolioName);
        }
        break;
      case "2":
        loadPortfolioDataFromXMLFile(userModel, portfolioName, userStockModel.getStockSetModel(),
                portfolioType);
        break;
      default:
        textBasedView.showWrongInputDialog();
        addNewPortfolioOptions(userModel, portfolioName, portfolioType);
        break;
    }
  }

  private void addPortfolioDataManuallyForFlexiblePortfolio(UserModel userModel,
                                                            String portfolioName) {
    int totalStocks = getTotalNumberOfStocksToBeAdded();
    PortfolioModel portfolioModel = FlexiblePortfolioModelImpl.getBuilder()
            .portfolioName(portfolioName)
            .build();
    userModel.addPortfolio(portfolioModel);
    for (int i = 0; i < totalStocks; i++) {
      String tickerName = inputTickerName();
      StockModel stockModel = userStockModel.getStockSetModel().getStock(tickerName);
      PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
              .stockModel(stockModel)
              .build();
      portfolioModel.addPortfolioElement(portfolioElementModel);
      TransactionModel transactionModel = inputTransaction(stockModel, portfolioModel);
      userModel.modifyPortfolio(portfolioName, stockModel, transactionModel);
    }
    saveToDB(userModel);
    textBasedView.showPortfolioAddSuccessMessage(portfolioName);
    showMainMenu(userModel);
  }

  private void showDifferentAddPortfolioTypeOptions(UserModel userModel) {
    textBasedView.showDifferentAddPortfolioTypeOptions();
    String option = inputScanner.nextLine();
    switch (option) {
      case "1":
        addNewPortfolio(userModel, PortfolioType.FLEXIBLE);
        break;
      case "2":
        addNewPortfolio(userModel, PortfolioType.INFLEXIBLE);
        break;
      default:
        textBasedView.showWrongInputDialog();
        showDifferentAddPortfolioTypeOptions(userModel);
        break;
    }
  }

  private void addNewPortfolio(UserModel userModel, PortfolioType portfolioType) {
    textBasedView.showAddPortfolioNamePrompt();
    String portfolioName = inputNonSpecialCharString();
    if (userModel.getAllPortfolios().containsKey(portfolioName)) {
      textBasedView.showPortfolioNameAlreadyExistsPrompt();
      addNewPortfolio(userModel, portfolioType);
      return;
    }
    addNewPortfolioOptions(userModel, portfolioName, portfolioType);
  }

  private int getTotalNumberOfStocksToBeAdded() {
    textBasedView.showTotalNumberOfStocksToBeAddedPrompt();
    int totalStocks = 0;
    try {
      totalStocks = Integer.parseInt(inputScanner.nextLine());
    } catch (Exception e) {
      textBasedView.showWrongInputDialog();
      return getTotalNumberOfStocksToBeAdded();
    }
    return totalStocks;
  }

  private int inputQuantity() {
    textBasedView.showEnterQuantityPrompt();
    int quantity = 0;
    try {
      quantity = Integer.parseInt(inputScanner.nextLine());
    } catch (Exception e) {
      textBasedView.showWrongInputDialog();
      return inputQuantity();
    }
    return quantity;
  }

  private double inputCommission() {
    textBasedView.showEnterCommissionPrompt();
    double commission = 0;
    try {
      commission = Double.parseDouble(inputScanner.nextLine());
      if (commission < 0) {
        textBasedView.showWrongInputDialog();
        return inputCommission();
      }
    } catch (Exception e) {
      textBasedView.showWrongInputDialog();
      return inputCommission();
    }
    return commission;
  }

  private int inputQuantityForInflexiblePortfolio(
          String tickerName,
          List<PortfolioElementModel> portfolioElementModels) {
    int quantity = inputQuantity();
    int totalQuantitySoFar = 0;
    for (PortfolioElementModel portfolioElementModel : portfolioElementModels) {
      if (portfolioElementModel.getStockModel().getStockTicker().equals(tickerName)) {
        totalQuantitySoFar += portfolioElementModel.getStockTotalQuantity();
      }
    }
    if (totalQuantitySoFar + quantity < 0) {
      textBasedView.showInvalidTransactionAsQuantityNegative();
      return inputQuantityForInflexiblePortfolio(tickerName, portfolioElementModels);
    }

    return quantity;
  }

  private void addPortfolioDataManually(UserModel userModel, String portfolioName) {
    int totalStocks = getTotalNumberOfStocksToBeAdded();
    List<PortfolioElementModel> portfolio = new ArrayList<>();
    for (int i = 0; i < totalStocks; i++) {
      String tickerName = inputTickerName();
      StockModel stockModel = userStockModel.getStockSetModel().getStock(tickerName);
      Date date = null;
      double price = 0;
      while (true) {
        date = inputDate();
        if (stockModel.doesPriceExistOnADate(date)) {
          price = stockModel.getPriceOnADate(date);
          break;
        } else {
          textBasedView.showNoDataFoundForDatePrompt();
        }
      }

      int quantity = inputQuantityForInflexiblePortfolio(tickerName, portfolio);

      PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
              .stockModel(stockModel)
              .avgPrice(price)
              .totalQuantity(quantity)
              .transactionDate(date)
              .build();
      portfolio.add(portfolioElementModel);
    }
    PortfolioModel portfolioModel = PortfolioModelImpl.getBuilder()
            .portfolioName(portfolioName)
            .addPortfolioList(portfolio, userStockModel.getStockSetModel())
            .build();

    userModel.addPortfolio(portfolioModel);
    saveToDB(userModel);
    textBasedView.showPortfolioAddSuccessMessage(portfolioName);
    showMainMenu(userModel);
  }

  private void loadPortfolioDataFromXMLFile(UserModel userModel, String portfolioName,
                                            StockSetModel stockSetModel,
                                            PortfolioType portfolioType) {
    textBasedView.showEnterFilePathPrompt();
    String path = inputScanner.nextLine();
    try {
      if (portfolioType == PortfolioType.INFLEXIBLE) {
        userModel.addPortfolio(PortfolioModelImpl.getBuilder()
                .buildPortfolioFromXML(path, portfolioName, stockSetModel).build());
      } else {
        userModel.addPortfolio(FlexiblePortfolioModelImpl.getBuilder()
                .buildPortfolioFromXML(path, portfolioName, stockSetModel).build());
      }
      userStockModel.getUserSetModel().getUserList().set(userModel.getUserId() - 1000, userModel);
      saveToDB(userModel);
    } catch (IllegalArgumentException e) {
      textBasedView.showInputFileViolatesPortfolioConstraints();
      textBasedView.showUploadPortfolioByXMLError();
      loadPortfolioDataFromXMLFile(userModel, portfolioName, stockSetModel, portfolioType);
      return;
    } catch (XPathExpressionException | IOException | ParserConfigurationException | SAXException
             | ParseException | RuntimeException e) {
      textBasedView.showUploadPortfolioByXMLError();
      loadPortfolioDataFromXMLFile(userModel, portfolioName, stockSetModel, portfolioType);
      return;
    }
    textBasedView.showPortfolioAddSuccessMessage(portfolioName);
    showMainMenu(userModel);
  }

  private int selectPortfolio(List<String> portfolioList) {
    textBasedView.showSelectPortfolioPrompt(portfolioList);
    int selection = 0;
    try {
      selection = Integer.parseInt(inputScanner.nextLine());
    } catch (Exception e) {
      textBasedView.showWrongInputDialog();
      return selectPortfolio(portfolioList);
    }
    return selection;
  }

  private boolean checkIfNoPortfoliosPresent(UserModel userModel, List<String> portfolioList) {
    if (portfolioList.size() == 0) {
      textBasedView.showNoPortfoliosPrompt();
      showMainMenu(userModel);
      return true;
    }
    return false;
  }

  private void viewAPortfolio(UserModel userModel) {
    List<String> portfolioList = new ArrayList<>(userModel.getAllPortfolios().keySet());
    if (checkIfNoPortfoliosPresent(userModel, portfolioList)) {
      return;
    }

    int selection = selectPortfolioWrapper(userModel, portfolioList);
    if (selection <= 0) {
      viewAPortfolio(userModel);
      return;
    }

    Date date = inputDate();

    PortfolioModel portfolioModel = userModel.getPortfolio(portfolioList.get(selection - 1));
    String portfolioModelName = portfolioModel.getName();
    List<List<String>> portfolioElement =
            userModel.getCompositionValueByDate(portfolioModelName, date);
    textBasedView.showPortfolioDetails(portfolioModelName, portfolioElement);
    showMainMenu(userModel);
  }

  private int selectPortfolioWrapper(UserModel userModel, List<String> portfolioList) {
    int selection = selectPortfolio(portfolioList);
    if (selection > userModel.getAllPortfolios().size()) {
      textBasedView.showWrongInputDialog();
      return -1;
    }
    return selection;
  }

  private void determinePortfolioForADate(UserModel userModel) {
    List<String> portfolioList = new ArrayList<>(userModel.getAllPortfolios().keySet());
    if (checkIfNoPortfoliosPresent(userModel, portfolioList)) {
      return;
    }

    int selection = selectPortfolioWrapper(userModel, portfolioList);
    if (selection <= 0) {
      determinePortfolioForADate(userModel);
      return;
    }

    Date date = inputDate();
    try {
      double value = userModel.getPortfolioValueByDate(portfolioList.get(selection - 1), date);
      textBasedView.showPortfolioValueForDate(date, value);
    } catch (IllegalArgumentException e) {
      textBasedView.showWrongInputDialog();
      determinePortfolioForADate(userModel);
      return;
    }
    showMainMenu(userModel);
  }

  private void savePortfolio(UserModel userModel) {
    List<String> portfolioList = new ArrayList<>(userModel.getAllPortfolios().keySet());
    if (portfolioList.size() == 0) {
      textBasedView.showNoPortfoliosPrompt();
      showMainMenu(userModel);
      return;
    }
    int selection = selectPortfolio(portfolioList);

    if (selection > portfolioList.size()) {
      textBasedView.showWrongInputDialog();
      savePortfolio(userModel);
      return;
    }

    try {
      String path = userModel.savePortfolio(portfolioList.get(selection - 1));
      textBasedView.savePortfolioSuccessMessage(path);
    } catch (IOException | TransformerException | ParserConfigurationException e) {
      textBasedView.showSavePortfolioErrorMessage(portfolioList.get(selection - 1));
    }
    showMainMenu(userModel);
  }

  private void saveToDB(UserModel userModel) {
    try {
      if (userModel.getUserId() - 1000 == userStockModel.getUserSetModel().getUserList().size()) {
        userStockModel.getUserSetModel().getUserList().add(userModel);
      } else {
        userStockModel.getUserSetModel().getUserList().set(userModel.getUserId() - 1000, userModel);
      }
      userStockModel.getUserSetModel().generateXML(root);
    } catch (IOException | ParserConfigurationException | TransformerException e) {
      textBasedView.showDBStoreErrorMessage();
    }
  }

  private enum PortfolioType {
    FLEXIBLE, INFLEXIBLE
  }
}
