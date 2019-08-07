package com.example.demo.domain;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "gpx_track_point")
public class TrackPoint {
	Double ele;
	Date time;
	Double lat;
	Double lon;

	public Double getEle() {
		return ele;
	}

	public void setEle(Double ele) {
		this.ele = ele;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

}
