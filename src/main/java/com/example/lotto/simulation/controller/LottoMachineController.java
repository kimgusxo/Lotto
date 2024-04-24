package com.example.lotto.simulation.controller;

import com.example.lotto.simulation.model.LottoNumber;
import com.example.lotto.simulation.service.LottoMachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lottoMachine")
public class LottoMachineController {

    private final LottoMachineService lottoMachineService;

    @Autowired
    public LottoMachineController(LottoMachineService lottoMachineService) {
        this.lottoMachineService = lottoMachineService;
    }

    @GetMapping("/get/count/{count}")
    public ResponseEntity<List<LottoNumber>> getNumberDraw(@PathVariable Integer count) {
        List<LottoNumber> lottoNumberList = new ArrayList<>();
        for(int i=0; i<count; i++) {
            LottoNumber lottoNumber = lottoMachineService.drawNumbers();
            log.info("numbers: {}, bonusNumber: {}",lottoNumber.getNumbers(), lottoNumber.getBonusNumber());
            lottoNumberList.add(lottoNumber);
        }
        return new ResponseEntity<>(lottoNumberList, HttpStatus.OK);
    }

}
