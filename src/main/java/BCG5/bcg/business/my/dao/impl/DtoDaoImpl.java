package BCG5.bcg.business.my.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import BCG5.bcg.business.my.dao.DTODao;
import BCG5.bcg.business.my.domain.DTO;

@Repository
public class DtoDaoImpl implements DTODao{

	private static final Logger logger = Logger.getLogger(DtoDaoImpl.class);
	
    @Autowired
    private SessionFactory sessionFactory;
    
	public DtoDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public DtoDaoImpl() {
		super();
	}
	
	@Override
	public void addDto(DTO dto) {
		sessionFactory.getCurrentSession().saveOrUpdate(dto);
		
	}

}
