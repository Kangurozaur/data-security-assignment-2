package assignment.passwordstorage;

import java.util.List;

public class User {
    private String username;
    private String pwdDigest;
    private List<Role> roles;

    public User(String username, String pwdDigest, List<Role> roles) {
        this.username = username;
        this.pwdDigest = pwdDigest;
        this.roles = roles;
    }

    public boolean canRun(String methodName) {
        return roles.stream().anyMatch(role -> role.isMethodAllowed(methodName));
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getPwdDigest() {
        return pwdDigest;
    }

    public List<Role> getRoles() {
        return roles;
    }
}
