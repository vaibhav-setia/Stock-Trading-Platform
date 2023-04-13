package controller;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import models.DollarCostAveragingImpl;
import models.PortfolioElementModel;
import models.PortfolioElementModelImpl;
import models.PortfolioModel;
import models.StockModel;
import models.StockModelImpl;
import models.StrategicalFlexiblePortfolioImpl;
import models.Strategy;
import models.StrategyModel;
import models.TransactionModel;
import models.TransactionModelImpl;
import models.UserModel;
import models.UserModelImpl;
import models.UserStockModel;
import utils.ChartPlot;
import utils.ChartPlotCalculatorUtil;
import views.GUIView;

/**
 * Controller class for managing the data flow between GUI and model class.
 * This class implements both the controller and features interface and has all the features
 * functionality built.
 */
public class GUIController implements Controller, Features {
  private final UserStockModel userStockModel;
  private final GUIView guiView;

  private final String root;

  private UserModel userModel;

  /**
   * Public constructor to initialise a GUI controller.
   *
   * @param userStockModel set of users and stock to be used for data.
   * @param guiView        gui view to be used to show data and take inputs.
   * @param root           path where data needs to be persisted.
   */
  public GUIController(UserStockModel userStockModel, GUIView guiView, String root) {
    this.userStockModel = userStockModel;
    this.guiView = guiView;
    this.root = root;
    this.guiView.addFeatures(this);
    initializeUserStockData();
  }

  private void initializeUserStockData() {
    try {
      guiView.showProcessingPopup();
      userStockModel.getStockSetModel().loadStocksFromDatabase();
      this.userStockModel.setUserSetModel(userStockModel.getUserSetModel()
              .loadUserSetFromDatabase(this.root));
      runAllStrategies();
      guiView.closeProcessingPopup();
    } catch (ParseException | XPathExpressionException | IOException
             | ParserConfigurationException | SAXException e) {
      guiView.showInitializationFailedPopup();
      System.exit(0);
    }
  }

  private void runAllStrategies() {
    for (UserModel userModel : userStockModel.getUserSetModel().getUserList()) {
      for (PortfolioModel portfolioModel : userModel.getAllPortfolios().values()) {
        portfolioModel.runAllStrategies();
      }
    }
  }

  @Override
  public void start() {
    return;
  }

  @Override
  public void exitProgram() {
    System.exit(0);
  }

  @Override
  public void login() {
    String userIDString = guiView.getUserId();
    int userId = 0;
    try {
      userId = Integer.parseInt(userIDString);
    } catch (Exception e) {
      guiView.showWrongUserIDPopup();
      return;
    }

    if (userId < 1000) {
      guiView.showNoUserFoundPrompt();
      return;
    }
    if (userStockModel.getUserSetModel().getUserList().size() > userId - 1000) {
      userModel = userStockModel.getUserSetModel().getUserList().get(userId - 1000);
      guiView.openMainMenuLayout(userModel.getUserName(), userModel.getUserId(),
              new ArrayList<>(userModel.getAllFlexiblePortfolios().keySet()));
      guiView.clearLoginPageInputs();
    } else {
      guiView.showNoUserFoundPrompt();
    }
  }

  @Override
  public void signUp() {
    String userName = guiView.getUserName();
    if (!isStringAcceptable(userName)) {
      guiView.showNoSpecialCharsAllowedPopup();
      return;
    }
    userModel = UserModelImpl.getBuilder().userId(userStockModel.getUserSetModel()
                    .getListSize() + 1000)
            .name(userName)
            .build();
    saveToDB(userModel);
    guiView.openMainMenuLayout(userModel.getUserName(), userModel.getUserId(),
            new ArrayList<>(userModel.getAllFlexiblePortfolios().keySet()));
    guiView.clearLoginPageInputs();
  }

  @Override
  public void logout() {
    userModel = null;
    guiView.switchLayout("Login");
  }

  @Override
  public void analyzePortfolio(String portfolioName) {
    List<List<String>> composition = userModel.getPortfolio(portfolioName)
            .getPortfolioComposition(new Date());
    guiView.openPortfolioDetails(portfolioName, composition);
  }

