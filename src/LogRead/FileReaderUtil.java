package LogRead;

import data.RecFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class FileReaderUtil {

    // 이 클래스를 상속받으면?

    // Log , SQL , CTI 파일의 확장자명 상수
    public final String logExtension = ".LOG";
    public final String sqlExtension = ".SQL";
    public final String ctiExtension = ".CTI";
    public final String filePath = "D:\\RecSee\\Siganl_20201111";
    public Queue<File> files;
    public BufferedReader br;


    public FileReaderUtil(Queue<RecFile> recFiles) {
        this.files = new LinkedList<>();
        this.setFiles(recFiles);
    }

    public void setFiles(Queue<RecFile> recFiles){
        File[] logFiles = new File(filePath).listFiles();
        Queue<File> collect = Arrays.stream(logFiles).filter(file -> {
            String fileName = file.getName();   //파일이름
            int cnt = 0;
            for (RecFile recFile : recFiles) {    //반복검사
                if (recFile.getFlag() && fileName.contains(recFile.getLogFileDate())) {
                    cnt++;
                }
                if (cnt > 3) {
                    break;
                }
            }
            if (cnt > 3) return true;
            return false;
        }).collect(Collectors.toCollection(LinkedList::new));
        this.files = collect;
    }

    public void closeStream(BufferedReader br){
        if(br !=null){
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // 싹읽어온다.

    // 해당 파일에 대한 날짜를 변환하는 메소드

    // 어떤 확장자명을 사용할건지? 오픈하는 파일마다 클래스가 다를건데 그 클래스를 구별하게 만들어주는 메소드?
    // 파라미터로는 확장자명 , arr log file list
}
