package com.log.event.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.log.event.model.Event;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Slf4j
public class DataParser {
    public static ArrayList<Event> parseLogFileData(BufferedReader br, File file, Connection dbConnection) {
        String line;
        ArrayList<Event> eventList = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) {
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();
                Event ev = null;
                if(line.startsWith("{") && line.endsWith("}")){
                    ev = gson.fromJson(line, Event.class);
                }else{
                    log.debug(""+"Event log entry is not in JSON format : ");
                }
                eventList.add(ev);
            }
        } catch (Exception ex) {
            log.error(String.valueOf(ex));
        }
        return eventList;
    }

    public static void compareEventTime(List<Event> allEventsList, Connection dbConnection, Properties properties) {

        HashMap<String, Event> eventMap = new HashMap<>();
        for (final Event event : allEventsList) {
            if (eventMap.containsKey(event.getId())) {
                if ("STARTED".equalsIgnoreCase(eventMap.get(event.getId()).getState())
                        && "FINISHED".equalsIgnoreCase(event.getState())) {
                    long exec_time_taken = event.getTimestamp() - eventMap.get(event.getId()).getTimestamp();
                    log.debug(
                            "Event "
                                    + event.getId()
                                    + " exits in Map and Time taken is : "
                                    + exec_time_taken);
                    //Insert Data into DB here
                    DbOperations.insertEventData(dbConnection,event,exec_time_taken,properties);
                    //Clearing compared data from Event Map
                    eventMap.remove(event.getId());

                } else {
                    log.debug("Entry for " + event.getId() + " event is not correct in log file");
                }
            } else {
                eventMap.put(event.getId(), event);
            }
        }
        log.debug("Un-Processed Events : " + eventMap);
        log.debug("Number of Un-Processed Events : " + eventMap.size());
    }

}
