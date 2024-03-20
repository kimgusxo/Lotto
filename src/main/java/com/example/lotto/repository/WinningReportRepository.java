package com.example.lotto.repository;

import com.example.lotto.domain.WinningReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WinningReportRepository extends MongoRepository<WinningReport, String> {

}
