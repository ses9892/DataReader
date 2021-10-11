package data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecFile {

    private String createdAt;

    private String innerLine;

//    public final String extension = ".mp3";

    private String userId;

    private String logFileDate;

    private Boolean flag;

    public RecFile(String createdAt, String innerLine, String userId) {
        this.createdAt = createdAt;
        this.innerLine = innerLine;
        this.userId = userId;
        this.selfCheck();
        this.convertLogFileDate();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getInnerLine() {
        return innerLine;
    }

    public void setInnerLine(String innerLine) {
        this.innerLine = innerLine;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getLogFileDate() {
        return logFileDate;
    }

    public void setLogFileDate(String logFileDate) {
        this.logFileDate = logFileDate;
    }

    @Override
    public String toString() {
        return "RecFile{" +
                "createdAt='" + createdAt + '\'' +
                ", innerLine='" + innerLine + '\'' +
                ", userId='" + userId + '\'' +
                ", logFileDate='" + logFileDate + '\'' +
                ", flag=" + flag +
                '}';
    }

    // mp3 파일의 userId 체크
    public void selfCheck(){
        if(!userId.toUpperCase().equals("SELF")){
            this.flag = false;
        }else{
            this.flag = true;
        }
    }

    public void convertLogFileDate(){
        this.logFileDate = this.createdAt.substring(0,10);
    }

    //CTI 검색을 위한 날짜 변환
    public String convertCtiLogDate(){
        String logDate = this.createdAt.substring(0, 12).trim();
        //2020/11/11/11-01-31-
        //2020/11/11/11-01-390728
        //2020/11/11/11-01-
        //2020/11/11/11-01-39-0728
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        SimpleDateFormat newSdf = new SimpleDateFormat("yyyy/MM/dd/HH-mm");
        String resultDate = null;
        try {
            Date formatDate =sdf.parse(logDate);
            resultDate = newSdf.format(formatDate);
        } catch (ParseException e) {
            System.out.println("파싱에러");
        }
        return resultDate;
    }
}
