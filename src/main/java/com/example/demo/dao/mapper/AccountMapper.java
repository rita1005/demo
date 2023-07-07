package com.example.demo.dao.mapper;

import com.example.demo.dao.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface AccountMapper {

    public Optional<AccountEntity> queryByUsername(String username);
}
