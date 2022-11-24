package com.example.picbed.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
public class TestController {

    @Test
    public static void main(String[] args) {
        String image = "1.png";
        String suffixName = image.substring(image.lastIndexOf("."));
        System.out.println(suffixName);
    }
}
