package com.ontology.sourcing2c.mapper;

import com.ontology.sourcing2c.model.SyncPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncMapper extends JpaRepository<SyncPojo, Integer> {

    // 根据规则自定义的

}