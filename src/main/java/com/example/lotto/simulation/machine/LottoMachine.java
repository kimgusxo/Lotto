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

        List<Integer> selectedNumbers = numbers.subList(0, 6); // 6개의 주요 번호 선택
        Integer bonusNumber = numbers.get(6); // 7번째 번호를 보너스 번호로 선택

        LottoNumber lottoNumber = new LottoNumber();
        lottoNumber.setNumbers(selectedNumbers);
        lottoNumber.setBonusNumber(bonusNumber);
        return lottoNumber;
    }

    private List<Integer> initializeMachine() {
        List<StatLotto> lottoStats = statLottoRepository.findAll();
        List<Integer> numbers = new ArrayList<>();
        for (StatLotto stat : lottoStats) {
            addNumber(numbers, stat.getNumber(), stat.getProbability());
        }
        return numbers;
    }

    private void addNumber(List<Integer> numbers, int number, double probability) {
        int weight = (int) (probability * 100);
        for (int i = 0; i < weight; i++) {
            numbers.add(number);
        }
    }
}
