package com.aryunin.skyengtask.repository;

import com.aryunin.skyengtask.entity.PostalPackage;
import com.aryunin.skyengtask.entity.PackageOffice;
import com.aryunin.skyengtask.entity.PackageOfficeId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PackageOfficeRepository extends CrudRepository<PackageOffice, PackageOfficeId> {

    @Query("SELECT po FROM PackageOffice po WHERE po.id.postalPackage = ?1 ORDER BY po.arrivalDate DESC")
    List<PackageOffice> getAllOfficesByPackage(PostalPackage pkg);
}
