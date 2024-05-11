package com.example.lotto.controller;

import com.example.lotto.domain.dto.StatLottoDTO;
import com.example.lotto.service.StatLottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/statLotto")
public class StatLottoController {

    private final StatLottoService statLottoService;

    @Autowired
    public StatLottoController(StatLottoService statLottoService) {
        this.statLottoService = statLottoService;
    }

    @GetMapping("/get/calc")
    public ResponseEntity<List<StatLottoDTO>> calcStatLotto() {
        List<StatLottoDTO> statLottoDTOList = statLottoService.calcStatLotto();
        return new ResponseEntity<>(statLottoDTOList, HttpStatus.OK);
    }

    @GetMapping("/get/number/{number}")
    public ResponseEntity<StatLottoDTO> getByNumber(@PathVariable Integer number) {
        StatLottoDTO statLottoDTO = statLottoService.readByNumber(number);
        return new ResponseEntity<>(statLottoDTO, HttpStatus.OK);
    }

    @GetMapping("/get/numberList")
    public ResponseEntity<List<StatLottoDTO>> getByNumberList(@RequestParam List<Integer> numberList) {
        List<StatLottoDTO> statLottoDTOList = statLottoService.readByNumberList(numberList);
        return new ResponseEntity<>(statLottoDTOList, HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<StatLottoDTO>> getAll() {
        List<StatLottoDTO> statLottoDTOList = statLottoService.readAll();
        return new ResponseEntity<>(statLottoDTOList, HttpStatus.OK);
    }

}
