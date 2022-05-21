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
@Table(name = "geo_upazilas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Upazila{

    @Id
//    @TableGenerator(name="UPAZILA_SEQ",initialValue=0,allocationSize=1)
//    @GeneratedValue(strategy = GenerationType.AUTO,generator = "UPAZILA_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id")
    @JsonIgnore
    private District district;
    private String name;
    private String bn_name;
    private String url;

//    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "upazilla_id" )
//    private List<Union> unions;



}
