package com.example.lotto.controller;

import com.example.lotto.domain.dto.WinningReportDTO;
import com.example.lotto.service.WinningReportService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/winningReport")
public class WinningReportController {

    private final WinningReportService winningReportService;

    @Autowired
    public WinningReportController(WinningReportService winningReportService) {
        this.winningReportService = winningReportService;
    }

    @GetMapping("/get/round/{round}")
    public ResponseEntity<WinningReportDTO> getByRound(@PathVariable @Min(1) Integer round) {
        WinningReportDTO winningReportDTO = winningReportService.readByRound(round);
        return new ResponseEntity<>(winningReportDTO, HttpStatus.OK);
    }

    @GetMapping("/get/lastOne")
    public ResponseEntity<WinningReportDTO> getLastOne() {
        WinningReportDTO winningReportDTO = winningReportService.readLastOne();
        return new ResponseEntity<>(winningReportDTO, HttpStatus.OK);
    }

    @GetMapping("/get/page/{page}")
    public ResponseEntity<List<WinningReportDTO>> getByPage(@PathVariable Integer page) {
        List<WinningReportDTO> winningReportDTOList = winningReportService.readByPage(page);
        return new ResponseEntity<>(winningReportDTOList, HttpStatus.OK);
    }

    @GetMapping("/get/date")
    public ResponseEntity<List<WinningReportDTO>> getByDate(@RequestParam @Past LocalDate startDate,
                                                            @RequestParam @Past LocalDate endDate) {
        List<WinningReportDTO> winningReportDTOList = winningReportService.readByDate(startDate, endDate);
        return new ResponseEntity<>(winningReportDTOList, HttpStatus.OK);
    }

    @GetMapping("/get/totalWinningAmount/{totalWinningAmount}")
    public ResponseEntity<List<WinningReportDTO>> getByTotalWinningAmount(@PathVariable @Min(1) Long totalWinningAmount) {
        List<WinningReportDTO> winningReportDTOList = winningReportService.readByTotalWinningAmount(totalWinningAmount);
        return new ResponseEntity<>(winningReportDTOList, HttpStatus.OK);
    }

    @PostMapping("/post/insert")
    public ResponseEntity<WinningReportDTO> insert(@RequestBody @Validated WinningReportDTO winningReportDTO) {
        WinningReportDTO createWinningReportDTO = winningReportService.insert(winningReportDTO);
        return new ResponseEntity<>(createWinningReportDTO, HttpStatus.CREATED);
    }

    @PutMapping("/put/update/{round}")
    public ResponseEntity<WinningReportDTO> update(@PathVariable @Min(1) Integer round,
                                                   @RequestBody @Validated WinningReportDTO winningReportDTO) {
        WinningReportDTO updateWinningReportDTO = winningReportService.update(round, winningReportDTO);
        return new ResponseEntity<>(updateWinningReportDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{round}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Integer round) {
        winningReportService.delete(round);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
