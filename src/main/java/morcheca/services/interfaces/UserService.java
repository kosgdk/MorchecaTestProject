package morcheca.services.interfaces;

import morcheca.entities.User;

import java.util.List;

public interface UserService {

    User getById(Long id);

    User getByName(String name);

    List<User> getByNameOrEmail(String name, String email);

    List<User> getAll();

    void save(User contact);

    void update(User contact, boolean encryptPassword);

    void deleteById(Long id);

}