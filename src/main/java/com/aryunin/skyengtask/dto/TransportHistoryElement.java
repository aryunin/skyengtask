package com.aryunin.skyengtask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportHistoryElement {
    private OfficeDTO office;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrival;
}
