package com.hotel.Security.Token.Query;

public class TokenQuery {

    public static final String FIND_ALL_VALID_TOKENS_BY_USER =
            """
            select t
            from Token t
            inner join Account u on t.account.accountId = u.accountId
            where u.accountId = :accountId and (t.revoked = false and t.expired = false)
            """;
}