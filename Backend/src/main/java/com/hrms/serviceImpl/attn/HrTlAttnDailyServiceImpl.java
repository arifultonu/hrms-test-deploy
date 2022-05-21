package com.hrms.serviceImpl.attn;

import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.entity.leave.HrCrLeaveAssignYear;
import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.repository.attn.HrTlAttnDailyRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.service.attn.IHrTlAttnDailyService;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class HrTlAttnDailyServiceImpl extends ServiceGenericImpl<HrTlAttnDaily> implements IHrTlAttnDailyService {
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private HrTlAttnDailyRepository hrTlAttnDailyRepository;
    @Override
    public List<HrTlAttnDaily> findAll() {

        return super.findAll();

    }

    @Override
    public HrTlAttnDaily save(HrTlAttnDaily entity) {
        return super.save(entity);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Optional<HrTlAttnDaily> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public HrTlAttnDaily update(HrTlAttnDaily entity) throws CustomException {
        return super.update(entity);
    }


    public List<HrTlAttnDaily> findAllBySrcType(String srcType)
    {
        return this.hrTlAttnDailyRepository.findAllBySrcTypeOrderByIdDesc(srcType);
    }


    @Override
    public Page<HrTlAttnDaily> findAllBySrcType2(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);



        Page<HrTlAttnDaily> hrTlAttnDailies = this.hrTlAttnDailyRepository.findAll((Specification<HrTlAttnDaily>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();

            // cq.orderBy(cb.asc(root.get("dgOrder")));
//            if(!clientParams.isEmpty()){
//                if(clientParams.containsKey("srcType")){
//                    if (StringUtils.hasLength(clientParams.get("srcType"))) {
//
//                        p = cb.and(p, cb.equal(root.get("srcType"), clientParams.get("srcType")));
//                        // p = cb.and(p, cb.like(root.get("loginCode"), "%" + clientParams.get("loginCode") + "%"));
//                    }
//                }
//            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("srcType2")||clientParams.containsKey("srcType")){
                    if (StringUtils.hasLength(clientParams.get("srcType2"))||StringUtils.hasLength(clientParams.get("srcType"))) {
                       // p = cb.and(p, cb.equal(root.get("srcType"), clientParams.get("srcType2")));
                       p=cb.and(p,cb.equal(root.get("srcType"),clientParams.get("srcType2")));
                       p=cb.or(p,cb.equal(root.get("srcType"),clientParams.get("srcType")));
                    }
                }
            }

            return p;



        }, pageable);
        System.out.println("ss=="+hrTlAttnDailies.getSize());
        return hrTlAttnDailies;
    }

    @Override
    public List<HrTlAttnDaily> searchRowAttn(Map<String, String> clientParams) {
        HrCrEmp hrCrEmp= this.hrCrEmpRepository.findByLoginCode(clientParams.get("eCode"));
        return this.hrTlAttnDailyRepository.findAllByEntryDateAndHrCrEmpIdAndSrcType(LocalDate.parse(clientParams.get("eDate")),hrCrEmp,clientParams.get("srcType"));
    }


}
