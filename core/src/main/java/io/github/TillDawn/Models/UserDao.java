package io.github.TillDawn.Models;

public interface UserDao {
    void initialize();
    boolean addUser(User user);
    User getUserByUsername(String username);
    boolean validateUser(String username, String password);
    boolean userExists(String username);
    void dispose();
}
