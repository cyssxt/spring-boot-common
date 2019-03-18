package com.cyssxt.common.hibernate.transformer;

import org.hibernate.transform.TupleSubsetResultTransformer;

import java.util.List;

public interface KeyTransformer extends TupleSubsetResultTransformer {

    List<String> getKeys();
}
