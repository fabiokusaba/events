package br.com.nlw.events.repository;

import br.com.nlw.events.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    // Recuperando um usuário pelo seu respectivo email
    User findByEmail(String email);
}
