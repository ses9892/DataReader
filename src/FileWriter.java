

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class FileWriter {
	public final String fileUploadPath = "C:/fileOutput/";
	public final String extension = ".log";
	private String keyword;
	private StringBuffer searchedLogBuffer;
	

	public FileWriter(String keyword, StringBuffer buffer) {
		this.keyword = keyword;
		this.searchedLogBuffer = buffer;
	}
	
	
	
	public boolean fileWrite() {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		if(!this.bufferCheck()) {
			//error
			return false;
		}
		File file = new File(this.filePathNameRefactor());
		try {
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(
					new OutputStreamWriter(fos, "UTF-8"));
			bw.write(this.searchedLogBuffer.toString());
			bw.flush();
		} catch(IOException ex){
			System.out.println("에러발생");
		}finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {

				}				
			}
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {

				}				
			}
			
		}
		return true;
		
	}
	
	public String filePathNameRefactor() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		System.out.println(sdf.format(date));
		String fileName_date = this.keyword+"_"+sdf.format(date);
//		System.out.println(fileName_date);
		String filePathName = this.fileUploadPath+fileName_date+this.extension;
		return filePathName;
	}
	
	public Boolean bufferCheck() {
		if(this.searchedLogBuffer.toString().trim().length()==0 || this.searchedLogBuffer ==null) {
			return false;
			}
		return true;
	}
	
	
	
	
	
}
