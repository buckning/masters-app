package zipFileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
/**
 * Class to unzip a zip file.
 * <br> Sample use: <br>
 * <code>
 * try{
 * <br>	Unzip unzip = new Unzip("test.zip",Unzip.DELETE_ZIP_FILE_AFTER_UNZIPPING);
 * 	<br>		unzip.unzipAllEntries();
 * <br>		}
 * 	<br>	catch(FileNotFoundException e){
 * 	<br>		e.printStackTrace();
 * 	<br>	}
 * </code>
 * @author Andrew McGlynn - 01/09/2011
 *
 */
public class Unzip {
	private int bufferSize = 1024;
	private File f;
	private int option;
	public static final int DELETE_ZIP_FILE_AFTER_UNZIPPING = 0;
	public static final int KEEP_ZIP_FILE_AFTER_UNZIPPING = 1;
	
	public Unzip(String zipFilename, int option)throws FileNotFoundException{
		f = new File(zipFilename);
		this.option = option;
		if(!f.isFile()){
			throw new FileNotFoundException();
		}
	}	
	/**
	 * Unzip all the contents of the zipfile to the current directory
	 */
	public void unzipAllEntries(){
		BufferedOutputStream bos = null;
		
		try{
			FileInputStream in = new FileInputStream(f);
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(in));
			ZipEntry entry;
			
			while((entry = zis.getNextEntry()) != null){
				int count;
				byte[] data = new byte[bufferSize];
				FileOutputStream fos = new FileOutputStream(entry.getName());	//name the new file the same as the zip entry
				bos = new BufferedOutputStream(fos, bufferSize);
			
				while((count = zis.read(data, 0, bufferSize)) != -1){
					bos.write(data,0,count);
				}
				bos.flush();
				bos.close();	
			}
			zis.close();
			if(option == DELETE_ZIP_FILE_AFTER_UNZIPPING)f.delete();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
