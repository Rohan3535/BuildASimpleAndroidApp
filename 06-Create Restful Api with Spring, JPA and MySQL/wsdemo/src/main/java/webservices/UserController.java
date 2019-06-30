package webservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Hashtable;
import java.util.List;

@Controller
@RequestMapping(path="/users") //http://localhost:8081/users/
public class UserController {
    @Autowired  //This means to get the bean called userRepository
    private  UserRepository userRepository;
    @PostMapping(path="/register") //POST http://localhost:8081/users/register
    public @ResponseBody Hashtable registerUser(@RequestParam String name,
                                                @RequestParam String email,
                                                @RequestParam String userType, //facebook / default ?
                                                //You should save "encrypted password"
                                                @RequestParam String password) {
        Hashtable<String, Object> response = new Hashtable<>();
        //check dublicate user
        List<UserRepository.User> foundUsers = userRepository.findByEmail(email);
        if(foundUsers.size() > 0) {
            if(userType.equals("facebook")) {
                response.put("result", "ok");
                response.put("data", foundUsers.get(0));
                response.put("message", "Login user successfully");
                return response;
            } else {
                response.put("result", "failed");
                response.put("data", "");
                response.put("message", "User already exists");
                return response;
            }
        }
        User newUser = new User(name, email, userType, password);
        userRepository.save(newUser);//insert to MySQL Database
        response.put("result", "ok");
        response.put("data", newUser);
        response.put("message", "Register user successfully");
        return response;
    }

}