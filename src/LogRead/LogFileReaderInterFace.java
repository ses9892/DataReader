package LogRead;

import java.io.File;

public interface LogFileReaderInterFace {




    // 파일별 스트림오픈 추상메소드
    void openInputStream();

    File extensionFilter();
}
