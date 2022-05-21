package com.hrms.modules.dashboard.service;

import com.hrms.entity.attn.ProcOutDtAttn;
import com.hrms.modules.address.entity.District;
import com.hrms.modules.address.repo.DistrictRepository;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.modules.common.repository.AlkpRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.repository.attn.ProcOutDtAttnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;

import java.time.temporal.TemporalAdjusters;
import java.util.*;


@Service
public class AdminDashboardService {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManagerFactory efm;


    // get the number of total employees in the company
    public Integer getTotalEmployees(){

        String sqlStr = "SELECT COUNT(ID) AS TOTAL_EMP FROM HR_CR_EMP";
        String sqlStr1 = "SELECT COUNT(ID) AS TOTAL_EMP FROM HR_CR_EMP_PRIMARY_ASSGNMNT WHERE ALKP_EMP_STS=43";
        String sqlStr2 = "SELECT COUNT(ID) AS TOTAL_EMP FROM HR_CR_EMP_PRIMARY_ASSGNMNT WHERE ALKP_EMP_STS=43";
        return jdbcTemplate.queryForObject(sqlStr, Integer.class);

    }


    //get the number of total employees joined this month
    public Integer getTotalEmployeesJoinedLastMonth(){

        Date todayDate = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String today = df.format(todayDate);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date date = cal.getTime();
        String firstDay = df.format(date);

        String sqlString = "SELECT\n" +
                "   COUNT(ID) AS LASTMONTHJOINED \n" +
                "FROM\n" +
                "   HR_CR_EMP \n" +
                "WHERE\n" +
                "   JOINING_DATE >= TO_DATE('" + firstDay + "', 'yyyy-MM-dd') \n" +
                "   AND JOINING_DATE <= TO_DATE('" + today + "', 'yyyy-MM-dd')";

        return jdbcTemplate.queryForObject(sqlString, Integer.class);
    }


    // get the number of total employees are present today
    public Integer getTotalEmployeesPresentToday() {

        Date todayDate = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = df.format(todayDate);

        String sqlString = "SELECT Count(attn_day_sts_final_type) AS TOTAL_NUM\n" +
                "FROM   proc_out_dt_attn\n" +
                "WHERE  attn_day_sts_final_type IN ( 'Present', 'Late', 'Early Gone',\n" +
                "                                           'Late And Early Gone' )\n" +
                "       AND created_at = To_date('"+todayDateStr+"', 'yyyy-MM-dd')";

        return jdbcTemplate.queryForObject(sqlString, Integer.class);
    }


    // get the number of total employees are absent today
    public Integer getTotalEmployeesAbsentToday() {

        Date todayDate = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = df.format(todayDate);

        String sqlString = "SELECT Count(attn_day_sts_final_type) AS TOTAL_NUM\n" +
                "FROM   proc_out_dt_attn\n" +
                "WHERE  attn_day_sts_final_type IN ( 'Absent' )\n" +
                "       AND attn_date = To_date('"+todayDateStr+"', 'yyyy-MM-dd')";

        return jdbcTemplate.queryForObject(sqlString, Integer.class);
    }


    // get the number of total employees late today
    public Integer getTotalEmployeesLateToday() {

        Date todayDate = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = df.format(todayDate);

        String sqlString = "SELECT Count(attn_day_sts_final_type) AS TOTAL_NUM\n" +
                "FROM   proc_out_dt_attn\n" +
                "WHERE  attn_day_sts_final_type IN ( 'Late', 'Late And Early Gone' )\n" +
                "       AND attn_date = To_date('"+todayDateStr+"', 'yyyy-MM-dd')";

        return jdbcTemplate.queryForObject(sqlString, Integer.class);
    }


    // get the number of total employees early gone today
    public Integer getTotalEmployeesEarlyGoneToday() {

        Date todayDate = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = df.format(todayDate);

        String sqlString = "SELECT Count(attn_day_sts_final_type) AS TOTAL_NUM\n" +
                "FROM   proc_out_dt_attn\n" +
                "WHERE  attn_day_sts_final_type IN ( 'Early Gone' )\n" +
                "       AND attn_date = To_date('"+todayDateStr+"', 'yyyy-MM-dd')";

        return jdbcTemplate.queryForObject(sqlString, Integer.class);
    }


    // get the number of total employees are on tour today
    public Integer getTotalEmployeesOnTourToday() {

        Date todayDate = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = df.format(todayDate);

        String sqlString = "SELECT Count(attn_day_sts_final_type) AS TOTAL_NUM\n" +
                "FROM   proc_out_dt_attn\n" +
                "WHERE  attn_day_sts_final_type IN ( 'On Tour' )\n" +
                "       AND attn_date = To_date('"+todayDateStr+"', 'yyyy-MM-dd')";

        return jdbcTemplate.queryForObject(sqlString, Integer.class);
    }


