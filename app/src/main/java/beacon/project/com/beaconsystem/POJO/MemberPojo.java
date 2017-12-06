package beacon.project.com.beaconsystem.POJO;

/**
 * Created by Dell4050 on 11/30/2017.
 */

public class MemberPojo {
    private String username;
    private String password;
    private String nameuser;
    private String permission;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    public MemberPojo() {

    }

    public MemberPojo(String username, String password, String nameuser, String permission) {
        this.username = username;
        this.password = password;
        this.nameuser = nameuser;
        this.permission = permission;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameuser() {
        return nameuser;
    }

    public void setNameuser(String nameuser) {
        this.nameuser = nameuser;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
