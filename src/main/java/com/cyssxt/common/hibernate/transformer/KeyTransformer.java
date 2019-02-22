package com.cyssxt.common.hibernate.transformer;

import org.hibernate.transform.ResultTransformer;

import java.util.List;

public interface KeyTransformer extends ResultTransformer {

    List<String> getKeys();
}
