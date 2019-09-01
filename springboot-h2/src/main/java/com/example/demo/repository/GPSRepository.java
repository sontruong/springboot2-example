package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.GPX;

@Repository
public interface GPSRepository extends  PagingAndSortingRepository<GPX, Long>, JpaSpecificationExecutor<GPX> {

}
