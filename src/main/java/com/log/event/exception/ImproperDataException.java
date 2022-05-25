package com.log.event.exception;

public class ImproperDataException extends LogAnalyzerException {
  public ImproperDataException(String msg) {
    super(msg);
  }

  public ImproperDataException(String msg, Exception exception) {
    super(msg, exception);
  }
}
