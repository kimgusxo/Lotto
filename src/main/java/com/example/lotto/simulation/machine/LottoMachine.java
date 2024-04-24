package com.example.lotto.simulation.machine;

import com.example.lotto.simulation.model.LottoNumber;
import com.example.lotto.domain.StatLotto;
import com.example.lotto.repository.StatLottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LottoMachine {

    private final StatLottoRepository statLottoRepository;
    private Random random = new Random();

    @Autowired
    public LottoMachine(StatLottoRepository statLottoRepository) {
        this.statLottoRepository = statLottoRepository;
    }

    public LottoNumber drawLottoNumbers() {
        List<Integer> numbers = initializeMachine();

        Collections.shuffle(numbers);
        List<Integer> selectedNumbers = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            selectedNumbers.add(numbers.remove(random.nextInt(numbers.size())));
        }

        Collections.sort(selectedNumbers);
        Integer bonusNumber = numbers.get(random.nextInt(numbers.size()));

        LottoNumber lottoNumber = new LottoNumber();
        lottoNumber.setNumbers(selectedNumbers);
        lottoNumber.setBonusNumber(bonusNumber);
        return lottoNumber;
    }

    private List<Integer> initializeMachine() {
        List<StatLotto> lottoStats = statLottoRepository.findAll();
        List<Integer> numbers = new ArrayList<>();
        for (StatLotto stat : lottoStats) {
            int weight = (int) (stat.getProbability() * 100);  // 가중치 적용
            for (int i = 0; i < weight; i++) {
                numbers.add(stat.getNumber());
            }
        }
        return numbers;
    }
}
