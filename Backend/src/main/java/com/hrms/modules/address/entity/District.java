package com.hrms.modules.address.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "geo_districts")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class District {

    @Id
//    @TableGenerator(name="DISTRICT_SEQ",initialValue=0,allocationSize=1)
//    @GeneratedValue(strategy = GenerationType.AUTO,generator = "DISTRICT_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "division_id")
    @JsonIgnore
    private Division division;

    private String name;
    private String bn_name;

    private String lat;
    private String lon;
    private String url;

//
//    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "district")
//    private List<Upazila> upazilas;


}
