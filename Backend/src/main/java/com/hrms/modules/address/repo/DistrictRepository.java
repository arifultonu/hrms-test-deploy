package com.hrms.modules.address.repo;

import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.address.entity.District;
import com.hrms.modules.address.entity.Division;
import com.hrms.modules.address.entity.Union;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DistrictRepository extends JpaRepository<District,Long>, JpaSpecificationExecutor<District> {
    List<District> findAllByDivision(Division division);


    Optional<District> findByName(String district);
}
