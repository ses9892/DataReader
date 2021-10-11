package LogRead;

import data.RecFile;
import data.RecFileArr;
import data.ResultInsertSqlLog;
import data.ResultInsertSqlLogList;

import java.io.*;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlReader extends FileReaderUtil implements LogFileReaderInterFace{


    public SqlReader(Queue<RecFile> recFiles) {
        super(recFiles);
    }



    @Override
    public void openInputStream() {
//        files 에는 해당 확장자를 고려하지 않고 읽어야할 파일이 준비되잇는상태
        File file = this.extensionFilter();
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"));
            this.extractInsertSql();
            // 검사메소드의 영역
        } catch (FileNotFoundException e) {
            System.out.println("녹취록에 대한 파일이 존재하지 않습니다.");
        }catch (UnsupportedEncodingException ex){
            System.out.println("인코딩에러");
        } finally {
            this.closeStream(br);
        }
    }



    // 로그파일들을 확장자명에 맞게 필터링하는 메소드
    @Override
    public File extensionFilter() {
        Optional<File> optionalFile = files.stream().filter(file -> {
            if (file.getName().contains(this.sqlExtension)) {
                return true;
            } else {
                return false;
            }
        }).findFirst();
//        System.out.println(optionalFile.get());
        if(! optionalFile.isPresent()){
            System.out.println("녹취에 대한 로그파일이 존재 하지않습니다.");
            return null;
        }
        return optionalFile.get();
    }

    //큐에대한 정보를담고 그것에대해 찾고 다시 resultQueue에 저장
    public void extractInsertSql(){
        //큐 첫번째 레코드 파일 참조 ---> poll 은 마지막 UPDATE 단위에서 참조를 하지않고 빼낸다.
        RecFile recFile = RecFileArr.getInstance().recFileQueue.peek();
        System.out.println(recFile);
        Scanner sqlScan = new Scanner(this.br);
        while (sqlScan.hasNext()) {
            String line = sqlScan.nextLine();
//            System.out.println(line);
            ResultInsertSqlLog insertSql = this.findInsertSql(line, recFile);
            if(insertSql == null){
//                System.out.println("Line is Not Insert!!");
            }else{
                ResultInsertSqlLogList.resultInsertSqlLogList.add(insertSql);
                break;
            }
        }
        //검사완료 큐에넣기 , 여기서 넣지말고 update까지 정상적으로 실행되면 그때 넣어도 무방
//        RecFileArr.resultRecFileQueue.add(recFile);
    }

    public ResultInsertSqlLog findInsertSql(String line , RecFile recFile){
        ResultInsertSqlLog sqlLog = null;
        String column = null;
        String value = null;
//        System.out.println("recfile info : "+recFile);
        //VALUES ('20201111','11','110736','2828',11,'110736','110820','23',
        String insertSql = null;
        Pattern p = Pattern.compile("\\[(.*?)\\]|\\((.*?)\\)");
        Matcher m = p.matcher(line);
        while (m.find()){
            if((m.group().contains("INSERT INTO"))){
                String group = m.group(1);
                sqlLog = new ResultInsertSqlLog(group);  //전체쿼리저장
                String[] group2 = group.replace("INSERT INTO RS_RECFILE", "").split("VALUES");
                this.sqlColumandValuesSplit(group2,sqlLog);
                Boolean searchedFlag = this.matchExtNumAndChannelNum(sqlLog, recFile);
                if(searchedFlag){
                    break;
                }else{
                    sqlLog = null;
                }
            }
            continue;
        }
        return sqlLog;
    }

    public void sqlColumandValuesSplit(String[] sqlGroup,ResultInsertSqlLog sqlLog){
        String[] columns = sqlGroup[0].replace("(", "").replace(")", "").split(",");
        String[] values = sqlGroup[1].replace("(", "").replace(")", "").split(",");
        sqlLog.setSqlColums(columns);
        sqlLog.setSqlValues(values);
//        System.out.println("colums : "+columns[0]);
//        System.out.println("values : "+values[0]);
    }

    public Boolean matchExtNumAndChannelNum(ResultInsertSqlLog sqlLog, RecFile recFile){
        String[] sqlColums = sqlLog.getSqlColums();
        String[] sqlValues = sqlLog.getSqlValues();
        int recExtNumIdx = 0; //내선번호 index
        int recChannelIdx = 0;
        for (int i=0; i<sqlColums.length; i++){
            if(sqlColums[i].contains("R_EXT_NUM")){
                recExtNumIdx = i;
            }
            if(sqlColums[i].contains("R_CH_NUM")){
                recChannelIdx = i;
        }
        }
        String queryInnerLine =sqlValues[recExtNumIdx];
        if(sqlValues[recExtNumIdx].contains(recFile.getInnerLine())){
            System.out.println("Query Searched!");
            System.out.println("Saving....");
            return true;
        }
//        RecFile{createdAt='2020/11/11/11-01-390728', innerLine='2961', userId='Self', logFileDate='2020111111', flag=true}
        return false;
    }


}

