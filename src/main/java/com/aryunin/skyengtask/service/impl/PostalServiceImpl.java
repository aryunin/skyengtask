package com.aryunin.skyengtask.service.impl;

import com.aryunin.skyengtask.dto.OfficeDTO;
import com.aryunin.skyengtask.dto.PackageTransportHistory;
import com.aryunin.skyengtask.dto.TransportHistoryElement;
import com.aryunin.skyengtask.entity.Office;
import com.aryunin.skyengtask.entity.PostalPackage;
import com.aryunin.skyengtask.entity.PackageOffice;
import com.aryunin.skyengtask.entity.PackageOfficeId;
import com.aryunin.skyengtask.exception.InvalidStatusException;
import com.aryunin.skyengtask.exception.OfficeNotFoundException;
import com.aryunin.skyengtask.exception.PackageNotFoundException;
import com.aryunin.skyengtask.repository.OfficesRepository;
import com.aryunin.skyengtask.repository.PackageOfficeRepository;
import com.aryunin.skyengtask.repository.PackagesRepository;
import com.aryunin.skyengtask.service.PostalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostalServiceImpl implements PostalService {
    private final PackagesRepository packagesRepository;
    private final OfficesRepository officesRepository;
    private final PackageOfficeRepository packageOfficeRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostalPackage register(PostalPackage pkg) {
        pkg.setStatus(PostalPackage.Status.REGISTERED);
        var res = packagesRepository.save(pkg);
        log.info("package " + res.getId() + " has been saved");
        return res;
    }

    @Override
    public PackageOffice addOffice(long packageId, String officeId) {
        var office = getOfficeByID(officeId);
        var pkg = getPackageByID(packageId);

        log.info("checking for status");
        pkg = checkStatusForOffice(pkg);
        log.info("status has been successfully changed");

        log.info("creating a history record");
        var record = createPackageOffice(pkg, office);
        log.info("record has been created & saved");

        return record;
    }

    @Override
    public PostalPackage depart(long packageId) {
        var pkg = getPackageByID(packageId);

        log.info("checking for status");
        pkg = checkStatusForDeparting(pkg);
        log.info("status has been successfully changed");

        return pkg;
    }

    @Override
    public PostalPackage issue(long packageId) {
        var pkg = getPackageByID(packageId);

        log.info("checking for status");
        pkg = checkStatusForIssue(pkg);
        log.info("status has been successfully changed");

        packagesRepository.delete(pkg);
        return pkg;
    }

    @Override
    public PackageTransportHistory getHistory(long packageId) {
        var pkg = getPackageByID(packageId);
        log.info("filling history");
        var records = packageOfficeRepository.getAllOfficesByPackage(pkg)
                .stream()
                .map(
                        v -> new TransportHistoryElement(
                                modelMapper.map(v.getId().getPostOffice(), OfficeDTO.class), v.getArrivalDate()
                        )
                )
                .toList();
        var history = new PackageTransportHistory();
        history.setHistory(records);
        history.setStatus(pkg.getStatus());
        log.info("history has been filled");
        return history;
    }

    private PackageOffice createPackageOffice(PostalPackage pkg, Office office) {
        var record = new PackageOffice();
        var id = new PackageOfficeId();

        id.setPostOffice(office);
        id.setPostalPackage(pkg);

        var today = LocalDate.now();
        log.info("today is " + today.format(DateTimeFormatter.ISO_DATE));

        record.setId(id);
        record.setArrivalDate(today);

        return packageOfficeRepository.save(record);
    }

    private PostalPackage checkStatusForIssue(PostalPackage pkg) {
        var status = pkg.getStatus();
        if(status != PostalPackage.Status.OFFICE) {
            log.info("invalid package status " + status.name());
            throw new InvalidStatusException("Invalid package's status " + status.name());
        }
        log.info("the status is correct, changing to HANDED");
        pkg.setStatus(PostalPackage.Status.HANDED);
        return packagesRepository.save(pkg);
    }

    private PostalPackage checkStatusForDeparting(PostalPackage pkg) {
        var status = pkg.getStatus();
        if(status != PostalPackage.Status.OFFICE) {
            log.info("invalid package status " + status.name());
            throw new InvalidStatusException("Invalid package's status " + status.name());
        }
        log.info("the status is correct, changing to TRANSPORT");
        pkg.setStatus(PostalPackage.Status.TRANSPORT);
        return packagesRepository.save(pkg);
    }

    private PostalPackage checkStatusForOffice(PostalPackage pkg) {
        var status = pkg.getStatus();
        if(status != PostalPackage.Status.TRANSPORT && status != PostalPackage.Status.REGISTERED) {
            log.info("invalid package status " + status.name());
            throw new InvalidStatusException("Invalid package's status " + status.name());
        }
        log.info("the status is correct, changing to OFFICE");
        pkg.setStatus(PostalPackage.Status.OFFICE);
        return packagesRepository.save(pkg);
    }

    private Office getOfficeByID(String index) {
        log.info("finding an office with index " + index);
        var found = officesRepository.findById(index);
        if(found.isEmpty()) {
            log.info("office with index " + index + " not found");
            throw new OfficeNotFoundException("Office with index " + index + " not found");
        }
        log.info("the office has been found");
        return found.get();
    }

    private PostalPackage getPackageByID(Long id) {
        log.info("finding a package with id " + id);
        var found = packagesRepository.findById(id);
        if(found.isEmpty()) {
            log.info("package with id " + id + " not found");
            throw new PackageNotFoundException("Package with id " + id + " not found");
        }
        log.info("the package has been found");
        return found.get();
    }
}
