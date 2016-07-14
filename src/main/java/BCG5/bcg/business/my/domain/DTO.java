package BCG5.bcg.business.my.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@javax.persistence.Entity
@Table(name = "DTO")
public class DTO implements Serializable{
	
	private String dtoId;
	private String dtoName;
//	private List<DTORelation> dtoDetails;
	
	@Id
    @Column(name = "DTO_ID", nullable = false, unique = true)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getDtoId() {
		return dtoId;
	}
	public void setDtoId(String dtoId) {
		this.dtoId = dtoId;
	}
	
	@Column(name = "DTO_NAME")
	public String getDtoName() {
		return dtoName;
	}
	public void setDtoName(String dtoName) {
		this.dtoName = dtoName;
	}
	
//	@JsonManagedReference
//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval= true)
//	@JoinColumn(name="DTO_NAME", referencedColumnName="DTO_NAME")
//	public List<DTORelation> getDtoDetails() {
//		return dtoDetails;
//	}
//	public void setDtoDetails(List<DTORelation> dtoDetails) {
//		this.dtoDetails = dtoDetails;
//	}
	
	

}
