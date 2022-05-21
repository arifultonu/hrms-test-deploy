package com.hrms.modules.irecruitment.telePhoneViva;

import com.hrms.dto.response.MessageResponse;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.modules.irecruitment.vacancy.Vacancy;
import com.hrms.modules.irecruitment.vacancy.VacancyRepository;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.*;


@Service
public class TelePhoneVivaService {

    @Autowired
    private TelePhoneVivaRepository telePhoneVivaRepository;



    /**
     * @return List
     */
    public List<telePhoneViva> getAll() {

        List<telePhoneViva> result = this.telePhoneVivaRepository.findAll();
        if (result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }

    }
    /**
     * @param clientParams Map
     * @param pageNum      int
     * @param pageSize     int
     * @param sortField    string
     * @param sortDir      string
     * @return page list
     */
    public Page<telePhoneViva> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.telePhoneVivaRepository.findAll((Specification<telePhoneViva>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {
                if (clientParams.containsKey("id")) {
                    if (StringUtils.hasLength(clientParams.get("id"))) {
                        p = cb.and(p, cb.equal(root.get("id"), clientParams.get("id")));
                    }
                }




                return p;
            }
            return null;
        }, pageable);
    }


    /**
     * @param entityInst instance
     * @return instance
     */
    public telePhoneViva setAttributeForCreateUpdate(telePhoneViva entityInst, String activeOperation) {

        if (activeOperation.equals("Create")) {
            entityInst.setCreationDateTime(new Date());
            entityInst.setCreationUser(UserUtil.getLoginUser());
        }
        else if (activeOperation.equals("Update")) {
            entityInst.setLastUpdateDateTime(new Date());
            entityInst.setLastUpdateUser(UserUtil.getLoginUser());

        }

        return entityInst;

    }





    /**
     * @param createEntity instance
     * @return instance
     */
    public telePhoneViva create(telePhoneViva createEntity) {

        // call setAttributeForCreateUpdate
        createEntity = this.setAttributeForCreateUpdate(createEntity, "Create");
        return this.telePhoneVivaRepository.save(createEntity);

    }

    /**
     * @param id Long
     * @return instance
     */
    public telePhoneViva findById(Long id) {
        Optional<telePhoneViva> entity = this.telePhoneVivaRepository.findById(id);
        return entity.orElse(null);
    }

    /**
     * @param id Long
     * @return instance
     */
    public telePhoneViva getById(Long id) {
        return this.findById(id);
    }

}
