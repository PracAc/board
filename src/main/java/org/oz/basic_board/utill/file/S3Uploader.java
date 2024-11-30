package org.oz.basic_board.utill.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
@Log4j2
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    @Value("${cloud.aws.s3.upload-folder}")
    private String uploadFolder;

    // S3로 파일 업로드하기
    public String upload(String filePath)throws RuntimeException {

        File targetFile = new File(filePath);
        log.info(targetFile);

        String uploadImageUrl = putS3(targetFile, targetFile.getName()); // s3로 업로드
        removeOriginalFile(targetFile);
        return uploadImageUrl;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName)throws RuntimeException {
        String uploadPath = bucket + "/" + uploadFolder;

        amazonS3Client.putObject(new PutObjectRequest(uploadPath, fileName,
                uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(uploadPath, fileName).toString();
    }

    // S3 업로드 후 원본 파일 삭제
    private void removeOriginalFile(File targetFile) {
        if (targetFile.exists() && targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("fail to remove");
    }

    // S3 파일 가져오기
    public Resource getFileUrl(String fileName) {
        String uploadPath = bucket + "/" + uploadFolder;

        // S3 객체 로드
        S3Object s3Object = amazonS3Client.getObject(uploadPath, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        // InputStream을 Resource로 변환
        Resource resource = new InputStreamResource(inputStream);

        return resource;
    }

    // S3 버켓 파일 삭제
    public void removeS3File(String fileName){
        final DeleteObjectRequest deleteObjectRequest = new
                DeleteObjectRequest(bucket + "/" + uploadFolder, fileName);
        amazonS3Client.deleteObject(deleteObjectRequest);
    }
}