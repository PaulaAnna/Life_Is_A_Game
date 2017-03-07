package intlig.lifeisagame.Models;

/**
 * Created by benjamindeutinger on 10.04.16.
 */
public class User {
    private String firstname;
    private String lastname;
    private String location;
    private String username;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User() {}

    public User(String location, String firstname, String lastname, String username, String uid) {
        this.location = location;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.uid = uid;

    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLocation() {
        return location;
    }

    public String getUsername() {
        return username;
    }
}

