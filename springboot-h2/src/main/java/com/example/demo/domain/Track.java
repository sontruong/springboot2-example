package com.example.demo.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "gpx_track")
public class Track {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;

	@Column(name = "gpx_id")
	Long gpxId;

	@OneToMany(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<TrackSeg> trkseg = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<TrackSeg> getTrkseg() {
		return trkseg;
	}

	public void setTrkseg(Collection<TrackSeg> trkseg) {
		this.trkseg = trkseg;
	}

	public Long getGpxId() {
		return gpxId;
	}

	public void setGpxId(Long gpxId) {
		this.gpxId = gpxId;
	}

}
