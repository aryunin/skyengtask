package com.aryunin.skyengtask.repository;

import com.aryunin.skyengtask.entity.Package;
import org.springframework.data.repository.CrudRepository;

public interface PackagesRepository extends CrudRepository<Package, Long> {
}
