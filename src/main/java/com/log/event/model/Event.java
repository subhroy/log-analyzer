package com.log.event.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Event {
  private String id;
  private String state;
  private String type;
  private String host;
  private long timestamp;
}
