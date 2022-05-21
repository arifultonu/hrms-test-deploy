package com.hrms.modules.commonJobProcess.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.modules.hris.entity.HrCrEmp;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.util.user.UserUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.helpers.Util;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonJobProcess {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "created_at",updatable = false)
    private LocalDate createDate;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy h:mm:ss a")
    @Column(name = "updated_at")
    private LocalDateTime updateDateTime;


//    private LocalTime jobStartTime;
//    private LocalTime jobEndTime;
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm:ss a")
      private LocalTime jobStartTime;
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm:ss a")
      private LocalTime jobEndTime;
      private LocalDate jobProcDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp createdBy ;

    @ManyToOne(fetch = FetchType.EAGER)
    private HrCrEmp processBy;

    private String jobStatus;

    private String jobTitle;

    public String jobType;


    private LocalDate procToDate;


    private LocalDate procFromDate;

    private String empIds;

    private Boolean isAllEmp;


}
