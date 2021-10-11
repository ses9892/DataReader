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
//        files ���� �ش� Ȯ���ڸ� ������� �ʰ� �о���� ������ �غ���մ»���
        File file = this.extensionFilter();
        try {
            this.br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"));
            this.extractInsertSql();
            // �˻�޼ҵ��� ����
        } catch (FileNotFoundException e) {
            System.out.println("����Ͽ� ���� ������ �������� �ʽ��ϴ�.");
        }catch (UnsupportedEncodingException ex){
            System.out.println("���ڵ�����");
        } finally {
            this.closeStream(br);
        }
    }



    // �α����ϵ��� Ȯ���ڸ� �°� ���͸��ϴ� �޼ҵ�
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
            System.out.println("���뿡 ���� �α������� ���� �����ʽ��ϴ�.");
            return null;
        }
        return optionalFile.get();
    }

    //ť������ ��������� �װͿ����� ã�� �ٽ� resultQueue�� ����
    public void extractInsertSql(){
        //ť ù��° ���ڵ� ���� ���� ---> poll �� ������ UPDATE �������� ������ �����ʰ� ������.
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
        //�˻�Ϸ� ť���ֱ� , ���⼭ �������� update���� ���������� ����Ǹ� �׶� �־ ����
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
                sqlLog = new ResultInsertSqlLog(group);  //��ü��������
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
        int recExtNumIdx = 0; //������ȣ index
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

