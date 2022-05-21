package com.hrms.serviceImpl.attn;

import com.hrms.entity.attn.HrTlShftAssign;
import com.hrms.entity.attn.HrTlShftDtl;
import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.repository.attn.HrTlShftAssignRepository;
import com.hrms.repository.attn.HrTlShftDtlRepository;
import com.hrms.service.attn.IHrTlShftAssignService;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class HrTlShftAssignServiceImpl extends ServiceGenericImpl<HrTlShftAssign> implements IHrTlShftAssignService {
    @Autowired
    private HrTlShftAssignRepository hrTlShftAssignRepository;
    @Autowired
    private HrTlShftDtlRepository hrTlShftDtlRepository;
    @Override
    public List<HrTlShftAssign> findAll() {
        return super.findAll();
    }

    @Override
    public HrTlShftAssign save(HrTlShftAssign entity) {


        HrTlShftAssign hrTlShftAssign=this.hrTlShftAssignRepository.findByHrCrEmpAndActiveStatus(entity.getHrCrEmp(),true);
        if(hrTlShftAssign!=null)
        {
            hrTlShftAssign.setActiveStatus(false);
            this.hrTlShftAssignRepository.save(hrTlShftAssign);
        }


        entity.setActiveStatus(true);
        return super.save(entity);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Optional<HrTlShftAssign> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public HrTlShftAssign update(HrTlShftAssign entity) throws CustomException {

        HrTlShftAssign hrTlShftAssign=this.hrTlShftAssignRepository.findByHrCrEmpAndActiveStatus(entity.getHrCrEmp(),true);
        if(hrTlShftAssign!=null)
        {
            hrTlShftAssign.setActiveStatus(false);
            this.hrTlShftAssignRepository.save(hrTlShftAssign);
        }
        entity.setActiveStatus(true);
        return super.save(entity);



    }

    public List<HrTlShftAssign> findAllByActiveStatus() {
        return  this.hrTlShftAssignRepository.findAllByActiveStatus(true);
    }

    @Override
    public Page<HrTlShftAssign> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<HrTlShftAssign> hrTlShftAssigns = this.hrTlShftAssignRepository.findAll((Specification<HrTlShftAssign>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("activeStatus")){
                    if (StringUtils.hasLength(clientParams.get("activeStatus"))) {
                        String activeStatus=clientParams.get("activeStatus");
                        boolean bool = Boolean.parseBoolean(activeStatus);
                        System.out.println(root.get("activeStatus"));
                        p = cb.and(p, cb.equal(root.get("activeStatus"), bool));

                    }
                }
            }

            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("hrCrEmp")){


                        p = cb.and(p, cb.equal(root.get("hrCrEmp").get("code"), clientParams.get("hrCrEmp")));

                    }
                }

            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("shiftTitle")){


               p=cb.and(p,cb.like(root.get("hrTlShftDtl").get("title"),"%"+clientParams.get("shiftTitle")+"%"));
                    //HrTlShftDtl hrTlShftDtl=this.hrTlShftDtlRepository.findByTitleContainingIgnoreCase(clientParams.get("shiftTitle"));
                    //p=cb.and(p,cb.equal(root.get("hrTlShftDtl"),hrTlShftDtl));



                }
            }

            return p;
        }, pageable);
        return hrTlShftAssigns;
    }

    @Override
    public void deleteById(Long id) {
        this.hrTlShftAssignRepository.deleteById(id);
    }


}
