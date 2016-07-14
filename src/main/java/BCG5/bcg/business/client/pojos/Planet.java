package BCG5.bcg.business.client.pojos;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@javax.persistence.Entity
@Table(name = "PLANET")
public class Planet {


	private String planetId;
	private String planetName;
	private Boolean planetHabitable;
	private Integer planetSize;
	private String planetType;
	
    @Id
    @Column(name = "PLANETID", nullable = false, unique = true)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getPlanetId() {
		return planetId;
	}
	public void setPlanetId(String planetId) {
		this.planetId = planetId;
	}
	
	@Column(name = "PLANETNAME")
	public String getPlanetName() {
		return planetName;
	}
	public void setPlanetName(String planetName) {
		this.planetName = planetName;
	}
	
	@Column(name = "PLANETHABITABLE")
	public Boolean getPlanetHabitable() {
		return planetHabitable;
	}
	public void setPlanetHabitable(Boolean planetHabitable) {
		this.planetHabitable = planetHabitable;
	}
	
	@Column(name = "PLANETSIZE")
	public Integer getPlanetSize() {
		return planetSize;
	}
	public void setPlanetSize(Integer planetSize) {
		this.planetSize = planetSize;
	}
	
	@Column(name = "PLANETTYPE")
	public String getPlanetType() {
		return planetType;
	}
	public void setPlanetType(String planetType) {
		this.planetType = planetType;
	}
	
	
	
}
