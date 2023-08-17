package com.aryunin.skyengtask.controller;

import com.aryunin.skyengtask.dto.PackageDTO;
import com.aryunin.skyengtask.dto.PackageTransportHistory;
import com.aryunin.skyengtask.entity.PostalPackage;
import com.aryunin.skyengtask.service.PostalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Postal Controller", description = "processing packages")
public class PostalController {
    private final PostalService postalService;
    private final ModelMapper modelMapper;

    @Operation(
            summary = "Register a package",
            description = "Registering a package"
    )
    @PostMapping(produces = "application/json")
    public ResponseEntity<String> registerPackage(@RequestBody PackageDTO packageDTO) {
        log.info("registering package");
        var postPackage = modelMapper.map(packageDTO, PostalPackage.class);
        postalService.register(postPackage);
        log.info("the package has been registered");
        return ResponseEntity.created(URI.create("/")).body("The package has been successfully registered");
    }

    @Operation(
            summary = "Add the office to a package",
            description = "Simulation of the arrival of a package in the office"
    )
    @PostMapping(value = "/{packageId}/add-office/{officeId}", produces = "application/json")
    public ResponseEntity<String> addOffice(@PathVariable long packageId,
                                            @PathVariable String officeId) {
        log.info("adding office " + officeId + " to package " + packageId );
        postalService.addOffice(packageId, officeId);
        log.info("the office has been successfully added");
        return ResponseEntity.ok().body("The office has been successfully added");
    }

    @Operation(
            summary = "Depart a package",
            description = "Simulation of departing a package"
    )
    @PostMapping(value = "/{packageId}/depart", produces = "application/json")
    public ResponseEntity<String> departPackage(@PathVariable long packageId) {
        log.info("departing package " + packageId);
        postalService.depart(packageId);
        log.info("the package has been successfully departed");
        return ResponseEntity.ok().body("The package has been successfully departed");
    }

    @Operation(
            summary = "Issue a package",
            description = "Simulation of issuing a package"
    )
    @DeleteMapping(value = "/{packageId}", produces = "application/json")
    public ResponseEntity<String> issuePackage(@PathVariable long packageId) {
        log.info("issuing package " + packageId);
        postalService.issue(packageId);
        log.info("the package has been successfully issued");
        return ResponseEntity.ok().body("The package has been successfully issued");
    }

    @Operation(
            summary = "Transport history",
            description = "Getting the history of package transportation"
    )
    @GetMapping(value = "/{packageId}", produces = "application/json")
    public PackageTransportHistory getHistory(@PathVariable long packageId) {
        log.info("getting a history for package " + packageId);
        return postalService.getHistory(packageId);
    }
}
