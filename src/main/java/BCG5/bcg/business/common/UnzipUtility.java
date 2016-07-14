package BCG5.bcg.business.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.maven.cli.MavenCli;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

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
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory) throws IOException {
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
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
                try {
                	String className = entry.getName().substring(0, entry.getName().indexOf("."));
//                	System.out.println("fileName > > > > > "+ className);
                	
//					Class<?> compiledClass = InMemoryJavaCompiler.compile("BCG5.bcg.business.client.pojos"+className, fileContent);
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
    
    public Map<String, Set<String>> unzipJson(String zipFilePath ) throws IOException {
    	logger.info("in the unzip method > > > > > >> ");
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        Map<String, Set<String>> dtoMap = new HashMap<>();
        while (entry != null) {
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
            	String dtoName = entry.getName();
            	Set<String> jsonKeys = extractFileAsJsonKeys(zipIn);
            	dtoMap.put(dtoName, jsonKeys);
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
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        bos.write("package BCG5.bcg.business.client.pojos;".getBytes());
        bos.write(ENDL.getBytes());
        bos.write("//".getBytes());
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
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
     */
    private Set<String> extractFileAsJsonKeys(ZipInputStream zipIn) throws IOException {
    	
        String jsonTxt = IOUtils.toString(zipIn);
        JSONObject json = (JSONObject) new JSONObject( jsonTxt );  
//        System.out.println(json.toString());
        Iterator<?> keys = json.keys();
        Set<String> jsonKeys = new HashSet<>();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
//            System.out.println(key);
            jsonKeys.add(key);
            if ( json.get(key) instanceof JSONObject ) {
//            	TODO:Possibly make a recursive function in case we need the inner Json keys..
//            	System.out.println("nothing done");
            }
        }
        return jsonKeys;
    }
    
    public void compile(String classDirectory){
    	 MavenCli cli = new MavenCli();
         int result = cli.doMain(new String[]{"clean", "install"},
        		 classDirectory,
                 System.out, System.out);
//         System.out.println("result: " + result);
    }
    
//	static String readFile(String path, Charset encoding) throws IOException {
//		byte[] encoded = Files.readAllBytes(Paths.get(path));
//		return new String(encoded, encoding);
//	}

}
