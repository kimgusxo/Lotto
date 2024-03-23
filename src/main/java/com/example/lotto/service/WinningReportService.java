package com.example.lotto.service;

import com.example.lotto.repository.WinningReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WinningReportService {

    @Autowired
    private WinningReportRepository winningReportRepository;

}
