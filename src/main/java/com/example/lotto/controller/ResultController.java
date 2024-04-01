package com.example.lotto.controller;

import com.example.lotto.domain.dto.ResultDTO;
import com.example.lotto.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/result")
public class ResultController {

    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/get/round/{round}")
    public ResponseEntity<ResultDTO> getByRound(@PathVariable
                                                    Integer round) {
        ResultDTO resultDTO = resultService.readByRound(round);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/get/bonusNumber/{bonusNumber}")
    public ResponseEntity<List<ResultDTO>> getByBonusNumber(@PathVariable Integer bonusNumber) {
        List<ResultDTO> resultDTOList = resultService.readByBonusNumber(bonusNumber);
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    @GetMapping("/get/number/{number}")
    public ResponseEntity<List<ResultDTO>> getByNumber(@PathVariable Integer number) {
        List<ResultDTO> resultDTOList = resultService.readByNumber(number);
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    @GetMapping("/get/date")
    public ResponseEntity<List<ResultDTO>> getByDate(@RequestParam("startDate") LocalDate startDate,
                                                     @RequestParam("endDate") LocalDate endDate) {
        List<ResultDTO> results = resultService.readByDate(startDate, endDate);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping("/post/insert")
    public ResponseEntity<ResultDTO> insert(@RequestBody ResultDTO resultDTO) {
        ResultDTO createResultDTO = resultService.insert(resultDTO);
        return new ResponseEntity<>(createResultDTO, HttpStatus.CREATED);
    }

    @PutMapping("/put/update/{round}")
    public ResponseEntity<ResultDTO> update(@PathVariable Integer round,
                                            @RequestBody ResultDTO resultDTO) {
        ResultDTO updateResultDTO = resultService.update(round, resultDTO);
        return new ResponseEntity<>(updateResultDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{round}")
    public ResponseEntity<Void> delete(@PathVariable Integer round) {
        resultService.delete(round);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
