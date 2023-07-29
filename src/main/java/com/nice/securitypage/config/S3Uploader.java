package com.nice.securitypage.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3Client;
    private final AESUtilConfig aesUtilConfig;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 여러 MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public List<String> uploadMultipleFiles(List<MultipartFile> multipartFiles, String dirName) throws IOException {
        List<String> uploadImageUrls = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            File uploadFile = convert(multipartFile)
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
            uploadImageUrls.add(upload(uploadFile, dirName));
        }

        return uploadImageUrls;
    }

    // 주어진 File을 S3에 업로드
    public String upload(File uploadFile, String dirName) {

        // 파일 이름 암호화
        String encryptFileName = aesUtilConfig.encrypt(uploadFile.getName());

        // 파일을 S3에 업로드하고, 그 URL을 반환
        String fileName = dirName + "/" + encryptFileName;
        String uploadImageUrl = putS3(uploadFile, fileName);

        // 로컬에 생성된 파일을 삭제
        removeNewFile(uploadFile);

        // 업로드된 파일의 S3 URL 주소 반환
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 생성된 파일 삭제
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    // MultipartFile을 File로 변환
    public Optional<File> convert(MultipartFile file) {
        File convertFile = new File(file.getOriginalFilename());
        try {
            // 새 파일을 생성하고, 해당 파일이 정상적으로 생성되었는지 확인.
            // 생성에 성공하면, MultipartFile의 내용을 새 파일에 쓰고, 해당 파일을 반환.
            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            // 파일 생성이나 내용 쓰기 과정에서 IOException이 발생하면, 스택 트레이스를 출력
            e.printStackTrace();
        }
        // 파일 생성이 실패하거나, IOException이 발생한 경우, 빈 Optional을 반환
        return Optional.empty();
    }

}