    // get the number of total employees are on leave today
    public Integer getTotalEmployeesOnLeaveToday() {

        Date todayDate = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = df.format(todayDate);

        String sqlString = "SELECT Count(attn_day_sts_final_type) AS TOTAL_NUM\n" +
                "FROM   proc_out_dt_attn\n" +
                "WHERE  attn_day_sts_final_type IN ( 'ML', 'AL', 'CL', 'Matrimony Leave' )\n" +
                "       AND attn_date = To_date('"+todayDateStr+"', 'yyyy-MM-dd')";

        return jdbcTemplate.queryForObject(sqlString, Integer.class);
    }


    // get the number of employees in each department
    public Map<String, Object> getNumberOfEmployeeEachDepartment() {

        String sqlString = "SELECT\n" +
                "    COUNT(emp_code) AS emp_head_count,\n" +
                "    dept_name \n" +
                "FROM\n" +
                "    (\n" +
                "        SELECT\n" +
                "            emp.id AS emp_id,\n" +
                "            emp.code AS emp_code,\n" +
                "            asmnt.all_org_mst_department_id AS dept_id,\n" +
                "            mst.title AS dept_name \n" +
                "        FROM\n" +
                "            hr_cr_emp emp \n" +
                "            LEFT JOIN\n" +
                "                hr_cr_emp_primary_assgnmnt asmnt \n" +
                "                ON emp.id = asmnt.hr_cr_emp_id \n" +
                "            LEFT JOIN\n" +
                "                all_org_mst mst \n" +
                "                ON mst.id = asmnt.all_org_mst_department_id\n" +
                "    )\n" +
                "    mq \n" +
                "GROUP BY\n" +
                "    dept_name;";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlString);
        System.out.println("Size "+rows.size());

