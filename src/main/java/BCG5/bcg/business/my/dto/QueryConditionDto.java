package BCG5.bcg.business.my.dto;

import java.util.List;

public class QueryConditionDto {
	
	private List<AliasDto> aliasDtoList;
	private List<PropertyDto> propertyDtoList;
	public List<AliasDto> getAliasDtoList() {
		return aliasDtoList;
	}
	public void setAliasDtoList(List<AliasDto> aliasDtoList) {
		this.aliasDtoList = aliasDtoList;
	}
	public List<PropertyDto> getPropertyDtoList() {
		return propertyDtoList;
	}
	public void setPropertyDtoList(List<PropertyDto> propertyDtoList) {
		this.propertyDtoList = propertyDtoList;
	}
	
}
