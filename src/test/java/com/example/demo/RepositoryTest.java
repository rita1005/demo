package com.example.demo;

import com.example.demo.dao.entity.Account;
import com.example.demo.dao.entity.CheckIn;
import com.example.demo.dao.repository.AccountRepository;
import com.example.demo.dao.repository.CheckInRepository;
import com.example.demo.dto.CheckInDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CheckInRepository checkInRepository;


    @Before
    public void init() {
        Account account1 = new Account("Leo","123");
        Account account2 = new Account("Lily","123");
        accountRepository.save(account1);
        accountRepository.save(account2);
        List<CheckIn> checkIns = Arrays.asList(
                new CheckIn(account1, LocalDateTime.of(2023, 7, 17, 8, 30)),
                new CheckIn(account1, LocalDateTime.of(2023, 7, 17, 10, 10)),
                new CheckIn(account1, LocalDateTime.of(2023, 7, 17, 17, 30)),
                new CheckIn(account2, LocalDateTime.of(2023, 7, 17, 9, 33)),
                new CheckIn(account2, LocalDateTime.of(2023, 7, 17, 11, 44)),
                new CheckIn(account2, LocalDateTime.of(2023, 7, 17, 18, 55)));
        checkInRepository.saveAll(checkIns);
    }

    @Test
    public void findByUsernameTest() {

        assertEquals("Leo", accountRepository.findByUsername("Leo").get().getUsername());
        System.out.println(accountRepository.findByUsername("Leo").get().getUsername());
    }

    @Test
    public void findUserCheckInTimeInfoTest() {


        List<CheckInDto> checkInDtos = checkInRepository.findUserCheckInTimeInfo(
                LocalDateTime.of(2023, 7, 17, 8, 00),
                LocalDateTime.of(2023, 7, 17, 20, 00));

        assertNotNull(checkInDtos);
        assertEquals(2, checkInDtos.size());
        checkInDtos.forEach(checkInDto -> System.out.println(
                "username:" + checkInDto.getAccount().getUsername() +
                ", first check-in time:" + checkInDto.getFirstCheckInTime() +
                ", last check-in time:" + checkInDto.getLastCheckInTime()));

    }
}
