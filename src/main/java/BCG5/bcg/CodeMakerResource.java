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
	
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		TestPojo pojo = new TestPojo();
//		Class class1 = pojo.getClass();
//		
//		Member[] mbrs = class1.getDeclaredFields();
//	    for(int i = 0; i < mbrs.length; i++) {
//	        System.out.println("member = " + mbrs[i].toString());
//	     }
//		System.out.println();
//
//	}
	
//	For next steps to produce the zip file
//	@Produces(MediaType.APPLICATION_OCTET_STREAM)
//    return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
//  	      .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
//  	      .build();
	
	
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
//    	System.out.println("in the resource POST method");
//    	String workingDir = "/home/ngadmin/neonworkspace/bcgNew/src/main/java/BCG5/bcg/business/client/pojos";
    	String workingDir = Constants.CLIENT_PACKAGE + Constants.CLIENT_POJO_PACKAGE;
// 	   System.out.println("Current working directory : " + workingDir);
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
    public Response uploadJson(@PathParam("filePath")String filePath) throws Exception{
//    	System.out.println("in the resource POST method");
         try {
        	 Map<String, Set<String>> dtoMap = unzipUtility.unzipJson(filePath);
        	 for (Map.Entry<String, Set<String>> entry : dtoMap.entrySet())
        	 {
//        	     System.out.println(entry.getKey() + "/" + entry.getValue());
        	     dtoMakerService.addDto(entry.getKey(), entry.getValue());
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
    @Path("/TestPropertyDto/{dtoName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testPropertyDto(@PathParam("filePath")String dtoName) throws Exception{
    	logger.info("in the resource POST method");
    	Set<String> propertyDtos = null;
         try {
        	 
//        	 propertyDtos = daoMakerService.testDao("PlanetView");
        	 propertyDtos = daoMakerService.testDao("PlanetView");
             
         } catch (Exception ex) {
             // some errors occurred
             ex.printStackTrace();
         }
       return Response.ok(propertyDtos).build();
       
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

}
