package BCG5.bcg.business.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import BCG5.bcg.business.my.dao.ClassEntityDao;
import BCG5.bcg.business.my.service.impl.ServiceMakerServiceImpl;

public abstract class  Constants {
	
	public static final String CLASS_START = "public class"; 
	public static final String CLASS =".class";
	
	public static final String LIST = "List";
	public static final String SET = "Set";
	
	public static final String CURLY_BRACKET_B = "{";
	public static final String CURLY_BRACKET_E = "}";
	public static final String BRACKET_B = "(";
	public static final String BRACKET_E = ")";
	public static final String PLUS = "+";
	public static final String PRIVATE ="private";
	public static final String PUBLIC ="public";
	public static final String VOID ="void";
	public static final String EQUAL ="=";
	public static final String SEMICOLON=";";
	public static final String GET_PREFIX ="get";
	public static final String SET_PREFIX="set";
	public static final String RETURN="return";
	public static final String THIS="this.";
	public static final String SPACE =" ";
	public static final String TAB ="	";
	public static final String COMMA =",";
	public static final String INSERT_HOOK ="In$3rtH00k";
	public static final String IMPORT_HOOK ="Im90rtH00k";
	public static final String CLASSHOOK = "cl@55H00k";
	public static final String POJOHOOK = "p0j0H00k";
	public static final String DAOHOOK = "d40H00k";
	public static final String ALIASHOOK = "41i45H00k";
	public static final String PROPERTYHOOK = "9r093rtyH00k";
	public static final String VARIABLEHOOK = "v4ri46le4H00k";
	public static final String NEW_LINE =System.getProperty("line.separator");
	public static final String JAVA_EXT =".java";
	public static final String ANG_BRACKET_B="<";
	public static final String ANG_BRACKET_E=">";
	public static final String IMPORT="import ";
	
	public static final String CLIENT_PACKAGE ="/home/ngadmin/neonworkspace/bcgNew/src/main/java/BCG5/bcg/business/client";
	public static final String CLIENT_POJO_PACKAGE ="/pojos";
	public static final String CLIENT_DTO_PACKAGE ="/dtos";
	public static final String CLIENT_DAO_PACKAGE ="/daos/";
	public static final String CLIENT_SERVICE_PACKAGE ="/service/";
	
	public static final String CODE_PKG = "package BCG5.bcg.business.client.";
	public static final String CODE_DTO_PKG = "dtos;";
	public static final String CODE_DAO_PKG = "daos;";
	public static final String CODE_SERVICE_PKG = "service;";
	public static final String POJO_PKG = "BCG5.bcg.business.client.pojos.";
	public static final String ALL_DAO_PKG = "BCG5.bcg.business.client.daos.";
	public static final String DTO_PKG = "BCG5.bcg.business.client.dtos.";
	
	public static final String SUPPRESS_WARNINGS = "@SuppressWarnings(\"unchecked\")";
	public static final String REPOSITORY = "@Repository";
	public static final String SERVICE = "@Service";
	public static final String TRANSACTIONAL = "@Transactional";
	
    public enum ClassType {
        POJO("pojo"),
        DAO("Dao"),
        DAOIMPL("DaoImpl"),
        SERVICE("Service"),
        SERVICEIMPL("ServiceImpl"),
        DTO("dto");

        private final String value;

        public String getValue() {
            return value;
        }

        private ClassType(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
    
    public enum CommonDaoStatements {
    	GETSESSION("sessionFactory.getCurrentSession()"),
    	LIST(".list()");
    	
        private final String value;

        public String getValue() {
            return value;
        }

        private CommonDaoStatements(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
    
    public enum CriteriaStatements {
        CRITERIA(".createCriteria"),
        ALIAS(".createAlias("),
        SET_PROJECTION(".setProjection(Projections.projectionList()"),
        ADD_PROJECTION(".add( Projections.property("),
        SETMAXRESULT(".setMaxResults("),
        ADD_ASC_ORDER(".addOrder(Order.asc("),
        ADD_DSC_ORDER(".addOrder(Order.dsc("),
        TRANS_BEAN(".setResultTransformer(Transformers.aliasToBean("),
        TRANS_LIST(".setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list()"),
        TRANS_UNIQUE(".setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult()"),
        RESTRICTION(".add(Restrictions");

        private final String value;

        public String getValue() {
            return value;
        }

        private CriteriaStatements(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }
    
    public enum HQLStatements {
//      .createQuery("SELECT NEW BCG5.bcg.business.my.dto.PropertyDto(field.fieldName, s1.fieldName) " +
//      "From Field field, SampleOne s1, SampleTwo s2  Where " +
//      		"field.fieldName = s1.fieldName AND field.fieldName = s2.fieldName")
    	CREATEQUERY(".createQuery"),
    	SELECT("SELECT"),
    	NEW("NEW"),
    	WHERE("WHERE"),
    	FROM("FROM"),
    	AND("AND");

        private final String value;

        public String getValue() {
            return value;
        }

        private HQLStatements(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    
    public static final String HBDAOIMPL = 
    "\tprivate static final Logger logger = Logger.getLogger(cl@55H00k.class);\n"
	+"\n" 
    +"\t@Autowired \n"
    +"\tprivate SessionFactory sessionFactory;\n"
    +"\n"
	+"\tpublic cl@55H00k(SessionFactory sessionFactory) {\n"
	+"\t	this.sessionFactory = sessionFactory;\n"
	+"\t}\n"
	+"\n"
	+"\tpublic cl@55H00k() {\n"
	+"\t	super();\n"
	+"\t}\n";
    
    public static final String SPRDAOIMPL = 
	"\tprivate static final Logger logger = Logger.getLogger(cl@55H00k.class);\n"
    +"\n"
	+"\t@Autowired \n"
	+"\tprivate d40H00k v4ri46le4H00k;\n"
    +"\n"
	+"\tpublic cl@55H00k(d40H00k v4ri46le4H00k) {\n"
	+"\t	super();\n"
	+"\t	this.v4ri46le4H00k = v4ri46le4H00k;\n"
	+"\t}\n"
    +"\n"
	+"\tpublic cl@55H00k() {\n"
	+"\t	super();\n"
	+"\t}\n";
	
    public static final String HBDAOIMPORT =
    "import org.apache.log4j.Logger;\n"
    +"import org.hibernate.SessionFactory;\n"
    +"import org.hibernate.criterion.Projections;\n"
    +"import org.hibernate.transform.Transformers;\n"
    +"import org.springframework.beans.factory.annotation.Autowired;\n"
    +"import org.springframework.stereotype.Repository;\n";
    
    public static final String SPRDAOIMPORT = 
    "import org.apache.log4j.Logger;\n"
    +"import org.springframework.beans.factory.annotation.Autowired;\n"
    +"import org.springframework.stereotype.Service;\n"
    +"import org.springframework.transaction.annotation.Transactional;\n";

}
