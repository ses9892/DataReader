

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class FileReader {
	
	private final String filePath = "C:/fileInput/";
	private String fileName;
	private String keyword;
	private StringBuffer stringBuffer;
	private FileInputStream fis;
	
	public FileReader(String keyword, String fileName) {
		this.keyword = keyword;
		this.fileName = fileName;
	}

	//inputStream ????
	public void readStream() {
		this.stringBuffer = new StringBuffer();
		File file = new File(this.filePath+this.fileName);;
		try {
			this.fis = new FileInputStream(file);
			this.reader();
			this.fis.close();
		} catch (FileNotFoundException e) {
			System.out.println("??? ?????? ???????? ??????.");
		}catch(IOException ex) {
			System.out.println("?????? ???????????.");
		}finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}
	//inputStream ?? keyword???? ???
	public void reader() {
		Scanner scan = new Scanner(this.fis);
		for(int i=0; scan.hasNext(); i++) {
			String line = scan.nextLine();
			if(line.indexOf(this.keyword)!=-1) {
				this.stringBuffer.append(line+"\n");
			}
		}
	}

	public StringBuffer getStringBuffer() {
		return stringBuffer;
	}

	
	
}
