package morcheca.dao;

import morcheca.dao.interfaces.UserDao;
import morcheca.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("UserDaoHibernateImpl")
@Transactional(propagation = Propagation.REQUIRED)
public class UserDaoHibernateImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public User getById(Long id) {
        if (id == null) return null;
        return currentSession().get(User.class, id);
    }

    @Override
    public User getByName(String name) {
        if (name == null) return null;

        String hql = "from User where name=:name";

        Query<User> query = currentSession().createQuery(hql, User.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .setCacheable(true);

        List<User> users = query.getResultList();
        if (users.isEmpty()) return null;
        return users.get(0);
    }
    
    @Override
    public List<User> getByNameOrEmail(String name, String email) {
        String hql = "from User where name=:name or email=:email";

        Query<User> query = currentSession().createQuery(hql, User.class)
                .setParameter("name", name)
                .setParameter("email", email)
                .setCacheable(true);

        return query.getResultList();
    }

    @Override
    public List<User> getAll() {
        String hql = "from User order by id desc";

        Query<User> query = currentSession().createQuery(hql, User.class)
                .setCacheable(true);

        return query.getResultList();
    }

    @Override
    public void save(User user) {
        if (user != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            currentSession().save(user);
        }
    }

    @Override
    public void update(User user, boolean encryptPassword) {
        if (user == null) return;
        if (encryptPassword) user.setPassword(passwordEncoder.encode(user.getPassword()));
        currentSession().merge(user);
    }

    @Override
    public void delete(User user) {
        if (user != null) currentSession().delete(user);
    }

    @Override
    public void deleteById(Long id) {
        delete(getById(id));
    }

}