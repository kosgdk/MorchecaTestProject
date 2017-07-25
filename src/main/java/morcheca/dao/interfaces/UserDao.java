package morcheca.dao.interfaces;

import morcheca.entities.User;

import java.util.List;

public interface UserDao {

    User getById(Long id);

    User getByName(String name);
    
    List<User> getByNameOrEmail(String name, String email);

    List<User> getAll();

    void save(User user);

    void update(User user, boolean encryptPassword);

    void delete(User user);

    void deleteById(Long id);

}