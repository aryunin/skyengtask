package com.aryunin.skyengtask.repository;

import com.aryunin.skyengtask.entity.Office;
import org.springframework.data.repository.CrudRepository;

public interface OfficesRepository extends CrudRepository<Office, String> {
}
