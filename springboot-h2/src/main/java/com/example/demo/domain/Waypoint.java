package com.example.demo.domain;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name = "gpx_wpt")
public class Waypoint {
	String name;
	String sym;
	Double lat;
	Double lon;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSym() {
		return sym;
	}

	public void setSym(String sym) {
		this.sym = sym;
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
