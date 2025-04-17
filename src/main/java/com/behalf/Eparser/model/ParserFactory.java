package com.behalf.Eparser.model;

import com.behalf.Eparser.service.AirlineParser;
import com.behalf.Eparser.service.impl.GenericParser;
import com.behalf.Eparser.service.impl.IndigoParser;

import java.util.Map;

public class ParserFactory {
    private static final Map<String, AirlineParser> PARSERS = Map.of(
            "INDIGO", new IndigoParser()
    );

    public static AirlineParser getParser(String airline) {
        return PARSERS.getOrDefault(airline, new GenericParser());
    }
}
