package BCG5.bcg;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import BCG5.bcg.business.common.UnzipUtility;
import BCG5.bcg.business.my.dao.ClassEntityDao;
import BCG5.bcg.business.my.dao.DTODao;
import BCG5.bcg.business.my.dao.DaoMakerDao;
import BCG5.bcg.business.my.dao.impl.ClassEntityDaoImpl;
import BCG5.bcg.business.my.dao.impl.DaoMakerDaoImpl;
import BCG5.bcg.business.my.dao.impl.DtoDaoImpl;
import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.DTO;
import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.domain.Field;
import BCG5.bcg.business.my.service.ClassEntityService;
import BCG5.bcg.business.my.service.DaoMakerService;
import BCG5.bcg.business.my.service.DtoMakerService;
import BCG5.bcg.business.my.service.GenericCodeGeneratorService;
import BCG5.bcg.business.my.service.ServiceMakerService;
import BCG5.bcg.business.my.service.impl.ClassEntityServiceImpl;
import BCG5.bcg.business.my.service.impl.DTOMakerServiceImpl;
import BCG5.bcg.business.my.service.impl.DaoMakerServiceImpl;
import BCG5.bcg.business.my.service.impl.GenericCodeGeneratorServiceImpl;
import BCG5.bcg.business.my.service.impl.ServiceMakerServiceImpl;

@Component
@Configuration
@ComponentScan(basePackages={"BCG5.bcg"})
@EnableTransactionManagement
public class SpringAppConfiguration {

    @Bean(name = "unzipUtility")
    public UnzipUtility unzipUtility() {
        return new UnzipUtility();
    }
    
    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/bcgdb");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
     
        return dataSource;
    }
    
    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {
     
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.addAnnotatedClasses(ClassEntity.class,Field.class,DTO.class,DTORelation.class);
        sessionBuilder.addProperties(getHibernateProperties());
        return sessionBuilder.buildSessionFactory();
    }
    
    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }
    
    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(
            SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(
                sessionFactory);
     
        return transactionManager;
    }
    
    @Autowired
    @Bean(name = "classEntityDao")
    public ClassEntityDao classEntityDao(SessionFactory sessionFactory) {
        return new ClassEntityDaoImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "classEntityService")
    public ClassEntityService classEntityService(ClassEntityDao classEntityDao) {
        return new ClassEntityServiceImpl(classEntityDao);
    }
    
    @Autowired
    @Bean(name = "dtoDao")
    public DTODao dtoDao(SessionFactory sessionFactory) {
        return new DtoDaoImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "daoMakerDao")
    public DaoMakerDao daoMakerDao(SessionFactory sessionFactory) {
        return new DaoMakerDaoImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "dtoMakerService")
    public DtoMakerService dtoMakerService(DTODao dtoDao) {
        return new DTOMakerServiceImpl(dtoDao);
    }
    
    @Autowired
    @Bean(name = "GenericCodeGeneratorServiceImpl")
    public GenericCodeGeneratorService genericCodeGeneratorService() {
        return new GenericCodeGeneratorServiceImpl();
    }
    
    @Autowired
    @Bean(name = "daoMakerService")
    public DaoMakerService daoMakerService(ClassEntityDao classEntityDao) {
        return new DaoMakerServiceImpl(classEntityDao);
    }
    
    @Autowired
    @Bean(name = "serviceMakerService")
    public ServiceMakerService serviceMakerService(ClassEntityDao classEntityDao) {
        return new ServiceMakerServiceImpl(classEntityDao);
    }
}
