package BCG5.bcg.business.my.service.impl;

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
	public Set<String> testDao(String dtoName) {
		return daoMakerDao.getPojoHierarchyByDtoName(dtoName);
	}
	
	@Override
	@Transactional
	public void addDao() {

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
			field.setFieldReturnType("BCG5.bcg.business.client.dtos." + entry.getKey());
			field.setFieldType("method");
			field.setFieldModifier(Constants.PUBLIC);

			fields.add(field);
			classEntity.setClassFields(fields);
			classEntitieSet.add(classEntity);
			
			addDaoText(classEntity);
			
		}

		for (ClassEntity classEntity : classEntitieSet) {
			classEntityDao.addClassEntity(classEntity);
//			add the entity for service
			serviceMakerService.addService(classEntity);
		}
	}

	public void addDaoText(ClassEntity daoEntity) {

		StringBuilder daoStructureText = genericCodeGeneratorService.getStructureText(daoEntity.getClassName(), 
				Constants.ClassType.DAOIMPL.getValue());
		String daoContentText = "";
		StringBuilder importString = new StringBuilder();
		Set<String> importSet = new HashSet<>();
		for (Field field : daoEntity.getClassFields()) {
			String fullReturnType = field.getFieldReturnType();
			String returnType = fullReturnType.substring(fullReturnType.lastIndexOf(".")+1, fullReturnType.length());
			Set<String> aliasSet = daoMakerDao.getPojoHierarchyByDtoName(returnType);
			
			Set<String> baseHierarchy = getBaseObjectset(aliasSet);
			
			// Need to combine the pojoHierarchySet and PropertyDtos to finally pass the QueryConditionDto into DaoTextGenerator.
			List<PropertyDto> propertyDtos = daoMakerDao.getPropertyDtos(returnType);
			daoContentText = daoContentText + genericCodeGeneratorService.getDaoText(
					daoEntity.getClassName(), field, baseHierarchy, aliasSet, propertyDtos, 
					daoStructureText);
			
			importSet.add(field.getFieldDataType());
			String baseClass = null;
			for(String hierarchy: baseHierarchy){
				baseClass = hierarchy.substring(0,hierarchy.indexOf("."));
				String baseClassName = baseClass.substring(0, 1).toUpperCase() 
						+ baseClass.substring(1);
				importSet.add(Constants.CODE_POJO_PKG+baseClassName);
			}

			importSet.add(field.getFieldReturnType());
		}	
//		Add import statements
		for(String importClass:importSet){
			importString.append(Constants.IMPORT+importClass).append(Constants.SEMICOLON)
			.append(Constants.NEW_LINE);
		}
		String finalDaoString = daoStructureText.toString();
		finalDaoString = finalDaoString.replace(Constants.INSERT_HOOK, daoContentText + Constants.NEW_LINE);
		finalDaoString = finalDaoString.replace(Constants.IMPORT_HOOK, importString.toString() + Constants.NEW_LINE + Constants.IMPORT_HOOK);
		finalDaoString = finalDaoString.replace(Constants.IMPORT_HOOK, Constants.HBDAOIMPORT 
				+ Constants.NEW_LINE + Constants.REPOSITORY + Constants.NEW_LINE);
		
		try {
			unzipUtility.makeClientFiles(finalDaoString, Constants.CLIENT_PACKAGE + Constants.CLIENT_DAO_PACKAGE
					+ daoEntity.getClassName() + Constants.JAVA_EXT, (Constants.CODE_PKG + Constants.CODE_DAO_PKG));
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

//	Lots of refactoring possible in the below code
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

	public Set<String> getHierarchySet(Set<String> basePairs, Set<String> updatedHierarchySet) {

		for (String staringPair : basePairs) {
			String hierarchy = makeHierarchy(staringPair, updatedHierarchySet);
		}

		return null;
	}

	private static Set<Set<String>> finalBranches = new HashSet<>();

//	No need for this method.. Need to remove
	public static Set<Set<String>> branchMaker(Set<Set<String>> setOfAllPairsSet) {
		// corner case of duplicate pairs to be handled later
		Boolean hasDuplicate = false;
		for (Set<String> allPairsSet : setOfAllPairsSet) {
			List<String> firstElements = new ArrayList<>();
			for (String pair : allPairsSet) {
				pair = pair.substring(0, pair.indexOf("."));
				firstElements.add(pair);
			}

			Set<String> firstElementsSet = new HashSet<>(firstElements);
			for (String firstElement : firstElementsSet) {
				if (Collections.frequency(firstElements, firstElement) > 1) {
					hasDuplicate = true;
					Set<String> commonElementSet = new HashSet<>();
					Iterator<String> iterator = allPairsSet.iterator();
					while (iterator.hasNext()) {
						String commonPair = iterator.next();
						if (commonPair.startsWith(firstElement)) {
							commonElementSet.add(commonPair);
							iterator.remove();
						}
					}

					for (String addElement : commonElementSet) {
						Set<String> newPairs = new HashSet<>();
						newPairs.add(addElement);
						newPairs.addAll(allPairsSet);
						finalBranches.add(newPairs);
//						System.out.println("finalBranches Size >>>> " + finalBranches.size());
					}
					return branchMaker(finalBranches);
				}
			}
		}
		int maxCount = 0;
		for (Set<String> removeBranch : finalBranches) {
			maxCount = maxCount > removeBranch.size() ? maxCount : removeBranch.size();
		}

		if (!hasDuplicate) {
//			System.out.println("branchMaking Over!");
			Iterator<Set<String>> branchIterator = finalBranches.iterator();

			while (branchIterator.hasNext()) {
				Set<String> strings = branchIterator.next();
				if (strings.size() == maxCount) {
//					System.out.println("finalBranches >>> " + finalBranches);
					Set<String> finalStrings = new HashSet<>(strings);
					Set<String> startingStrings = getBaseObjectset(strings);
					for (String string : startingStrings) {
						finalStrings.remove(string);
						makeHierarchy(string, finalStrings);
					}
				}
				branchIterator.remove();
			}

		}
		return finalBranches;
	}

	private static String hierarchy;
	private static Set<String> hierarchySet = new HashSet<>();

	static int counter = 0;

	public static String makeHierarchy(String staringPair, Set<String> updatedHierarchySet) {

		String secondStartingPair = staringPair.substring(staringPair.lastIndexOf(".") + 1, staringPair.length());
		Boolean noMatch = true;
		for (Iterator<String> eachPairIterator = updatedHierarchySet.iterator(); eachPairIterator.hasNext();) {
			String eachPair = eachPairIterator.next();
			String[] testPairs = eachPair.split("\\.");
			if (eachPair.startsWith(secondStartingPair)) {
				hierarchy = staringPair + "." + testPairs[1].toString();
				eachPairIterator.remove();
				noMatch = false;
				break;
			}
		}
		if (updatedHierarchySet.size() <= 0 || noMatch) {
//			System.out.println("final hierarchy >> > > > " + hierarchy);
			hierarchySet.add(hierarchy);
//			System.out.println("hierarchySet > > > > "+hierarchySet);
			return hierarchy;
		} else {
			return makeHierarchy(hierarchy, updatedHierarchySet);

		}
	}

	public static void main(String[] args) {
		String staringPair = "Cluster.Galaxy";
		String setVal4 = "Stars.Planets";
		String setVal1 = "Galaxy.Area";
		String setVal2 = "Galaxy.Arms";
		String setVal3 = "Area.Stars";
		String setVal5 = "Cluster.Galaxy";
		String setVal8 = "Planets.Species";
		String setVal9 = "Planets.Environment";
		String setVal6 = "Cluster.Void";

		Set<String> updatedHierarchySet = new HashSet<>();
		updatedHierarchySet.add(setVal3);
		updatedHierarchySet.add(setVal4);
		updatedHierarchySet.add(setVal1);
		updatedHierarchySet.add(setVal2);
		updatedHierarchySet.add(setVal5);
		updatedHierarchySet.add(setVal8);
		updatedHierarchySet.add(setVal9);
		updatedHierarchySet.add(setVal6);

		Set<Set<String>> newBranches = new HashSet<>();
		newBranches.add(updatedHierarchySet);
//		System.out.println(branchMaker(newBranches));
		// System.out.println(makeHierarchy(staringPair, updatedHierarchySet));
		
//		Set<String> sampleHierarchySet = new HashSet<String>(Arrays.asList("Area.Stars.Planets.Species", 
//				"Galaxy.Area.Stars.Planets.Species", 
//				"Cluster.Galaxy.Area.Stars.Planets.Species", 
//				"Galaxy.Area.Stars.Planets.Environment", 
//				"Cluster.Galaxy.Arms", 
//				"Area.Stars.Planets.Environment", 
//				"Cluster.Galaxy.Area.Stars.Planets.Environment"));
//		System.out.println(getBaseObjectset(sampleHierarchySet));
		
	}

}