  @Override
  public void downloadPortfolio(String portfolioName) {
    String path = null;
    try {
      path = userModel.savePortfolio(portfolioName);
    } catch (IOException | ParserConfigurationException | TransformerException e) {
      guiView.showFailedToSavePortfolio();
      return;
    }
    guiView.showDownloadPortfolioPopup(path);
  }

  @Override
  public void requestCreatePortfolio() {
    guiView.requestCreatePortfolio();
  }

  @Override
  public void goToMainMenu() {
    guiView.openMainMenuLayout(userModel.getUserName(), userModel.getUserId(),
            new ArrayList<>(userModel.getAllFlexiblePortfolios().keySet()));
    guiView.clearPortfolioDetailsPage();
  }

  @Override
  public void getValueOnADate(String portfolioName) {
    String dateInput = guiView.getDateInput();
    if (dateInput != null) {
      try {
        guiView.showProcessingPopup();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        Date date = format.parse(dateInput);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        if (date.compareTo(today.getTime()) >= 0) {
          guiView.closeProcessingPopup();
          guiView.showWrongDateInputPopup();
          return;
        }
        double value = userModel.getPortfolio(portfolioName).getPortfolioValueByDate(date);
        guiView.showValueOnADate(date, value);
        guiView.closeProcessingPopup();
      } catch (IllegalArgumentException | ParseException e) {
        guiView.closeProcessingPopup();
        guiView.showWrongDateInputPopup();
      }
    }
  }

  @Override
  public void getCostBasis(String portfolioName) {
    String dateInput = guiView.getDateInput();
    if (dateInput != null) {
      try {
        guiView.showProcessingPopup();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        Date date = format.parse(dateInput);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        if (date.compareTo(today.getTime()) >= 0) {
          guiView.closeProcessingPopup();
          guiView.showWrongDateInputPopup();
          return;
        }
        double value = userModel.getPortfolio(portfolioName).getPortfolioCostBasis(date);
        guiView.showCostBasis(date, value);
        guiView.closeProcessingPopup();
      } catch (ParseException e) {
        guiView.closeProcessingPopup();
        guiView.showWrongDateInputPopup();
      }
    }
  }

  @Override
  public void showComposition(String portfolioName) {
    String dateInput = guiView.getDateInput();
    if (dateInput != null) {
      try {
        guiView.showProcessingPopup();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        Date date = format.parse(dateInput);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        if (date.compareTo(today.getTime()) >= 0) {
          guiView.closeProcessingPopup();
          guiView.showWrongDateInputPopup();
          return;
        }

        List<List<String>> composition = userModel.getPortfolio(portfolioName)
                .getPortfolioComposition(date);
        guiView.showComposition(composition);
        guiView.closeProcessingPopup();
      } catch (ParseException e) {
        guiView.closeProcessingPopup();
        guiView.showWrongDateInputPopup();
      }
    }
  }

  @Override
  public void requestModifyPortfolio(String portfolioName) {
    Map<String, PortfolioElementModel> portfolioElementModelList = userModel
            .getPortfolio(portfolioName).getPortfolioElements();

    Map<String, Double> portfolioElements = new HashMap<>();
    for (Map.Entry<String, PortfolioElementModel> entry : portfolioElementModelList.entrySet()) {
      portfolioElements.put(entry.getKey(), entry.getValue().getStockTotalQuantity());
    }

    List<String> stockModels = new ArrayList<>(userStockModel.getStockSetModel()
            .getAllStocks().keySet());

    guiView.showModifyPortfolioPopup(portfolioName, portfolioElements, stockModels);
  }

  @Override
  public void requestMoreStockInput() {
    guiView.requestMoreStockInput();
  }

  @Override
  public void requestAddStrategy(String portfolioName) {
    List<String> strategyTypes = EnumSet.allOf(Strategy.class).stream()
            .map(Strategy::toString).collect(Collectors.toList());
    guiView.requestAddStrategy(portfolioName,
            new ArrayList<>(userStockModel.getStockSetModel().getAllStocks().keySet()),
            new ArrayList<>(EnumSet.allOf(Strategy.class)));
  }

