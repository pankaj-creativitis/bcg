package BCG5.bcg.business.my.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "CLASS_META_DATA")
public class ClassMetaData {
	
	private String classMetaDataId;
	private String classMetaDataName;
	private String classMetaDataType;
	private String classMetaDataArgument;
	private String classMetaDataModifier;
	private ClassData classData;
	
	@Id
    @Column(name = "CLASS_META_DATA_ID", nullable = false, unique = true)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getClassMetaDataId() {
		return classMetaDataId;
	}
	public void setClassMetaDataId(String classMetaDataId) {
		this.classMetaDataId = classMetaDataId;
	}
	
	@Column(name = "CLASS_META_DATA_NAME")
	public String getClassMetaDataName() {
		return classMetaDataName;
	}
	public void setClassMetaDataName(String classMetaDataName) {
		this.classMetaDataName = classMetaDataName;
	}
	
	@Column(name = "CLASS_META_DATA_TYPE")
	public String getClassMetaDataType() {
		return classMetaDataType;
	}
	public void setClassMetaDataType(String classMetaDataType) {
		this.classMetaDataType = classMetaDataType;
	}
	
	@Column(name = "CLASS_META_DATA_ARGUMENT")
	public String getClassMetaDataArgument() {
		return classMetaDataArgument;
	}
	public void setClassMetaDataArgument(String classMetaDataArgument) {
		this.classMetaDataArgument = classMetaDataArgument;
	}
	
	@Column(name = "CLASS_META_DATA_MODIFIER")
	public String getClassMetaDataModifier() {
		return classMetaDataModifier;
	}
	public void setClassMetaDataModifier(String classMetaDataModifier) {
		this.classMetaDataModifier = classMetaDataModifier;
	}
	
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CLASS_DATA_NAME", nullable = false)
	public ClassData getClassData() {
		return classData;
	}
	public void setClassData(ClassData classData) {
		this.classData = classData;
	}
	
	

}
