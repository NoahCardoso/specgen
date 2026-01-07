package specgen;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

private int age;
private String passwordHash;
private String email;
private long id;

public long getId() {
return id;
}

public void setId(long id) {
this.id = id;
}

public String getEmail() {
return email;
}

public void setEmail(String email) {
this.email = email;
}

public String getPasswordHash() {
return passwordHash;
}

public void setPasswordHash(String passwordHash) {
this.passwordHash = passwordHash;
}

public int getAge() {
return age;
}

public void setAge(int age) {
this.age = age;
}

}