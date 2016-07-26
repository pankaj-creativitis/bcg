package BCG5.bcg;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

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
import BCG5.bcg.business.my.dto.DtoRelationDto;
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
    @Path("/uploadConfigFile/{configFilePath : .+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadBaseFiles(@PathParam("configFilePath")String configFilePath) throws Exception{
//    	String workingDir = "/home/ngadmin/neonworkspace/bcgNew/src/main/java/BCG5/bcg/business/client/pojos";
    	
    	String rootDir = getRootPath();
//    	String workingDir = Constants.CLIENT_PACKAGE + Constants.CLIENT_POJO_PACKAGE;
    	
    	String workingDir = rootDir + Constants.SRC.substring(1, Constants.SRC.length()) 
    	+ Constants.POJO_PKG.replace(".", "/");
    	System.out.println(workingDir);
    	File configFile = new File(configFilePath);
    	Set<String> configValues = unzipUtility.readFile(configFile);
    	System.out.println(configValues);
    	String jsonFilePath = null;
    	String pojoFilePath = null;
    	String projectroot = null;
    	String basepackage = null;
    	for(String configValue: configValues){
    		
//    		Need to remove the if-else and simple pick and put all config values in a map
    		if(configValue.contains(Constants.POJOZIP)){
    			pojoFilePath = configValue.substring(configValue.indexOf(Constants.EQUAL) + 1, 
    					configValue.length());
    		} else if(configValue.contains(Constants.JSONZIP)){
    			jsonFilePath = configValue.substring(configValue.indexOf(Constants.EQUAL) + 1, 
    					configValue.length());
    		} else if(configValue.contains(Constants.PROJECTROOT)){
    			System.out.println("projectroot > > >"+configValue);
    			projectroot = configValue.substring(configValue.indexOf(Constants.EQUAL) + 1, 
    					configValue.length());
    			System.out.println("projectroot > > >"+projectroot);
    		} else if(configValue.contains(Constants.BASEPACKAGE)){
    			System.out.println("configValue > > >"+configValue);
    			basepackage = configValue.substring(configValue.indexOf(Constants.EQUAL) + 1, 
    					configValue.length());
    			System.out.println("basepackage > > >"+basepackage);
    		}
    	}
    	 String baseLocation = projectroot + Constants.SRC + basepackage.replaceAll("\\.", "/");
    	 System.out.println("baseLocation > > >"+baseLocation);
         try {
        	 unzipUtility.unzip(pojoFilePath, workingDir, basepackage, baseLocation);
//           compile the newly imported classes
        	 String classDirectory = "/home/ngadmin/neonworkspace/bcgNew";
//        	 String classDirectory = "/home/ngadmin/neonworkspace/bcgNew/src/main/java/BCG5/bcg/business/client/pojos";
        	 unzipUtility.compile(classDirectory);
//        	 save the pojo meta info in database
        	 classEntityService.addClassEntities(workingDir, basepackage);
         } catch (Exception ex) {
             // some errors occurred
             ex.printStackTrace();
         }
         
         try {
        	 Map<String, Set<DtoRelationDto>> dtoMap = unzipUtility.unzipJsonNew(jsonFilePath);
        	 for (Map.Entry<String, Set<DtoRelationDto>> entry : dtoMap.entrySet())
        	 {
        	     dtoMakerService.addDtoNew(entry.getKey(), entry.getValue(), baseLocation, basepackage);
        	 }
         } catch (Exception ex) {
             // some errors occurred
             ex.printStackTrace();
         }
         
       return Response.accepted().build();
       
    }
    
    public String getRootPath() throws UnsupportedEncodingException {
		String path = this.getClass().getClassLoader().getResource("").getPath();
		String fullPath = URLDecoder.decode(path, "UTF-8");
		String newFullPath[] = fullPath.split("target/classes/");
		return newFullPath[0];
	}

}