  @Override
  public void requestMoreStockInputForStrategy() {
    guiView.requestMoreStockInputForStrategy();
  }

  @Override
  public void packLayout() {
    guiView.packLayout();
  }

  @Override
  public void requestCreateFlexiblePortfolioWithoutStrategy() {
    guiView.requestCreateFlexiblePortfolioWithoutStrategy(new
            ArrayList<>(userStockModel.getStockSetModel().getAllStocks().keySet()));
  }

  @Override
  public void requestCreateFlexiblePortfolioWithStrategy() {
    List<String> strategyTypes = EnumSet.allOf(Strategy.class).stream()
            .map(Strategy::toString).collect(Collectors.toList());
    guiView.requestCreateFlexiblePortfolioWithStrategy(new
                    ArrayList<>(userStockModel.getStockSetModel().getAllStocks().keySet()),
            new ArrayList<>(EnumSet.allOf(Strategy.class)));
  }

  @Override
  public void requestCreateFlexiblePortfolioWithXML() {
    guiView.requestCreateFlexiblePortfolioWithXML();
  }

  @Override
  public void createFlexiblePortfolioWithXML(String portfolioName, File selectedFile) {
    if (!validatePortfolioName(portfolioName)) {
      return;
    }

    guiView.showProcessingPopup();
    try {
      userModel.addPortfolio(
              new StrategicalFlexiblePortfolioImpl.StrategicalFlexiblePortfolioBuilder()
                      .buildPortfolioFromXML(selectedFile.getPath(),
                              portfolioName,
                              userStockModel.getStockSetModel()).build());
      saveToDB(userModel);
      guiView.closeProcessingPopup();
      guiView.closeAddFlexiblePortfolioWithUploadXMLPopup(new ArrayList<>(userModel
              .getAllFlexiblePortfolios().keySet()));
    } catch (IllegalArgumentException | NullPointerException | XPathExpressionException
             | IOException | ParserConfigurationException | SAXException | ParseException e) {
      guiView.closeProcessingPopup();
      guiView.showIncorrectFileUploadPopup();
    }
  }

  @Override
  public void requestCloseCreateFlexiblePortfolioWithXML() {
    guiView.closeAddFlexiblePortfolioWithUploadXMLPopup(new ArrayList<>(userModel
            .getAllFlexiblePortfolios().keySet()));
  }

  @Override
  public void setSelectedFile(File file) {
    guiView.setSelectedFile(file);
  }

  @Override
  public void showGraph(String portfolioName) {
    String[] dates = guiView.getStartEndDateInput();

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);

    Date startDate;
    Date endDate;

    if (dates.length != 2) {
      return;
    }

    try {
      startDate = format.parse(dates[0]);
    } catch (ParseException e) {
      guiView.showWrongDateInputPopup();
      return;
    }

    try {
      endDate = format.parse(dates[1]);
    } catch (ParseException e) {
      guiView.showWrongDateInputPopup();
      return;
    }

    if (startDate.compareTo(endDate) > 0) {
      guiView.showWrongDateInputPopup();
      return;
    }

    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    if (startDate.compareTo(today.getTime()) >= 0 || endDate.compareTo(today.getTime()) >= 0) {
      guiView.showWrongDateInputPopup();
      return;
    }

