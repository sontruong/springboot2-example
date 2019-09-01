package com.example.demo.domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "gpx")
public class GPX {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String filename;
	
	@JsonIgnore
	@Lob
	@Column(name = "file_data", columnDefinition="BLOB")
	byte[] fileData;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "meta_id", referencedColumnName = "id")
	private GpxMetadata metadata;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "gpx_wpt", joinColumns = @JoinColumn(name = "gpx_id"))
	Collection<Waypoint> wpt;

	@Transient
	Collection<Track> tracks;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public GpxMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(GpxMetadata metadata) {
		this.metadata = metadata;
	}

	public Collection<Waypoint> getWpt() {
		return wpt;
	}

	public void setWpt(Collection<Waypoint> wpt) {
		this.wpt = wpt;
	}

	public Collection<Track> getTracks() {
		return tracks;
	}

	public void setTracks(Collection<Track> tracks) {
		this.tracks = tracks;
	}
	
}
