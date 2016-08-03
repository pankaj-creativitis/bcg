package BCG5.bcg.business.my.dao;

import java.util.List;

import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.domain.Field;

public interface ClassEntityDao {
	
	void addClassEntity(ClassEntity classEntity);

	List<Field> getFields();

	List<Field> getMemberFieldsByClassType();

	List<DTORelation> getAllDTORelations();

	ClassEntity getClassEntityByName(String className);

	List<Field> getFieldListByDtoName(String dtoName);

}
