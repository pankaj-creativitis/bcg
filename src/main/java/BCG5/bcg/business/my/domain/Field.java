package BCG5.bcg.business.my.domain;

import java.io.Serializable;
import java.util.Set;

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
@Table(name = "FIELD")
public class Field implements Serializable, Comparable<Field>{
	

	private String fieldId;
	private String fieldName;
	private String fieldDataType;
	private String fieldType;
	private String fieldArgument;
	private String fieldModifier;
	private String className;
	private String classType;
	private String fieldArgType;
	private String fieldReturnType;
	private Set<DTORelation> dtoRelations;
//	private ClassEntity classEntity;
	
	@Id
    @Column(name = "FIELD_ID", nullable = false, unique = true)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	
	@Column(name = "FIELD_NAME")
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Column(name = "FIELD_DATA_TYPE")
	public String getFieldDataType() {
		return fieldDataType;
	}
	public void setFieldDataType(String fieldDataType) {
		this.fieldDataType = fieldDataType;
	}
	
	@Column(name="CLASS_NAME")
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Column(name="FIELD_TYPE")
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	@Column(name="FIELD_ARGUMENT")
	public String getFieldArgument() {
		return fieldArgument;
	}
	public void setFieldArgument(String fieldArgument) {
		this.fieldArgument = fieldArgument;
	}
	
	@Column(name="FIELD_MODIFIER")
	public String getFieldModifier() {
		return fieldModifier;
	}
	public void setFieldModifier(String fieldModifier) {
		this.fieldModifier = fieldModifier;
	}
	
	@Column(name = "CLASS_TYPE")
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	
	@Column(name = "FIELD_ARG_TYPE")
	public String getFieldArgType() {
		return fieldArgType;
	}
	public void setFieldArgType(String fieldArgType) {
		this.fieldArgType = fieldArgType;
	}
	
	@Column(name = "FIELD_RET_TYPE")
	public String getFieldReturnType() {
		return fieldReturnType;
	}
	public void setFieldReturnType(String fieldReturnType) {
		this.fieldReturnType = fieldReturnType;
	}
	
//	@JsonManagedReference
//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval= true)
//	@JoinColumn(name="FIELD_ID", referencedColumnName="FIELD_ID", nullable = true, unique = true)
//	public DTORelation getDtoRelation() {
//		return dtoRelation;
//	}
//	public void setDtoRelation(DTORelation dtoRelation) {
//		this.dtoRelation = dtoRelation;
//	}
	
	@JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval= true)
	@JoinColumn(name="FIELD_ID", referencedColumnName="FIELD_ID", nullable = true, unique = true)
	public Set<DTORelation> getDtoRelations() {
		return dtoRelations;
	}
	public void setDtoRelations(Set<DTORelation> dtoRelations) {
		this.dtoRelations = dtoRelations;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
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
		Field other = (Field) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		return true;
	}
	@Override
	public int compareTo(Field field) {
		return (this.fieldName).compareTo(field.fieldName);
	}
	
	

}
