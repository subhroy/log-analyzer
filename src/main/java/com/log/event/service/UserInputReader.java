package com.log.event.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class UserInputReader {

  public static File readUserInput(BufferedReader bufferedReader) {
    File file = null;
    try {
      log.info("Enter Log file with location (Ex: C:\\app-log\\logfile.txt) : ");
      String logFileLocation = bufferedReader.readLine();
      log.info("logFileLocation : " + logFileLocation);
      file = new File(logFileLocation);
    } catch (IOException e) {
      log.error("Exception in Reading user input file ", e.fillInStackTrace());
    }
    return file;
  }
}
