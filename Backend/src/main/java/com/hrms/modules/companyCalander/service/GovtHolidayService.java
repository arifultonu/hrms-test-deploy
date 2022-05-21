package com.hrms.modules.companyCalander.service;

import com.hrms.modules.companyCalander.entity.GovtHoliday;
import com.hrms.modules.companyCalander.repository.GovtHolidayRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.shortLeave.entity.ShortLeave;
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
import java.util.Map;

@Service
public class GovtHolidayService {
    @Autowired
    private GovtHolidayRepository govtHolidayRepository;
    public GovtHoliday save(GovtHoliday govtHoliday) {
        //get the current year
        LocalDate localDate = LocalDate.now();
        if(govtHoliday.getHolidayYear().equals(String.valueOf(localDate.getYear()))){
            govtHoliday.setIsActive(true);
        }
        else {
            govtHoliday.setIsActive(false);
        }

        return this.govtHolidayRepository.save(govtHoliday);
    }

    public Page<GovtHoliday> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<GovtHoliday> govtHolidays = this.govtHolidayRepository.findAll((Specification<GovtHoliday>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("holidayName")) {
                    if (StringUtils.hasLength(clientParams.get("holidayName"))) {

                        GovtHoliday govtHoliday = this.govtHolidayRepository.findByHolidayNameContainingIgnoreCase(clientParams.get("holidayName"));
                        p=cb.equal(root.get("holidayName"),govtHoliday.getHolidayName());


                       // p=cb.and(p,cb.like(root.get("holidayName"),"%"+clientParams.get("holidayName")+"%"));
                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("year")) {
                    if (StringUtils.hasLength(clientParams.get("year"))) {

                      p=cb.and(p,cb.equal(root.get("holidayYear"),clientParams.get("year")));
                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("fromDate")) {
                    if (StringUtils.hasLength(clientParams.get("fromDate"))) {

                       p=cb.and(p,cb.greaterThanOrEqualTo(root.get("fromDate"), LocalDate.parse(clientParams.get("fromDate"))));
                    }
                }
            }
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("toDate")) {
                    if (StringUtils.hasLength(clientParams.get("toDate"))) {

                       p=cb.and(p,cb.lessThanOrEqualTo(root.get("toDate"),LocalDate.parse(clientParams.get("toDate"))));
                    }
                }
            }



            return p;
        }, pageable);
        return govtHolidays;
    }

    public GovtHoliday getById(Long id) {
        return this.govtHolidayRepository.findById(id).get();
    }

    public void delete(Long id) {
        this.govtHolidayRepository.deleteById(id);
    }
}
