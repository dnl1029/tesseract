package com.example.ocr.test;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageProcessingService {

    static {
        try {
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {
            throw new RuntimeException("Failed to load OpenCV library", e);
        }
    }

    public byte[] preprocessImage(MultipartFile file) throws IOException {
        // MultipartFile을 OpenCV의 Mat 객체로 변환
        Mat image = Imgcodecs.imdecode(new MatOfByte(file.getBytes()), Imgcodecs.IMREAD_COLOR);

        // 그레이스케일 변환
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // 이진화
        Mat binary = new Mat();
        Imgproc.threshold(gray, binary, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        // 이미지를 바이트 배열로 변환하여 반환
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", binary, matOfByte);

        return matOfByte.toArray();
    }
}

