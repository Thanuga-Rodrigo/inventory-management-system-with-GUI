//The User class represents a user with a username and password.
public class User {
        private String username;
        private String password;
        //Constructor to initialize a User object with the specified username and password.

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }
        //getters and setters
        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
}
