package BCG5.bcg.business.my.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import BCG5.bcg.business.common.Constants;
import BCG5.bcg.business.common.UnzipUtility;
import BCG5.bcg.business.my.dao.ClassEntityDao;
import BCG5.bcg.business.my.dao.DaoMakerDao;
import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.domain.Field;
import BCG5.bcg.business.my.dto.PropertyDto;
import BCG5.bcg.business.my.service.DaoMakerService;
import BCG5.bcg.business.my.service.GenericCodeGeneratorService;
import BCG5.bcg.business.my.service.ServiceMakerService;

@Service
public class DaoMakerServiceImpl implements DaoMakerService {

	@Autowired
	private ClassEntityDao classEntityDao;

	@Autowired
	private DaoMakerDao daoMakerDao;

	@Autowired
	private UnzipUtility unzipUtility;

	@Autowired
	private ServiceMakerService serviceMakerService;

	public DaoMakerServiceImpl() {
		super();
	}

	public DaoMakerServiceImpl(ClassEntityDao classEntityDao) {
		super();
		this.classEntityDao = classEntityDao;
	}

	@Autowired
	private GenericCodeGeneratorService genericCodeGeneratorService;

	@Override
	@Transactional
	public void addDao(String baseLocation, String basepackage) {

		List<DTORelation> dtoRelations = classEntityDao.getAllDTORelations();

		Map<String, List<DTORelation>> dtoRelationsByNameMap = new HashMap<>();

		for (DTORelation dtoRelation : dtoRelations) {
			String dtoName = dtoRelation.getDtoName();
			dtoRelationsByNameMap.putIfAbsent(dtoName, new ArrayList<>());
			List<DTORelation> dtoRelationList = dtoRelationsByNameMap.get(dtoName);
			dtoRelationList.add(dtoRelation);
			dtoRelationsByNameMap.put(dtoName, dtoRelationList);
		}

		Set<ClassEntity> classEntitieSet = new HashSet<>();
		for (Map.Entry<String, List<DTORelation>> entry : dtoRelationsByNameMap.entrySet()) {
			List<DTORelation> dtoRelationsByName = entry.getValue();
			Map<String, Integer> dtoPojoTypeMap = new HashMap<>();
			for (DTORelation dtoRelation : dtoRelationsByName) {
				String pojoName = dtoRelation.getDtoPojoClassName();
				dtoPojoTypeMap.putIfAbsent(pojoName, new Integer(0));
				dtoPojoTypeMap.put(pojoName, dtoPojoTypeMap.get(pojoName) + 1);
			}

			String key = Collections.max(dtoPojoTypeMap.entrySet(), Map.Entry.comparingByValue()).getKey();
			ClassEntity classEntity;
			List<Field> fields;
			String daoClassName = key + Constants.ClassType.DAOIMPL.getValue();
			List<ClassEntity> existingClassEntityList = classEntitieSet.stream()
					.filter(ce -> ce.getClassName().equals(daoClassName)).limit(1).collect(Collectors.toList());

			if (existingClassEntityList.size() != 0) {
				classEntity = existingClassEntityList.get(0);
				fields = classEntity.getClassFields();
				classEntity.setClassFields(null);
			} else {
				classEntity = new ClassEntity();
				classEntity.setClassName(key + Constants.ClassType.DAOIMPL.getValue());
				classEntity.setClassType(Constants.ClassType.DAOIMPL.getValue());
				fields = new ArrayList<>();
			}

			Field field = new Field();
			field.setClassType(Constants.ClassType.DAOIMPL.getValue());
			field.setClassName(key + Constants.ClassType.DAOIMPL.getValue());
			field.setFieldName(Constants.GET_PREFIX + entry.getKey() + "List");
			field.setFieldDataType("java.util.List");
			field.setFieldReturnType(basepackage + ".dtos." + entry.getKey());
			field.setFieldType("method");
			field.setFieldModifier(Constants.PUBLIC);

			fields.add(field);
			classEntity.setClassFields(fields);
			classEntitieSet.add(classEntity);

			addDaoText(classEntity, baseLocation, basepackage);

		}

		for (ClassEntity classEntity : classEntitieSet) {
			classEntityDao.addClassEntity(classEntity);
			// add the entity for service
			serviceMakerService.addService(classEntity, baseLocation, basepackage);
		}
	}

