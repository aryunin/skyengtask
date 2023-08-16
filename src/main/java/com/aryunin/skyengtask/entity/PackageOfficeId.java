package com.aryunin.skyengtask.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class PackageOfficeId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private Package postPackage;
    @ManyToOne
    @JoinColumn(name = "office_index", nullable = false)
    private Office postOffice;
}
