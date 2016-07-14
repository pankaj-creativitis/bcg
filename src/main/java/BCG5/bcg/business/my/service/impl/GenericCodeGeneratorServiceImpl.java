package BCG5.bcg.business.my.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import BCG5.bcg.business.common.Constants;
import BCG5.bcg.business.my.domain.Field;
import BCG5.bcg.business.my.dto.PropertyDto;
import BCG5.bcg.business.my.service.GenericCodeGeneratorService;

public class GenericCodeGeneratorServiceImpl implements GenericCodeGeneratorService{

	
	
	@Override
	public StringBuilder getStructureText(String className, String classType) {
		StringBuilder builder = new StringBuilder();
		builder.append(Constants.IMPORT_HOOK);
		 builder.append(Constants.CLASS_START).append(Constants.SPACE).append(className)
		.append(Constants.CURLY_BRACKET_B).append(Constants.NEW_LINE).append(Constants.INSERT_HOOK)
		.append(Constants.NEW_LINE).append(Constants.CURLY_BRACKET_E);
		System.out.println(builder.toString());
		if(classType.equals(Constants.ClassType.DAOIMPL.getValue())){

			String generalDaoImplText = Constants.HBDAOIMPL.replace(Constants.CLASSHOOK, className);
			int index = builder.indexOf(Constants.INSERT_HOOK);
			builder.replace(index, index + Constants.INSERT_HOOK.length(), 
					(generalDaoImplText + Constants.NEW_LINE) + Constants.INSERT_HOOK);
//			builder.append(generalDaoImplText);
		}
		
		if(classType.equals(Constants.ClassType.SERVICEIMPL.getValue())){

			String generalServiceImplText = Constants.SPRDAOIMPL.replace(Constants.CLASSHOOK, className);
			int index = builder.indexOf(Constants.INSERT_HOOK);
			builder.replace(index, index + Constants.INSERT_HOOK.length(), 
					(generalServiceImplText + Constants.NEW_LINE) + Constants.INSERT_HOOK);
//			builder.append(generalDaoImplText);
		}
		
//		System.out.println(builder.toString());
		return builder;
	}

	
	@Override
	public StringBuilder getDtoText(List<Field> allFields, String dtoName, StringBuilder builder) {
		StringBuilder dtoText = new StringBuilder();
		
		List<Field> memberFields = allFields.stream().filter(mb -> mb.getFieldType()
				.equals("member")).collect(Collectors.toList());
		StringBuilder constructorParams = new StringBuilder();
		StringBuilder constructorBody = new StringBuilder();
		for(Field dtoDetail:memberFields){
			dtoText.append(Constants.NEW_LINE).append(Constants.TAB).append(dtoDetail.getFieldModifier())
		.append(Constants.SPACE).append(dtoDetail.getFieldDataType()).append(Constants.SPACE)
		.append(dtoDetail.getFieldName()).append(Constants.SEMICOLON).append(Constants.NEW_LINE);
			constructorParams.append(Constants.TAB)
			.append(dtoDetail.getFieldDataType()).append(Constants.SPACE)
			.append(dtoDetail.getFieldName()).append(Constants.COMMA);
			constructorBody.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
			.append(Constants.THIS).append(dtoDetail.getFieldName()).append(Constants.SPACE)
			.append(Constants.EQUAL).append(Constants.SPACE).append(dtoDetail.getFieldName())
			.append(Constants.SEMICOLON);
		}
		System.out.println(dtoText.toString());
//		public PlanetView(String starName, String starType, String planetId, String planetName, Boolean planetHabitable,
//				Integer planetSize) {
//			super();
//			this.starName = starName;
//			this.starType = starType;
//			this.planetId = planetId;
//			this.planetName = planetName;
//			this.planetHabitable = planetHabitable;
//			this.planetSize = planetSize;
//		}
		constructorParams.replace(constructorParams.length()-1, constructorParams.length(), "");
		String constructorBodyStr = constructorBody.toString();
//		constructorBodyStr = constructorBodyStr.substring(0, constructorBodyStr.length()-1);
		dtoText.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.PUBLIC)
		.append(Constants.SPACE).append(dtoName).append(Constants.BRACKET_B)
		.append(constructorParams.toString()).append(Constants.BRACKET_E).append(Constants.CURLY_BRACKET_B)
		.append(constructorBodyStr).append(Constants.NEW_LINE).append(Constants.TAB)
		.append(Constants.CURLY_BRACKET_E).append(Constants.NEW_LINE);
		
