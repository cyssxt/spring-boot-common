package com.cyssxt.common.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CommonRepository<T,V> extends CrudRepository<T,V>, JpaSpecificationExecutor<T> {
}
