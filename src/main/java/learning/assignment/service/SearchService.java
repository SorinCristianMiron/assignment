package learning.assignment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

/**
 * @param <T> type of filter class
 */
public interface SearchService<T> {

    Page<?> search(T searchDTO, Integer page, Integer size, String sort);
    Specification<?> getSpecification(T searchDTO);
}
