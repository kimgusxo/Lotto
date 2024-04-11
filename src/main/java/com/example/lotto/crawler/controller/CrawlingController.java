package com.example.lotto.crawler.controller;


import com.example.lotto.crawler.model.CrawlingModel;
import com.example.lotto.crawler.service.CrawlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/crawling")
public class CrawlingController {

    private final CrawlingService crawlingService;

    @Autowired
    public CrawlingController(CrawlingService crawlingService) {
        this.crawlingService = crawlingService;
    }

    @GetMapping("/get/crawlWebsite/{page}")
    public ResponseEntity<CrawlingModel> getByCrawlWebsite(@PathVariable Integer page) throws IOException {
        CrawlingModel crawlingModel = crawlingService.crawlWebsite(page);
        return new ResponseEntity<>(crawlingModel, HttpStatus.OK);
    }

    @GetMapping("/get/crawlWebsite/list")
    public ResponseEntity<List<CrawlingModel>> getListByCrawlWebsite(@RequestParam Integer startPage,
                                                                     @RequestParam Integer endPage) throws IOException {
        List<CrawlingModel> crawlingModelList = new ArrayList<>();

        for(int i=startPage; i<=endPage; i++) {
            CrawlingModel crawlingModel = crawlingService.crawlWebsite(i);
            crawlingModelList.add(crawlingModel);
        }

        return new ResponseEntity<>(crawlingModelList, HttpStatus.OK);
    }

    @PostMapping("/post/insert")
    public ResponseEntity<Void> insertByCrawl(@RequestBody CrawlingModel crawlingModel) {
        crawlingService.insertByCrawl(crawlingModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/post/insert/list")
    public ResponseEntity<Void> insertAllByCrawl(@RequestBody List<CrawlingModel> crawlingModelList) {
        crawlingService.insertAllByCrawl(crawlingModelList);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}
