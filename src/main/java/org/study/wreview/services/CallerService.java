package org.study.wreview.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.wreview.repositories.CallerRepository;

@Service
@Transactional
public class CallerService {
    CallerRepository callerRepository;

    @Autowired
    public CallerService(CallerRepository callerRepository) {
        this.callerRepository = callerRepository;
    }

    public void test(){
        System.out.println(callerRepository.findAll());
    }
}
