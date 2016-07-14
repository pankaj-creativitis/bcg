package BCG5.bcg.business.my.dao;

import java.util.List;
import java.util.Set;

import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.domain.Field;

public interface ClassEntityDao {
	
	void addClassEntity(ClassEntity classEntity);

	List<Field> getFields();

	List<Field> getMemberFieldsByClassType();

	List<DTORelation> getDTORelationsByDtoName(String className);

	List<DTORelation> getAllDTORelations();

	ClassEntity getClassEntityByName(String className);

	Set<String> getPojoHierarchyByDtoName(String dtoName);

	List<Field> getFieldListByDtoName(String dtoName);

}
