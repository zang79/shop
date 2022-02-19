package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID(); //서로 다른 객체들을 구별하기 위해서 이름을 부여할때 사용
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension; //uuid 값과 원래 파이일의 이름의 확장자를 조합해서 저장될 파일 이름을 만듭니다.
        String fileUploadFullUrl = uploadPath + "/" +savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);//바이트 단위의 출력을 내보내는 클래스입니다.
        fos.write(fileData); //출력 스트림에 입력합니다.
        fos.close();
        return savedFileName; //업로드된 파일의 이름을 반환합니다.
    }
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);

        if(deleteFile.exists()) { //파일이 저장된 경로를 이용하여 파일 객체를 생성합니다.
            deleteFile.delete(); //해당 파일이 존재하면 파일을 삭제합니다.
            log.info("파일을 삭제하였습니다.");
        }else{
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
