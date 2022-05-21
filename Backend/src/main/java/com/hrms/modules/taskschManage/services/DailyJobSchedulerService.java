package com.hrms.modules.taskschManage.services;

import com.hrms.exception.CustomException;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.onTour.entity.HrCrOnTourTnx;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import com.hrms.modules.taskschManage.repository.DailyJobSchedulerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Map;
import java.util.Optional;

@Service
public class DailyJobSchedulerService {
    @Autowired
    private DailyJobSchedulerRepository dailyJobSchedulerRepository;

    public DailyJobScheduler save(DailyJobScheduler dailyJobScheduler) throws CustomException {
       DailyJobScheduler dailyJobScheduler1=this.dailyJobSchedulerRepository.findByJobTitle(dailyJobScheduler.getJobTitle());
       if(dailyJobScheduler1==null)
       {
           return this.dailyJobSchedulerRepository.save(dailyJobScheduler);
       }
       else
       {
           throw new  CustomException("already exists");
       }

    }

    public Page<DailyJobScheduler> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<DailyJobScheduler> dailyJobSchedulers = this.dailyJobSchedulerRepository.findAll((Specification<DailyJobScheduler>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();

            return p;
        }, pageable);
        return dailyJobSchedulers;
    }

    public String  stsChange(Long id) {
        DailyJobScheduler dailyJobScheduler=this.dailyJobSchedulerRepository.findById(id).get();
        if(dailyJobScheduler.getIsActive()==true)
        {
            dailyJobScheduler.setIsActive(false);
            this.dailyJobSchedulerRepository.save(dailyJobScheduler);
            return "Scheduler Stopped";
        }
        else
        {
            dailyJobScheduler.setIsActive(true);
            this.dailyJobSchedulerRepository.save(dailyJobScheduler);
            return "Scheduler Started";
        }


    }
}
