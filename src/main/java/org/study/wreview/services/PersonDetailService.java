package org.study.wreview.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.wreview.models.Person;
import org.study.wreview.repositories.PersonRepository;
import org.study.wreview.security.PersonDetails;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonDetailService implements UserDetailsService {
    final private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findByUsername(username);

        if (person.isEmpty())
            throw new UsernameNotFoundException("Пользователь не найден");

        return new PersonDetails(person.get());
    }
}
