package BCG5.bcg.business.my.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@javax.persistence.Entity
@Table(name = "DTO_RELATION")
public class DTORelation implements Serializable{
	
	private String dtoDetailId;
	private String dtoPojoClassName;
	private String dtoName;
	private String dtoFieldName;

	@Id
    @Column(name = "DTO_RELATION_ID", nullable = false, unique = true)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    public String getDtoDetailId() {
		return dtoDetailId;
	}

	public void setDtoDetailId(String dtoDetailsId) {
		this.dtoDetailId = dtoDetailsId;
	}

	@Column(name = "DTO_RELATION_POJO")
	public String getDtoPojoClassName() {
		return dtoPojoClassName;
	}

	public void setDtoPojoClassName(String dtoPojoClassName) {
		this.dtoPojoClassName = dtoPojoClassName;
	}

	@Column(name = "DTO_NAME")
	public String getDtoName() {
		return dtoName;
	}

	public void setDtoName(String dtoName) {
		this.dtoName = dtoName;
	}

	@Column(name = "DTO_RELATION_FIELD")
	public String getDtoFieldName() {
		return dtoFieldName;
	}

	public void setDtoFieldName(String dtoFieldName) {
		this.dtoFieldName = dtoFieldName;
	}	
	
	
	
	

}
