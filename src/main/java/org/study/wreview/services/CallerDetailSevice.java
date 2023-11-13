package org.study.wreview.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.study.wreview.models.Caller;
import org.study.wreview.repositories.CallerRepository;
import org.study.wreview.security.CallerDetails;

import java.util.Optional;


@Service
public class CallerDetailSevice implements UserDetailsService {
    final private CallerRepository callerRepository;

    public CallerDetailSevice(CallerRepository callerRepository) {
        this.callerRepository = callerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Caller> caller = callerRepository.findByUsername(username);

        if (caller.isEmpty())
            throw new UsernameNotFoundException("Пользователь не найден");

        return new CallerDetails(caller.get());
    }
}
