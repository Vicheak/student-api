package com.acledabank.vicheak.api.core.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface PageUtil {

    int DEFAULT_PAGE_NUMBER = 1;
    int DEFAULT_PAGE_LIMIT = 3;
    String PAGE_NUMBER = "_page";
    String PAGE_LIMIT = "_limit";

    static Pageable getPageable(int pageNumber, int pageLimit) {
        if (pageNumber < DEFAULT_PAGE_NUMBER) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (pageLimit < 1) {
            pageLimit = DEFAULT_PAGE_LIMIT;
        }

        // start from 0
        return PageRequest.of(pageNumber - 1, pageLimit);
    }

}
