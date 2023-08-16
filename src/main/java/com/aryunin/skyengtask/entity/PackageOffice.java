package com.aryunin.skyengtask.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "package_office")
@NoArgsConstructor
public class PackageOffice {
    @EmbeddedId
    private PackageOfficeId id;
    @Nonnull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;
}
