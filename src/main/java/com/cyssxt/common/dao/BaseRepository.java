package com.cyssxt.common.dao;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by zqy on 12/05/2018.
 */
@NoRepositoryBean
public interface BaseRepository<T> extends CrudRepository<T,String>,JpaSpecificationExecutor<T> {

}


