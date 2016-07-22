package BCG5.bcg;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import BCG5.bcg.business.common.Constants;
import BCG5.bcg.business.common.UnzipUtility;
import BCG5.bcg.business.my.dao.ClassEntityDao;
import BCG5.bcg.business.my.domain.ClassEntity;
import BCG5.bcg.business.my.domain.DTORelation;
import BCG5.bcg.business.my.dto.DtoRelationDto;
import BCG5.bcg.business.my.dto.PropertyDto;
import BCG5.bcg.business.my.service.ClassEntityService;
import BCG5.bcg.business.my.service.DaoMakerService;
import BCG5.bcg.business.my.service.DtoMakerService;

@Path("generateCode")
public class CodeMakerResource {

	private static final Logger logger = Logger.getLogger(CodeMakerResource.class);
	
	@Autowired
	private UnzipUtility unzipUtility;
	
	@Autowired
	private ClassEntityService classEntityService;
	
	@Autowired
	private DtoMakerService dtoMakerService;
	
	@Autowired
	private DaoMakerService daoMakerService;
	
    /**
     * Method handling HTTP POST requests. The returned object will be java Files
     * sent to the client as "text/plain" media type.
     *
     * @return SimpleObject object that will be returned as a application/json response.
     */
    @POST
    @Path("/uploadPojoZip/{filePath : .+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPojos(@PathParam("filePath")String filePath) throws Exception{
//    	String workingDir = "/home/ngadmin/neonworkspace/bcgNew/src/main/java/BCG5/bcg/business/client/pojos";
    	String workingDir = Constants.CLIENT_PACKAGE + Constants.CLIENT_POJO_PACKAGE;
         try {
        	 unzipUtility.unzip(filePath, workingDir);
//           compile the newly imported classes
        	 String classDirectory = "/home/ngadmin/neonworkspace/bcgNew";
        	 unzipUtility.compile(classDirectory);
//        	 save the pojo meta info in database
        	 classEntityService.addClassEntities(workingDir);
         } catch (Exception ex) {
             // some errors occurred
             ex.printStackTrace();
         }
       return Response.accepted().build();
       
    }
    
    @POST
    @Path("/uploadJsonZip/{filePath : .+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadJsonNew(@PathParam("filePath")String filePath) throws Exception{
         try {
        	 Map<String, Set<DtoRelationDto>> dtoMap = unzipUtility.unzipJsonNew(filePath);
        	 for (Map.Entry<String, Set<DtoRelationDto>> entry : dtoMap.entrySet())
        	 {
        	     dtoMakerService.addDtoNew(entry.getKey(), entry.getValue());
        	 }
         } catch (Exception ex) {
             // some errors occurred
             ex.printStackTrace();
         }
       return Response.accepted().build();
       
    }
    
    @GET
    @Path("/uploadPojoZip")
    @Produces(MediaType.APPLICATION_JSON)
    public String testPojos() throws Exception{
    	logger.info("in the resource GET method");
       return "hello there !";
       
    }
    
    @POST
    @Path("/addDao")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDaoClass() throws Exception{
    	logger.info("in the resource POST method");
         try {
        	 
        	daoMakerService.addDao();
             
         } catch (Exception ex) {
             // some errors occurred
             ex.printStackTrace();
         }
       return Response.accepted().build();
       
    }
    
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }

}
