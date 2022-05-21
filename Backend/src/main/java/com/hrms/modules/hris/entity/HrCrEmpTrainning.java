package com.hrms.modules.hris.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HR_CR_EMP_TRAINING")
public class HrCrEmpTrainning {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String trainingType;
    private String trainingTitle;
    private String major;
    private String organization;
    private String durationType;
    private String durationValue;
    private String achievement;
    private String files;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "HR_CR_EMP_ID")
    private HrCrEmp hrCrEmp;

}
