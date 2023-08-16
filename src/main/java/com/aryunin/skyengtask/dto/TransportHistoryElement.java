package com.aryunin.skyengtask.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class TransportHistoryElement {
    private OfficeDTO office;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrival;
}
