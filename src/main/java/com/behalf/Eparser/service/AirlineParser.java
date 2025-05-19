package com.behalf.Eparser.service;

import com.behalf.Eparser.model.FlightDetails;

public interface AirlineParser {
    FlightDetails parse(String emailBody);
}
