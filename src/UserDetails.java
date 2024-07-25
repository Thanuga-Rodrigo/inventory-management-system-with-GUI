import java.io.*;

public class UserDetails {
    private static final String USER_DETAILS_FILE = "userDetails.txt";

    public static void saveUser(User user) throws IOException {
        // Check if the user already exists
        if (!isUserExists(user)) {
            // Append new user details to file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_DETAILS_FILE, true))) {
                bw.write(user.getUsername() + ":" + user.getPassword() + "\n");
            }
        }
    }
// checking whether user is existing
    public static boolean isUserExists(User user) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_DETAILS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(user.getUsername() + ":" + user.getPassword())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

