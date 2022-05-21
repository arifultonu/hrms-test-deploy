package com.hrms.tasksch;

import com.hrms.dto.attn.WrkDeviceAttData;
import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.repository.attn.HrTlAttnDailyRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.util.api.APIEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling
public class DailyAttendanceRowDataScheduler {


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HrTlAttnDailyRepository hrTlAttnDailyRepository;

    @Autowired
    HrCrEmpRepository hrCrEmpRepository;




  // @Scheduled(fixedDelay = 600000, initialDelay = 10)
    public void insertDailyAttendanceDataFromDevice()
    {
        System.out.println("rdc");
        List<WrkDeviceAttData> wrkDeviceAttDataArrayList = new ArrayList<>();
        WrkDeviceAttData[] wrkDeviceAttDatas=restTemplate.getForObject(APIEndPoint.GET_ATTENDANCE_DATA_ENDPOINT,WrkDeviceAttData[].class);

        List<HrTlAttnDaily>hrTlAttnDailyList= new ArrayList<>();
        for(WrkDeviceAttData wrkDeviceAttData : wrkDeviceAttDatas){

            HrTlAttnDaily hrTlAttnDaily= new HrTlAttnDaily();
            hrTlAttnDaily.setAttnType("DEVICE");
            hrTlAttnDaily.setEntryTime(wrkDeviceAttData.getAriseTime().toLocalTime().atDate(wrkDeviceAttData.getAriseDate().toLocalDate()));
            hrTlAttnDaily.setEntryDate(wrkDeviceAttData.getAriseDate().toLocalDate());
            hrTlAttnDaily.setEntryTimeOnly(wrkDeviceAttData.getAriseTime().toLocalTime());
            hrTlAttnDaily.setSrcType("DEVICE");
            hrTlAttnDaily.setTrnscTime(LocalDateTime.now());
            HrCrEmp hrCrEmp= new HrCrEmp();
            //
            hrCrEmp.setLoginCode(wrkDeviceAttData.getCode());
            hrCrEmp.setId(this.hrCrEmpRepository.findByLoginCode(hrCrEmp.getLoginCode()).getId());
            //
            //hrCrEmp.setId(Long.parseLong(wrkDeviceAttData.getCode()));
            hrTlAttnDaily.setHrCrEmpId(hrCrEmp);
            hrTlAttnDaily.setCardNumber(wrkDeviceAttData.getCard());

            HrTlAttnDaily hrTlAttnDaily1=this.hrTlAttnDailyRepository.findByCardNumberAndEntryTime(hrTlAttnDaily.getCardNumber(),hrTlAttnDaily.getEntryTime());

            if(hrTlAttnDaily1==null)
            {
                hrTlAttnDailyList.add(hrTlAttnDaily);
            }


        }
        this.hrTlAttnDailyRepository.saveAll(hrTlAttnDailyList);


    }



}
