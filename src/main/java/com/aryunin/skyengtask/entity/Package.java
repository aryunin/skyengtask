package com.aryunin.skyengtask.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Package {
    public enum Type {LETTER, PACKAGE, PARCEL, POSTCARD};
    public enum Status {REGISTERED, OFFICE, TRANSPORT, HANDED}
    @Id
    private String id;
    @Nonnull
    @Enumerated(EnumType.STRING)
    private Type type;
    @Nonnull
    private String recipientIndex;
    @Nonnull
    private String recipientAddress;
    @Nonnull
    private String recipientName;
    @Nonnull
    @Enumerated(EnumType.STRING)
    private Status status;
}
