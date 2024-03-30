package com.example.lotto.repository;

import com.example.lotto.domain.WinningReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WinningReportRepository extends MongoRepository<WinningReport, String> {

    boolean existsByRound(Integer round);

    WinningReport findByRound(Integer round);
    List<WinningReport> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<WinningReport> findByTotalWinningAmountGreaterThanEqual(Long totalWinningAmount);
    Integer deleteByRound(Integer round);

}
