package data;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class RecFileArr {

    private static RecFileArr recFileArr;

    private RecFileArr(){
        this.recFileQueue = new LinkedList<>();
        this.resultRecFileQueue = new LinkedList<>();
        File[] files = new File(recordFilePath).listFiles();
        this.filesChangeRecFile2(files);
    }

    public static RecFileArr getInstance(){
        if ( RecFileArr.recFileArr == null) {
            RecFileArr.recFileArr = new RecFileArr();
        }
        return recFileArr;
    }

    public final String extension = ".mp3";
    public final String recordFilePath  = "D:\\RecSee\\RecSee_Data_20201111_11";

    public Queue<RecFile> recFileQueue ;

    public Queue<RecFile> resultRecFileQueue;





    //queue
    public void filesChangeRecFile2(File[] files){
        this.recFileQueue = Arrays.stream(files).map(file -> {
            String[] nameArr = file.getName().replace(extension,"").split("_");
            RecFile recFile = new RecFile(nameArr[0], nameArr[1], nameArr[2]);
            return recFile;
        }).filter(recFile -> {
            if( recFile.getFlag() ){
                return true;
            }else{
                return false;
            }
        }).collect(Collectors.toCollection(LinkedList::new));
    }


    public Queue<RecFile> getRecFileQueue() {
        return recFileQueue;
    }

    public void setRecFileQueue(Queue<RecFile> recFileQueue) {
        this.recFileQueue = recFileQueue;
    }

    public Queue<RecFile> getResultRecFileQueue() {
        return resultRecFileQueue;
    }

    public void setResultRecFileQueue(Queue<RecFile> resultRecFileQueue) {
        this.resultRecFileQueue = resultRecFileQueue;
    }
}
