package BCG5.bcg.business.my.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import BCG5.bcg.business.common.Constants;
import BCG5.bcg.business.my.domain.Field;
import BCG5.bcg.business.my.dto.PropertyDto;
import BCG5.bcg.business.my.service.GenericCodeGeneratorService;

public class GenericCodeGeneratorServiceImpl implements GenericCodeGeneratorService {

	@Override
	public StringBuilder getStructureText(String className, String classType) {
		StringBuilder builder = new StringBuilder();
		builder.append(Constants.IMPORT_HOOK);
		builder.append(Constants.CLASS_START).append(Constants.SPACE).append(className)
				.append(Constants.CURLY_BRACKET_B).append(Constants.NEW_LINE).append(Constants.INSERT_HOOK)
				.append(Constants.NEW_LINE).append(Constants.CURLY_BRACKET_E);
		if (classType.equals(Constants.ClassType.DAOIMPL.getValue())) {

			String generalDaoImplText = Constants.HBDAOIMPL.replace(Constants.CLASSHOOK, className);
			int index = builder.indexOf(Constants.INSERT_HOOK);
			builder.replace(index, index + Constants.INSERT_HOOK.length(),
					(generalDaoImplText + Constants.NEW_LINE) + Constants.INSERT_HOOK);
		}

		if (classType.equals(Constants.ClassType.SERVICEIMPL.getValue())) {

			String generalServiceImplText = Constants.SPRDAOIMPL.replace(Constants.CLASSHOOK, className);
			int index = builder.indexOf(Constants.INSERT_HOOK);
			builder.replace(index, index + Constants.INSERT_HOOK.length(),
					(generalServiceImplText + Constants.NEW_LINE) + Constants.INSERT_HOOK);
		}
		return builder;
	}

