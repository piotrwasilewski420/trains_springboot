package com.springjpa.datajpa.Repository;

import com.springjpa.datajpa.Entity.Station;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends CrudRepository<Station,Integer> {
    Station findByName(String name);

    List<Station> findAll();
}
