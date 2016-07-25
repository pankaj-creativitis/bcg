package BCG5.bcg.business.my.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import BCG5.bcg.business.common.Constants;
import BCG5.bcg.business.common.UnzipUtility;
import BCG5.bcg.business.my.dao.ClassEntityDao;
import BCG5.bcg.business.my.dao.DTODao;
import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.DTO;
import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.domain.Field;
import BCG5.bcg.business.my.dto.DtoRelationDto;
import BCG5.bcg.business.my.service.ClassEntityService;
import BCG5.bcg.business.my.service.DaoMakerService;
import BCG5.bcg.business.my.service.DtoMakerService;
import BCG5.bcg.business.my.service.GenericCodeGeneratorService;

@Service
public class DTOMakerServiceImpl implements DtoMakerService{

	private static final Logger logger = Logger.getLogger(DTOMakerServiceImpl.class);
	
	@Autowired
	private ClassEntityDao classEntityDao;
	
	@Autowired
	private DTODao dtoDao;
	
	@Autowired
	private GenericCodeGeneratorService genericCodeGeneratorService;
	
	@Autowired
	private ClassEntityService classEntityService;
	
	@Autowired
	private UnzipUtility unzipUtility;
	
	@Autowired
	private DaoMakerService daoMakerService;
	
	public DTOMakerServiceImpl(DTODao dtoDao) {
		super();
		this.dtoDao = dtoDao;
	}

	public DTOMakerServiceImpl() {
		super();
	}
	
	@Override
	@Transactional
	public void addDtoNew(String dtoName, Set<DtoRelationDto> fields, String baseLocation, String basepackage) {
		
		List<Field> dtoFields = new ArrayList<>();
		ClassEntity dtoEntity = new ClassEntity();
		dtoEntity.setClassName(dtoName);
		dtoEntity.setClassType("dto");
		DTO dto = new DTO();
		dto.setDtoName(dtoName);
		List<Field> memberFields = classEntityDao.getMemberFieldsByClassType();
		
		List<DTORelation> dtoRelations = new ArrayList<>();
		for(Field field : memberFields){
			for(DtoRelationDto dtoRelationDto : fields){
				if(dtoRelationDto.getFieldName().contains(field.getFieldName())){
					Field dtoField = new Field();
					
					dtoField.setClassName(dtoName);
					dtoField.setClassType("dto");
					dtoField.setFieldDataType(field.getFieldDataType());
					dtoField.setFieldModifier(Constants.PRIVATE);
					dtoField.setFieldName(field.getFieldName());
					dtoField.setFieldType("member");
					
	//				Make a DTORelation object
					Set<DTORelation> dummyDTORelationSet = new HashSet<>();
					DTORelation dtoRelation = new DTORelation();
					dtoRelation.setDtoPojoClassName(field.getClassName());
					System.out.println("dtoRelationDto.getFullFieldName() > > >"+dtoRelationDto.getFullFieldName());
					dtoRelation.setDtoFieldName(dtoRelationDto.getFullFieldName());
					dtoRelation.setDtoName(dtoName);
					dummyDTORelationSet.add(dtoRelation);
					dtoField.setDtoRelations(dummyDTORelationSet);
					dtoRelations.add(dtoRelation);
					dtoFields.add(dtoField);
					
					Field getterDtoField = new Field();
					String getterMethodName =  "get" + field.getFieldName().substring(0, 1).toUpperCase() 
							+ field.getFieldName().substring(1);
					getterDtoField.setClassName(dtoName);
					getterDtoField.setClassType("dto");
					getterDtoField.setFieldDataType(field.getFieldDataType());
					getterDtoField.setFieldModifier(Constants.PUBLIC);
					getterDtoField.setFieldName(getterMethodName);
					getterDtoField.setFieldType("method");
					dtoFields.add(getterDtoField);
					
					Field setterDtoField = new Field();
					String setterMethodName =  "set" + field.getFieldName().substring(0, 1).toUpperCase() 
							+ field.getFieldName().substring(1);
					setterDtoField.setClassName(dtoName);
					setterDtoField.setClassType("dto");
					setterDtoField.setFieldDataType(Constants.VOID);
					setterDtoField.setFieldArgument(field.getFieldDataType());
					setterDtoField.setFieldModifier(Constants.PUBLIC);
					setterDtoField.setFieldName(setterMethodName);
					setterDtoField.setFieldType("method");
					dtoFields.add(setterDtoField);
					
				}	
			}
		}
		dtoEntity.setClassFields(dtoFields);
		classEntityService.addClassEntity(dtoEntity);
		makeDtoClass(dtoEntity, baseLocation, basepackage);
//		add the dao and service classes
		daoMakerService.addDao(baseLocation, basepackage);
	}
	
	private void makeDtoClass(ClassEntity dtoEntity, String baseLocation, String basepackage) {
		
		StringBuilder dtoStructureText = genericCodeGeneratorService.getStructureText(dtoEntity.getClassName(), 
				Constants.ClassType.DTO.getValue());

		StringBuilder dtofinalText = genericCodeGeneratorService
				.getDtoText(dtoEntity.getClassFields(), dtoEntity.getClassName(), dtoStructureText);
				
		String finalClassString = dtofinalText.toString();
		finalClassString = finalClassString.replace(Constants.IMPORT_HOOK, "");
		String finalOutputPath = baseLocation + Constants.CLIENT_DTO_PACKAGE 
				+ File.separator + dtoEntity.getClassName() + Constants.JAVA_EXT;
		String dtoLocationDir = baseLocation + Constants.CLIENT_DTO_PACKAGE;
        File locationDir = new File(dtoLocationDir);
        if (!locationDir.exists()) {
        	locationDir.mkdirs();
        }
		try {
			unzipUtility.makeClientFiles(finalClassString, finalOutputPath, ("package " + basepackage + Constants.CODE_DTO_PKG));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
