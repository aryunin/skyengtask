package com.aryunin.skyengtask.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PackageOfficeId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private PostalPackage postalPackage;
    @ManyToOne
    @JoinColumn(name = "office_index", nullable = false)
    private Office postOffice;
}
