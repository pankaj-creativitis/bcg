package BCG5.bcg.business.my.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="CLASS_DATA")
public class ClassData {
	
	private String classDataId;
	private String classDataName;
	private String classDataCode;
	private String classDataClassification;
	private List<ClassMetaData> metadataList; 
	
	@Id
    @Column(name = "CLASS_DATA_ID", nullable = false, unique = true)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getClassDataId() {
		return classDataId;
	}
	public void setClassDataId(String classDataId) {
		this.classDataId = classDataId;
	}
	
	@Column(name = "CLASS_DATA_CODE")
	public String getClassDataCode() {
		return classDataCode;
	}
	public void setClassDataCode(String classDataCode) {
		this.classDataCode = classDataCode;
	}
	
	@Column(name = "CLASS_DATA_CLASSIFICATION")
	public String getClassDataClassification() {
		return classDataClassification;
	}
	public void setClassDataClassification(String classDataClassification) {
		this.classDataClassification = classDataClassification;
	}
	
	@Column(name = "CLASS_DATA_NAME")
	public String getClassDataName() {
		return classDataName;
	}
	public void setClassDataName(String classDataName) {
		this.classDataName = classDataName;
	}
	
	@JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval= true)
	@JoinColumn(name="CLASS_DATA_NAME", referencedColumnName="CLASS_DATA_NAME")
	public List<ClassMetaData> getMetadataList() {
		return metadataList;
	}
	public void setMetadataList(List<ClassMetaData> metadataList) {
		this.metadataList = metadataList;
	}
	
	

}
