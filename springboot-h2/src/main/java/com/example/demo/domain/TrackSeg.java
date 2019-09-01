package com.example.demo.domain;

import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "gpx_trackseg")
public class TrackSeg {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	String name;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "track_id")
	private Track track;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "gpx_track_point", joinColumns = @JoinColumn(name = "seg_id"))
	Collection<TrackPoint> trkpt;

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

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public Collection<TrackPoint> getTrkpt() {
		return trkpt;
	}

	public void setTrkpt(Collection<TrackPoint> trkpt) {
		this.trkpt = trkpt;
	}

}
