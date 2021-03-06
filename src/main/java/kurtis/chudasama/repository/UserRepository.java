package kurtis.chudasama.repository;

import kurtis.chudasama.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

//TODO annotate repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findById(int id);

    ArrayList<User> findByUsernameLike(String username);
}
