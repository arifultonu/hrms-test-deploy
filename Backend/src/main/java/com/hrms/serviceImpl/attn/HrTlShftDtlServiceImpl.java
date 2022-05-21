package com.hrms.serviceImpl.attn;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.entity.attn.HrTlShftDtl;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.exception.CustomException;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.service.attn.IHrTlShftDtlService;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
public class HrTlShftDtlServiceImpl extends ServiceGenericImpl<HrTlShftDtl> implements IHrTlShftDtlService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Override
    public List<HrTlShftDtl> findAll() {

        return super.findAll();
    }

    @Override
    public HrTlShftDtl save(HrTlShftDtl entity) {
        entity.setActive(true);
        //login user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user=this.userRepository.findByUsername(currentPrincipalName);
        HrCrEmp hrCrEmp= this.hrCrEmpRepository.findByUser(user);
        entity.setHrCrEmpEntryById(hrCrEmp);

        long differ=HOURS.between(entity.getEndTime(),entity.getStartTime());
        if(differ>12)
        {
            differ=24-differ;
            entity.setShiftAbnormal(true);
        }
        else
        {
            differ=(-1*(differ));
        }
        entity.setShiftHour(differ);
        return super.save(entity);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Optional<HrTlShftDtl> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public HrTlShftDtl update(HrTlShftDtl entity) throws CustomException {
        return super.update(entity);
    }
}
