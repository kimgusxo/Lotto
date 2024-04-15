package com.example.lotto.controller;

import com.example.lotto.domain.dto.StatLottoDTO;
import com.example.lotto.service.StatLottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
