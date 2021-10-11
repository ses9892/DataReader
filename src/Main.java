import LogRead.CtiReader;
import LogRead.SqlReader;
import data.RecFileArr;

public class Main {

    public static void main(String[] args) {
        // 해당 폴더 MP3 파일 list로 가져온다.
        // 해당 list내부의 객체에는 userId가있고 userId == self 이면 flag = true
        // flag = true 인것만 검사하는걸로하자....
        RecFileArr recFileArr = RecFileArr.getInstance();
        SqlReader sqlReader = new SqlReader(recFileArr.recFileQueue);
        sqlReader.openInputStream();
        CtiReader ctiReader = new CtiReader(recFileArr.recFileQueue);
        ctiReader.openInputStream();



        //확인용
//        for(String a :ResultInsertSqlLogList.resultInsertSqlLogList.get(0).getSqlValues()){
//            System.out.println(a.trim());
//        }
//        System.out.println(RecFileArr.recFileQueue.size()); //큐소멸 안되는거 확인






        //결론적으로는 날짜를 구분하여 리더에 넘겨줘야한다.
        //날짜를 어디서 구분할지? == RecFile 에서 생성자로 바로 구분하여 저장됨
        //파일들의 경로는 어디서 정할지 ==




    }
}
