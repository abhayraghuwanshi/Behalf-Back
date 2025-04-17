package com.behalf.Eparser.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


public record FlightDetails(
        String from,
        String to,
        LocalDate date
) {
    public boolean isValid() {
        return from != null && !from.isEmpty() &&
                to != null && !to.isEmpty() &&
                date != null;
    }
}
