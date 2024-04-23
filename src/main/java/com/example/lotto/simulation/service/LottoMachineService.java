package com.example.lotto.simulation.service;

import com.example.lotto.simulation.machine.LottoMachine;
import com.example.lotto.simulation.model.LottoNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LottoMachineService {

    private final LottoMachine lottoMachine;

    @Autowired
    public LottoMachineService(LottoMachine lottoMachine) {
        this.lottoMachine = lottoMachine;
    }

    public LottoNumber drawNumbers() {
        return lottoMachine.drawLottoNumbers();
    }
}
