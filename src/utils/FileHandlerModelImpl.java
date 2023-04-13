package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * This class implements the File Handler Model and all its functionalities.
 * This file interacts with system via I/O operations.
 */
public class FileHandlerModelImpl implements FileHandlerModel {
  @Override
  public String readFileDataAsString(String path) throws IOException {
    try (InputStream in = new FileInputStream(path);
         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
      return reader.lines().collect(Collectors.joining("\r\n"));
    }
  }

  @Override
  public String getFilePathForATicker(String tickerName) {
    return String.format("StockSetModel//tickers/%s.csv", tickerName);
  }

  @Override
  public String getFilePathForTickerList() {
    return "StockSetModel//tickers_supported.csv";
  }

  @Override
  public boolean checkFileExists(String path) {
    return new File(path).exists();
  }
}
