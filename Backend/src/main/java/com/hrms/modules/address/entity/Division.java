package com.hrms.modules.address.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "geo_divisions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Division{

    @Id

//    @TableGenerator(name="DIVISION_SEQ",initialValue=0,allocationSize=1)
//    @GeneratedValue(strategy = GenerationType.AUTO,generator = "DIVISION_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    private String name;
    private String bn_name;

    private String url;

//    @OneToMany(cascade = CascadeType.MERGE,mappedBy = "division_id" )
//    private List<District> districts;



}