package com.example.lotto.service;

import com.example.lotto.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

}
