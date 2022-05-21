package com.hrms.modules.hris.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HR_CR_EMP_NOMINEE")
public class HrCrEmpNominee {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String nomineeName;
    private String relation;
    private String mobile;
    private String nidNominee;
    private String birsNo;
    private String bankAccInfo;

    @ColumnDefault("0")
    private Double shareOfPercentage;



    private String image;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dob;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn (name = "HR_CR_EMP_ID")
    private HrCrEmp hrCrEmp;

}
