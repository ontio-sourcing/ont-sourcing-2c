package com.ontology.sourcing2c.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tbl_sync")
public class SyncPojo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer complete;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;
}
