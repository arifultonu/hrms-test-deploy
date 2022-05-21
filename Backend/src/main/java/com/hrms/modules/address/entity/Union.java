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
@Table(name = "geo_unions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Union {
    @Id
//    @TableGenerator(name="UPAZILA_SEQ",initialValue=0,allocationSize=1)
//    @GeneratedValue(strategy = GenerationType.AUTO,generator = "UPAZILA_SEQ")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "upazilla_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Upazila upazila;
    private String name;
    private String bn_name;
    private String url;
}