		System.out.println("CONSTRUCTOR >>>>"+dtoText.toString());
		System.out.println("member flds >>>>"+dtoText.toString());
		
		List<Field> getterMethodFields = allFields.stream().filter(md -> md.getFieldType()
				.equals("method") && md.getFieldDataType() != Constants.VOID).collect(Collectors.toList());
		for(Field dtoDetail:getterMethodFields){
			String methodName = dtoDetail.getFieldName();
			methodName = methodName.substring(3,methodName.length());
			String getterVariable = methodName.substring(0, 1).toLowerCase() 
					+ methodName.substring(1);
			
			dtoText.append(Constants.TAB).append(dtoDetail.getFieldModifier())
		.append(Constants.SPACE).append(dtoDetail.getFieldDataType()).append(Constants.SPACE)
		.append(dtoDetail.getFieldName()).append(Constants.BRACKET_B)
		.append(Constants.BRACKET_E).append(Constants.CURLY_BRACKET_B)
		.append(Constants.NEW_LINE).append(Constants.TAB)
		.append(Constants.TAB).append(Constants.RETURN).append(Constants.SPACE)
		.append(getterVariable).append(Constants.SEMICOLON)
		.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.CURLY_BRACKET_E)
		.append(Constants.NEW_LINE);
		}
		
		System.out.println("getter flds >>>>"+dtoText.toString());
		
		List<Field> setterMethodFields = allFields.stream().filter(md -> md.getFieldType()
				.equals("method") && md.getFieldDataType() == Constants.VOID).collect(Collectors.toList());
		for(Field dtoDetail:setterMethodFields){
			String methodName = dtoDetail.getFieldName();
			methodName = methodName.substring(3,methodName.length());
			String setterVariable = methodName.substring(0, 1).toLowerCase() 
					+ methodName.substring(1);
			
			dtoText.append(Constants.TAB).append(dtoDetail.getFieldModifier())
		.append(Constants.SPACE).append(dtoDetail.getFieldDataType()).append(Constants.SPACE)
		.append(dtoDetail.getFieldName()).append(Constants.BRACKET_B)
		.append(dtoDetail.getFieldArgument()).append(Constants.SPACE).append(setterVariable)
		.append(Constants.BRACKET_E).append(Constants.CURLY_BRACKET_B)
		.append(Constants.NEW_LINE).append(Constants.TAB)
		.append(Constants.TAB).append(Constants.THIS).append(setterVariable).append(Constants.SPACE)
		.append(Constants.EQUAL).append(Constants.SPACE).append(setterVariable).append(Constants.SEMICOLON)
		.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.CURLY_BRACKET_E)
		.append(Constants.NEW_LINE);
		}
		System.out.println("setter flds >>>>"+dtoText.toString());
		String dtoString = dtoText.toString();
		int index = builder.indexOf(Constants.INSERT_HOOK);
		builder.replace(index, index + Constants.INSERT_HOOK.length(), 
				(dtoString + Constants.NEW_LINE));
		System.out.println("updated BUILDER >> " + builder.toString());
		return builder;
		
	}
	
	@Override
	public StringBuilder getServiceMethodText(Field field, Field daoField) {
//		Sample Format
//		@Override
//		@Transactional
//		public Set<String> testDao(String dtoName) {
//			return daoMakerDao.getPojoHierarchyByDtoName(dtoName);
//		}
		
//		StringBuilder serviceStructureText = genericCodeGeneratorService.getStructureText(serviceClassEntity.getClassName(), 
//		Constants.ClassType.SERVICEIMPL.getValue());

		//String daoContentText = "";
		StringBuilder serviceMethodText = new StringBuilder();
		StringBuilder importString = new StringBuilder();
		Set<String> importSet = new HashSet<>();
		//for (Field field : serviceClassEntity.getClassFields()) {

			String dataTypeClass = field.getFieldDataType().substring(
					field.getFieldDataType().lastIndexOf(".")+1, field.getFieldDataType().length());
			String returnTypeClass = field.getFieldReturnType().substring(
					field.getFieldReturnType().lastIndexOf(".")+1, field.getFieldReturnType().length());
			StringBuilder typeReturn = new StringBuilder();
			typeReturn.append(dataTypeClass).append(Constants.ANG_BRACKET_B)
			.append(returnTypeClass).append(Constants.ANG_BRACKET_E);
			
			serviceMethodText
			// add method signature
			.append(Constants.NEW_LINE).append(Constants.TAB)
			.append(field.getFieldModifier()).append(Constants.SPACE)
			.append(typeReturn.toString())
			.append(Constants.SPACE).append(field.getFieldName())
			.append(Constants.BRACKET_B).append(Constants.BRACKET_E).append(Constants.SPACE)
			.append(Constants.CURLY_BRACKET_B).append(Constants.NEW_LINE)
			// add method content
			.append(Constants.TAB).append(Constants.TAB).append(Constants.RETURN)
			.append(Constants.SPACE).append(Constants.VARIABLEHOOK).append(".")
			.append(daoField.getFieldName()).append(Constants.BRACKET_B).append(Constants.BRACKET_E)
			.append(Constants.SEMICOLON).append(Constants.NEW_LINE).append(Constants.TAB)
			.append(Constants.CURLY_BRACKET_E).append(Constants.NEW_LINE);
			
			return serviceMethodText;
	}
	
	@Override
	public Set<String> getServiceClassImports(Field field, Field daoField, Set<String> existingImports) {
		existingImports.add(Constants.ALL_DAO_PKG + daoField.getClassName());
		existingImports.add(field.getFieldDataType());
		existingImports.add(field.getFieldReturnType());
		
			return existingImports;
	}
	
	@Override
	public String getDaoText(String daoEntityName, Field field, Set<String> baseHierarchy, 
			Set<String> aliasSet, List<PropertyDto> propertyDtos, StringBuilder builder) {
		// Criteria criteria = session.createCriteria(Report.class,"r");
		// criteria
		// .createAlias("template", "t")
		// .createAlias("constituents", "rc")
		// .createAlias("rc.entity", "pe")
		// .createAlias("pe.model", "m")
		// .createAlias("pe.scenario", "s")
		// .setProjection(Projections.projectionList()
		// .add( Projections.property("r.Id"), "Id")
		// .add( Projections.property("t.Typ"), "Typ")
		// .add( Projections.property("pe.bId"), "bId")
		// .add( Projections.property("m.model"), "model")
		// .add( Projections.property("s.decay"), "decay")
		// ).setMaxResults(100)
		// .addOrder(Order.asc("r.Id"))
		// .setResultTransformer(Transformers.aliasToBean(BO.class));
		
//		List<Field> fieldList= (List<Field>) sessionFactory.getCurrentSession()
		
//		Refactor duplicate code later
		String baseClass = null;
		for(String hierarchy: baseHierarchy){
			baseClass = hierarchy.substring(0,hierarchy.indexOf("."));
		}
		String baseClassName = baseClass.substring(0, 1).toUpperCase() 
				+ baseClass.substring(1);
		
		String dataTypeClass = field.getFieldDataType().substring(
				field.getFieldDataType().lastIndexOf(".")+1, field.getFieldDataType().length());
		String returnTypeClass = field.getFieldReturnType().substring(
				field.getFieldReturnType().lastIndexOf(".")+1, field.getFieldReturnType().length());
		StringBuilder typeReturn = new StringBuilder();
		typeReturn.append(dataTypeClass).append(Constants.ANG_BRACKET_B)
		.append(returnTypeClass).append(Constants.ANG_BRACKET_E);
		
//		String returnVariable = (field.getFieldReturnType().substring(0, 1).toUpperCase() 
//				+ field.getFieldReturnType().substring(1)) + "List";
//			StringBuilder daoMethodText = new StringBuilder();
			
			String returnType = field.getFieldReturnType();
			String returnVariable = returnType.substring(returnType.lastIndexOf(".")+1,returnType.length());
			returnVariable = returnVariable.substring(0, 1).toLowerCase() 
					+ returnVariable.substring(1);
			returnVariable = returnVariable + "List";
			StringBuilder daoMethodText = new StringBuilder();
			
			daoMethodText
			// add method signature
			.append(Constants.NEW_LINE).append(Constants.TAB)
			.append(field.getFieldModifier()).append(Constants.SPACE)
			.append(typeReturn.toString())
			.append(Constants.SPACE).append(field.getFieldName())
			.append(Constants.BRACKET_B).append(Constants.BRACKET_E).append(Constants.SPACE)
			.append(Constants.CURLY_BRACKET_B).append(Constants.NEW_LINE)
			// add Criteria 
			.append(Constants.TAB).append(Constants.TAB).append(Constants.SUPPRESS_WARNINGS)
			.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB).append(typeReturn.toString())
			.append(Constants.SPACE).append(returnVariable).append(Constants.SPACE)
			.append(Constants.EQUAL).append(Constants.SPACE).append(Constants.BRACKET_B)
			.append(typeReturn).append(Constants.BRACKET_E).append(Constants.SPACE)
			.append(Constants.CriteriaStatements.GETSESSION.getValue())
			.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
			.append(Constants.CriteriaStatements.CRITERIA.getValue()).append(Constants.BRACKET_B)
			.append(baseClassName).append(Constants.CLASS).append(Constants.COMMA)
			.append("\"").append(baseClass).append("\"").append(Constants.BRACKET_E).append(Constants.ALIASHOOK)
			.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
			.append(Constants.CriteriaStatements.SET_PROJECTION.getValue())
			.append(Constants.PROPERTYHOOK).append(Constants.NEW_LINE)
			.append(Constants.TAB).append(Constants.TAB).append(Constants.BRACKET_E)
			.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
			.append(Constants.CriteriaStatements.TRANS_BEAN.getValue())
			.append(returnTypeClass).append(Constants.CLASS).append(Constants.BRACKET_E)
			.append(Constants.BRACKET_E).append(Constants.CriteriaStatements.LIST.getValue())
			.append(Constants.SEMICOLON).append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
			.append(Constants.RETURN).append(Constants.SPACE).append(returnVariable).append(Constants.SEMICOLON)
			.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.CURLY_BRACKET_E)
			.append(Constants.NEW_LINE);
			
			String aliasText =  getAliasText(aliasSet);
			String propertyText =  getPropertyText(propertyDtos);
			
			String daoMethodString = daoMethodText.toString();
			daoMethodString = daoMethodString.replace(Constants.ALIASHOOK, aliasText);
			System.out.println("final String >>>>>>" + daoMethodString);
			daoMethodString = daoMethodString.replace(Constants.PROPERTYHOOK, propertyText);		
			
