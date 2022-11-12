package gogitek.restaurentorder.service;

import gogitek.restaurentorder.entity.User;
import gogitek.restaurentorder.modelutil.PasswordDTO;

public interface UserService {
    boolean registerUser(User user);
    boolean checkExist(String email);
    User getCurrentUser();
    void updateUser(int id, User userRequest);
    boolean updatePassword(PasswordDTO passwordDTO);
}
