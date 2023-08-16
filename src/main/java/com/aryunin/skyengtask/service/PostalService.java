package com.aryunin.skyengtask.service;

import com.aryunin.skyengtask.dto.PackageTransportHistory;
import com.aryunin.skyengtask.entity.Package;
import com.aryunin.skyengtask.entity.PackageOffice;

public interface PostalService {
    Package register(Package postPackage);

    PackageOffice addOffice(long packageId, String officeId);

    Package depart(long packageId);

    Package issue(long packageId);

    PackageTransportHistory getHistory(long packageId);
}
