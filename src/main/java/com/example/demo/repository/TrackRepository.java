package com.example.demo.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Track;

@Repository
public interface TrackRepository extends  PagingAndSortingRepository<Track, Long>, JpaSpecificationExecutor<Track> {
	Collection<Track> findByGpxId(Long id);
}
