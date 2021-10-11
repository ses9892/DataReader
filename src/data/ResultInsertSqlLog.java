package data;

import java.util.Arrays;

public class ResultInsertSqlLog {

    private String insertSqlQuery;

    private String[] sqlColums;
    //vo형태로 담아서 처리 하자 배열노노
    private String[] sqlValues;

    public ResultInsertSqlLog(String insertSqlQuery) {
        this.insertSqlQuery = insertSqlQuery;
    }

    public String getInsertSqlQuery() {
        return insertSqlQuery;
    }

    public void setInsertSqlQuery(String insertSqlQuery) {
        this.insertSqlQuery = insertSqlQuery;
    }

    public String[] getSqlColums() {
        return sqlColums;
    }

    public void setSqlColums(String[] sqlColums) {
        this.sqlColums = sqlColums;
    }

    public String[] getSqlValues() {
        return sqlValues;
    }

    public void setSqlValues(String[] sqlValues) {
        this.sqlValues = sqlValues;
    }

    @Override
    public String toString() {
        return "ResultInsertSqlLog{" +
                "insertSqlQuery='" + insertSqlQuery + '\'' +
                ", sqlColums=" + Arrays.toString(sqlColums) +
                ", sqlValues=" + Arrays.toString(sqlValues) +
                '}';
    }

    public String getChannelNum(){
        int recChannelIdx = 0;
        for (int i=0; i<this.sqlColums.length; i++){
            if(sqlColums[i].contains("R_CH_NUM")){
                recChannelIdx = i;
            }
        }
        return sqlValues[recChannelIdx].trim();
    }
}
