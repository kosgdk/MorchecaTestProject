package morcheca.services;

import morcheca.dao.interfaces.UserDao;
import morcheca.entities.User;
import morcheca.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao dao;


    @Override
    public User getById(Long id) {
        return dao.getById(id);
    }

    @Override
    public User getByName(String name) {
        return dao.getByName(name);
    }

    @Override
    public List<User> getByNameOrEmail(String name, String email) {
        return dao.getByNameOrEmail(name, email);
    }

    @Override
    public List<User> getAll() {
        return dao.getAll();
    }

    @Override
    public void save(User user) {
        dao.save(user);
    }

    @Override
    public void update(User user, boolean encryptPassword) {
        dao.update(user, encryptPassword);
    }

    @Override
    public void deleteById(Long id) {
        dao.deleteById(id);
    }

}