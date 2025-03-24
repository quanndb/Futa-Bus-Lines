package com.fasfood.iamservice.domain.query;

import com.fasfood.common.enums.AccountStatus;
import com.fasfood.common.enums.Gender;
import com.fasfood.common.query.PagingQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class AccountPagingQuery extends PagingQuery {
    private List<Gender> gender;
    private List<AccountStatus> status;
}
