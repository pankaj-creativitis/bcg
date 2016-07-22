package BCG5.bcg.business.my.service.impl;

import java.io.File;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import BCG5.bcg.business.my.dao.ClassEntityDao;
import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.Field;
import BCG5.bcg.business.my.service.ClassEntityService;

@Service
@Transactional
public class ClassEntityServiceImpl implements ClassEntityService {

	private static final Logger logger = Logger.getLogger(ClassEntityServiceImpl.class);

	@Autowired
	private ClassEntityDao classEntityDao;

	public ClassEntityServiceImpl(ClassEntityDao classEntityDao) {
		super();
		this.classEntityDao = classEntityDao;
	}

	public ClassEntityServiceImpl() {
		super();
	}

	@Override
	@Transactional
	public void addClassEntity(ClassEntity classEntity) {
		classEntityDao.addClassEntity(classEntity);
	}

	@Override
	@Transactional
	public void addClassEntities(String directoryPath) {
		File dir = new File(directoryPath);
		File[] directoryListing = dir.listFiles();
		String pojoFilename;
		if (directoryListing != null) {
			for (File pojoFile : directoryListing) {
				pojoFilename = pojoFile.getName().substring(0, pojoFile.getName().indexOf("."));
				ClassEntity classEntity = new ClassEntity();
				classEntity.setClassName(pojoFilename);
				classEntity.setClassType("pojo");
				List<Field> fields = new ArrayList<>();
				try {
					Class pojoClass = Class.forName("BCG5.bcg.business.client.pojos." + pojoFilename);
					Member[] fieldMbrs = pojoClass.getDeclaredFields();
					for (int i = 0; i < fieldMbrs.length; i++) {
						Field field = new Field();
						Member member = fieldMbrs[i];

						java.lang.reflect.Field fieldType = pojoClass.getDeclaredField(member.getName());
						Type genericFieldType = fieldType.getGenericType();

						if (genericFieldType instanceof ParameterizedType) {
							ParameterizedType aType = (ParameterizedType) genericFieldType;
							if (aType.getActualTypeArguments()[0] != null) {
								Type fieldArgType = aType.getActualTypeArguments()[0];
								Class fieldArgClass = (Class) fieldArgType;
								field.setFieldReturnType(fieldArgClass.getName());
							}
						}

						field.setFieldName(member.getName());
						String dataType = pojoClass.getDeclaredField(member.getName()).getType().getTypeName();
						field.setFieldDataType(dataType);
						field.setFieldModifier(Modifier.toString(member.getModifiers()));
						field.setFieldType("member");
						field.setClassName(classEntity.getClassName());
						field.setClassType("pojo");
						fields.add(field);
					}

					Method[] methodMbrs = pojoClass.getDeclaredMethods();
					for (int i = 0; i < methodMbrs.length; i++) {
						Field field = new Field();
						Method method = methodMbrs[i];

						if (method.getGenericParameterTypes().length != 0) {
							Type[] argTypes = method.getGenericParameterTypes();
							for (Type argType : argTypes) {
								if (argType instanceof ParameterizedType) {
									Type elementType = ((ParameterizedType) argType).getActualTypeArguments()[0];
									Class typeArgClass = (Class) elementType;
									field.setFieldArgType(typeArgClass.getName());
								}
							}
						}

						Type returnType = method.getGenericReturnType();
						if (returnType instanceof ParameterizedType) {
							Type elementType = ((ParameterizedType) returnType).getActualTypeArguments()[0];
							Class typeArgClass = (Class) elementType;
							field.setFieldReturnType(typeArgClass.getName());
						}

						field.setFieldName(method.getName());
						String dataType = method.getReturnType().getTypeName();
						field.setFieldDataType(dataType);
						field.setFieldModifier(Modifier.toString(method.getModifiers()));
						field.setFieldArgument(Arrays.toString(method.getParameterTypes()));
						field.setFieldType("method");
						field.setClassName(classEntity.getClassName());
						field.setClassType("pojo");
						fields.add(field);
					}

				} catch (ClassNotFoundException | NoSuchFieldException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				classEntity.setClassFields(fields);
				classEntityDao.addClassEntity(classEntity);
			}
		} else {
			// Handle the case where dir is not really a directory.
			// Checking dir.isDirectory() above would not be sufficient
			// to avoid race conditions with another membmeprocess that deletes
			// directories.
		}

	}

}
