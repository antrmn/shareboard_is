package service;

import http.util.InputValidator;
import persistence.model.*;
import persistence.repo.*;
import service.dto.UserIdentityDTO;
import service.dto.UserEditPage;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    @Inject private UserRepository userRepo;

    @RolesAllowed({"admin"})
    public void ToggleAdmin(int id){
        User u = userRepo.findById(id);
        u.setAdmin(!u.getAdmin());

    }

    public List<UserIdentityDTO> ShowUsers(){
        List<User> users = userRepo.findAll();
        List<UserIdentityDTO> usersDTO = new ArrayList<>();

        for(User u : users){
            UserIdentityDTO userDTO = new UserIdentityDTO(u.getId(), u.getUsername(), u.getAdmin());
            usersDTO.add(userDTO);
        }
        return usersDTO;
    }

    @RolesAllowed({"admin"})
    public void Delete(int id){
        userRepo.remove(userRepo.findById(id));
    }

    public UserIdentityDTO Get(int id){
        User u = userRepo.findById(id);
        return new UserIdentityDTO(u.getId(), u.getUsername(), u.getAdmin());
    }

    @RolesAllowed({"admin", "user"})
    public void Edit(UserEditPage edit){

    }

    public int Create(UserRegisterDTO register){

    }
}
