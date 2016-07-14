package BCG5.bcg.business.my.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import BCG5.bcg.business.common.Constants;
import BCG5.bcg.business.common.UnzipUtility;
import BCG5.bcg.business.my.dao.ClassEntityDao;
import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.Field;
import BCG5.bcg.business.my.dto.PropertyDto;
import BCG5.bcg.business.my.service.GenericCodeGeneratorService;
import BCG5.bcg.business.my.service.ServiceMakerService;

public class ServiceMakerServiceImpl implements ServiceMakerService{
	private static final Logger logger = Logger.getLogger(ServiceMakerServiceImpl.class);

	@Autowired
	private ClassEntityDao classEntityDao;

	public ServiceMakerServiceImpl(ClassEntityDao classEntityDao) {
		super();
		this.classEntityDao = classEntityDao;
	}

	public ServiceMakerServiceImpl() {
		super();
	}
	
	@Autowired
	private GenericCodeGeneratorService genericCodeGeneratorService;
	
	@Autowired
	private UnzipUtility unzipUtility;
	
	@Override
	public void addService(ClassEntity daoEntity){

		String className = daoEntity.getClassName();
		className = className.replace(Constants.ClassType.DAOIMPL.getValue(), 
				Constants.ClassType.SERVICEIMPL.getValue());
		ClassEntity serviceClassEntity = new ClassEntity();
		serviceClassEntity.setClassName(className);
		serviceClassEntity.setClassType(Constants.ClassType.SERVICEIMPL.getValue());
		
		StringBuilder methodBuilder = new StringBuilder();
		Set<String> serviceImports = new HashSet<>();
		List<Field> serviceFields = new ArrayList<>();
		String methodString = "";
		for(Field daoField:daoEntity.getClassFields()){
			Field serviceField = new Field();
			serviceField.setFieldName(daoField.getFieldName());
			serviceField.setClassName(serviceClassEntity.getClassName());
			serviceField.setClassType(Constants.ClassType.SERVICEIMPL.getValue());
			serviceField.setFieldDataType(daoField.getFieldDataType());
			serviceField.setFieldModifier(daoField.getFieldModifier());
			serviceField.setFieldReturnType(daoField.getFieldReturnType());
			serviceField.setFieldType(daoField.getFieldType());
			serviceFields.add(serviceField);
			
			serviceImports = genericCodeGeneratorService.getServiceClassImports(
					serviceField, daoField, serviceImports);
			methodBuilder.append(genericCodeGeneratorService.getServiceMethodText(
					serviceField, daoField));
			
		}
		
		serviceClassEntity.setClassFields(serviceFields);
		String daoClassName = daoEntity.getClassName();
		makeServiceText(serviceImports, methodBuilder, serviceClassEntity, daoClassName);
		classEntityDao.addClassEntity(serviceClassEntity);
	}
	
	public void makeServiceText(Set<String> serviceImports, StringBuilder methodContent, 
			ClassEntity serviceClassEntity, String daoClassName){
		
		StringBuilder serviceStructureText = genericCodeGeneratorService.getStructureText(
				serviceClassEntity.getClassName(), serviceClassEntity.getClassType());
		
		StringBuilder importText = new StringBuilder();
		for(String importString:serviceImports){
			importText.append(Constants.IMPORT).append(importString).append(Constants.SEMICOLON)
			.append(Constants.NEW_LINE);
		}
		
		String serviceText = serviceStructureText.toString();
		serviceText = serviceText.replace(Constants.IMPORT_HOOK, importText.toString()
				+ Constants.NEW_LINE + Constants.IMPORT_HOOK);
		serviceText = serviceText.replace(Constants.IMPORT_HOOK, Constants.SPRDAOIMPORT);
		serviceText = serviceText.replace(Constants.INSERT_HOOK, methodContent.toString());
		serviceText = serviceText.replace(Constants.DAOHOOK, daoClassName);
		
		String daoClassNameVariable =  daoClassName.substring(0, 1).toLowerCase() 
				+ daoClassName.substring(1);
		serviceText = serviceText.replace(Constants.VARIABLEHOOK, daoClassNameVariable);
//		System.out.println(serviceText);
		
		try {
			unzipUtility.makeClientFiles(serviceText, Constants.CLIENT_PACKAGE + Constants.CLIENT_SERVICE_PACKAGE
					+ serviceClassEntity.getClassName() + Constants.JAVA_EXT, (Constants.CODE_PKG + Constants.CODE_SERVICE_PKG));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
