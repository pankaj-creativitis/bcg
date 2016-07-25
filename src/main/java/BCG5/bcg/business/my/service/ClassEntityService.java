package BCG5.bcg.business.my.service;

import BCG5.bcg.business.my.domain.ClassEntity;

public interface ClassEntityService {
	
//	To be removed
	void addClassEntity(ClassEntity classEntity);
	
	void addClassEntities(String directoryPath, String basepackage);

}
