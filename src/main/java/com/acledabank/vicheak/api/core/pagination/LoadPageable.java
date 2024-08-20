package com.acledabank.vicheak.api.core.pagination;

import com.acledabank.vicheak.api.core.util.PageUtil;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public class LoadPageable {

    private LoadPageable(){

    }

    public static @NonNull Pageable loadPageable(@NonNull Map<String, String> requestMap) {
        //extract dynamic content (pageNumber & pageLimit) from request map

        //set up the default content
        int pageNumber = PageUtil.DEFAULT_PAGE_NUMBER;
        int pageLimit = PageUtil.DEFAULT_PAGE_LIMIT;

        if (requestMap.containsKey(PageUtil.PAGE_NUMBER))
            pageNumber = Integer.parseInt(requestMap.get(PageUtil.PAGE_NUMBER));

        if (requestMap.containsKey(PageUtil.PAGE_LIMIT))
            pageLimit = Integer.parseInt(requestMap.get(PageUtil.PAGE_LIMIT));

        return PageUtil.getPageable(pageNumber, pageLimit);
    }

}
