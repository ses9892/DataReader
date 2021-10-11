package LogRead;

import data.*;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CtiReader extends FileReaderUtil implements LogFileReaderInterFace{
    private String[] szCallId;
    private String hour;
    private String callStartTime;
    private String callEndTime;

    public CtiReader(Queue<RecFile> recFiles) {
        super(recFiles);
    }

    @Override
    public void openInputStream() {
        File file = this.extensionFilter();
        ExtractCtiLog extractCtiLog = new ExtractCtiLog();
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"));
            Boolean ctiStartFlag = this.extractCtiStartLog(extractCtiLog);
            Boolean ctiEndFlag = this.extractCtiEndLog(extractCtiLog);
            if(ctiStartFlag && ctiEndFlag){
                System.out.println("Cti 로그의 정보를 정상 추출 했습니다.");
                this.modifyInsertSqlToExtractCtiData();
            }else {
                System.out.println("Cti 로그에 대한 정보가 존재하지 않습니다.");
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println("인코딩에러");
        } catch (FileNotFoundException e) {
            System.out.println("녹취록 날짜에 대한 CTI파일이 디렉토리 내부에 존재하지 않습니다.");
        }finally {
            this.closeStream(br);
        }
        System.out.println("검색한 cti 파일명 : "+file.getName());
        System.out.println(extractCtiLog);
    }

    private void modifyInsertSqlToExtractCtiData() {
        int executeCnt = ResultInsertSqlLogList.executeCnt;
        ResultInsertSqlLog resultInsertSqlLog = ResultInsertSqlLogList.resultInsertSqlLogList.get(executeCnt);

    }


    //녹취로 파일 날짜에 맞는 CTI 파일을 필터링후 File객체로 리턴
    @Override
    public File extensionFilter() {
        Optional<File> optionalFile = files.stream().filter(file -> {
            if (file.getName().contains(this.ctiExtension)) {
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

    private Boolean extractCtiStartLog(ExtractCtiLog extractCtiLog){
        Boolean extractFlag = false;    //추출을 완벽하게 해냇는지 에대한 기준
        Boolean extractCtiStartFlag = false;
        RecFile recFile = RecFileArr.getInstance().recFileQueue.peek();
        String channelNum = "CH:"+String.format("%04d",this.getChannelNum());       // 검색해야할 채널번호
        String searchLogDateFormat = recFile.convertCtiLogDate();   // 로그에서 확인해봐야할 날짜 포맷
        System.out.println(channelNum);
        Scanner ctiScan = new Scanner(this.br);
//        System.out.println(recFile.convertCtiLogDate());
        while (ctiScan.hasNext()){
            String line = ctiScan.nextLine();
            extractCtiStartFlag = this.searchStartLogAndCallId(line, channelNum, searchLogDateFormat,extractCtiLog);//여기서 flag로 끊어줘야 더 안읽는다.
            if(extractCtiStartFlag){
                extractFlag = true;
                break;
            }
        }
        return extractFlag;
    }

    private Boolean extractCtiEndLog(ExtractCtiLog extractCtiLog) {
        Boolean extractFlag = false;
        RecFile recFile = RecFileArr.getInstance().recFileQueue.peek();
        String channelNum = "CH:"+String.format("%04d",this.getChannelNum());       // 검색해야할 채널번호
        String searchLogDateFormat = recFile.convertCtiLogDate();   // 로그에서 확인해봐야할 날짜 포맷
        Scanner ctiScan = new Scanner(this.br);
        while (ctiScan.hasNext()){
            String line = ctiScan.nextLine();
            Boolean ctiEndLogFlag = this.searchEndLog(line,channelNum,extractCtiLog);
            if(ctiEndLogFlag){
                extractFlag = true;
                break;
            }
        }
        if(!extractFlag){
            System.out.println("endTime 을 찾을 수 없습니다.");
        }
        return extractFlag;
    }

    private Boolean searchStartLogAndCallId(String line, String channelNum, String searchLogDateFormat, ExtractCtiLog extractCtiLog){
        String resultLine = null;
        Boolean flag = false;
        if(line.contains(channelNum)){ //채널넘버 먼저검사
//                resultLine = line;
            if(line.contains(searchLogDateFormat)){//날짜로그검사
                resultLine = line;
            }
        }
        if(resultLine != null){
//            System.out.println(resultLine);
            if(resultLine.contains("CTI START")){   //시작시간 검색
                System.out.println(resultLine);
                String subTime = resultLine.substring(0, 24);
                //2020/11/11/11-01-31-0617
                this.setStartTimeAndHour(subTime,extractCtiLog);  //시,통화시작시각 저장
                String id1 = resultLine.substring(156, 176).trim(); //szCallID1 [37710159]
                String id2 = resultLine.substring(177, 197).trim(); //2
                String id3 = resultLine.substring(198, 218).trim(); //3
                this.setSzCallId(id1,id2,id3,extractCtiLog);      // szCallId 배열 저장
                flag = true;
            }
        }
        return flag;
    }

    private Boolean searchEndLog(String line, String channelNum, ExtractCtiLog extractCtiLog) {
        String resultLine = null;
        if(line.contains(channelNum)){ //채널넘버 먼저검사
            if(line.contains("CTI STOP")){
                if(line.contains(extractCtiLog.getSzCallIdList()[0])){
                    resultLine = line;
                }
            }
        }
        if(resultLine != null){
            String subTime = resultLine.substring(0, 24);
            this.setEndTime(subTime,extractCtiLog);
            return true;
        }
        return false;
    }

    private void setEndTime(String time, ExtractCtiLog extractCtiLog) {
        String endTime = time.substring(14,16)+time.substring(17,19)+time.substring(20,22);
        extractCtiLog.setRecCallEtime(endTime);
        extractCtiLog.setRecCallTTime();

    }

    //channelNum구하기
    private int getChannelNum(){
        int readArrIndex = ResultInsertSqlLogList.executeCnt;
        ResultInsertSqlLog insertSql = ResultInsertSqlLogList.resultInsertSqlLogList.get(readArrIndex);
        return Integer.parseInt(insertSql.getChannelNum());
    }

    //szCallId 1~3까지 저장
    private void setSzCallId(String id1, String id2, String id3, ExtractCtiLog extractCtiLog){
        int idxLength = 0;
        Pattern p = Pattern.compile("\\[(.*?)\\]|\\((.*?)\\)");
        String[] idArr = { id1,id2,id3 };
        String[] resultIdArr = new String[3];
        for (int i=0; i<idArr.length ; i++) {
            Matcher m = p.matcher(""+idArr[i]);
            while (m.find()){
                String idValue = m.group(1);
                if(idValue.trim().length()>0){
                    idxLength++;
                    resultIdArr[i] = idValue;
                    break;
                }
            }
        }
        extractCtiLog.setSzCallIdList(new String[idxLength]);
//        this.szCallId = new String[idxLength];
        String[] szCallIdList = extractCtiLog.getSzCallIdList();
        for (int i=0; i<idxLength; i++){
            szCallIdList[i] = resultIdArr[i];
        }
    }

    private void setStartTimeAndHour(String time, ExtractCtiLog extractCtiLog){
//        System.out.println(time); //2020/11/11/11-01-31-0648
        String hour = time.substring(11,13);
        String recTime = time.substring(14,16)+time.substring(17,19)+time.substring(20,22);
        extractCtiLog.setHour(hour);
        extractCtiLog.setRecTime(recTime);
        extractCtiLog.setRecTime2(recTime);


    }
}
