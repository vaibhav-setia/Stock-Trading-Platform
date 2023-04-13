package views;


import java.awt.Component;
import java.util.List;


import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Features;
import models.Strategy;

/**
 * Panel class to show add strategy option.
 * This panel extends the jpanel and all its features.
 */
public class AddStrategyPanel extends JPanel {
  private AddStrategyComponentPanel addStrategyComponentPanel;

  private final String portfolioName;
  private final List<String> stocks;
  private final List<Strategy> strategyTypes;
  private JButton bSaveStrategy;

  /**
   * Public constructor to initialise a strategy panel.
   * @param portfolioName name of portfolio to be initialised.
   * @param stocks list of stocks to be initialised.
   * @param strategyTypes list of strategies avaialble to be initialised.
   */
  public AddStrategyPanel(String portfolioName,
                          List<String> stocks,
                          List<Strategy> strategyTypes) {
    this.portfolioName = portfolioName;
    this.stocks = stocks;
    this.strategyTypes = strategyTypes;
    initializeViews();
  }

  private void initializeViews() {
    addStrategyComponentPanel = new AddStrategyComponentPanel(stocks, strategyTypes);
    bSaveStrategy = new JButton("Save Strategy");
    bSaveStrategy.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    this.add(addStrategyComponentPanel);
    this.add(bSaveStrategy);
  }

  protected void addFeatures(Features features) {
    addStrategyComponentPanel.addFeatures(features);
    bSaveStrategy.addActionListener((e) -> features.saveStrategy(this.portfolioName,
            addStrategyComponentPanel.getStrategyType(),
            addStrategyComponentPanel.getStrategyName(),
            addStrategyComponentPanel.getAmount(),
            addStrategyComponentPanel.getCommission(),
            addStrategyComponentPanel.getStartDate(),
            addStrategyComponentPanel.getEndDate(),
            addStrategyComponentPanel.getFrequency(),
            addStrategyComponentPanel.getStockNames(),
            addStrategyComponentPanel.getWeights()));
  }

  protected AddStrategyComponentPanel getAddStrategyComponentPanel() {
    return this.addStrategyComponentPanel;
  }
}
