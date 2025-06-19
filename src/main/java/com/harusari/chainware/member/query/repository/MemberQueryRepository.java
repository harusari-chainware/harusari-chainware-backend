package com.harusari.chainware.member.query.repository;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberQueryRepository {

    boolean existsByEmail(String email);

}