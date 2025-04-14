package com.fasfood.web.support;

import com.fasfood.common.dto.PageDTO;
import com.fasfood.common.dto.request.PagingRequest;
import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.common.mapper.QueryMapper;
import com.fasfood.common.query.PagingQuery;
import com.fasfood.common.query.QueryService;
import com.fasfood.persistence.custom.EntityRepository;

import java.util.List;

public abstract class AbstractQueryService<D, E, O, I, Q extends PagingQuery, R extends PagingRequest>
        implements QueryService<O, I, R> {

    protected final DomainRepository<D, I> domainRepository;
    protected final EntityRepository<E, I> entityRepository;
    protected final DTOMapper<O, D, E> dtoMapper;
    protected final QueryMapper<Q, R> pagingRequestMapper;

    protected AbstractQueryService(DomainRepository<D, I> domainRepository,
                                   EntityRepository<E, I> entityRepository,
                                   DTOMapper<O, D, E> dtoMapper,
                                   QueryMapper<Q, R> pagingRequestMapper) {
        this.domainRepository = domainRepository;
        this.entityRepository = entityRepository;
        this.dtoMapper = dtoMapper;
        this.pagingRequestMapper = pagingRequestMapper;
    }

    @Override
    public O getById(I id) {
        return this.dtoMapper.domainToDTO(this.domainRepository.getById(id));
    }

    @Override
    public PageDTO<O> getList(R pagingRequest) {
        Q query = this.pagingRequestMapper.queryFromRequest(pagingRequest);
        long count = this.entityRepository.count(query);
        if (count == 0) return PageDTO.empty();
        List<E> data = this.entityRepository.search(query);
        return new PageDTO<>(this.dtoMapper.entityToDTO(data),
                pagingRequest.getPageIndex(), pagingRequest.getPageSize(), count);
    }
}
