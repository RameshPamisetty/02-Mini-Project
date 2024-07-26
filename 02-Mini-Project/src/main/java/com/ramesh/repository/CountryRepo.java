package com.ramesh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramesh.entity.Country;

public interface CountryRepo extends JpaRepository<Country, Integer> {

}
