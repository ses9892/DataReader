package data;

import java.util.Arrays;

public class ExtractCtiLog {

    String hour;

    String recRtime; //��ȭ���� (��)

    String recTime; // ��ȭ���۽ð�(��,�� )  = 10.0132

    String recTime2;// ��ȭ���۽ð�2

    String recCallEtime; // ��ȭ����ð�(��,��)

    String[] szCallIdList; // �ݳѹ�

    String recCallTTime; //��ȭ�����ð�(��)


    public String getRecRtime() {
        return recRtime;
    }

    public void setRecRtime(String recRtime) {
        this.recRtime = recRtime;
    }

    public String getRecTime() {
        return recTime;
    }

    public void setRecTime(String recTime) {
        this.recTime = recTime;
    }

    public String getRecTime2() {
        return recTime2;
    }

    public void setRecTime2(String recTime2) {
        this.recTime2 = recTime2;
    }

    public String getRecCallEtime() {
        return recCallEtime;
    }

    public void setRecCallEtime(String recCallEtime) {
        this.recCallEtime = recCallEtime;
    }

    public String[] getSzCallIdList() {
        return szCallIdList;
    }

    public void setSzCallIdList(String[] szCallIdList) {
        this.szCallIdList = szCallIdList;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "ExtractCtiLog{" +
                "hour='" + hour + '\'' +
                ", recRtime='" + recRtime + '\'' +
                ", recTime='" + recTime + '\'' +
                ", recTime2='" + recTime2 + '\'' +
                ", recCallEtime='" + recCallEtime + '\'' +
                ", szCallIdList=" + Arrays.toString(szCallIdList) +
                '}';
    }

    public String getRecCallTTime() {
        return recCallTTime;
    }

    public void setRecCallTTime() {
        this.recCallTTime = String.valueOf(Integer.parseInt(this.recTime2) - Integer.parseInt(this.recCallEtime));
    }
}
