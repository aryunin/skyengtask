package com.aryunin.skyengtask.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Office {
    @Id
    private String index;
    @Nonnull
    private String name;
    @Nonnull
    private String address;
}
