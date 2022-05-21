package com.hrms.acl.auth.repository;

import com.hrms.acl.auth.entity.RequestUrl;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestUrlRepository extends GenericRepository<RequestUrl> {

    Optional<RequestUrl> getByUrl(String url);

    @Query("SELECT e FROM RequestUrl e WHERE e.url = ?1")
    RequestUrl getByUrlPath(String url);

}
