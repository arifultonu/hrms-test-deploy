package com.hrms.modules.apiManage.service;

import com.hrms.exception.CustomException;
import com.hrms.modules.apiManage.entity.ApiConfig;
import com.hrms.modules.apiManage.repository.ApiConfigRepository;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;

@Service
public class ApiConfigService {
    @Autowired
    private ApiConfigRepository apiConfigRepository;
    public ApiConfig save(ApiConfig apiConfig) throws CustomException {
        ApiConfig apiConfig1=this.apiConfigRepository.findByLinkBody(apiConfig.getLinkBody());
        if(apiConfig1==null)
        {
            if(apiConfig.getLinkBody().equals("http://192.168.134.92:8080/access/getAll2")||
                    apiConfig.getLinkBody().equals("http://192.168.61.66:9191/access/getAll2"))
            {
                apiConfig.setLinkType("A");
            }
            else
            {
                apiConfig.setLinkType("B");
            }

            return this.apiConfigRepository.save(apiConfig);
        }
        else
        {
            throw new CustomException("already exists");
        }
    }

    public Page<ApiConfig> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<ApiConfig> apiConfigs = this.apiConfigRepository.findAll((Specification<ApiConfig>) (root, cq, cb) -> {

            Predicate p = cb.conjunction();

            return p;
        }, pageable);
        return apiConfigs;
    }



    public String stsChange(Long id) {
        ApiConfig apiConfig=this.apiConfigRepository.findById(id).get();
        if(apiConfig.getIsActive())
        {
            apiConfig.setIsActive(false);
            this.apiConfigRepository.save(apiConfig);
            return "Api Stopped";
        }
        else
        {
            List<ApiConfig> apiConfigs=this.apiConfigRepository.findByLinkTypeAndIsActive(apiConfig.getLinkType(),true);
            if(apiConfigs.size()>0)
            {
                return "Please Stop Second Link";
            }

            apiConfig.setIsActive(true);
            this.apiConfigRepository.save(apiConfig);
            return "Api Started";
        }

    }
}
