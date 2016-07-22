package BCG5.bcg.business.my.dao;

import java.util.List;
import java.util.Set;

import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.dto.PropertyDto;

public interface DaoMakerDao {

	List<PropertyDto> getPropertyDtos(String dtoName);

	List<DTORelation> getDTORelationsByDtoName(String dtoName);

	Set<String> getPojoHierarchyByDtoName(String dtoName);

	List<String> getHQLFields(String dtoName);

	Boolean checkHQlMethod(String dtoName);
}
