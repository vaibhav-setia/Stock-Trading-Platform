package views;

import java.io.File;

import javax.swing.JFileChooser;

import controller.Features;

/**
 * Class for choosing a custom file.
 * This class extends the JFileChooser class and all its features.
 */
public class CustomFileChooser extends JFileChooser {
  private Features features;

  @Override
  public void approveSelection() {
    File selectedFile = getSelectedFile();
    features.setSelectedFile(selectedFile);
    super.approveSelection();
  }

  public void cancelSelection() {
    features.requestCloseCreateFlexiblePortfolioWithXML();
    super.cancelSelection();
  }

  protected void addFeatures(Features features) {
    this.features = features;
  }
}
