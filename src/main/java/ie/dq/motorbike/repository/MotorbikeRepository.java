package ie.dq.motorbike.repository;

import ie.dq.motorbike.domain.Motorbike;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotorbikeRepository extends PagingAndSortingRepository<Motorbike, Long> {

    boolean existsMotorbikeByMakeAndModel(String make, String model);

}
