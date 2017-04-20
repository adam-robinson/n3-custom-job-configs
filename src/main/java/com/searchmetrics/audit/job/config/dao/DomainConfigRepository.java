package com.searchmetrics.audit.job.config.dao;

import com.searchmetrics.audit.job.config.dto.DomainConfig;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface DomainConfigRepository extends CassandraRepository<DomainConfig> {
    DomainConfig findByDomain(String domain);
}
