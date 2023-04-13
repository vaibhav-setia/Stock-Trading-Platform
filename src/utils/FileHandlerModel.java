package utils;

import java.io.IOException;

/**
 * This interface represents the complete set of I/O
 * operations that can be done on different files.
 */
public interface FileHandlerModel {
  /**
   * Read the file data from a source as string.
   *
   * @param path route where the file is stored.
   * @return the data in file as string only.
   * @throws IOException if file is not able to be read.
   */
  String readFileDataAsString(String path) throws IOException;

  /**
   * Get file path where a particular
   * ticker symbol list is stored.
   *
   * @param tickerName ticker whose data needs to be fetched.
   * @return file path as string only.
   */
  String getFilePathForATicker(String tickerName);

  /**
   * Get file path where tickers symbol list is stored.
   *
   * @return file path as string only.
   */
  String getFilePathForTickerList();

  /**
   * Method to check whether a file/directory exists.
   *
   * @param path file/directory path to be checked.
   * @return a true/false boolean only, whether path exists
   */
  boolean checkFileExists(String path);
}
