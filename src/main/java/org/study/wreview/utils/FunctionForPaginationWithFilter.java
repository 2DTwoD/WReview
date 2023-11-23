package org.study.wreview.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@FunctionalInterface
public interface FunctionForPaginationWithFilter<T>{
    Page<T> apply(Pageable pageable, String filter);
}
