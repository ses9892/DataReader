package LogRead;

import data.RecFile;

import java.io.File;
import java.util.Queue;

public class ApiReader extends FileReaderUtil implements LogFileReaderInterFace{


    public ApiReader(Queue<RecFile> recFiles) {
        super(recFiles);
    }

    @Override
    public void openInputStream() {

    }

    @Override
    public File extensionFilter() {
        return null;
    }
}
