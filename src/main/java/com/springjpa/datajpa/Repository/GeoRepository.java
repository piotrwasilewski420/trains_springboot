package com.springjpa.datajpa.Repository;

import com.springjpa.datajpa.Entity.Geo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoRepository extends CrudRepository<Geo,Integer> {
}
