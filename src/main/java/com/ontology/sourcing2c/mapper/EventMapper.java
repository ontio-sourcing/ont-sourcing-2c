package com.ontology.sourcing2c.mapper;

import com.ontology.sourcing2c.dao.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventMapper extends JpaRepository<Event, Integer> {

    // 根据规则自定义的
    Event findByTxhash(String txhash);

}