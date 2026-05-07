package it.defendimattia.backenddemo.validator;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class PageableValidator {

    private static final int MAX_PAGE_SIZE = 50;

    private PageableValidator() {
    }

    public static Pageable sanitize(
            Pageable pageable,
            List<String> allowedSortFields) {

        int size = pageable.getPageSize();

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
        }

        Sort validatedSort = validateSort(
                pageable.getSort(),
                allowedSortFields);

        return PageRequest.of(
                pageable.getPageNumber(),
                size,
                validatedSort);
    }

    private static Sort validateSort(
            Sort sort,
            List<String> allowedSortFields) {

        for (Sort.Order order : sort) {

            String property = order.getProperty();

            if (!allowedSortFields.contains(property)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid sort field: " + property);
            }
        }
        return sort;
    }
}