	@Override
	public StringBuilder getDtoText(List<Field> allFields, String dtoName, StringBuilder builder) {
		StringBuilder dtoText = new StringBuilder();

		List<Field> memberFields = allFields.stream().filter(mb -> mb.getFieldType().equals("member"))
				.collect(Collectors.toList());
		Collections.sort(memberFields);
		StringBuilder constructorParams = new StringBuilder();
		StringBuilder constructorBody = new StringBuilder();
		for (Field dtoDetail : memberFields) {
			dtoText.append(Constants.NEW_LINE).append(Constants.TAB).append(dtoDetail.getFieldModifier())
					.append(Constants.SPACE).append(dtoDetail.getFieldDataType()).append(Constants.SPACE)
					.append(dtoDetail.getFieldName()).append(Constants.SEMICOLON).append(Constants.NEW_LINE);
			constructorParams.append(Constants.TAB).append(dtoDetail.getFieldDataType()).append(Constants.SPACE)
					.append(dtoDetail.getFieldName()).append(Constants.COMMA);
			constructorBody.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
					.append(Constants.THIS).append(dtoDetail.getFieldName()).append(Constants.SPACE)
					.append(Constants.EQUAL).append(Constants.SPACE).append(dtoDetail.getFieldName())
					.append(Constants.SEMICOLON);
		}
		// Syntax
		// public PlanetView(String starName, String starType, String planetId,
		// String planetName, Boolean planetHabitable,
		// Integer planetSize) {
		// super();
		// this.starName = starName;
		// this.starType = starType;
		// this.planetId = planetId;
		// this.planetName = planetName;
		// this.planetHabitable = planetHabitable;
		// this.planetSize = planetSize;
		// }
		constructorParams.replace(constructorParams.length() - 1, constructorParams.length(), "");
		String constructorBodyStr = constructorBody.toString();
		dtoText.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.PUBLIC).append(Constants.SPACE)
				.append(dtoName).append(Constants.BRACKET_B).append(constructorParams.toString())
				.append(Constants.BRACKET_E).append(Constants.CURLY_BRACKET_B).append(constructorBodyStr)
				.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.CURLY_BRACKET_E)
				.append(Constants.NEW_LINE);

		List<Field> getterMethodFields = allFields.stream()
				.filter(md -> md.getFieldType().equals("method") && md.getFieldDataType() != Constants.VOID)
				.collect(Collectors.toList());
		for (Field dtoDetail : getterMethodFields) {
			String methodName = dtoDetail.getFieldName();
			methodName = methodName.substring(3, methodName.length());
			String getterVariable = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);

			dtoText.append(Constants.TAB).append(dtoDetail.getFieldModifier()).append(Constants.SPACE)
					.append(dtoDetail.getFieldDataType()).append(Constants.SPACE).append(dtoDetail.getFieldName())
					.append(Constants.BRACKET_B).append(Constants.BRACKET_E).append(Constants.CURLY_BRACKET_B)
					.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB).append(Constants.RETURN)
					.append(Constants.SPACE).append(getterVariable).append(Constants.SEMICOLON)
					.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.CURLY_BRACKET_E)
					.append(Constants.NEW_LINE);
		}

		List<Field> setterMethodFields = allFields.stream()
				.filter(md -> md.getFieldType().equals("method") && md.getFieldDataType() == Constants.VOID)
				.collect(Collectors.toList());
		for (Field dtoDetail : setterMethodFields) {
			String methodName = dtoDetail.getFieldName();
			methodName = methodName.substring(3, methodName.length());
			String setterVariable = methodName.substring(0, 1).toLowerCase() + methodName.substring(1);

			dtoText.append(Constants.TAB).append(dtoDetail.getFieldModifier()).append(Constants.SPACE)
					.append(dtoDetail.getFieldDataType()).append(Constants.SPACE).append(dtoDetail.getFieldName())
					.append(Constants.BRACKET_B).append(dtoDetail.getFieldArgument()).append(Constants.SPACE)
					.append(setterVariable).append(Constants.BRACKET_E).append(Constants.CURLY_BRACKET_B)
					.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB).append(Constants.THIS)
					.append(setterVariable).append(Constants.SPACE).append(Constants.EQUAL).append(Constants.SPACE)
					.append(setterVariable).append(Constants.SEMICOLON).append(Constants.NEW_LINE).append(Constants.TAB)
					.append(Constants.CURLY_BRACKET_E).append(Constants.NEW_LINE);
		}
		String dtoString = dtoText.toString();
		int index = builder.indexOf(Constants.INSERT_HOOK);
		builder.replace(index, index + Constants.INSERT_HOOK.length(), (dtoString + Constants.NEW_LINE));
		return builder;

	}

	@Override
	public StringBuilder getServiceMethodText(Field field, Field daoField) {
		// Sample Format
		// @Override
		// @Transactional
		// public Set<String> testDao(String dtoName) {
		// return daoMakerDao.getPojoHierarchyByDtoName(dtoName);
		// }

		// StringBuilder serviceStructureText =
		// genericCodeGeneratorService.getStructureText(serviceClassEntity.getClassName(),
		// Constants.ClassType.SERVICEIMPL.getValue());

		// String daoContentText = "";
		StringBuilder serviceMethodText = new StringBuilder();
		StringBuilder typeReturn = getMethodReturnTypeBuilder(field);

		serviceMethodText
				// add Transactional annotation
				.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TRANSACTIONAL)
				// add method signature
				.append(Constants.NEW_LINE).append(Constants.TAB).append(field.getFieldModifier())
				.append(Constants.SPACE).append(typeReturn.toString()).append(Constants.SPACE)
				.append(field.getFieldName()).append(Constants.BRACKET_B).append(Constants.BRACKET_E)
				.append(Constants.SPACE).append(Constants.CURLY_BRACKET_B).append(Constants.NEW_LINE)
				// add method content
				.append(Constants.TAB).append(Constants.TAB).append(Constants.RETURN).append(Constants.SPACE)
				.append(Constants.VARIABLEHOOK).append(".").append(daoField.getFieldName()).append(Constants.BRACKET_B)
				.append(Constants.BRACKET_E).append(Constants.SEMICOLON).append(Constants.NEW_LINE)
				.append(Constants.TAB).append(Constants.CURLY_BRACKET_E).append(Constants.NEW_LINE);

		return serviceMethodText;
	}

	@Override
	public Set<String> getServiceClassImports(Field field, Field daoField, Set<String> existingImports, String basePackage) {
		existingImports.add(basePackage+ ".daos." + daoField.getClassName());
		existingImports.add(field.getFieldDataType());
		existingImports.add(field.getFieldReturnType());

		return existingImports;
	}

	@Override
	public String getCriteriaDaoText(Field field, Set<String> baseHierarchy, Set<String> aliasSet,
			List<PropertyDto> propertyDtos, StringBuilder builder) {
		// Syntax
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

		// List<Field> fieldList= (List<Field>)
		// sessionFactory.getCurrentSession()

		// Refactor duplicate code later
		String baseClass = null;
		for (String hierarchy : baseHierarchy) {
			baseClass = hierarchy.substring(0, hierarchy.indexOf("."));
		}
		String baseClassName = baseClass.substring(0, 1).toUpperCase() + baseClass.substring(1);
		String returnTypeClass = field.getFieldReturnType().substring(field.getFieldReturnType().lastIndexOf(".") + 1,
				field.getFieldReturnType().length());
		StringBuilder typeReturn = getMethodReturnTypeBuilder(field);
		String returnType = field.getFieldReturnType();
		String returnVariable = returnType.substring(returnType.lastIndexOf(".") + 1, returnType.length());
		returnVariable = returnVariable.substring(0, 1).toLowerCase() + returnVariable.substring(1);
		returnVariable = returnVariable + "List";
		StringBuilder daoMethodText = new StringBuilder();

		daoMethodText
				// add method signature
				.append(getMethodSignatureBuilder(field).toString())
				// add Criteria
				.append(Constants.TAB).append(Constants.TAB).append(Constants.SUPPRESS_WARNINGS)
				.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB).append(typeReturn.toString())
				.append(Constants.SPACE).append(returnVariable).append(Constants.SPACE).append(Constants.EQUAL)
				.append(Constants.SPACE).append(Constants.BRACKET_B).append(typeReturn).append(Constants.BRACKET_E)
				.append(Constants.SPACE).append(Constants.CommonDaoStatements.GETSESSION.getValue())
				.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
				.append(Constants.CriteriaStatements.CRITERIA.getValue()).append(Constants.BRACKET_B)
				.append(baseClassName).append(Constants.CLASS).append(Constants.COMMA).append("\"").append(baseClass)
				.append("\"").append(Constants.BRACKET_E).append(Constants.ALIASHOOK).append(Constants.NEW_LINE)
				.append(Constants.TAB).append(Constants.TAB)
				.append(Constants.CriteriaStatements.SET_PROJECTION.getValue()).append(Constants.PROPERTYHOOK)
				.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB).append(Constants.BRACKET_E)
				.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
				.append(Constants.CriteriaStatements.TRANS_BEAN.getValue()).append(returnTypeClass)
				.append(Constants.CLASS).append(Constants.BRACKET_E).append(Constants.BRACKET_E)
				.append(Constants.CommonDaoStatements.LIST.getValue()).append(Constants.SEMICOLON)
				.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB).append(Constants.RETURN)
				.append(Constants.SPACE).append(returnVariable).append(Constants.SEMICOLON).append(Constants.NEW_LINE)
				.append(Constants.TAB).append(Constants.CURLY_BRACKET_E).append(Constants.NEW_LINE);

		String aliasText = getAliasText(aliasSet);
		String propertyText = getPropertyText(propertyDtos);

		String daoMethodString = daoMethodText.toString();
		daoMethodString = daoMethodString.replace(Constants.ALIASHOOK, aliasText);
		daoMethodString = daoMethodString.replace(Constants.PROPERTYHOOK, propertyText);

		return daoMethodString;
	}

	// Generate Method structure
	public StringBuilder getMethodSignatureBuilder(Field field) {
		StringBuilder typeReturn = getMethodReturnTypeBuilder(field);

		StringBuilder methodSignatureBuilder = new StringBuilder();
		methodSignatureBuilder.append(Constants.NEW_LINE).append(Constants.TAB).append(field.getFieldModifier())
				.append(Constants.SPACE).append(typeReturn.toString()).append(Constants.SPACE)
				.append(field.getFieldName()).append(Constants.BRACKET_B).append(Constants.BRACKET_E)
				.append(Constants.SPACE).append(Constants.CURLY_BRACKET_B).append(Constants.NEW_LINE);
		return methodSignatureBuilder;
	}

	// generate typeReturn
	public StringBuilder getMethodReturnTypeBuilder(Field field) {

		String dataTypeClass = field.getFieldDataType().substring(field.getFieldDataType().lastIndexOf(".") + 1,
				field.getFieldDataType().length());
		String returnTypeClass = field.getFieldReturnType().substring(field.getFieldReturnType().lastIndexOf(".") + 1,
				field.getFieldReturnType().length());
		StringBuilder typeReturn = new StringBuilder();
		typeReturn.append(dataTypeClass).append(Constants.ANG_BRACKET_B).append(returnTypeClass)
				.append(Constants.ANG_BRACKET_E);
		return typeReturn;
	}

	public String getAliasText(Set<String> aliasSet) {
		StringBuilder aliasText = new StringBuilder();

		for (String alias : aliasSet) {
			String property = alias.substring(0, alias.lastIndexOf("."));
			String aliasName = alias.substring(alias.lastIndexOf(".") + 1, alias.length());
			aliasText.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
					.append(Constants.CriteriaStatements.ALIAS.getValue()).append("\"").append(property).append("\"")
					.append(Constants.COMMA).append(Constants.SPACE).append("\"").append(aliasName).append("\"")
					.append(Constants.BRACKET_E);
		}
		return aliasText.toString();

	}

	public String getPropertyText(List<PropertyDto> propertyDtos) {
		StringBuilder propertyText = new StringBuilder();
		for (PropertyDto propertyDto : propertyDtos) {
			String propertyName = propertyDto.getClassProperty();
			String pojoName = propertyDto.getPojoClassName();
			pojoName = pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1);
			pojoName = pojoName + "." + propertyName;
			propertyText.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
					.append(Constants.CriteriaStatements.ADD_PROJECTION.getValue()).append("\"").append(pojoName)
					.append("\"").append(Constants.BRACKET_E).append(Constants.COMMA).append(Constants.SPACE)
					.append("\"").append(propertyName).append("\"").append(Constants.BRACKET_E);
		}
		return propertyText.toString();

	}

	@Override
	public String getHQLDaoText(Field field, List<String> hqlFields, StringBuilder builder) {
		// Accepted format
		// public List<PropertyDto> testPropertyDto() {
		// @SuppressWarnings("unchecked")
		// List<PropertyDto> listDtoRelation = (List<PropertyDto>)
		// sessionFactory.getCurrentSession()
		// .createQuery("SELECT NEW
		// BCG5.bcg.business.my.dto.PropertyDto(field.fieldName, s1.fieldName) "
		// +
		// "From Field field, SampleOne s1, SampleTwo s2 Where " +
		// "field.fieldName = s1.fieldName AND field.fieldName = s2.fieldName")
		// .list();
		// return listDtoRelation;
		// }
		// Refactor duplicate code later
		Set<String> joinClasses = new HashSet<>();
		Set<String> joinConditions = new HashSet<>();
		Set<String> remainingFields = new HashSet<>();
		Iterator<String> fieldIterator = hqlFields.iterator();
		while (fieldIterator.hasNext()) {
			String hqlField = fieldIterator.next();
			// error handling pending
			// fetch the join classes for FROM clause
			// fetch the join conditions for WHERE clause
			if (hqlField.contains(Constants.COMMA)) {
				String joinCondition = hqlField;
				String[] joinClassesArray = joinCondition.split(Constants.COMMA);
				remainingFields.add(joinClassesArray[0].toString());
				for (int i = 0; i < joinClassesArray.length; i++) {
					joinConditions.add(joinClassesArray[i]);
					String joinClass = joinClassesArray[i].substring(0, joinClassesArray[i].indexOf("."));
					joinClasses.add(joinClass);
				}
				// Update the hqlFields and remove duplicate fields
				fieldIterator.remove();
			}
		}
		hqlFields.addAll(remainingFields);
		String returnTypeClass = field.getFieldReturnType().substring(field.getFieldReturnType().lastIndexOf(".") + 1,
				field.getFieldReturnType().length());

		StringBuilder typeReturn = getMethodReturnTypeBuilder(field);
		String returnType = field.getFieldReturnType();
		String returnVariable = returnType.substring(returnType.lastIndexOf(".") + 1, returnType.length());
		returnVariable = returnVariable.substring(0, 1).toLowerCase() + returnVariable.substring(1);
		returnVariable = returnVariable + "List";
		StringBuilder daoMethodText = new StringBuilder();

		daoMethodText
				// add method signature
				.append(getMethodSignatureBuilder(field).toString())
				// add HQL
				.append(Constants.TAB).append(Constants.TAB).append(Constants.SUPPRESS_WARNINGS)
				.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB).append(typeReturn.toString())
				.append(Constants.SPACE).append(returnVariable).append(Constants.SPACE).append(Constants.EQUAL)
				.append(Constants.SPACE).append(Constants.BRACKET_B).append(typeReturn).append(Constants.BRACKET_E)
				.append(Constants.SPACE).append(Constants.CommonDaoStatements.GETSESSION.getValue())
				.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
				.append(Constants.HQLStatements.CREATEQUERY.getValue()).append(Constants.BRACKET_B).append("\"")
				.append(Constants.HQLStatements.SELECT.getValue()).append(Constants.SPACE)
				.append(Constants.HQLStatements.NEW.getValue()).append(Constants.SPACE)
				.append(field.getFieldReturnType()).append(Constants.BRACKET_B).append("\"").append(Constants.NEW_LINE)
				.append(Constants.TAB).append(Constants.TAB).append(Constants.TAB).append(Constants.PLUS)
				.append(Constants.SPACE).append("\"").append(getHQLFields(hqlFields)).append(Constants.BRACKET_E)
				.append(Constants.SPACE).append("\"").append(Constants.NEW_LINE).append(Constants.TAB)
				.append(Constants.TAB).append(Constants.TAB).append(Constants.PLUS).append(Constants.SPACE).append("\"")
				.append(Constants.HQLStatements.FROM.getValue()).append(Constants.SPACE)
				.append(getHQLClasses(joinClasses)).append(Constants.SPACE).append("\"").append(Constants.NEW_LINE)
				.append(Constants.TAB).append(Constants.TAB).append(Constants.TAB).append(Constants.PLUS)
				.append(Constants.SPACE).append("\"").append(Constants.HQLStatements.WHERE.getValue())
				.append(Constants.SPACE).append(getHQLConditions(joinConditions)).append("\"")
				.append(Constants.BRACKET_E).append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB)
				.append(Constants.CommonDaoStatements.LIST.getValue()).append(Constants.SEMICOLON)
				.append(Constants.NEW_LINE).append(Constants.TAB).append(Constants.TAB).append(Constants.RETURN)
				.append(Constants.SPACE).append(returnVariable).append(Constants.SEMICOLON).append(Constants.NEW_LINE)
				.append(Constants.TAB).append(Constants.CURLY_BRACKET_E).append(Constants.NEW_LINE);

		String daoMethodString = daoMethodText.toString();
		return daoMethodString;
	}

	private String getHQLFields(List<String> hqlFields) {
		StringBuilder hqlBuilder = new StringBuilder();
		for (String field : hqlFields) {
			hqlBuilder.append(field).append(Constants.COMMA).append(Constants.SPACE);
		}
		String hqlFieldString = hqlBuilder.toString();
		hqlFieldString = hqlFieldString.substring(0, hqlFieldString.length() - 2);
		return hqlFieldString;
	}

	private String getHQLClasses(Set<String> joinClasses) {
		StringBuilder hqlBuilder = new StringBuilder();
		for (String field : joinClasses) {
			hqlBuilder.append(field.substring(0, 1).toUpperCase() + field.substring(1)).append(Constants.SPACE)
					.append(field).append(Constants.COMMA).append(Constants.SPACE);
		}
		String hqlClassString = hqlBuilder.toString();
		hqlClassString = hqlClassString.substring(0, hqlClassString.length() - 2);
		return hqlClassString;
	}

	private String getHQLConditions(Set<String> joinConditions) {
		StringBuilder hqlBuilder = new StringBuilder();
		Set<String> conditionalPairs = new HashSet<>();
		List<String> joinConditionsList = new ArrayList<>(joinConditions);
		for (int i = 0; i < joinConditionsList.size(); i++) {
			if (i + 1 < joinConditionsList.size() && joinConditionsList.get(i + 1) != null) {
				conditionalPairs.add(joinConditionsList.get(i) + "##" + joinConditionsList.get(i + 1));
			} else {
				break;
			}
		}

		for (String condition : conditionalPairs) {

			String[] conditionArray = condition.split("##");
			hqlBuilder.append(Constants.SPACE).append(conditionArray[0].toString()).append(Constants.SPACE)
					.append(Constants.EQUAL).append(Constants.SPACE).append(conditionArray[1].toString())
					.append(Constants.SPACE).append(Constants.HQLStatements.AND.getValue()).append(Constants.SPACE);
		}
		String hqlConditionString = hqlBuilder.toString();
		hqlConditionString = hqlConditionString.substring(0, hqlConditionString.length() - 4);
		return hqlConditionString;
	}

}
