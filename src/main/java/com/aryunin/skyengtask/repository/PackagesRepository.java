package com.aryunin.skyengtask.repository;

import com.aryunin.skyengtask.entity.PostalPackage;
import org.springframework.data.repository.CrudRepository;

public interface PackagesRepository extends CrudRepository<PostalPackage, Long> {
}
