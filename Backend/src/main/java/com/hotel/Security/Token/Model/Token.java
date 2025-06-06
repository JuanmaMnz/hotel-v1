package com.hotel.Security.Token.Model;

import com.hotel.Security.Account.Model.Account;
import com.hotel.Security.Token.Enums.TokenCategory;
import com.hotel.Security.Token.Enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "token_id", updatable = false, nullable = false)
    private UUID tokenId;

    @Column(name = "token", nullable = false)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", updatable = false, nullable = false)
    private TokenType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private TokenCategory category;

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "account_id_type"),
            @JoinColumn(name = "account_id_number")
    })
    private Account account;

    public static Token buildAccessToken(Account account, String Jwt){
        return Token.builder()
                .account(account)
                .token(Jwt)
                .type(TokenType.BEARER)
                .category(TokenCategory.ACCESS)
                .revoked(false)
                .expired(false)
                .build();
    }

}