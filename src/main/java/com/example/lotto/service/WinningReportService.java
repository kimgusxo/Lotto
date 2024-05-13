package com.example.lotto.service;

import com.example.lotto.domain.WinningReport;
import com.example.lotto.domain.dto.WinningReportDTO;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.repository.WinningReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WinningReportService {

    @Autowired
    private WinningReportRepository winningReportRepository;

    private static final int PAGE_SIZE = 10;

    @Transactional
    public WinningReportDTO readByRound(Integer round) {
        boolean exist = winningReportRepository.existsByRound(round);

        if(!exist) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_WINNING_REPORT_ROUND);
        }

        WinningReportDTO winningReportDTO = winningReportRepository.findByRound(round).toDTO();
        return winningReportDTO;
    }

    @Transactional
    public WinningReportDTO readLastOne() {
        WinningReport winningReport = winningReportRepository.findFirstByOrderByRoundDesc();

        if(Objects.isNull(winningReport)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_WINNING_REPORT);
        }

        WinningReportDTO winningReportDTO = winningReport.toDTO();
        return winningReportDTO;
    }

    @Transactional
    public List<WinningReportDTO> readByPage(Integer page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<WinningReport> winningReportPage = winningReportRepository.findAll(pageable);

        if(winningReportPage.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_WINNING_REPORT);
        }

        List<WinningReportDTO> winningReportDTOList = new ArrayList<>();

        winningReportPage.getContent().forEach((w) ->
                winningReportDTOList.add(w.toDTO()));

        return winningReportDTOList;
    }

    @Transactional
    public List<WinningReportDTO> readByDate(LocalDate startDate, LocalDate endDate) {
        List<WinningReport> winningReportList = winningReportRepository.findByDateBetween(startDate, endDate);

        if(winningReportList.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INCORRECT_WINNING_REPORT_DATE);
        }

        List<WinningReportDTO> winningReportDTOList = new ArrayList<>();

        winningReportList.forEach((w) ->
                winningReportDTOList.add(w.toDTO()));
        return winningReportDTOList;
    }

    @Transactional
    public List<WinningReportDTO> readByTotalWinningAmount(Long totalWinningAmount) {
        List<WinningReport> winningReportList = winningReportRepository.findByTotalWinningAmountGreaterThanEqual(totalWinningAmount);

        if(winningReportList.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_WINNING_REPORT);
        }

        List<WinningReportDTO> winningReportDTOList = new ArrayList<>();

        winningReportList.forEach((w) ->
                winningReportDTOList.add(w.toDTO()));

        return winningReportDTOList;
    }

    @Transactional
    public WinningReportDTO insert(WinningReportDTO winningReportDTO) {
        boolean exist = winningReportRepository.existsByRound(winningReportDTO.getRound());

        if(exist) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATE_WINNING_REPORT_ROUND);
        }

        WinningReportDTO updateWinningReportDTO = winningReportRepository.insert(winningReportDTO.toEntity()).toDTO();

        return updateWinningReportDTO;
    }

    @Transactional
    public WinningReportDTO update(Integer updateRound, WinningReportDTO winningReportDTO) {
        WinningReport winningReport = winningReportRepository.findByRound(updateRound);

        if(Objects.isNull(winningReport)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_WINNING_REPORT);
        }

        winningReport.setDate(winningReportDTO.getDate());
        winningReport.setTotalWinningAmount(winningReportDTO.getTotalWinningAmount());
        winningReport.setRankList(winningReportDTO.toEntity().getRankList());

        WinningReportDTO updateWinningReportDTO = winningReportRepository.save(winningReport).toDTO();

        return updateWinningReportDTO;
    }

    @Transactional
    public void delete(Integer round) {
        Integer token = winningReportRepository.deleteByRound(round);

        if(token.equals(0)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_WINNING_REPORT);
        }

    }
}
