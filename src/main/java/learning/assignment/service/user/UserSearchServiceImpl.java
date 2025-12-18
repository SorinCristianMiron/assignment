package learning.assignment.service.user;

import jakarta.persistence.criteria.Predicate;
import learning.assignment.dto.UserSearchDTO;
import learning.assignment.model.User;
import learning.assignment.repository.UserRepository;
import learning.assignment.service.SearchService;
import learning.assignment.util.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserSearchServiceImpl extends UserServiceImpl implements SearchService<UserSearchDTO> {

    private final UserRepository userRepository;

    @Autowired
    public UserSearchServiceImpl(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    public Page<User> search(UserSearchDTO userSearchDTO, Integer page, Integer size, String sort) {
        return userRepository.findAll(getSpecification(userSearchDTO), new SearchUtil().getPageRequest(page, size, sort));
    }

    public Specification<User> getSpecification(UserSearchDTO userSearchDTO) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (userSearchDTO.getEmail() != null) {
                predicates.add(criteriaBuilder.equal(root.get("email"),userSearchDTO.getEmail()));
            }

            if (userSearchDTO.getUsername() != null) {
                predicates.add(criteriaBuilder.equal(root.get("username"),userSearchDTO.getUsername()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
