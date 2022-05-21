package com.hrms.modules.common.serviceImpl;

import com.hrms.modules.common.dto.AlkpDTO;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.exception.CustomException;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.modules.common.repository.AlkpRepository;
import com.hrms.modules.common.repository.AllOrgMstRepository;
import com.hrms.modules.common.service.IAlkpService;
import com.hrms.modules.irecruitment.applicant.Applicant;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AlkpServiceImpl extends ServiceGenericImpl<Alkp> implements IAlkpService {

    @Autowired
    private AlkpRepository alkpRepository;


    @Override
    public List<Alkp> findAll() {
        return super.findAll();
    }

    @Override
    public Alkp save(Alkp entity)  {
        return super.save(entity);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Optional<Alkp> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Alkp update(Alkp entity) throws CustomException {
        return super.update(entity);
    }

    @Override
    public List<Alkp> findActiveAlkp() {
        return this.alkpRepository.findAllByIsActiveTrue();
    }

    @Override
    public List<Alkp> findByIsActiveTrueAndParentIsNull() {
        return this.alkpRepository.findAllByIsActiveTrueAndParentIdIsNull();
    }


    @Override
    public Alkp create(AlkpDTO alkpDTO) {

        Alkp alkp = new Alkp();
        alkp.setTitle(alkpDTO.getTitle());
        alkp.setKeyword(alkpDTO.getKeyword());
        alkp.setIsActive(alkpDTO.isActive());
        alkp.setSequence(alkpDTO.getSequence());
        alkp.setCode(alkpDTO.getCode());
        if (alkpDTO.getParentId()!=null){
            Alkp alkpEntity = this.alkpRepository.findById(alkpDTO.getParentId()).get();
            alkp.setParentId(alkpEntity);
        }else{
            alkp.setParentId(null);
        }
        return this.alkpRepository.save(alkp);
    }

    @Override
    public Alkp search(String keyword) {
        return this.alkpRepository.findByKeyword(keyword);
    }


    public List<Alkp> getAllAlkpType(String alkpType) {
        return this.alkpRepository.findByAlkpType(alkpType);
    }

    public Alkp getById(Long id) {
        return this.findbyId(id);
    }
    public Alkp findbyId(Long id) {
        Optional<Alkp> entity = this.alkpRepository.findById(id);
        return entity.orElse(null);
    }
}
