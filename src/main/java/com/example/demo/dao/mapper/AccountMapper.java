package com.example.demo.dao.mapper;

import com.example.demo.dao.entity.Account;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AccountMapper {

    public Optional<Account> queryByUsername(String username);
}
