package views;


import java.awt.Component;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.Features;
import models.Strategy;

/**
 * Panel class to show Create Portfolio With Strategy panel.
 * This panel extends the JPanel and all its features.
 */
public class CreatePortfolioWithStrategyPanel extends JPanel {
  private final List<String> stocks;
  private final List<Strategy> strategyTypes;
  private AddStrategyComponentPanel addStrategyComponentPanel;
  private JTextField tPortfolioName;
  private JButton bCreatePortfolio;

  /**
   * Public constructor to create portfolio with strategy panel.
   * @param stocks list of stocks available.
   * @param strategyTypes list of all strategies available.
   */
  public CreatePortfolioWithStrategyPanel(List<String> stocks, List<Strategy> strategyTypes) {
    this.stocks = stocks;
    this.strategyTypes = strategyTypes;
    initializeViews();
  }

  private void initializeViews() {
    this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

    addStrategyComponentPanel = new AddStrategyComponentPanel(stocks, strategyTypes);
    JLabel lPortfolioName = new JLabel("Portfolio Name", SwingConstants.CENTER);
    lPortfolioName.setAlignmentX(Component.CENTER_ALIGNMENT);

    bCreatePortfolio = new JButton("Create Portfolio");
    bCreatePortfolio.setAlignmentX(Component.CENTER_ALIGNMENT);

    tPortfolioName = new JTextField();
    tPortfolioName.setAlignmentX(Component.CENTER_ALIGNMENT);

    this.add(lPortfolioName);
    this.add(tPortfolioName);
    this.add(addStrategyComponentPanel);
    this.add(bCreatePortfolio);
  }

  protected void addFeatures(Features features) {
    addStrategyComponentPanel.addFeatures(features);
    bCreatePortfolio.addActionListener((e) ->
            features.createFlexiblePortfolioWithStrategy(tPortfolioName.getText(),
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
