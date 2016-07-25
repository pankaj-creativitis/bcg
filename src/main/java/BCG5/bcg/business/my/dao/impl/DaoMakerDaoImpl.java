package BCG5.bcg.business.my.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import BCG5.bcg.business.common.Constants;
import BCG5.bcg.business.my.dao.DaoMakerDao;
import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.domain.Field;
import BCG5.bcg.business.my.dto.PropertyDto;

@Repository
public class DaoMakerDaoImpl implements DaoMakerDao {

	private static final Logger logger = Logger.getLogger(DaoMakerDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public DaoMakerDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public DaoMakerDaoImpl() {
		super();
	}

	@Override
	public List<PropertyDto> getPropertyDtos(String dtoName) {
		@SuppressWarnings("unchecked")
		List<PropertyDto> listPropertyDto = (List<PropertyDto>) sessionFactory.getCurrentSession()
				.createCriteria(Field.class, "fld").createAlias("dtoRelations", "dtoRelation")
				.add(Restrictions.eq("dtoRelation.dtoName", dtoName))
				.setProjection(Projections.projectionList()
						.add(Projections.property("dtoRelation.dtoPojoClassName"), "pojoClassName").add(
								Projections.property("fld.fieldName"), "classProperty"))
				.setResultTransformer(Transformers.aliasToBean(PropertyDto.class)).list();
		return listPropertyDto;
	}

	@Override
	public List<DTORelation> getDTORelationsByDtoName(String dtoName) {
		@SuppressWarnings("unchecked")
		List<DTORelation> listDtoRelation = (List<DTORelation>) sessionFactory.getCurrentSession()
				.createCriteria(DTORelation.class).add(Restrictions.eq("dtoName", dtoName))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		return listDtoRelation;
	}

	@Override
	public Set<String> getPojoHierarchyByDtoName(String dtoName) {
		List<DTORelation> listDtoRelation = getDTORelationsByDtoName(dtoName);
		Set<String> pojos = new HashSet<>();
		for (DTORelation dtoRelation : listDtoRelation) {
			pojos.add(dtoRelation.getDtoPojoClassName());
		}

		// System.out.println(pojos);

		@SuppressWarnings("unchecked")
		List<Field> fieldList = (List<Field>) sessionFactory.getCurrentSession().createCriteria(Field.class)
				.add(Restrictions.eq("classType", "pojo")).add(Restrictions.eq("fieldType", "member"))
				.add(Restrictions.in("className", pojos)).add(Restrictions.isNotNull("fieldReturnType"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		Set<String> pojoHierarchySet = new HashSet<>();
		for (Field field : fieldList) {
			String pojoHierarchy = new String();
			String fullPojoName = field.getFieldReturnType();
			fullPojoName = fullPojoName.substring(0, 1).toLowerCase() + fullPojoName.substring(1);
			String pojoName = fullPojoName.substring(fullPojoName.lastIndexOf(".") + 1, fullPojoName.length());
			pojoName = pojoName.substring(0, 1).toLowerCase() + pojoName.substring(1);
			String className = field.getClassName();
			className = className.substring(0, 1).toLowerCase() + className.substring(1);

			String fieldName = field.getFieldName();
			fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
			pojoHierarchy = className + "." + fieldName + "." + pojoName;
			pojoHierarchySet.add(pojoHierarchy);
		}

		return pojoHierarchySet;
	}

	// This method might be reused in case of alias methods as well, need to
	// refactor later
	@Override
	public List<String> getHQLFields(String dtoName) {

		@SuppressWarnings("unchecked")
		List<String> dtoRelations = (List<String>) sessionFactory.getCurrentSession()
				.createQuery("SELECT dr.dtoFieldName FROM DTORelation dr WHERE dr.dtoName = :dtoName "
						+ "order by dr.dtoFieldName asc")
				.setParameter("dtoName", dtoName).list();

		return dtoRelations;
	}

	// This method might be needed to be removed for better approach of checking
	// the join field
	// by adding extra field in the FIELD table
	@Override
	public Boolean checkHQLMethod(String dtoName) {
		Boolean isJoin = false;
		@SuppressWarnings("unchecked")
		List<String> dtoRelations = (List<String>) sessionFactory.getCurrentSession()
				.createQuery("SELECT dr.dtoFieldName FROM DTORelation dr WHERE dr.dtoName = :dtoName "
						+ "AND dr.dtoFieldName LIKE :commaToken")
				.setParameter("dtoName", dtoName).setParameter("commaToken", "%,%").list();

		if (null != dtoRelations && dtoRelations.size() != 0) {
			isJoin = true;
		}
		return isJoin;
	}

}
