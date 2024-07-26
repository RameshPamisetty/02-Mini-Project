package com.ramesh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramesh.entity.State;

public interface StateRepo extends JpaRepository<State, Integer> {

	public List<State> findByCountryId(Integer countryId);
}