	public void addDaoText(ClassEntity daoEntity, String baseLocation, String basepackage) {

		StringBuilder daoStructureText = genericCodeGeneratorService.getStructureText(daoEntity.getClassName(),
				Constants.ClassType.DAOIMPL.getValue());
		String daoContentText = "";
		StringBuilder importString = new StringBuilder();

		// Need a better way for method identification, possibly add an extra
		// column in the field table for join field
		Set<String> importSet = new HashSet<>();
		for (Field field : daoEntity.getClassFields()) {

			String fullReturnType = field.getFieldReturnType();
			String returnType = fullReturnType.substring(fullReturnType.lastIndexOf(".") + 1, fullReturnType.length());
			Boolean isHQLMethod = daoMakerDao.checkHQLMethod(returnType);

			if (isHQLMethod) {
				System.out.println("isHQLMethod > > > " + isHQLMethod);
				List<String> hqlFields = daoMakerDao.getHQLFields(returnType);
				daoContentText = daoContentText
						+ genericCodeGeneratorService.getHQLDaoText(field, hqlFields, daoStructureText);
			} else {

				Set<String> aliasSet = daoMakerDao.getPojoHierarchyByDtoName(returnType);
				Set<String> baseHierarchy = getBaseObjectset(aliasSet);

				List<PropertyDto> propertyDtos = daoMakerDao.getPropertyDtos(returnType);
				daoContentText = daoContentText + genericCodeGeneratorService.getCriteriaDaoText(field, baseHierarchy,
						aliasSet, propertyDtos, daoStructureText);
				String baseClass = null;
				for (String hierarchy : baseHierarchy) {
					baseClass = hierarchy.substring(0, hierarchy.indexOf("."));
					String baseClassName = baseClass.substring(0, 1).toUpperCase() + baseClass.substring(1);
					importSet.add(basepackage + ".pojos." + baseClassName);
				}
			}
			importSet.add(field.getFieldDataType());
			importSet.add(fullReturnType);
		}
		// Add import statements
		for (String importClass : importSet) {
			importString.append(Constants.IMPORT + importClass).append(Constants.SEMICOLON).append(Constants.NEW_LINE);
		}
		String finalDaoString = daoStructureText.toString();
		finalDaoString = finalDaoString.replace(Constants.INSERT_HOOK, daoContentText + Constants.NEW_LINE);
		finalDaoString = finalDaoString.replace(Constants.IMPORT_HOOK,
				importString.toString() + Constants.NEW_LINE + Constants.IMPORT_HOOK);
		finalDaoString = finalDaoString.replace(Constants.IMPORT_HOOK,
				Constants.HBDAOIMPORT + Constants.NEW_LINE + Constants.REPOSITORY + Constants.NEW_LINE);

		String daoLocationDir = baseLocation + Constants.CLIENT_DAO_PACKAGE;
        File locationDir = new File(daoLocationDir);
        if (!locationDir.exists()) {
        	locationDir.mkdirs();
        }
        
		try {
			unzipUtility.makeClientFiles(finalDaoString, baseLocation + Constants.CLIENT_DAO_PACKAGE
					+ daoEntity.getClassName() + Constants.JAVA_EXT, ("package " + basepackage + Constants.CODE_DAO_PKG));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String, String> objectHierarchy(Set<String> objectPairSet) {

		Set<String> baseElementSet = getBaseObjectset(objectPairSet);
		Iterator<String> allIterator = objectPairSet.iterator();

		// Possibly can be coupled with the above code
		while (allIterator.hasNext()) {
			String removePair = allIterator.next();
			for (String basePair : baseElementSet) {
				if (removePair.equals(basePair)) {
					allIterator.remove();
				}
			}

		}

		return null;
	}

	public static Set<String> getBaseObjectset(Set<String> objectPairSet) {
		Set<String> baseElementSet = new HashSet<>();
		for (String objectPair : objectPairSet) {
			String base = objectPair.substring(objectPair.lastIndexOf(".") + 1, objectPair.length());
			baseElementSet.add(base);
		}

		// Remove the duplicate sub elements
		Iterator<String> iterator = objectPairSet.iterator();
		while (iterator.hasNext()) {
			String endingText = iterator.next();
			for (String endElement : baseElementSet) {
				if (endingText.startsWith(endElement)) {
					iterator.remove();
				}
			}
		}
		return objectPairSet;
	}
}
