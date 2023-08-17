package com.aryunin.skyengtask.service;

import com.aryunin.skyengtask.dto.PackageTransportHistory;
import com.aryunin.skyengtask.entity.PostalPackage;
import com.aryunin.skyengtask.entity.PackageOffice;

public interface PostalService {
    PostalPackage register(PostalPackage pkg);

    PackageOffice addOffice(long packageId, String officeId);

    PostalPackage depart(long packageId);

    PostalPackage issue(long packageId);

    PackageTransportHistory getHistory(long packageId);
}
