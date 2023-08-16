package com.aryunin.skyengtask.dto;

import com.aryunin.skyengtask.entity.Package.Status;
import lombok.Data;

import java.util.List;

@Data
public class PackageTransportHistory {
    private Status status;
    private List<TransportHistoryElement> history;
}
