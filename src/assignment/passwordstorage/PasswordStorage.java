package assignment.passwordstorage;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordStorage {

    final static String USERS_FILE_NAME = Paths.get("passwords.csv").toAbsolutePath().toString();
    final static String ROLES_FILE_NAME = Paths.get("roles.csv").toAbsolutePath().toString();
    private List<User> _users;
    private List<Role> _roles;

    public PasswordStorage () throws IOException, FileCorruptedException {
        _users = new LinkedList<>();
        _roles = new LinkedList<>();
        var reader = new BufferedReader(new FileReader(ROLES_FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            var decoded = line.split(",");
            if (decoded.length != 3) throw new FileCorruptedException();
            var methodList = decoded[2].equals("null") ?
                    new LinkedList<String>() :
                    Arrays.asList(decoded[2].split(";"));
            var extendedRolesList = decoded[1].equals("null") ?
                    new LinkedList<String>() :
                    Arrays.asList(decoded[1].split(";"));
            var loadedRole = new Role(decoded[0], methodList, extendedRolesList);

            _roles.add(loadedRole);
        }
        reader.close();

        // linking extended roles to roles
        for (var role: _roles) {
            var roles = role.getExtendedRoleNames()
                    .stream()
                    .map(this::roleNameToRole)
                    .collect(Collectors.toList());
            role.setExtendedRoles(roles);
        }
    }

    private Role roleNameToRole(String roleName) {
        return _roles
                .stream()
                .filter(r -> r.getName().equals(roleName))
                .findFirst()
                .orElseThrow();
    }

    public void load () throws IOException, FileCorruptedException {
        var reader = new BufferedReader(new FileReader(USERS_FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            var decoded = line.split(",");
            if (decoded.length != 3) throw new FileCorruptedException();
            var loadedRoleNames = Arrays.asList(decoded[1].split(";"));
            var loadedRoles = loadedRoleNames.stream().map(this::roleNameToRole).collect(Collectors.toList());
            var loadedUser = new User(decoded[0], decoded[2], loadedRoles);
            _users.add(loadedUser);
        }
        reader.close();
    }

    public void store () throws IOException {
        var writer = new BufferedWriter(new FileWriter(USERS_FILE_NAME));
        for (var user: _users) {
            var userRoles = user.getRoles()
                    .stream()
                    .map(Role::getName)
                    .reduce("", (s, rn) -> rn + ";" + s);
            // remove the last character
            userRoles = userRoles.length() > 0 ? userRoles.substring(0, userRoles.length() - 1) : "";

            var message = user.getUsername() + "," + userRoles + "," + user.getPwdDigest();
            writer.write(message, 0, message.length());
            writer.newLine();
        }
        writer.close();
    }

    public boolean verify (String username, String password) throws Exception {
        var user = getUserByUsername(username);

        if (user == null) {
            throw new Exception("Entry for user " + username + " not found");
        }
        return user.getPwdDigest().equals(hashPassword(username, password));
    }

    public void addUser(String username, String password, List<String> roleNames) throws NoSuchAlgorithmException {
        if (roleNames == null) {
            System.err.println("Adding a user requires specifying users roles");
        } else {
            var roles = roleNames.stream().map(this::roleNameToRole).collect(Collectors.toList());
            var newUser = new User(username, hashPassword(username, password), roles);
            _users.add(newUser);
        }
    }

    public User getUserByUsername(String username) {
        return _users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    public void addRole(String username, String roleName) throws Exception {
        var newRole = roleNameToRole(roleName);
        var user = getUserByUsername(username);
        if (user == null) throw new Exception("User not found");

        List<Role> oldRoles = user.getRoles();

        if (oldRoles.stream().anyMatch(r -> newRole.isExtending(r.getName()))) {
            // if there are roles to be upgraded
            oldRoles = List.copyOf((oldRoles
                    .stream()
                    .map(r -> newRole.isExtending(r.getName()) ? newRole : r)
                    .collect(Collectors.toSet())));
        } else if (oldRoles.stream().noneMatch(r -> r.isExtending(newRole.getName()))) {
            // if the roles are unrelated
            oldRoles.add(newRole);
        }

        user.setRoles(oldRoles);
    }

    public void removeRole(String username, String roleName) throws Exception {
        var user = getUserByUsername(username);
        if (user == null) throw new Exception("User not found");
        var newRoles = user.getRoles()
                .stream()
                .filter(r -> !r.getName().equals(roleName))
                .collect(Collectors.toList());
        user.setRoles(newRoles);
    }

    public void removeUser(String username) throws Exception {
        var user = getUserByUsername(username);
        if (user == null) throw new Exception("User not found");
        _users = _users.stream()
                .filter(u -> !u.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    private String hashPassword (String username, String password) throws NoSuchAlgorithmException {
        var md = MessageDigest.getInstance("SHA-512");
        var withSalt = username + password;
        var digest = md.digest(withSalt.getBytes());
        return new BigInteger(1, digest).toString(16);
    }
}
