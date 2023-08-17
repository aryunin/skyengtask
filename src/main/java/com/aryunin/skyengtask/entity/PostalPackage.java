package com.aryunin.skyengtask.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "package")
public class PostalPackage {
    public enum Type {LETTER, PACKAGE, PARCEL, POSTCARD}
    public enum Status {REGISTERED, OFFICE, TRANSPORT, HANDED}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
