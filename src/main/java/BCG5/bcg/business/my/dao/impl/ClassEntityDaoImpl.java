package BCG5.bcg.business.my.dao.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.maven.artifact.versioning.Restriction;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import BCG5.bcg.business.common.Constants;
import BCG5.bcg.business.my.dao.ClassEntityDao;
import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.domain.Field;

@Repository
public class ClassEntityDaoImpl implements ClassEntityDao{

	private static final Logger logger = Logger.getLogger(ClassEntityDaoImpl.class);
	 
    @Autowired
    private SessionFactory sessionFactory;
    
	public ClassEntityDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public ClassEntityDaoImpl() {
		super();
	}

	@Override
	public void addClassEntity(ClassEntity classEntity) {
		sessionFactory.getCurrentSession().saveOrUpdate(classEntity);
	}
	
	@Override
	public List<Field> getFields() {
		@SuppressWarnings("unchecked")
        List<Field> listField = (List<Field>) sessionFactory.getCurrentSession()
                .createCriteria(Field.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
 
        return listField;
	}
	
	@Override
	public ClassEntity getClassEntityByName(String className) {
		@SuppressWarnings("unchecked")
        ClassEntity classEntity = (ClassEntity) sessionFactory.getCurrentSession()
                .createCriteria(ClassEntity.class)
                .add(Restrictions.eq("className", className))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
 
        return classEntity;
	}
	
	@Override
	public List<Field> getMemberFieldsByClassType() {
		@SuppressWarnings("unchecked")
        List<Field> listField = (List<Field>) sessionFactory.getCurrentSession()
                .createCriteria(Field.class)
                .add(Restrictions.eq("classType", "pojo"))
                .add(Restrictions.eq("fieldType", "member"))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
 
        return listField;
	}
	
//	Duplicate method - to be removed
	@Override
	public List<DTORelation> getDTORelationsByDtoName(String dtoName) {
		@SuppressWarnings("unchecked")
        List<DTORelation> listDtoRelation = (List<DTORelation>) sessionFactory.getCurrentSession()
                .createCriteria(DTORelation.class)
                .add(Restrictions.eq("dtoName", dtoName))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
 
        return listDtoRelation;
	}
	
	@Override
	public List<Field> getFieldListByDtoName(String dtoName) {
		@SuppressWarnings("unchecked")
        List<Field> listField = (List<Field>) sessionFactory.getCurrentSession()
                .createCriteria(Field.class)
                .add(Restrictions.eq("className", dtoName))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		
        return listField;
	}
	
	@Override
	public List<DTORelation> getAllDTORelations() {
		@SuppressWarnings("unchecked")
        List<DTORelation> listDtoRelation = (List<DTORelation>) sessionFactory.getCurrentSession()
                .createCriteria(DTORelation.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
 
        return listDtoRelation;
        
	}
	
//	Need to remove this method..
	@Override
	public Set<String> getPojoHierarchyByDtoName(String dtoName) {
		@SuppressWarnings("unchecked")
        List<DTORelation> listDtoRelation = getDTORelationsByDtoName(dtoName);
		Set<String> pojos = new HashSet<>();
		for(DTORelation dtoRelation:listDtoRelation){
			pojos.add(dtoRelation.getDtoPojoClassName());
		}
		
		@SuppressWarnings("unchecked")
        List<Field> fieldList= (List<Field>) sessionFactory.getCurrentSession()
                .createCriteria(Field.class)
                .add(Restrictions.eq("classType", "pojo"))
                .add(Restrictions.eq("fieldType", "member"))
                .add(Restrictions.in("className", pojos))
                .add(Restrictions.isNotNull("fieldReturnType"))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
        
        Set<String> pojoHierarchySet = new HashSet<>();
        for(Field field : fieldList){
        	String pojoHierarchy = new String();
        	String fullPojoName = field.getFieldReturnType();
        	fullPojoName = fullPojoName.substring(0, 1).toLowerCase() 
			+ fullPojoName.substring(1);
        	String pojoName = fullPojoName.substring(
        			fullPojoName.lastIndexOf("."), fullPojoName.length());
        	String className = field.getClassName();
        	className = className.substring(0, 1).toLowerCase() 
        			+ className.substring(1);
        	pojoHierarchy = className + "." + pojoName;
        	pojoHierarchySet.add(pojoHierarchy);
        }
		
        return pojoHierarchySet;
	}

}
