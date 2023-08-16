package com.aryunin.skyengtask.controller;

import com.aryunin.skyengtask.entity.Package;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostalController {
    private final PostalService postalService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<String> registerPackage(PackageDTO packageDTO) {
        log.info("registering package");
        var postPackage = modelMapper.map(packageDTO, Package.class);
        postalService.register(postPackage);
        log.info("the package has been registered");
        return ResponseEntity.created(URI.create("/")).body("The package has been successfully registered");
    }

    @PostMapping("/{packageId}/add-office/{officeId}")
    public ResponseEntity<String> addOffice(@PathVariable int packageId,
                                            @PathVariable String officeId) {
        log.info("adding office " + officeId + " to package " + packageId );
        postalService.addOffice(packageId, officeId);
        log.info("the office has been successfully added");
        return ResponseEntity.ok().body("The office has been successfully added");
    }

    @PostMapping("/{packageId}/depart")
    public ResponseEntity<String> departPackage(@PathVariable int packageId) {
        log.info("departing package " + packageId);
        postalService.depart(packageId);
        log.info("the package has been successfully departed");
        return ResponseEntity.ok().body("The package has been successfully departed");
    }

    @DeleteMapping("/{packageId}")
    public ResponseEntity<String> issuePackage(@PathVariable int packageId) {
        log.info("issuing package " + packageId);
        postalService.issue(packageId);
        log.info("the package has been successfully issued");
        return ResponseEntity.ok().body("The package has been successfully issued");
    }

    @GetMapping("/{packageId}")
    public PackageTransportHistory getHistory(@PathVariable int packageId) {
        log.info("getting a history for package " + packageId);
        return postalService.getHistory(packageId);
    }
}
