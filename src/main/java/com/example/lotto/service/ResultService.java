package com.example.lotto.service;

import com.example.lotto.domain.Result;
import com.example.lotto.domain.dto.ResultDTO;
import com.example.lotto.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Transactional
    public ResultDTO readByRound(Integer round) {
        boolean exist = resultRepository.existsByRound(round);

        if(!exist) {
            throw new NotExistRoundException();
        }

        return resultRepository.findByRound(round).toDTO();
    }

    @Transactional
    public List<ResultDTO> readByBonusNumber(Integer bonusNumber) {
        List<Result> resultList = resultRepository.findByBonusNumber(bonusNumber);

        if(resultList.isEmpty()) {
            throw new NotExistBonusNumberException();
        }

        List<ResultDTO> resultDTOList = new ArrayList<>();
        resultList.forEach((r) ->
                resultDTOList.add(r.toDTO()));

        return resultDTOList;
    }

    @Transactional
    public List<ResultDTO> readByNumber(Integer number) {
        List<Result> resultList = resultRepository.findByNumbersContaining(number);

        if(resultList.isEmpty()) {
            throw new NotExistNumberException();
        }

        List<ResultDTO> resultDTOList = new ArrayList<>();

        resultList.forEach((r)->
                resultDTOList.add(r.toDTO()));

        return resultDTOList;
    }

    @Transactional
    public List<ResultDTO> readByDate(LocalDate startDate, LocalDate endDate) {
        List<Result> resultList = resultRepository.findByDateBetween(startDate, endDate);

        if(resultList.isEmpty()) {
            throw new InCorrectDateException();
        }

        List<ResultDTO> resultDTOList = new ArrayList<>();

        resultList.forEach((r) ->
                resultDTOList.add(r.toDTO()));

        return resultDTOList;
    }

    @Transactional
    public ResultDTO insert(ResultDTO resultDTO) {
        boolean exist = resultRepository.existsByRound(resultDTO.getRound());

        if(exist) {
            throw new DuplicateResultException();
        }

        ResultDTO saveResultDTO = resultRepository.insert(resultDTO.toEntity()).toDTO();

        return saveResultDTO;
    }

    @Transactional
    public ResultDTO update(Integer updateRound, ResultDTO resultDTO) {
        Result result = resultRepository.findByRound(updateRound);

        if(Objects.isNull(result)) {
            throw new NullPointerException();
        }

        result = resultDTO.toEntity();
        ResultDTO updateResultDTO = resultRepository.save(result).toDTO();

        return updateResultDTO;
    }

    @Transactional
    public void delete(Integer round) {
        Integer token = resultRepository.deleteByRound(round);

        if(token.equals(0)) {
            throw new NotExistResultException();
        }

    }

}
