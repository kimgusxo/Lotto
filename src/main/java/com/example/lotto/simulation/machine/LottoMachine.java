package com.example.lotto.simulation.machine;

import com.example.lotto.error.CustomException;
import com.example.lotto.error.ErrorCode;
import com.example.lotto.simulation.model.LottoNumber;
import com.example.lotto.domain.StatLotto;
import com.example.lotto.repository.StatLottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        Map<Integer, Integer> weightedNumbers = initializeWeightedNumbers(false);  // 일반 번호의 가중치
        List<Integer> selectedNumbers = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            int selected = selectNumber(weightedNumbers);
            selectedNumbers.add(selected);
            weightedNumbers.remove(selected); // 선택된 번호 완전 제거
        }

        Collections.sort(selectedNumbers);

        // 보너스 번호 추출: 이미 선택된 번호를 제외하고 보너스 가중치 적용
        Map<Integer, Integer> bonusWeightedNumbers = initializeWeightedNumbers(true); // 보너스 번호의 가중치
        selectedNumbers.forEach(bonusWeightedNumbers::remove); // 이미 선택된 번호 제거
        Integer bonusNumber = selectNumber(bonusWeightedNumbers);

        LottoNumber lottoNumber = new LottoNumber();
        lottoNumber.setNumbers(selectedNumbers);
        lottoNumber.setBonusNumber(bonusNumber);
        return lottoNumber;
    }

    private Map<Integer, Integer> initializeWeightedNumbers(boolean forBonus) {
        List<StatLotto> lottoStats = statLottoRepository.findAll();
        Map<Integer, Integer> weightedNumbers = new HashMap<>();
        for (StatLotto stat : lottoStats) {
            int weight = (int) (forBonus ? stat.getBonusProbability() * 10000 : stat.getProbability() * 10000);
            weightedNumbers.put(stat.getNumber(), weight);
        }
        return weightedNumbers;
    }

    private int selectNumber(Map<Integer, Integer> weightedNumbers) {
        int totalWeight = weightedNumbers.values().stream().mapToInt(Integer::intValue).sum();
        int randomIndex = random.nextInt(totalWeight);
        int cumulativeWeight = 0;
        for (Map.Entry<Integer, Integer> entry : weightedNumbers.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomIndex < cumulativeWeight) {
                return entry.getKey();
            }
        }
        throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.UNKNOWN);
    }
}
