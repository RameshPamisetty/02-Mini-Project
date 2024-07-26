package com.ramesh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramesh.entity.City;

public interface CityRepo extends JpaRepository<City, Integer> {

	public List<City> findByStateId(Integer stateId);

}
