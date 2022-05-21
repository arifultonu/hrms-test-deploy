package com.hrms.serviceImpl.attn;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.dto.attn.ProcOutDtAttnDTO;
import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.exception.CustomException;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.service.attn.IProcOutDtAttnService;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProcOutDtAttnServiceImpl extends ServiceGenericImpl<ProcOutDtAttn> implements IProcOutDtAttnService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Override
    public List<ProcOutDtAttn> findAll() {
        return super.findAll();
    }

    @Override
    public ProcOutDtAttn save(ProcOutDtAttn entity) {
        return super.save(entity);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    public Optional<ProcOutDtAttn> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public ProcOutDtAttn update(ProcOutDtAttn entity) throws CustomException {
        return super.update(entity);
    }

    public List<ProcOutDtAttnDTO> lastSevenDaysAttn() {
        //login user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        User user=this.userRepository.findByUsername(currentPrincipalName);


        HrCrEmp hrCrEmp= this.hrCrEmpRepository.findByUser(user);
       //HrCrEmp hrCrEmp= this.hrCrEmpRepository.findById(3L).get();
      //  List<ProcOutDtAttn> procOutDtAttnList=this.procOutDtAttnRepository.findFirst7ByhrCrEmpIdOrderByIdDesc(hrCrEmp);
        List<ProcOutDtAttn> procOutDtAttnList=this.procOutDtAttnRepository.findFirst7ByhrCrEmpIdOrderByThisCreateDateDesc(hrCrEmp);
        List<ProcOutDtAttnDTO>procOutDtAttnDTOList= new LinkedList<>();
        for (ProcOutDtAttn p:procOutDtAttnList) {
            ProcOutDtAttnDTO procOutDtAttnDTO= new ProcOutDtAttnDTO();
            if(p.getInTime()!=null)
            {
                //in time
                int hour=p.getInTime().getHour();
                int min=p.getInTime().getMinute();
                int sec=p.getInTime().getSecond();
                String inTime =  ((hour > 12) ? hour % 12 : hour) + ":" + (min < 10 ? ("0" + min) : min) + ":" + (sec < 10 ? ("0" + sec) : sec) + " "+ ((hour >= 12) ? "PM" : "AM");


                //out time
                int hour1=p.getOutTime().getHour();
                int min1=p.getOutTime().getMinute();
                int sec1=p.getOutTime().getSecond();
                String outTime =  ((hour1 > 12) ? hour1 % 12 : hour1) + ":" + (min1 < 10 ? ("0" + min1) : min1) + ":" + (sec1 < 10 ? ("0" + sec1) : sec1) + " "+ ((hour1 >= 12) ? "PM" : "AM");

                //set data to dto

                procOutDtAttnDTO.setAttnDate(p.getAttnDate());
                procOutDtAttnDTO.setOtHour(p.getOtHour());
                procOutDtAttnDTO.setHrCrEmpId(p.getHrCrEmpId());
                procOutDtAttnDTO.setProcDate(p.getProcDate());
                procOutDtAttnDTO.setRemarks(p.getRemarks());
                procOutDtAttnDTO.setAttnDayStsType(p.getAttnDayStsType());
                procOutDtAttnDTO.setInTime(inTime);
                procOutDtAttnDTO.setOutTime(outTime);
                procOutDtAttnDTO.setLateByMin(p.getLateByMin());
                procOutDtAttnDTO.setIsOffDayBill(p.getIsOffDayBill());
                procOutDtAttnDTO.setAttnDayStsFinalType(p.getAttnDayStsFinalType());
                procOutDtAttnDTO.setEarlyGoneByMin(p.getEarlyGoneByMin());
                procOutDtAttnDTO.setIsColor(p.getIsColor());
                procOutDtAttnDTO.setThisCreateDate(p.getThisCreateDate());
                procOutDtAttnDTO.setHrTlShftDtl(p.getHrTlShftDtl());



            }
            else
            {
                procOutDtAttnDTO.setThisCreateDate(p.getThisCreateDate());
                procOutDtAttnDTO.setIsColor(p.getIsColor());
                procOutDtAttnDTO.setAttnDayStsFinalType(p.getAttnDayStsFinalType());
                procOutDtAttnDTO.setHrTlShftDtl(p.getHrTlShftDtl());
            }

            procOutDtAttnDTOList.add(procOutDtAttnDTO);

        }
        return procOutDtAttnDTOList;

//        HrCrEmp hrCrEmp= this.hrCrEmpRepository.findById(4L).get();
//        return  this.procOutDtAttnRepository.findFirst7ByhrCrEmpIdOrderByIdDesc(hrCrEmp);
    }
}
