package com.hrms.modules.AttnDataPush.Service;

import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.modules.AttnDataPush.Dto.PushData;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.repository.attn.HrTlAttnDailyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PushDataService {
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private HrTlAttnDailyRepository hrTlAttnDailyRepository;
    public void saveAll(List<PushData> pushData) {
        List<HrTlAttnDaily> hrTlAttnDailyList = new ArrayList<>();
        for (PushData pd : pushData) {
            // System.out.println("Code="+wrkDeviceAttData.getCode());
            HrCrEmp hrCrEmp1 = this.hrCrEmpRepository.findByLoginCode(pd.getCode());
            if(hrCrEmp1!=null)//checking for is this emp present in db of hrms??
            {
                // System.out.println("Code2="+wrkDeviceAttData.getCode()+" "+wrkDeviceAttData.getAriseDate()+" "+wrkDeviceAttData.getAriseTime());

                HrTlAttnDaily hrTlAttnDaily = new HrTlAttnDaily();
                hrTlAttnDaily.setAttnType("DEVICE");
                hrTlAttnDaily.setEntryTime(pd.getAriseTime().atDate(pd.getAriseDate()));
                hrTlAttnDaily.setEntryDate(pd.getAriseDate());
                hrTlAttnDaily.setEntryTimeOnly(pd.getAriseTime());
                hrTlAttnDaily.setSrcType("DEVICE");
                hrTlAttnDaily.setTrnscTime(LocalDateTime.now());
                HrCrEmp hrCrEmp = new HrCrEmp();
                //
                hrCrEmp.setLoginCode(pd.getCode());

                hrCrEmp.setId(this.hrCrEmpRepository.findByLoginCode(hrCrEmp.getLoginCode()).getId());
                //
                //hrCrEmp.setId(Long.parseLong(wrkDeviceAttData.getCode()));
                hrTlAttnDaily.setHrCrEmpId(hrCrEmp);
                hrTlAttnDaily.setCardNumber(pd.getCard());
                HrTlAttnDaily hrTlAttnDaily1 = this.hrTlAttnDailyRepository.findByCardNumberAndEntryTime(hrTlAttnDaily.getCardNumber(), hrTlAttnDaily.getEntryTime());

                if (hrTlAttnDaily1 == null) {
                    System.out.println(hrTlAttnDaily);
                    hrTlAttnDailyList.add(hrTlAttnDaily);
                }
            }
        }
        this.hrTlAttnDailyRepository.saveAll(hrTlAttnDailyList);
    }
}
