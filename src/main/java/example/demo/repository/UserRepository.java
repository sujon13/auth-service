package example.demo.repository;

import example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    //Optional<BaMeetingSchedule> findBaMeetingScheduleByBondApplicationId(Long bondApplicationId);

    Optional<User> findByUserName(String userName);

    void deleteById(int userId);
}