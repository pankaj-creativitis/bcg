package BCG5.bcg.business.my.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.domain.Field;
import BCG5.bcg.business.my.dto.PropertyDto;

@Service
public interface GenericCodeGeneratorService {
	StringBuilder getDtoText(List<Field> allFields, String dtoName, StringBuilder builder);

	StringBuilder getStructureText(String className, String classType);

	String getCriteriaDaoText(Field field, Set<String> baseHierarchy, Set<String> aliasSet,
			List<PropertyDto> propertyDtos, StringBuilder builder);

	StringBuilder getServiceMethodText(Field field, Field daoField);

	Set<String> getServiceClassImports(Field field, Field daoField, Set<String> existingImports);

	String getHQLDaoText(Field field, List<String> hqlFields, StringBuilder builder);

}
