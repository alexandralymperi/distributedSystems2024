package gr.hua.dit.ds.ds2024Team77.payload;

import jakarta.validation.constraints.NotBlank;

//The LoginRequest class is used to transfer the data
//user login (username and password).
public class LoginRequest {

    @NotBlank //Ensures that the field is not empty or only spaces
    private String username;

    @NotBlank
    private String password;

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
}
