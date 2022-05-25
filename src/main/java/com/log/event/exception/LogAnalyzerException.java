package com.log.event.exception;

public class LogAnalyzerException extends RuntimeException {

  public LogAnalyzerException(String msg) {
    super(msg);
  }

  public LogAnalyzerException(String msg, Exception exception) {
    super(msg, exception);
  }
}
