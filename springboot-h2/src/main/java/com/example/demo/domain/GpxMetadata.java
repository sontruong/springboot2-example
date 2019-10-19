package com.example.demo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "gpx_metadata")
public class GpxMetadata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String name;
	@Lob
	@Column(columnDefinition="CLOB")
	String desc;
	String author;
	@Column(name = "link_href")
	String linkHref;
	@Column(name = "link_text")
	String linkText;
	
	@Temporal(TemporalType.DATE)
	Date time;

	@JsonIgnore
	@OneToOne(mappedBy = "metadata")
	private GPX gpx;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLinkHref() {
		return linkHref;
	}

	public void setLinkHref(String linkHref) {
		this.linkHref = linkHref;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public GPX getGpx() {
		return gpx;
	}

	public void setGpx(GPX gpx) {
		this.gpx = gpx;
	}

}
