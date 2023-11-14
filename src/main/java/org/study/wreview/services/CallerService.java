package org.study.wreview.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.study.wreview.models.Caller;
import org.study.wreview.repositories.CallerRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CallerService {
    final
    PasswordEncoder passwordEncoder;
    final
    CallerRepository callerRepository;

    public CallerService(CallerRepository callerRepository, PasswordEncoder passwordEncoder) {
        this.callerRepository = callerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Caller> findAll(){
        return callerRepository.findAll(Sort.by("username"));
    }

    public void save(Caller caller){
        callerRepository.save(caller);
    }

    public void update(String name, Caller caller){
        Optional<Caller> callerForUpdate = callerRepository.findByUsername(name);
        callerForUpdate.ifPresent(c -> {
            c.setUsername(caller.getUsername());
            c.setPosition(caller.getPosition());
            c.setPassword(passwordEncoder.encode(caller.getPassword()));
            c.setEnabled(caller.isEnabled());
            callerRepository.save(c);
        });
    }

    public Optional<Caller> findByUsername(String name){
        return callerRepository.findByUsername(name);
    }
}
