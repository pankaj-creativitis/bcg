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
import org.hibernate.type.TrueFalseType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@javax.persistence.Entity
@Table(name = "CLASS_ENTITY")
public class ClassEntity implements Serializable{
	
	private String classEntityId;
	private String className;
	private String classText;
	private String classType;
//	private String classParent;
//	private List<DTORelation> dtoRelations;
	private List<Field> classFields;
	
	@Id
    @Column(name = "CLASS_ENTITY_ID", nullable = false, unique = true)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getClassEntityId() {
		return classEntityId;
	}
	public void setClassEntityId(String classEntityId) {
		this.classEntityId = classEntityId;
	}
	
	@Column(name = "CLASS_ENTITY_NAME")
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Column(name = "CLASS_TEXT")
	public String getClassText() {
		return classText;
	}
	public void setClassText(String classText) {
		this.classText = classText;
	}
	
	@Column(name = "CLASS_TYPE")
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	
//	@Column(name = "CLASS_PARENT")
//	public String getClassParent() {
//		return classParent;
//	}
//	public void setClassParent(String classParent) {
//		this.classParent = classParent;
//	}
	@JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval= true)
	@JoinColumn(name="CLASS_ENTITY_ID", referencedColumnName="CLASS_ENTITY_ID")
	public List<Field> getClassFields() {
		return classFields;
	}
	public void setClassFields(List<Field> classFields) {
		this.classFields = classFields;
	}
	
//	@JsonManagedReference
//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval= true)
//	@JoinColumn(name="CLASS_ENTITY_ID", referencedColumnName="CLASS_ENTITY_ID", nullable = true)
//	public List<DTORelation> getDtoRelations() {
//		return dtoRelations;
//	}
//	public void setDtoRelations(List<DTORelation> dtoRelations) {
//		this.dtoRelations = dtoRelations;
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassEntity other = (ClassEntity) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		return true;
	}

}
