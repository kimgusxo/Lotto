package com.example.lotto.crawler.service;

import com.example.lotto.crawler.model.CrawlingModel;
import com.example.lotto.domain.Rank;
import com.example.lotto.domain.Result;
import com.example.lotto.domain.WinningReport;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.repository.ResultRepository;
import com.example.lotto.repository.WinningReportRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CrawlingService {

    private final ResultRepository resultRepository;
    private final WinningReportRepository winningReportRepository;

    @Autowired
    public CrawlingService(ResultRepository resultRepository,
                           WinningReportRepository winningReportRepository) {
        this.resultRepository = resultRepository;
        this.winningReportRepository = winningReportRepository;
    }

    public CrawlingModel crawlWebsite(String url) throws IOException {
            CrawlingModel crawlingModel = new CrawlingModel();

        Document doc = Jsoup.connect(url).get();

        // 회차
        String roundStr = doc.select("div.win_result > h4 > strong").first().text();
        Integer round = Integer.parseInt(roundStr.replace("회", ""));
        crawlingModel.setRound(round);

        // 날짜
        String dateStr = doc.select("div.win_result > p.desc").text();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("(yyyy년 MM월 dd일 추첨)");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        crawlingModel.setDate(date);

        // 당첨번호
        Elements numbersElements = doc.select("div.num.win > p > span");
        List<Integer> numbers = new ArrayList<>();
        for (Element numberElement : numbersElements) {
            numbers.add(Integer.parseInt(numberElement.text()));
        }
        crawlingModel.setNumbers(numbers);

        // 보너스 번호
        String bonusNumberStr = doc.select("div.num.bonus > p > span").text();
        Integer bonusNumber = Integer.parseInt(bonusNumberStr);
        crawlingModel.setBonusNumber(bonusNumber);

        // 총 당첨 금액
        String totalWinningAmountStr = doc.select("ul.list_text_common > li > strong").text();
        Long totalWinningAmount = Long.parseLong(totalWinningAmountStr.replaceAll("[^\\d]", ""));
        crawlingModel.setTotalWinningAmount(totalWinningAmount);

        // 등수 & 등수별 당첨자 & 등수별 총 당첨금 & 등수별 당첨금
        Elements rows = doc.select("tbody > tr");
        List<Integer> rankings = new ArrayList<>();
        List<Integer> winningCounts = new ArrayList<>();
        List<Long> totalWinningAmounts = new ArrayList<>();
        List<Long> winningAmounts = new ArrayList<>();

        for (Element row : rows) {
            Elements tds = row.select("td");
            // 등수 추출
            rankings.add(Integer.parseInt(tds.get(0).text().replace("등", "")));
            // 등수별 당첨자 수 추출
            winningCounts.add(Integer.parseInt(tds.get(2).text().replace(",", "")));
            // 등수별 총 당첨금 추출
            totalWinningAmounts.add(Long.parseLong(tds.get(1).text().replaceAll("[^\\d]", "")));
            // 등수별 당첨금 추출
            winningAmounts.add(Long.parseLong(tds.get(3).text().replaceAll("[^\\d]", "")));
        }
        crawlingModel.setRankings(rankings);
        crawlingModel.setWinningCounts(winningCounts);
        crawlingModel.setTotalWinningAmounts(totalWinningAmounts);
        crawlingModel.setWinningAmounts(winningAmounts);

        validateCrawlingModel(crawlingModel);

        return crawlingModel;

    }

    public void validateCrawlingModel(CrawlingModel crawlingModel) {
        if(crawlingModel.getRound() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_CRAWLING_MODEL);
        }
        if(crawlingModel.getDate() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_CRAWLING_MODEL);
        }
        if(crawlingModel.getNumbers() == null || crawlingModel.getNumbers().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_CRAWLING_MODEL);
        }
        if(crawlingModel.getBonusNumber() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_CRAWLING_MODEL);
        }
        if(crawlingModel.getTotalWinningAmount() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_CRAWLING_MODEL);
        }
        if(crawlingModel.getRankings() == null || crawlingModel.getRankings().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_CRAWLING_MODEL);
        }
        if(crawlingModel.getWinningCounts() == null || crawlingModel.getWinningCounts().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_CRAWLING_MODEL);
        }
        if(crawlingModel.getTotalWinningAmounts() == null || crawlingModel.getTotalWinningAmounts().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_CRAWLING_MODEL);
        }
        if(crawlingModel.getWinningAmounts() == null || crawlingModel.getWinningAmounts().isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_CRAWLING_MODEL);
        }
    }

    public Result bindingResult(CrawlingModel crawlingModel) {
        // result 생성
        Result result = Result.builder()
                .round(crawlingModel.getRound())
                .numbers(crawlingModel.getNumbers())
                .bonusNumber(crawlingModel.getBonusNumber())
                .date(crawlingModel.getDate())
                .build();

        if(Objects.isNull(result)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_RESULT);
        }

        return result;
    }

    public WinningReport bindingWinningReport(CrawlingModel crawlingModel) {
        // RankList 생성
        List<Rank> rankList = new ArrayList<>();
        for (int i = 0; i < crawlingModel.getRankings().size(); i++) {
            rankList.add(Rank.builder()
                    .ranking(crawlingModel.getRankings().get(i))
                    .winningCount(crawlingModel.getWinningCounts().get(i))
                    .totalWinningAmount(crawlingModel.getTotalWinningAmounts().get(i))
                    .winningAmount(crawlingModel.getWinningAmounts().get(i))
                    .build());
        }

        // WinningReport 생성
        WinningReport winningReport = WinningReport.builder()
                .round(crawlingModel.getRound())
                .date(crawlingModel.getDate())
                .totalWinningAmount(crawlingModel.getTotalWinningAmount())
                .rankList(rankList)
                .build();

        if(Objects.isNull(winningReport)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_WINNING_REPORT);
        }

        return winningReport;
    }

    @Transactional
    public void insertByCrawl(CrawlingModel crawlingModel) {
        resultRepository.save(bindingResult(crawlingModel));
        winningReportRepository.save(bindingWinningReport(crawlingModel));
    }

    @Transactional
    public void insertAllByCrawl(List<CrawlingModel> crawlingModelList) {

        List<Result> resultList = new ArrayList<>();
        List<WinningReport> winningReportList = new ArrayList<>();

        crawlingModelList.forEach((crawlingModel) -> {
            resultList.add(bindingResult(crawlingModel));
            winningReportList.add(bindingWinningReport(crawlingModel));
        });

        resultRepository.saveAll(resultList);
        winningReportRepository.saveAll(winningReportList);

    }

}
