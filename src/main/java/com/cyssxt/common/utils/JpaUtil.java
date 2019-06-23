package com.cyssxt.common.utils;

import com.cyssxt.common.constant.ErrorMessage;
import com.cyssxt.common.dao.CommonRepository;
import com.cyssxt.common.entity.BaseEntity;
import com.cyssxt.common.exception.ValidException;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * Created by zqy on 2018/7/3.
 */
public class JpaUtil {
    /**
     * 检测实体是否有效
     * @param repository
     * @param <T>
     * @return
     * @throws ValidException
     */
    public static <T extends BaseEntity> T check(Object id, CommonRepository repository) throws ValidException {
        return check(id,repository,true);
    }

    public static <T> T check(Integer id, CommonRepository repository, boolean throwException) throws ValidException {
        if(StringUtils.isEmpty(id)){
            throw new ValidException(ErrorMessage.ID_NOT_NULL.getMessageInfo());
        }
        T entity = getEntity(throwException, repository.findById(id));
        return entity;
    }

    public static <T> T getEntity(CommonRepository commonRepository,Object userId) throws ValidException {
        return getEntity(true,commonRepository.findById(userId));
    }
    public static <T> T getEntity(boolean throwException, Optional byId) throws ValidException {
        Optional<T> optional = byId;
        if (!optional.isPresent()) {
            if (throwException) {
                throw new ValidException(ErrorMessage.NOT_EXIST.getMessageInfo());
            }
            return null;
        }
        T entity = optional.get();
        return entity;
    }

    public static <T extends BaseEntity> T check(Object id, CommonRepository repository, boolean throwException) throws ValidException {
        T entity = getEntity(throwException, repository.findById(id));
        if (entity == null) return null;
        return check(entity,throwException);
    }

    public static <T extends BaseEntity> T check(T entity,boolean checkDelete,String messageCode) throws ValidException {
        if(entity==null){
            if(!StringUtils.isEmpty(messageCode)){
                throw new ValidException(messageCode);
            }else{
                throw new ValidException(ErrorMessage.NOT_EXIST.getMessageInfo());
            }
        }
        if(entity.getDelFlag()!=null && entity.getDelFlag() && checkDelete){
            throw new ValidException(ErrorMessage.HAS_DELETE.getMessageInfo());
        }
//        if(checkExpireTime && entity.getExpireTime()!=null && entity.getExpireTime().getTime()<DateUtils.getCurrentTimestamp().getTime()){
//            throw new ValidException(ErrorMessage.HAS_EXPIRE,contentType);
//        }
        return entity;
    }

    public static <T extends BaseEntity> T check(T entity,boolean checkDelete) throws ValidException {
        return check(entity,checkDelete,null);
    }

    public static <T extends BaseEntity> T check(T entity) throws ValidException {
        return check(entity,true,null);
    }

}
