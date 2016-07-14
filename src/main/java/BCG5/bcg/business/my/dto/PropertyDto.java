package BCG5.bcg.business.my.dto;

public class PropertyDto {
	
	private String classProperty;
//	private String dtoProperty;
	private String pojoClassName;

	public PropertyDto() {
		super();
	}

	public PropertyDto(String classProperty, String pojoClassName) {
		super();
		this.classProperty = classProperty;
		this.pojoClassName = pojoClassName;
	}

	
	public String getPojoClassName() {
		return pojoClassName;
	}
	public void setPojoClassName(String pojoClassName) {
		this.pojoClassName = pojoClassName;
	}

	public String getClassProperty() {
		return classProperty;
	}
	public void setClassProperty(String classProperty) {
		this.classProperty = classProperty;
	}

	@Override
	public String toString() {
		return "PropertyDto [classProperty=" + classProperty + ", pojoClassName=" + pojoClassName + "]";
	}

	
	

}
