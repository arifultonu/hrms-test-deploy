package com.hrms.tasksch;



import com.hrms.dto.attn.WrkDeviceAttData2;
import com.hrms.entity.attn.HrTlAttnDaily;
import com.hrms.modules.apiManage.entity.ApiConfig;
import com.hrms.modules.apiManage.repository.ApiConfigRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.taskschManage.entity.DailyJobScheduler;
import com.hrms.modules.taskschManage.repository.DailyJobSchedulerRepository;
import com.hrms.repository.attn.HrTlAttnDailyRepository;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.util.api.APIEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Configuration
@EnableScheduling
public class DailyAttendanceRowDataScheduler2 {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
   private HrTlAttnDailyRepository hrTlAttnDailyRepository;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    @Autowired
    private DailyJobSchedulerRepository dailyJobSchedulerRepository;
    @Autowired
    private ApiConfigRepository apiConfigRepository;

    @Scheduled(fixedDelay =180000, initialDelay = 50000)
    public void insertDailyAttendanceDataFromDevice() {
        DailyJobScheduler dailyJobScheduler=this.dailyJobSchedulerRepository.findByJobTitle("ATTN_ROW_DATA_SCHEDULER");
        if(dailyJobScheduler.getIsActive()==false)
        {
            return;
        }


        String link=null;
        List<ApiConfig> apiConfig=this.apiConfigRepository.findByLinkTypeAndIsActive("A",true);
        if(apiConfig.size()!=0)
        {
            link=apiConfig.get(0).getLinkBody();
        }

        // System.out.println("raw data call");
//        WrkDeviceAttData2[] wrkDeviceAttDatas = restTemplate.getForObject(APIEndPoint.GET_ATTENDANCE_DATA_ENDPOINT2, WrkDeviceAttData2[].class);
        WrkDeviceAttData2[] wrkDeviceAttDatas = restTemplate.getForObject(link, WrkDeviceAttData2[].class);

        List<HrTlAttnDaily> hrTlAttnDailyList = new ArrayList<>();
        for (WrkDeviceAttData2 wrkDeviceAttData : wrkDeviceAttDatas) {
           // System.out.println("Code="+wrkDeviceAttData.getCode());
            HrCrEmp hrCrEmp1 = this.hrCrEmpRepository.findByLoginCode(wrkDeviceAttData.getCode());
            if(hrCrEmp1!=null)//checking for is this emp present in db of hrms??
            {
               // System.out.println("Code2="+wrkDeviceAttData.getCode()+" "+wrkDeviceAttData.getAriseDate()+" "+wrkDeviceAttData.getAriseTime());

            HrTlAttnDaily hrTlAttnDaily = new HrTlAttnDaily();
            hrTlAttnDaily.setAttnType("DEVICE");
            hrTlAttnDaily.setEntryTime(wrkDeviceAttData.getAriseTime().atDate(wrkDeviceAttData.getAriseDate()));
            hrTlAttnDaily.setEntryDate(wrkDeviceAttData.getAriseDate());
            hrTlAttnDaily.setEntryTimeOnly(wrkDeviceAttData.getAriseTime());
            hrTlAttnDaily.setSrcType("DEVICE");
            hrTlAttnDaily.setTrnscTime(LocalDateTime.now());
            HrCrEmp hrCrEmp = new HrCrEmp();
            //
            hrCrEmp.setLoginCode(wrkDeviceAttData.getCode());

            hrCrEmp.setId(this.hrCrEmpRepository.findByLoginCode(hrCrEmp.getLoginCode()).getId());
            //
            //hrCrEmp.setId(Long.parseLong(wrkDeviceAttData.getCode()));
            hrTlAttnDaily.setHrCrEmpId(hrCrEmp);
            hrTlAttnDaily.setCardNumber(wrkDeviceAttData.getCard());

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
