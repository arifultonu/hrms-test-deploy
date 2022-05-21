package com.hrms.modules.payroll.service;

import com.hrms.modules.hris.entity.HrCrEmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayrollComNative1Service {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Map<String, Object>> getAllActiveEmp( String procFromDate, String procToDate ){

        String sqlString = "" +
                "SELECT\n" +
                "    EMP.ID      AS EMP_ID,\n" +
                "    ASG.HR_CR_EMP_ID,\n" +
                "    EMP.CODE    AS EMP_CODE,\n" +
                "    EMP.LOGIN_CODE,\n" +
                "    EMP.FIRST_NAME,\n" +
                "    EMP.LAST_NAME,\n" +
                "    EMP.NICK_NAME,\n" +
                "    EMP.TITLE   AS EMP_TITLE,\n" +
                "    EMP.SAL_CURR,\n" +
                "    ASG.ALKP_EMP_STS,\n" +
                "    ASG.EMP_PAY_STS,\n" +
                "    PRL_ASG.GROSS_SALARY ,\n" +
                "    'foo' AS FOO\n" +
                "FROM\n" +
                "    HR_CR_EMP                    EMP\n" +
                "    LEFT JOIN HR_CR_EMP_PRIMARY_ASSGNMNT   ASG ON EMP.ID = ASG.HR_CR_EMP_ID\n" +
                "    LEFT JOIN PRL_ELMNT_ASGMNT  PRL_ASG ON EMP.ID = PRL_ASG.EMP_ID" +
                "    WHERE  PRL_ASG.ACTIVE_START_DATE<=TO_DATE('"+procToDate+"', 'yyyy-MM-dd')";

//        System.out.println(sqlString);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlString);


        /*List<HrCrEmp> empList = new ArrayList<>();
        for (Map row : rows) {
            HrCrEmp obj = new HrCrEmp();
            obj.setId( ( (BigDecimal)row.get("EMP_ID") ).longValue() );
            obj.setFirstName((String) row.get("EMP_CODE"));
            empList.add(obj);
        }*/

        List<Map<String, Object>> empList = new ArrayList<>();

        for (Map row : rows) {

            Map<String, Object> empBean = new HashMap<>();

            empBean.put("EMP_ID",row.get("EMP_ID"));
            empBean.put("EMP_CODE", row.get("EMP_CODE"));
            empBean.put("LOGIN_CODE", row.get("LOGIN_CODE"));
            empBean.put("FIRST_NAME", row.get("FIRST_NAME"));
            empBean.put("LAST_NAME", row.get("LAST_NAME"));
            empBean.put("EMP_TITLE", row.get("EMP_TITLE"));
            empBean.put("GROSS_SALARY", row.get("GROSS_SALARY"));
            empBean.put("SALARY_CURRENCY", row.get("SAL_CURR"));
            empBean.put("EMP_STATUS", row.get("EMP_STS"));

            empList.add(empBean);

        }

        return empList;

    }



    public Integer getTotalPresentDay( Long empId, String strProcFromDate, String strProcToDate){

        String sqlStr = "" +
                "SELECT\n" +
                "    COUNT(ATTN_DAY_STS_FINAL_TYPE) AS TOTAL_NUM\n" +
                "FROM\n" +
                "    PROC_OUT_DT_ATTN\n" +
                "WHERE\n" +
                "    HR_CR_EMP_ID = '"+empId+"'\n" +
                "    AND ATTN_DAY_STS_FINAL_TYPE = 'Present'\n" +
                "    AND ( ATTN_DATE >= TO_DATE('"+strProcFromDate+"', 'yyyy-MM-dd')\n" +
                "          AND ATTN_DATE <= TO_DATE('"+strProcToDate+"', 'yyyy-MM-dd') )";

        return jdbcTemplate.queryForObject(sqlStr, Integer.class);
    }


    public Integer getTotalAbsentDay( Long empId, String strProcFromDate, String strProcToDate ){
        String sqlStr = "" +
                "SELECT\n" +
                "    COUNT(ATTN_DAY_STS_FINAL_TYPE) AS TOTAL_NUM\n" +
                "FROM\n" +
                "    PROC_OUT_DT_ATTN\n" +
                "WHERE\n" +
                "    HR_CR_EMP_ID = '"+empId+"'\n" +
                "    AND ATTN_DAY_STS_FINAL_TYPE = 'Absent'\n" +
                "    AND ( ATTN_DATE >= TO_DATE('"+strProcFromDate+"', 'yyyy-MM-dd')\n" +
                "          AND ATTN_DATE <= TO_DATE('"+strProcToDate+"', 'yyyy-MM-dd') )";

        return jdbcTemplate.queryForObject(sqlStr, Integer.class);
    }


    public double getOvertimeAllowance(HrCrEmp hrCrEmp, String strProcFromDate, String strProcToDate) {
        String sqlStr = "" +
                "SELECT\n" +
                "    * \n" +
                "FROM\n" +
                "    prl_payroll_element_value \n" +
                "WHERE\n" +
                "  (emp_id = '"+hrCrEmp.getId()+"' \n" +
                "    OR emp_code = '"+hrCrEmp.getLoginCode()+"' ) \n" +
                "    AND element_title = 'OT_ALW' \n" +
                "    AND active_start_date = TO_DATE('"+strProcFromDate+"', 'yyyy-MM-dd')\n" +
                "    AND active_end_date = TO_DATE('"+strProcToDate+"', 'yyyy-MM-dd') " +
                "ORDER BY\n" +
                "    id ASC;";
        System.out.println(sqlStr);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlStr);
        for (Map row : rows) {

            Map<String, Object> empBean = new HashMap<>();

           if (row.get("ELEMENT_AMOUNT") != null) {
               return (double) row.get("ELEMENT_AMOUNT");
           }else{
               return 0.0;
           }
        }
        return 0.0;

    }

    public double getFoodDeduction(HrCrEmp empInst, String strProcFromDate, String strProcToDate) {
        String sqlStr = "" +
                "SELECT\n" +
                "    * \n" +
                "FROM\n" +
                "    prl_payroll_element_value \n" +
                "WHERE\n" +
                "  (emp_id = '"+empInst.getId()+"' \n" +
                "    OR emp_code = '"+empInst.getLoginCode()+"' ) \n" +
                "    AND element_title = 'FOOD_DD' \n" +
                "    AND active_start_date = TO_DATE('"+strProcFromDate+"', 'yyyy-MM-dd')\n" +
                "    AND active_end_date = TO_DATE('"+strProcToDate+"', 'yyyy-MM-dd') " +
                "ORDER BY\n" +
                "    id ASC;";
        System.out.println(sqlStr);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlStr);
        for (Map row : rows) {

            Map<String, Object> empBean = new HashMap<>();

            if (row.get("ELEMENT_AMOUNT") != null) {
                return (double) row.get("ELEMENT_AMOUNT");
            }else{
                return 0.0;
            }
        }
        return 0.0;
    }

    public double getAcmDeduction(HrCrEmp empInst, String strProcFromDate, String strProcToDate) {
        String sqlStr = "" +
                "SELECT\n" +
                "    * \n" +
                "FROM\n" +
                "    prl_payroll_element_value \n" +
                "WHERE\n" +
                "  (emp_id = '"+empInst.getId()+"' \n" +
                "    OR emp_code = '"+empInst.getLoginCode()+"' ) \n" +
                "    AND element_title = 'ACCOMMODATION_DD' \n" +
        "    AND active_start_date = TO_DATE('"+strProcFromDate+"', 'yyyy-MM-dd')\n" +
                "    AND active_end_date = TO_DATE('"+strProcToDate+"', 'yyyy-MM-dd') " +
                "ORDER BY\n" +
                "    id ASC;";
        System.out.println(sqlStr);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlStr);
        for (Map row : rows) {

            Map<String, Object> empBean = new HashMap<>();

            if (row.get("ELEMENT_AMOUNT") != null) {
                return (double) row.get("ELEMENT_AMOUNT");
            }else{
                return 0.0;
            }
        }
        return 0.0;
    }
}
