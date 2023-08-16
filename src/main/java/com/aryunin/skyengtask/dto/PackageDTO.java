package com.aryunin.skyengtask.dto;

import com.aryunin.skyengtask.entity.Package.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PackageDTO {
    private Type type;
    private String recipientIndex;
    private String recipientAddress;
    private String recipientName;
}
