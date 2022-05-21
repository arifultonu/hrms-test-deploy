package com.hrms.modules.address.repo;

import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {

    Address findByAllOrgMst(AllOrgMst allOrgMst);

    List<Address> findAllByAllOrgMst(AllOrgMst allOrgMstInst);
}
