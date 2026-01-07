package specgen;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

private final UserRepository repo;

public UserController(UserRepository repo) {
this.repo = repo;
}

@PostMapping
public User create(@RequestBody User user) {
return repo.save(user);
}

@GetMapping
public List<User> getAll() {
return repo.findAll();
}

@GetMapping("/{id}")
public User getOne(@PathVariable long id) {
return repo.findById(id);
}

@PutMapping("/{id}")
public User update(@PathVariable long id, @RequestBody User updated) {
User user = repo.findById(id);
user.setEmail(updated.getEmail());
user.setPasswordHash(updated.getPasswordHash());
user.setAge(updated.getAge());
return repo.save(user);
}

@DeleteMapping("/{id}")
public void delete(@PathVariable long id) {
repo.deleteById(id);
}

}