package com.aryunin.skyengtask.dto;

import com.aryunin.skyengtask.entity.PostalPackage.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PackageTransportHistory {
    private Status status;
    private List<TransportHistoryElement> history;
}
