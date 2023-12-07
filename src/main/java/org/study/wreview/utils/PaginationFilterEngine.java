package org.study.wreview.utils;

import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.stream.Collectors;

public interface PaginationFilterEngine {

    int rowsInPage = 5;

    default <T> Page<T> getPage(Model model,
                                   Integer pageNum,
                                   String filter,
                                   Sort sort,
                                   FunctionForPaginationWithFilter<T> function){
        if(pageNum == null) {
            pageNum = 0;
        } else {
            pageNum = Math.max(0, pageNum);
        }
        if(filter == null){
            filter = "";
        }
        Pageable pages = PageRequest.of(pageNum, rowsInPage, sort);

        String queryFilter = Arrays.stream(filter.split(" "))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|", "(", ")"));

        Page<T> page = function.apply(pages, queryFilter);

        int totalPage = page == null? 0: page.getTotalPages() - 1;

        pageNum = Math.max(0, Math.min(totalPage, pageNum));

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", totalPage);
        model.addAttribute("filter", filter);
        return page;
    }
}

