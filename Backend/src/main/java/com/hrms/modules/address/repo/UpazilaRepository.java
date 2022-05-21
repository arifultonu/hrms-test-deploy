package com.hrms.modules.address.repo;

import com.hrms.modules.address.entity.District;
import com.hrms.modules.address.entity.Union;
import com.hrms.modules.address.entity.Upazila;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UpazilaRepository extends JpaRepository<Upazila,Long>, JpaSpecificationExecutor<Upazila> {

    List<Upazila> findByDistrict(District district);
}
