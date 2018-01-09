package zipFileUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Class to create zip files and add files to the zipfile.<br><br>
 * Example use: <br><br>
 * <code>
 * try{<br>
 * <t>ZipFileCreator zipFile = new ZipFileCreator("Test.zip");
 * <br>	String[] fileNames = {"textFile.txt","sample.jpg"};
 * <br>	for(int i = 0; i < fileNames.length; i++){
 * <br>	zipFile.addFile(fileNames[i]);
 * <br>}
 * <br>	zipFile.finalize();
 * <br>
 * <br>		System.out.println("File created successfully!!!!");
 * <br>	} 
 * <br>		catch(Exception e){
 * <br>			e.printStackTrace();
 * <br>			System.out.println("Error creating file");
 * <br>		}
 * </code>
 * @author Andrew McGlynn - 01/09/2011
 *
 */
public class ZipFileCreator {
	private String zipFile;
	private ZipOutputStream output;
	
	/**
	 * Create the zipfile
	 * @param outputName the name of the zipfile
	 * @throws IOException
	 */
	public ZipFileCreator(String outputName)throws IOException{
		this.zipFile = outputName;
		this.output = new ZipOutputStream(new FileOutputStream(zipFile));
	}
	/**
	 * Add a zip entry to the zipfile.
	 * @param filename the input file to be added to the zip file
	 * @throws IOException 
	 */
	public void addFile(String filename)throws IOException{
		byte[] buf = new byte[1024];		//buffer to read in a zip file entry
		//read in the file to be inserted in the zip file
		FileInputStream in = new FileInputStream(filename);
		//create a zipfile entry
		output.putNextEntry(new ZipEntry(filename));

		//loop through the input file and write it to zipfile
		int len;
		while((len = in.read(buf))>0){
			output.write(buf, 0, len);
		}
		
		//close the zip entry and the input file
		output.closeEntry();
		in.close();
	}
	
	/**
	 * Finalize and close the zip file
	 */
	public void finalize()throws IOException{
		output.close();
	}
}
