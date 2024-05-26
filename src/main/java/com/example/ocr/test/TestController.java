package com.example.ocr.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/ocr")
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final Tesseract tesseract;
    private final ImageProcessingService imageProcessingService;
    @PostMapping("/extract-text")
    public ResponseEntity<String> extractText(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
        }

        try {
            /*
            // 디스크에 저장하여 수행하는 방식
            File convFile = convertMultiPartToFile(file);

            // OCR 수행
            String extractedText = tesseract.doOCR(convFile);
            log.info("extractedText : {}",extractedText);
             */

            //메모리로 수행하는 방식
//            String newExtractedText = tesseract.doOCR(TesseractOcrUtil.createImageFromBytes(file.getBytes()));

            // 이미지 전처리
            byte[] preprocessedImage = imageProcessingService.preprocessImage(file);
            String newExtractedText = tesseract.doOCR(TesseractOcrUtil.createImageFromBytes(preprocessedImage));

            log.info("newExtractedText : {}",newExtractedText);

            return new ResponseEntity<>(newExtractedText, HttpStatus.OK);

        } catch (TesseractException | IOException e) {
            return new ResponseEntity<>("Error during OCR processing: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        file.transferTo(convFile);
        return convFile;
    }
     */
}
