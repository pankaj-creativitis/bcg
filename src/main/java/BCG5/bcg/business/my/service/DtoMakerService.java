package BCG5.bcg.business.my.service;

import java.util.Set;

import BCG5.bcg.business.my.domain.DTO;

public interface DtoMakerService {
	
	void addDto(String dtoName, Set<String> fields);

//	void makeDtoClass(DTO dto);
}
