package com.example.lotto.service;

import com.example.lotto.domain.WinningReport;
import com.example.lotto.domain.dto.WinningReportDTO;
import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.repository.WinningReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Transactional
    public WinningReportDTO readByRound(Integer round) {
        boolean exist = winningReportRepository.existsByRound(round);

        if(!exist) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_ROUND_TOKEN);
        }

        WinningReportDTO winningReportDTO = winningReportRepository.findByRound(round).toDTO();
        return winningReportDTO;
    }

    @Transactional
    public List<WinningReportDTO> readByDate(LocalDate startDate, LocalDate endDate) {
        List<WinningReport> winningReportList = winningReportRepository.findByDateBetween(startDate, endDate);

        if(winningReportList.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INCORRECT_DATE_TOKEN);
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
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATE_ROUND_TOKEN);
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
