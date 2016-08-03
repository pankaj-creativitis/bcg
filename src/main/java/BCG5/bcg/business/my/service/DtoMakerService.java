package BCG5.bcg.business.my.service;

import java.util.Set;

import BCG5.bcg.business.my.dto.DtoRelationDto;

public interface DtoMakerService {

	void addDtoNew(String dtoName, Set<DtoRelationDto> fields, String baseLocation, String basepackage);
}
