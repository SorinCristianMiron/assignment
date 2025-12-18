package learning.assignment.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import learning.assignment.dto.SortDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class SearchUtil {

    private List<SortDTO> mapStringToJSON(String jsonString) {
        try {
            ObjectMapper obj = new ObjectMapper();
            return obj.readValue(jsonString, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("Exception: ", e);
            return null;
        }
    }

    public PageRequest getPageRequest(Integer page, Integer size, String sort) {
        // Parse and create sort orders
        List<SortDTO> sortDtos = mapStringToJSON(sort);
        List<Sort.Order> orders = new ArrayList<>();

        if (sortDtos != null) {
            for(SortDTO sortDto: sortDtos) {
                Sort.Direction direction = Objects.equals(sortDto.getDirection(), "desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                orders.add(new Sort.Order(direction,sortDto.getField()));
            }
        }

        // Create page request with sorting
        return PageRequest.of(
                page,
                size,
                Sort.by(orders)
        );
    }
}
