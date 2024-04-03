package com.example.lotto.controller;

import com.example.lotto.domain.dto.ResultDTO;
import com.example.lotto.service.ResultService;
import jakarta.validation.constraints.Max;
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
@RequestMapping("/result")
public class ResultController {

    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("/get/round/{round}")
    public ResponseEntity<ResultDTO> getByRound(@PathVariable @Min(1) Integer round) {
        ResultDTO resultDTO = resultService.readByRound(round);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/get/bonusNumber/{bonusNumber}")
    public ResponseEntity<List<ResultDTO>> getByBonusNumber(@PathVariable @Min(1) @Max(45)
                                                                Integer bonusNumber) {
        List<ResultDTO> resultDTOList = resultService.readByBonusNumber(bonusNumber);
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    @GetMapping("/get/number/{number}")
    public ResponseEntity<List<ResultDTO>> getByNumber(@PathVariable @Min(1) @Max(45)
                                                           Integer number) {
        List<ResultDTO> resultDTOList = resultService.readByNumber(number);
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }

    @GetMapping("/get/date")
    public ResponseEntity<List<ResultDTO>> getByDate(@RequestParam("startDate") @Past LocalDate startDate,
                                                     @RequestParam("endDate") @Past LocalDate endDate) {
        List<ResultDTO> results = resultService.readByDate(startDate, endDate);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping("/post/insert")
    public ResponseEntity<ResultDTO> insert(@RequestBody @Validated
                                                ResultDTO resultDTO) {
        ResultDTO createResultDTO = resultService.insert(resultDTO);
        return new ResponseEntity<>(createResultDTO, HttpStatus.CREATED);
    }

    @PutMapping("/put/update/{round}")
    public ResponseEntity<ResultDTO> update(@PathVariable @Min(1) Integer round,
                                            @RequestBody @Validated ResultDTO resultDTO) {
        ResultDTO updateResultDTO = resultService.update(round, resultDTO);
        return new ResponseEntity<>(updateResultDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{round}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) Integer round) {
        resultService.delete(round);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