//			System.out.println("method print >>>>>" + daoMethodString);
		return daoMethodString;
	}
	
	public String getAliasText(Set<String> aliasSet){
		StringBuilder aliasText = new StringBuilder();
		
		for(String alias : aliasSet){
			String property = alias.substring(0,alias.lastIndexOf("."));
			String aliasName = alias.substring(alias.lastIndexOf(".")+1,alias.length());
			aliasText.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
			.append(Constants.CriteriaStatements.ALIAS.getValue()).append("\"")
			.append(property).append("\"").append(Constants.COMMA).append(Constants.SPACE)
			.append("\"").append(aliasName).append("\"").append(Constants.BRACKET_E);
		}
		System.out.println("aliasText >> >>>" +aliasText);
		return aliasText.toString();
		
	}
	
	public String getPropertyText(List<PropertyDto> propertyDtos){
		StringBuilder propertyText = new StringBuilder();
		System.out.println("propertyDtos > > >> > "+propertyDtos.toString());
		for(PropertyDto propertyDto: propertyDtos){
			String propertyName = propertyDto.getClassProperty();
			String pojoName = propertyDto.getPojoClassName();
			pojoName = pojoName.substring(0, 1).toLowerCase() 
        			+ pojoName.substring(1);
			pojoName = pojoName + "." + propertyName;
			propertyText.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
			.append(Constants.CriteriaStatements.ADD_PROJECTION.getValue()).append("\"")
			.append(pojoName).append("\"").append(Constants.BRACKET_E).append(Constants.COMMA).append(Constants.SPACE)
			.append("\"").append(propertyName).append("\"").append(Constants.BRACKET_E);
		}
		System.out.println("propertyText > > > > " + propertyText);
		return propertyText.toString();
		
	}

}