    guiView.showProcessingPopup();
    ChartPlot chartPlot = userModel.getPerformanceForPortfolio(startDate, endDate,
            portfolioName);
    guiView.showBarChart(ChartPlotCalculatorUtil.getDateLabel(startDate,
                    chartPlot.getDateRangeType()),
            ChartPlotCalculatorUtil.getDateLabel(endDate, chartPlot.getDateRangeType()),
            portfolioName,
            chartPlot);
    guiView.closeProcessingPopup();
  }

  private boolean saveStrategyHelper(String portfolioName, String strategyType,
                                     String strategyName, String amountText, String commission,
                                     String tStartDateText, String tEndDateText,
                                     String tFrequencyCountText, List<String> stockNames,
                                     List<String> weights) {
    Date startDate;
    Date endDate;
    int frequency;

    double commissionDouble;
    StrategyModel strategyModel = null;
    double amount;

    List<Double> weightsList = new ArrayList<>();
    Map<StockModel, Double> stockWeightMap = new HashMap<>();

    if (strategyName.equals("") || !isStringAcceptable(strategyName)) {
      guiView.showStrategyNameShouldOnlyBeAlphaNumericPopup();
      return false;
    }

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);
    try {
      startDate = format.parse(tStartDateText);
    } catch (ParseException e) {
      guiView.showWrongDateInputPopup();
      return false;
    }

    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    if (startDate.compareTo(today.getTime()) >= 0) {
      guiView.showWrongDateInputPopup();
      return false;
    }

    if (tEndDateText.equals("")) {
      endDate = null;
    } else {
      try {
        endDate = format.parse(tEndDateText);
      } catch (ParseException e) {
        guiView.showWrongDateInputPopup();
        return false;
      }

      if (startDate.compareTo(endDate) > 0) {
        guiView.showWrongDateInputPopup();
        return false;
      }
    }

    try {
      commissionDouble = Double.parseDouble(commission);
      if (commissionDouble < 0) {
        guiView.showIncorrectCommissionPopup();
        return false;
      }
    } catch (NumberFormatException e) {
      guiView.showIncorrectCommissionPopup();
      return false;
    }

    try {
      amount = Double.parseDouble(amountText);
      if (amount <= 0) {
        guiView.showIncorrectAmountPopup();
        return false;
      }
    } catch (NumberFormatException e) {
      guiView.showIncorrectAmountPopup();
      return false;
    }

    if (commissionDouble > amount) {
      guiView.showIncorrectCommissionPopup();
      return false;
    }

    try {
      frequency = Integer.parseInt(tFrequencyCountText);
    } catch (NumberFormatException e) {
      guiView.showIncorrectFrequencyPopup();
      return false;
    }

    if (frequency <= 0) {
      guiView.showIncorrectFrequencyPopup();
      return false;
    }

    for (String weight : weights) {
      try {
        double weightDouble = Double.parseDouble(weight);
        weightsList.add(weightDouble);
      } catch (NumberFormatException e) {
        guiView.showIncorrectWeightPopup();
        return false;
      }
    }

    double totalWeight = 0;
    for (double weight : weightsList) {
      totalWeight += weight;
    }

    if (Math.abs(totalWeight - 100) > 0.02) {
      guiView.showIncorrectWeightPopup();
      return false;
    }

    for (int i = 0; i < stockNames.size(); i++) {
      StockModel stockModel = userStockModel.getStockSetModel().getStock(stockNames.get(i));
      stockWeightMap.put(new StockModelImpl(stockModel.getStockTicker(),
              stockModel.getStockName(),
              stockModel.getExchangeName(),
              stockModel.getIpoDate()), weightsList.get(i));
    }

    for (StrategyModel entry : userModel.getPortfolio(portfolioName).getAllStrategies()) {
      if (entry.getStrategyName().equals(strategyName)) {
        guiView.showSameStrategyNameAlreadyExist();
        return false;
      }
    }

    if (Objects.requireNonNull(Strategy.fromString(strategyType))
            == Strategy.DOLLAR_COST_AVERAGING) {
      strategyModel = DollarCostAveragingImpl.getBuilder()
              .amount(amount)
              .strategyName(strategyName)
              .startDate(startDate)
              .endDate(endDate)
              .dateFrequency(frequency)
              .stockMapPercent(stockWeightMap)
              .brokerCommission(commissionDouble)
              .build();
    }

    if (strategyModel == null) {
      guiView.showIncorrectStrategyPopup();
      return false;
    }

    guiView.showProcessingPopup();
    userModel.getPortfolio(portfolioName).addStrategy(strategyModel);
    userModel.getPortfolio(portfolioName).runAllStrategies();
    saveToDB(userModel);
    guiView.closeProcessingPopup();

    return true;
  }

  @Override
  public void saveStrategy(String portfolioName, String strategyType,
                           String strategyName, String amountText, String commission,
                           String tStartDateText, String tEndDateText,
                           String tFrequencyCountText, List<String> stockNames,
                           List<String> weights) {
    if (saveStrategyHelper(portfolioName, strategyType,
            strategyName, amountText,
            commission, tStartDateText,
            tEndDateText, tFrequencyCountText,
            stockNames, weights)) {
      guiView.closeAddStrategyPopup();
      guiView.showAddStrategySuccessPopup();
    }
  }

  private boolean validatePortfolioName(String portfolioName) {
    if (portfolioName.equals("") || !isStringAcceptable(portfolioName)) {
      guiView.showPortfolioNameShouldOnlyBeAlphaNumericPopup();
      return false;
    }

    if (userModel.getAllPortfolios().containsKey(portfolioName)) {
      guiView.showPortfolioNameAlreadyExists();
      return false;
    }

    return true;
  }

  @Override
  public void createFlexiblePortfolioWithoutStrategy(String portfolioName,
                                                     List<String> stockNames,
                                                     List<String> quantities,
                                                     List<String> commissions,
                                                     List<String> dates) {
    List<Integer> quantitiesInt = new ArrayList<>();
    List<Double> commissionsDouble = new ArrayList<>();
    List<Double> prices = new ArrayList<>();
    List<Date> transactionDates = new ArrayList<>();

    if (!validatePortfolioName(portfolioName)) {
      return;
    }

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);
    for (String date : dates) {
      try {
        Date currentDate = format.parse(date);
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        if (currentDate.compareTo(today.getTime()) >= 0) {
          guiView.showWrongDateInputPopup();
          return;
        }
        transactionDates.add(currentDate);
      } catch (ParseException e) {
        guiView.showWrongDateInputPopup();
        return;
      }
    }

    for (String quantity : quantities) {
      try {
        int quantityInt = Integer.parseInt(quantity);
        if (quantityInt < 0) {
          guiView.showIncorrectQuantityPopup();
          return;
        }

        quantitiesInt.add(quantityInt);
      } catch (NumberFormatException e) {
        guiView.showIncorrectQuantityPopup();
        return;
      }
    }

    for (String commission : commissions) {
      try {
        double commissionDouble = Double.parseDouble(commission);
        if (commissionDouble < 0) {
          guiView.showIncorrectCommissionPopup();
          return;
        }
        commissionsDouble.add(commissionDouble);
      } catch (NumberFormatException e) {
        guiView.showIncorrectCommissionPopup();
        return;
      }
    }


    guiView.showProcessingPopup();

    for (int i = 0; i < stockNames.size(); i++) {
      String stockName = stockNames.get(i);
      Date transactionDate = transactionDates.get(i);
      if (!userStockModel.getStockSetModel().getStock(stockName)
              .doesPriceExistOnADate(transactionDate)) {
        guiView.closeProcessingPopup();
        guiView.showNoPriceOnDatePopup(stockName);
        return;
      }
      double price = userStockModel.getStockSetModel().getStock(stockName)
              .getPriceOnADate(transactionDate);
      prices.add(price);
    }

    PortfolioModel portfolioModel = new StrategicalFlexiblePortfolioImpl
            .StrategicalFlexiblePortfolioBuilder()
            .portfolioName(portfolioName)
            .portfolioHashMap(new HashMap<>())
            .build();
    userModel.addPortfolio(portfolioModel);

    for (int i = 0; i < stockNames.size(); i++) {
      String tickerName = stockNames.get(i);
      StockModel stockModel = userStockModel.getStockSetModel().getStock(tickerName);
      PortfolioElementModel portfolioElementModel = PortfolioElementModelImpl.getBuilder()
              .stockModel(stockModel)
              .build();
      portfolioModel.addPortfolioElement(portfolioElementModel);
      TransactionModel transactionModel = TransactionModelImpl.getBuilder()
              .date(transactionDates.get(i))
              .price(prices.get(i))
              .qty(quantitiesInt.get(i))
              .brokerCommission(commissionsDouble.get(i))
              .exchange(stockModel.getExchangeName())
              .build();
      userModel.modifyPortfolio(portfolioName, stockModel, transactionModel);
    }

    saveToDB(userModel);

    guiView.closeProcessingPopup();

    guiView.closeAddFlexiblePortfolioWithoutStrategyPopup(
            new ArrayList<>(userModel.getAllFlexiblePortfolios().keySet()));
  }

  @Override
  public void createFlexiblePortfolioWithStrategy(String portfolioName,
                                                  String strategyType,
                                                  String strategyName,
                                                  String amountText,
                                                  String commission,
                                                  String tStartDateText,
                                                  String tEndDateText,
                                                  String tFrequencyCountText,
                                                  List<String> stockNames,
                                                  List<String> weights) {
    if (!validatePortfolioName(portfolioName)) {
      return;
    }

    PortfolioModel portfolioModel = new StrategicalFlexiblePortfolioImpl
            .StrategicalFlexiblePortfolioBuilder()
            .portfolioName(portfolioName)
            .portfolioHashMap(new HashMap<>())
            .build();
    userModel.addPortfolio(portfolioModel);

    if (saveStrategyHelper(portfolioName, strategyType, strategyName, amountText, commission,
            tStartDateText, tEndDateText, tFrequencyCountText, stockNames, weights)) {
      guiView.closeAddFlexiblePortfolioWithStrategyPopup(
              new ArrayList<>(userModel.getAllFlexiblePortfolios().keySet()));
    } else {
      userModel.removePortfolio(portfolioName);
      saveToDB(userModel);
    }

  }

  @Override
  public void modifyPortfolio(String portfolioName, String stockTicker,
                              String quantity, String commission, String buySell, String date) {
    int quantityInt = 0;
    double commissionDouble = 0;
    Date transactionDate;
    try {
      quantityInt = Integer.parseInt(quantity);
    } catch (NumberFormatException e) {
      guiView.showIncorrectQuantityPopup();
      return;
    }

    if (quantityInt < 0) {
      guiView.showIncorrectQuantityPopup();
      return;
    }

    if (buySell.equals("Sell")) {
      quantityInt = -1 * quantityInt;
    }

    double quantitySoFar = 0;
    PortfolioElementModel portfolioElementModel = userModel.getPortfolio(portfolioName)
            .getPortfolioElements()
            .get(stockTicker);
    if (portfolioElementModel != null) {
      quantitySoFar = portfolioElementModel.getStockTotalQuantity();
    }

    if (quantitySoFar + quantityInt < 0) {
      guiView.showIncorrectQuantityPopup();
      return;
    }

    try {
      commissionDouble = Double.parseDouble(commission);
      if (commissionDouble < 0) {
        guiView.showIncorrectCommissionPopup();
        return;
      }
    } catch (NumberFormatException e) {
      guiView.showIncorrectCommissionPopup();
      return;
    }

    try {
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      format.setLenient(false);
      transactionDate = format.parse(date);
    } catch (ParseException e) {
      guiView.showWrongDateInputPopup();
      return;
    }

    Calendar today = Calendar.getInstance();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 0);
    today.set(Calendar.MILLISECOND, 0);
    if (transactionDate.compareTo(today.getTime()) >= 0) {
      guiView.showWrongDateInputPopup();
      return;
    }

    guiView.showProcessingPopup();

    if (!userStockModel.getStockSetModel()
            .getStock(stockTicker).doesPriceExistOnADate(transactionDate)) {
      guiView.closeProcessingPopup();
      guiView.showNoPriceOnDatePopup(stockTicker);
      return;
    }
    double price = userStockModel.getStockSetModel()
            .getStock(stockTicker).getPriceOnADate(transactionDate);

    TransactionModel transactionModel = TransactionModelImpl.getBuilder()
            .date(transactionDate)
            .transactionSource("Manual")
            .brokerCommission(commissionDouble)
            .qty(quantityInt)
            .exchange(userStockModel.getStockSetModel().getStock(stockTicker).getExchangeName())
            .price(price)
            .build();

    userModel.modifyPortfolio(portfolioName,
            userStockModel.getStockSetModel().getStock(stockTicker), transactionModel);
    saveToDB(userModel);
    guiView.closeProcessingPopup();
    guiView.closeModifyPortfolioPopup();
    guiView.showModifyPortfolioSuccessPopup();
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
      guiView.showDBStoreErrorMessage();
    }
  }

  private boolean isStringAcceptable(String input) {
    Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(input);
    return !m.find() && input.length() != 0;
  }

}
