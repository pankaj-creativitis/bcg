package BCG5.bcg.business.common;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.maven.cli.MavenCli;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import BCG5.bcg.business.my.dto.DtoRelationDto;

@Component("unzipUtility")
public class UnzipUtility {
	
//	TODO: Need to devise a way to compile new client classes at compile time. At-present, a second call is needed to make the entries in database.
	
	private static final Logger logger = Logger.getLogger(UnzipUtility.class);
	private String ENDL = System.getProperty("line.separator");
	 /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    
	 /**
     * Reads the file line by line and returns the config values
     */
    public Set<String> readFile(File fin) throws IOException {
    	FileInputStream fis = new FileInputStream(fin);
     
    	//Construct BufferedReader from InputStreamReader
    	BufferedReader br = new BufferedReader(new InputStreamReader(fis));
     
    	String line = null;
    	Set<String> configValues = new HashSet<>();
    	while ((line = br.readLine()) != null) {
    		line = line.trim();
    		line = line.replace(" ", "");
    		configValues.add(line);
    	}
     
    	br.close();
    	return configValues;
    }
    
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory, String basePackage, String baseLocation) 
    		throws IOException {
    	logger.info("in the unzip method > > > > > >> ");
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            
//         Set the client path location
//        	String baseLocation = projectRoot + Constants.SRC + basePackage.replaceAll("\\.", "/");
//        	System.out.println("baseLocation > > > "+baseLocation);
            String pojoLocation = baseLocation + Constants.CLIENT_POJO_PACKAGE;
            File baseLocationDir = new File(pojoLocation);
            if (!baseLocationDir.exists()) {
            	baseLocationDir.mkdirs();
            }
            String clientFilePath = pojoLocation + File.separator + entry.getName();
            
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath, clientFilePath, basePackage);
                try {
                	String className = entry.getName().substring(0, entry.getName().indexOf("."));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    
//    Method Marked for deletion
//    public Map<String, Set<String>> unzipJson(String zipFilePath ) throws IOException {
//    	logger.info("in the unzip method > > > > > >> ");
//        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
//        ZipEntry entry = zipIn.getNextEntry();
//        Map<String, Set<String>> dtoMap = new HashMap<>();
//        while (entry != null) {
//            if (!entry.isDirectory()) {
//                // if the entry is a file, extracts it
//            	String dtoName = entry.getName();
//            	Set<String> jsonKeys = extractFileAsJsonKeys(zipIn);
//            	dtoMap.put(dtoName, jsonKeys);
//            } 
//            zipIn.closeEntry();
//            entry = zipIn.getNextEntry();
//        }
//        zipIn.close();
//        return dtoMap;
//    }
    
    public Map<String, Set<DtoRelationDto>> unzipJsonNew(String zipFilePath ) throws IOException {
    	logger.info("in the unzip method > > > > > >> ");
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        Map<String, Set<DtoRelationDto>> dtoMap = new HashMap<>();
        while (entry != null) {
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
            	String dtoName = entry.getName();
            	Set<DtoRelationDto> jsonDtos = extractFileAsJsonKeysNew(zipIn);
            	dtoMap.put(dtoName, jsonDtos);
            } 
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        return dtoMap;
    }
    
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath, String clientFilePath,
    		String basePackage) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        BufferedOutputStream clientBos = new BufferedOutputStream(new FileOutputStream(clientFilePath));
        String clientPackage = "package " + basePackage + Constants.CODE_POJO_PKG;

        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        
        clientBos.write(clientPackage.getBytes());
        clientBos.write(ENDL.getBytes());
        clientBos.write("//".getBytes());
        
        bos.write("package BCG5.bcg.business.client.pojos;".getBytes());
        bos.write(ENDL.getBytes());
        bos.write("//".getBytes());
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
            clientBos.write(bytesIn, 0, read);
        }
        bos.close();
        clientBos.close();
        
//        byte[] clientBytesIn = new byte[BUFFER_SIZE];
//        int readCount = 0;
//        String clientPackage = "package " + basePackage + ".pojos";
//        clientBos.write(clientPackage.getBytes());
//        clientBos.write(ENDL.getBytes());
//        clientBos.write("//".getBytes());
//        while ((readCount = zipIn.read(clientBytesIn)) != -1) {
//        	clientBos.write(clientBytesIn, 0, readCount);
//        }
//        clientBos.close();
    }
    
    /**
     * Extracts a zip entry (file entry)
     * @param packageName TODO
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    public void makeClientFiles(String fileText, String fileOutputPath, String packageName) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileOutputPath));
        
        bos.write(packageName.getBytes());
        bos.write(ENDL.getBytes());
        bos.write(ENDL.getBytes());
        bos.write(fileText.getBytes());
        bos.close();
    }
    
    /**
     * Extracts a zip entry as a JSON object (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     * Method Marked for deletion
     */
    private Set<DtoRelationDto> extractFileAsJsonKeysNew(ZipInputStream zipIn) throws IOException {
    	
        String jsonTxt = IOUtils.toString(zipIn);
        JSONObject json = (JSONObject) new JSONObject( jsonTxt );  
        Iterator<?> keys = json.keys();
        Set<DtoRelationDto> jsonDtos = new HashSet<>();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            DtoRelationDto dto = new DtoRelationDto();
            dto.setFieldName(key);
            if(json.get(key) instanceof String == false){
            	JSONArray innerJson = json.getJSONArray(key);
            	String conditionalKey = new String();
            	for(int i =0;i< innerJson.length(); i++ ) {
                    String innerKey = (String)innerJson.getString(i);
                    conditionalKey = innerKey + "," + conditionalKey;
            	}
            	conditionalKey = conditionalKey.substring(0, conditionalKey.length()-1);
            	dto.setFullFieldName(conditionalKey);
            } else {
            	dto.setFullFieldName(json.getString(key));
            }
            jsonDtos.add(dto);
            if ( json.get(key) instanceof JSONObject ) {
//            	TODO:Possibly make a recursive function in case we need the inner Json keys..
            }
        }
        return jsonDtos;
    }
    
    /**
     * Extracts a zip entry as a JSON object (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private Set<String> extractFileAsJsonKeys(ZipInputStream zipIn) throws IOException {
    	
        String jsonTxt = IOUtils.toString(zipIn);
        JSONObject json = (JSONObject) new JSONObject( jsonTxt );  
        Iterator<?> keys = json.keys();
        Set<String> jsonKeys = new HashSet<>();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            jsonKeys.add(key);
            if ( json.get(key) instanceof JSONObject ) {
//            	TODO:Possibly make a recursive function in case we need the inner Json keys..
            }
        }
        return jsonKeys;
    }
    
    public void compile(String classDirectory){
    	 MavenCli cli = new MavenCli();
         int result = cli.doMain(new String[]{"clean", "install"},
        		 classDirectory,
                 System.out, System.out);
    }

}
