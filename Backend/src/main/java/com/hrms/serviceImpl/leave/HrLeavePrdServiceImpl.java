package com.hrms.serviceImpl.leave;

import com.hrms.entity.leave.HrLeavePrd;
import com.hrms.exception.CustomException;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import com.hrms.service.leave.IHrLeavePrdService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class HrLeavePrdServiceImpl extends ServiceGenericImpl<HrLeavePrd> implements IHrLeavePrdService {
    @Override
    public List<HrLeavePrd> findAll() {
        return super.findAll();
    }

    @Override
    public HrLeavePrd save(HrLeavePrd entity) {
        return super.save(entity);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Optional<HrLeavePrd> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public HrLeavePrd update(HrLeavePrd entity) throws CustomException {
        return super.update(entity);
    }
}
