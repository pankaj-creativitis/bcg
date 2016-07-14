package BCG5.bcg.business.my.dto;

public class AliasDto {
	
	private String fieldName;
	private String aliasName;
	
	public AliasDto(String fieldName, String aliasName) {
		super();
		this.fieldName = fieldName;
		this.aliasName = aliasName;
	}
	
	public AliasDto() {
		super();
	}

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
}