        List<Map<String, Object>> dataList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Map<String, Object> mapBean = new HashMap<>();
            mapBean.put("emp_head_count", row.get("emp_head_count"));
            mapBean.put("dept_name", row.get("dept_name"));
            dataList.add(mapBean);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", dataList);
        return response;
    }

    @Autowired
    private AlkpRepository alkpRepository;
    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;
    public Map<String, Object> getNumberOfEmployeeEachGender() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Alkp alkp=this.alkpRepository.findByKeyword("GENDER");
        for (Alkp ak:alkp.getSubALKP()) {
            List<HrCrEmp> hrCrEmpList=this.hrCrEmpRepository.findAllByAlkpGenderIdAlkp(ak);
            Map<String, Object> mapBean = new HashMap<>();
            mapBean.put("gender",ak.getTitle());
            mapBean.put("count",hrCrEmpList.size());
            dataList.add(mapBean);

        }
        Map<String, Object> response = new HashMap<>();
        response.put("objectList", dataList);
        return response;
    }

    @Autowired
    DistrictRepository districtRepository;
    public Map<String, Object> getNumberOfEmployeeEachDistrict() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<District>districtList =this.districtRepository.findAll();
        for (District ds:districtList) {
            List<HrCrEmp> hrCrEmpList=this.hrCrEmpRepository.findAllByDistrict(ds);
            Map<String, Object> mapBean = new HashMap<>();
            mapBean.put("district",ds.getName());
            mapBean.put("count",hrCrEmpList.size());
            dataList.add(mapBean);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("objectList", dataList);
        return response;
    }

    public Map<String, Object> getNumberOfEmployeeEachAgeGroup() {
        String sqlString = "select dob from hr_cr_emp;";
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlString);
        HashMap<String, Integer> ageGroupCount = new HashMap<String, Integer>();
        int i1520 = 0,i2025 = 0,i2530 = 0,i3035 = 0,i3540 = 0,i4045 = 0,i4550 = 0,i5055 = 0,i5560 = 0,i6065 = 0,i6570=0;
        String s1520="15-20",s2025="20-25",s2530="25-30",s3035="30-35",s3540="35-40",s4045="40-45",s4550="45-50",s5055="50-55",s5560="55-60",s6065="60-65",s6570="65-70";
        for (Map<String, Object> row : rows) {
            String dob=  row.get("dob").toString();
            LocalDate birthDate = LocalDate.parse(dob);
            LocalDate now=LocalDate.now();
            Period period = Period.between(birthDate, now);
            int age=period.getYears();



            if(age>=15&&age<=20)
            {
                i1520++;
                ageGroupCount.put(s1520,i1520);
            }
            else if(age>20&&age<=25)
            {
                i2025++;
                ageGroupCount.put(s2025,i2025);
            }
            else if(age>25&&age<=30)
            {
                i2530++;
                ageGroupCount.put(s2530,i2530);
            }
            else if(age>30&&age<=35)
            {
                i3035++;
                ageGroupCount.put(s3035,i3035);
            }
            else if(age>35&&age<=40)
            {
                i3540++;
                ageGroupCount.put(s3540,i3540);
            }
            else if(age>40&&age<=45)
            {
                i4045++;
                ageGroupCount.put(s4045,i4045);
            }
            else if(age>45&&age<=50)
            {
                i4550++;
                ageGroupCount.put(s4550,i4550);
            }
            else if(age>50&&age<=55)
            {
                i5055++;
                ageGroupCount.put(s5055,i5055);
            }
            else if(age>55&&age<=60)
            {
                i5560++;
                ageGroupCount.put(s5560,i5560);
            }
            else if(age>60&&age<=65)
            {
                i6065++;
                ageGroupCount.put(s6065,i6065);
            }
            else if(age>65&&age<=70)
            {
                i6570++;
                ageGroupCount.put(s6570,i6570);
            }

        }

        for (String i : ageGroupCount.keySet()) {
            Map<String, Object> mapBean = new HashMap<>();
            mapBean.put("ageGroup",i);
            mapBean.put("count",ageGroupCount.get(i));
            dataList.add(mapBean);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", dataList);
        return response;
    }

    @Autowired
    private ProcOutDtAttnRepository procOutDtAttnRepository;
    public Map<String, Object> getNumberOfEmployeeAttnThisMonth() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        LocalDate toDay=LocalDate.now();
        //LocalDate firstDayOfMonth = toDay.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate firstDayOfMonth = toDay.minusDays(6);


        for(LocalDate ld=firstDayOfMonth;ld.isBefore(toDay.plusDays(1));ld=ld.plusDays(1))
        {
            HashMap<String, Long> statusCount = new HashMap<>();

            Map<String, Object> mapBean = new HashMap<>();
            List<ProcOutDtAttn>procOutDtAttnList =this.procOutDtAttnRepository.findAllByAttnDate(ld);

            Long absentCount= procOutDtAttnList.stream().filter(c->c.getAttnDayStsFinalType().equals("Absent")).count();
            statusCount.put("Absent",absentCount);

            Long presentCount= procOutDtAttnList.stream().filter(c->c.getAttnDayStsFinalType().equals("Present")).count();
            statusCount.put("Present",presentCount);

            Long lateCount= procOutDtAttnList.stream().filter(c->c.getAttnDayStsFinalType().equals("Late")).count();
            statusCount.put("Late",lateCount);

            Long egCount= procOutDtAttnList.stream().filter(c->c.getAttnDayStsFinalType().equals("Early Gone")).count();
            statusCount.put("Early_Gone",egCount);

            Long ltegCount= procOutDtAttnList.stream().filter(c->c.getAttnDayStsFinalType().equals("Late And Early Gone")).count();
            statusCount.put("Late_And_Early_Gone",ltegCount);

            Long weekendCount= procOutDtAttnList.stream().filter(c->c.getAttnDayStsFinalType().equals("Weekend")).count();
            statusCount.put("Weekend",weekendCount);

            Long onTourCount= procOutDtAttnList.stream().filter(c->c.getAttnDayStsFinalType().equals("On Tour")).count();
            statusCount.put("On_Tour",onTourCount);

            mapBean.put("date",ld.toString());
            mapBean.put("data",statusCount);

            dataList.add(mapBean);
        }
        // System.out.println(dataList);

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", dataList);
        return response;
    }

    public Map<String, Object> getNumberOfEmployeeEachExperienceGroup() {
        String sqlString = "select experience_year,count(*) as empCount from hr_cr_emp group by experience_year";
        List<Map<String, Object>> dataList = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlString);


        for (Map<String, Object> row : rows) {
            Map<String, Object> mapBean = new HashMap<>();
            if(row.get("experience_year")==null)
            {
                mapBean.put("emp_head_count",row.get("empCount"));
                mapBean.put("type_of_ex", 0+" years");
            }
            else
            {
                mapBean.put("emp_head_count", row.get("empCount"));
                mapBean.put("type_of_ex", row.get("experience_year")+" years");
            }

            dataList.add(mapBean);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", dataList);
        return response;

    }


}
