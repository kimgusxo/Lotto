package com.example.lotto.crawler.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Jsoup 클래스가 static 메소드라
 * Mocking 불편을 느껴
 * utils 클래스로 분리
 */
@Component
public class CrawlingUtils {
    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
