package com.aryunin.skyengtask.service.impl;

import com.aryunin.skyengtask.dto.OfficeDTO;
import com.aryunin.skyengtask.dto.PackageTransportHistory;
import com.aryunin.skyengtask.dto.TransportHistoryElement;
import com.aryunin.skyengtask.entity.Office;
import com.aryunin.skyengtask.entity.PackageOffice;
import com.aryunin.skyengtask.entity.PackageOfficeId;
import com.aryunin.skyengtask.entity.PostalPackage;
import com.aryunin.skyengtask.exception.InvalidStatusException;
import com.aryunin.skyengtask.exception.OfficeNotFoundException;
import com.aryunin.skyengtask.exception.PackageNotFoundException;
import com.aryunin.skyengtask.repository.OfficesRepository;
import com.aryunin.skyengtask.repository.PackageOfficeRepository;
import com.aryunin.skyengtask.repository.PackagesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostalServiceTest {
    @Mock
    private PackagesRepository packagesRepository;
    @Mock
    private OfficesRepository officesRepository;
    @Mock
    private PackageOfficeRepository packageOfficeRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private PostalServiceImpl postalService;
    private PostalPackage pkg;

    @BeforeEach
    void setUp() {
        pkg = new PostalPackage();
        pkg.setId(1L);
        pkg.setType(PostalPackage.Type.LETTER);
        pkg.setRecipientAddress("address");
        pkg.setRecipientName("name");
        pkg.setRecipientIndex("index");
    }

    @AfterEach
    void tearDown() {
        pkg = null;
    }

    @Test
    void register() {
        var data = new PostalPackage();
        data.setType(PostalPackage.Type.LETTER);
        data.setRecipientAddress("address");
        data.setRecipientName("name");
        data.setRecipientIndex("index");

        Mockito.when(packagesRepository.save(Mockito.any())).thenReturn(pkg);
        var actual = postalService.register(data);
        Mockito.verify(packagesRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(pkg.getStatus(), actual.getStatus());
    }

    @Test
    void addOffice() {
        var office = new Office();
        office.setIndex("123456");
        office.setName("name");
        office.setAddress("address");

        var pkgOfficeReturn = new PackageOffice();
        var pkgOfficeId = new PackageOfficeId();
        pkgOfficeId.setPostalPackage(pkg);
        pkgOfficeId.setPostOffice(office);
        pkgOfficeReturn.setArrivalDate(LocalDate.now());
        pkgOfficeReturn.setId(pkgOfficeId);

        Mockito.when(packagesRepository.findById(1L)).thenReturn(Optional.of(pkg));
        Mockito.when(packagesRepository.findById(2L)).thenReturn(Optional.empty());
        Mockito.when(officesRepository.findById("123456")).thenReturn(Optional.of(office));
        Mockito.when(officesRepository.findById("654321")).thenReturn(Optional.empty());
        Mockito.when(packageOfficeRepository.save(Mockito.any())).thenReturn(pkgOfficeReturn);

        var packageExc = assertThrows(PackageNotFoundException.class,
                () -> postalService.addOffice(2L, "123456"));
        assertTrue(Objects.requireNonNull(packageExc.getBody().getDetail())
                .contains("Package with id " + 2L)
        );

        var officeExc = assertThrows(OfficeNotFoundException.class,
                () -> postalService.addOffice(1L, "654321"));
        assertTrue(Objects.requireNonNull(officeExc.getBody().getDetail())
                .contains("Office with index 654321")
        );

        pkg.setStatus(PostalPackage.Status.OFFICE);
        var statusExc = assertThrows(InvalidStatusException.class,
                () -> postalService.addOffice(1L, "123456"));
        pkg.setStatus(PostalPackage.Status.HANDED);
        statusExc = assertThrows(InvalidStatusException.class,
                () -> postalService.addOffice(1L, "123456"));
        assertTrue(Objects.requireNonNull(statusExc.getBody().getDetail())
                .contains("status " + pkg.getStatus())
        );
        pkg.setStatus(PostalPackage.Status.REGISTERED);

        var pkgOffice = postalService.addOffice(1L, "123456");
        Mockito.verify(packageOfficeRepository, Mockito.times(1))
                .save(Mockito.any());

        assertEquals(LocalDate.now(), pkgOffice.getArrivalDate());
        assertEquals(PostalPackage.Status.OFFICE, pkgOffice.getId().getPostalPackage().getStatus());
        assertEquals(1L, pkgOffice.getId().getPostalPackage().getId());
        assertEquals("123456", pkgOffice.getId().getPostOffice().getIndex());
    }

    @Test
    void depart() {
        Mockito.when(packagesRepository.findById(1L)).thenReturn(Optional.of(pkg));
        Mockito.when(packagesRepository.findById(2L)).thenReturn(Optional.empty());
        Mockito.when(packagesRepository.save(Mockito.any())).thenReturn(pkg);

        var packageExc = assertThrows(PackageNotFoundException.class,
                () -> postalService.depart(2L));
        assertTrue(Objects.requireNonNull(packageExc.getBody().getDetail())
                .contains("Package with id " + 2L)
        );

        pkg.setStatus(PostalPackage.Status.TRANSPORT);
        var statusExc = assertThrows(InvalidStatusException.class,
                () -> postalService.depart(1L));
        pkg.setStatus(PostalPackage.Status.HANDED);
        statusExc = assertThrows(InvalidStatusException.class,
                () -> postalService.depart(1L));
        pkg.setStatus(PostalPackage.Status.REGISTERED);
        statusExc = assertThrows(InvalidStatusException.class,
                () -> postalService.depart(1L));
        assertTrue(Objects.requireNonNull(statusExc.getBody().getDetail())
                .contains("status " + pkg.getStatus())
        );
        pkg.setStatus(PostalPackage.Status.OFFICE);

        var res = postalService.depart(1L);
        Mockito.verify(packagesRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(PostalPackage.Status.TRANSPORT, res.getStatus());
    }

    @Test
    void issue() {
        Mockito.when(packagesRepository.findById(1L)).thenReturn(Optional.of(pkg));
        Mockito.when(packagesRepository.findById(2L)).thenReturn(Optional.empty());
        Mockito.when(packagesRepository.save(Mockito.any())).thenReturn(pkg);

        var packageExc = assertThrows(PackageNotFoundException.class,
                () -> postalService.issue(2L));
        assertTrue(Objects.requireNonNull(packageExc.getBody().getDetail())
                .contains("Package with id " + 2L)
        );

        pkg.setStatus(PostalPackage.Status.TRANSPORT);
        var statusExc = assertThrows(InvalidStatusException.class,
                () -> postalService.issue(1L));
        pkg.setStatus(PostalPackage.Status.HANDED);
        statusExc = assertThrows(InvalidStatusException.class,
                () -> postalService.issue(1L));
        pkg.setStatus(PostalPackage.Status.REGISTERED);
        statusExc = assertThrows(InvalidStatusException.class,
                () -> postalService.issue(1L));
        assertTrue(Objects.requireNonNull(statusExc.getBody().getDetail())
                .contains("status " + pkg.getStatus())
        );
        pkg.setStatus(PostalPackage.Status.OFFICE);

        var res = postalService.issue(1L);
        Mockito.verify(packagesRepository, Mockito.times(1)).delete(Mockito.any());

        assertEquals(PostalPackage.Status.HANDED, res.getStatus());
    }

    @Test
    void getHistory() {
        Mockito.when(packagesRepository.findById(1L)).thenReturn(Optional.of(pkg));
        Mockito.when(packagesRepository.findById(2L)).thenReturn(Optional.empty());

        var packageExc = assertThrows(PackageNotFoundException.class,
                () -> postalService.issue(2L));
        assertTrue(Objects.requireNonNull(packageExc.getBody().getDetail())
                .contains("Package with id " + 2L)
        );

        var of1 = new Office();
        of1.setIndex("123");
        var of2 = new Office();
        of2.setIndex("456");

        var pkgOffice1 = new PackageOffice();
        pkgOffice1.setId(new PackageOfficeId(pkg, of1));
        pkgOffice1.setArrivalDate(LocalDate.now());

        var pkgOffice2 = new PackageOffice();
        pkgOffice2.setId(new PackageOfficeId(pkg, of2));
        pkgOffice2.setArrivalDate(LocalDate.now().minusDays(1));

        var pkgOfficesList = List.of(pkgOffice1, pkgOffice2);
        Mockito.when(packageOfficeRepository.getAllOfficesByPackage(Mockito.any())).thenReturn(pkgOfficesList);

        var ofDto1 = new OfficeDTO();
        ofDto1.setIndex(of1.getIndex());
        var ofDto2 = new OfficeDTO();
        ofDto2.setIndex(of2.getIndex());

        Mockito.when(modelMapper.map(Mockito.any(), Mockito.any())).thenReturn(ofDto1, ofDto2);

        var el1 = new TransportHistoryElement();
        el1.setOffice(ofDto1);
        el1.setArrival(pkgOffice1.getArrivalDate());
        var el2 = new TransportHistoryElement();
        el2.setOffice(ofDto2);
        el2.setArrival(pkgOffice2.getArrivalDate());
        var records = List.of(el1, el2);
        var history = new PackageTransportHistory();
        history.setHistory(records);
        history.setStatus(pkg.getStatus());

        assertEquals(history, postalService.getHistory(1L));
    }
}