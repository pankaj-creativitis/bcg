package BCG5.bcg.business.client.pojos;
//package com.mean.business.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@javax.persistence.Entity
@Table(name = "STAR")
public class Star {
	
	private String starId;
	private String starName;
	private String starType;
	private Integer starSize;
	private List<Planet> starPlanets;
	
    @Id
    @Column(name = "PLANETID", nullable = false, unique = true)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getStarId() {
		return starId;
	}
	public void setStarId(String starId) {
		this.starId = starId;
	}
	
	@Column(name = "STARNAME")
	public String getStarName() {
		return starName;
	}
	public void setStarName(String starName) {
		this.starName = starName;
	}
	
	@Column(name = "STARTYPE")
	public String getStarType() {
		return starType;
	}
	public void setStarType(String starType) {
		this.starType = starType;
	}
	
	@Column(name = "STARSIZE")
	public Integer getStarSize() {
		return starSize;
	}
	public void setStarSize(Integer starSize) {
		this.starSize = starSize;
	}
	
    @JsonManagedReference
    @OneToMany(mappedBy = "star", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval= true)
	public List<Planet> getStarPlanets() {
		return starPlanets;
	}
	public void setStarPlanets(List<Planet> starPlanets) {
		this.starPlanets = starPlanets;
	}
	
	
	
	

}
