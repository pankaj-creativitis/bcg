package BCG5.bcg.business.my.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import BCG5.bcg.business.my.dao.ClassEntityDao;
import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.domain.Field;

@Repository
public class ClassEntityDaoImpl implements ClassEntityDao {

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
		List<Field> listField = (List<Field>) sessionFactory.getCurrentSession().createCriteria(Field.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listField;
	}

	@Override
	public ClassEntity getClassEntityByName(String className) {
		ClassEntity classEntity = (ClassEntity) sessionFactory.getCurrentSession().createCriteria(ClassEntity.class)
				.add(Restrictions.eq("className", className)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.uniqueResult();

		return classEntity;
	}

	// Marked for Maybe deletion
	@Override
	public List<Field> getMemberFieldsByClassType() {
		@SuppressWarnings("unchecked")
		List<Field> listField = (List<Field>) sessionFactory.getCurrentSession().createCriteria(Field.class)
				.add(Restrictions.eq("classType", "pojo")).add(Restrictions.eq("fieldType", "member"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listField;
	}

	@Override
	public List<Field> getFieldListByDtoName(String dtoName) {
		@SuppressWarnings("unchecked")
		List<Field> listField = (List<Field>) sessionFactory.getCurrentSession().createCriteria(Field.class)
				.add(Restrictions.eq("className", dtoName)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listField;
	}

	@Override
	public List<DTORelation> getAllDTORelations() {
		@SuppressWarnings("unchecked")
		List<DTORelation> listDtoRelation = (List<DTORelation>) sessionFactory.getCurrentSession()
				.createCriteria(DTORelation.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listDtoRelation;

	}

}
