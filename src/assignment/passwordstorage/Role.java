package assignment.passwordstorage;

import java.util.List;

public class Role {
    private String name;
    private List<String> allowedMethods;
    private List<String> extendedRoleNames;
    private List<Role> extendedRoles;

    public Role(String name, List<String> allowedMethods, List<String> extendedRoleNames) {
        this.name = name;
        this.allowedMethods = allowedMethods;
        this.extendedRoleNames = extendedRoleNames;
    }

    public boolean isMethodAllowed(String methodName) {
        return allowedMethods.contains(methodName) ||
               extendedRoles.stream().anyMatch(role -> role.isMethodAllowed(methodName));
    }

    public List<String> getExtendedRoleNames() {
        return extendedRoleNames;
    }

    public void setExtendedRoles(List<Role> roles) {
        extendedRoles = roles;
    }

    public boolean isExtending(String roleName) {
        return name.equals(roleName) || extendedRoles.stream().anyMatch(role -> role.isExtending(roleName));
    }

    public String getName() {
        return name;
    }
}
