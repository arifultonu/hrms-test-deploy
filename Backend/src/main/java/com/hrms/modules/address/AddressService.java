package com.hrms.modules.address;

import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.address.entity.*;
import com.hrms.modules.address.repo.*;
import com.hrms.modules.common.repository.AllOrgMstRepository;
import com.hrms.modules.hris.entity.HrCrEmpEducation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;

@Service
public class AddressService {

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private UpazilaRepository upazilaRepository;

    @Autowired
    private UnionRepository unionRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AllOrgMstRepository allOrgMstRepository;

    public Division getDivisionsById(Long id) {

        return divisionRepository.findById(id).get();
    }

    public List<District> getDistrictByDivId(Long id) {
        Division division = divisionRepository.findById(id).get();
        return districtRepository.findAllByDivision(division);
    }

    public List<Upazila> getUpazilaByDistrict(Long id) {
        District district = districtRepository.findById(id).get();
        return upazilaRepository.findByDistrict(district);
    }

    public List<Division> getAll() {
        return divisionRepository.findAll();
    }


    public Page<District> getAllPaginatedDistricts( Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        return this.districtRepository.findAll((Specification<District>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();

            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("divisionId")){
                    if (StringUtils.hasLength(clientParams.get("divisionId"))) {

                        Long divisionId = Long.parseLong( clientParams.get("divisionId") );
                        Division division = divisionRepository.findById(divisionId).get();
                        p = cb.and(p, cb.equal(root.get("division"), division));
                    }
                }
                if(clientParams.containsKey("name")){
                    if (StringUtils.hasLength(clientParams.get("name"))) {
                        p = cb.and(p, cb.like(root.get("name"), "%"+clientParams.get("name")+"%"));
                    }
                }

                return p;
            }

            return null;

        }, pageable);
    }

    public Page<Upazila> getAllPaginatedUpazilas(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);


        return this.upazilaRepository.findAll((Specification<Upazila>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();

            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("districtId")){
                    if (StringUtils.hasLength(clientParams.get("districtId"))) {

                        Long districtId = Long.parseLong( clientParams.get("districtId") );
                        District district = districtRepository.findById(districtId).get();
                        p = cb.and(p, cb.equal(root.get("district"), district));
                    }
                }

                if(clientParams.containsKey("name")){
                    if (StringUtils.hasLength(clientParams.get("name"))) {
                        p = cb.and(p, cb.like(root.get("name"), "%"+clientParams.get("name")+"%"));
                    }
                }


                return p;
            }

            return null;

        }, pageable);

    }


    public Page<Union> getAllPaginatedUnions(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.unionRepository.findAll((Specification<Union>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("upazilaId")){
                    if (StringUtils.hasLength(clientParams.get("upazilaId"))) {

                        Long upazilaId = Long.parseLong( clientParams.get("upazilaId") );
                        Upazila upazila = upazilaRepository.findById(upazilaId).get();
                        p = cb.and(p, cb.equal(root.get("upazila"), upazila));
                    }
                }

                if(clientParams.containsKey("name")){
                    if (StringUtils.hasLength(clientParams.get("name"))) {
                        p = cb.and(p, cb.like(root.get("name"), "%"+clientParams.get("name")+"%"));
                    }
                }
                return p;
            }
            return null;

        }, pageable);

    }

    public Address createAddress(Address address) {
        return this.addressRepository.save(address);
    }

    public Page<Address> getAllPaginatedAddresses(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.addressRepository.findAll((Specification<Address>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("allOrgMstId")){
                    if (StringUtils.hasLength(clientParams.get("allOrgMstId"))) {

                        Long allOrgMstId = Long.parseLong( clientParams.get("allOrgMstId") );
                        AllOrgMst allOrgMstInst = allOrgMstRepository.findById(allOrgMstId).get();
                        p = cb.and(p, cb.equal(root.get("allOrgMst"), allOrgMstInst));
                    }
                }
                return p;
            }
            return null;

        }, pageable);
    }

    public List<Address> findByAllOrgMstId(Long allOrgMstId) throws CustomException {

        //findById orElse throw ResourceNotFoundException
        AllOrgMst allOrgMstInst = allOrgMstRepository.findById(allOrgMstId).get();
        if(allOrgMstInst == null){
            throw new CustomException("No AllOrgMst found for id: " + allOrgMstId);
        }
        return addressRepository.findAllByAllOrgMst(allOrgMstInst);

    }
}
