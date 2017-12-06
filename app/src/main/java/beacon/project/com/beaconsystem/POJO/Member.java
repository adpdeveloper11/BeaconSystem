package beacon.project.com.beaconsystem.POJO;

/**
 * Created by Dell4050 on 12/6/2017.
 */

public class Member {
    private String username,nameuser,password,email,permission,path;

    public Member(String username, String nameuser, String password, String email, String permission, String path) {
        this.username = username;
        this.nameuser = nameuser;
        this.password = password;
        this.email = email;
        this.permission = permission;
        this.path = path;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
