package com.hotel.Security.Token.Repository;

import com.hotel.Security.Token.Model.Token;
import com.hotel.identity_document.Model.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.hotel.Security.Token.Query.TokenQuery.FIND_ALL_VALID_TOKENS_BY_USER;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {

    @Query(FIND_ALL_VALID_TOKENS_BY_USER)
    List<Token> findAllValidTokensByAccount(@Param("accountId") Id accountId);

    Optional<Token> findByToken(String token);
}