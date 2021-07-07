package ru.vinigret.limit.users;

import lombok.*;
import javax.validation.constraints.Size;

@Data
public class UserLoginDTO {

    @Size(min = 5, max = 20, message = "не меньше 5 и не более 20 знаков")
    protected String login = "";

    @Size(min = 5, max = 20, message = "не меньше 5 и не более 20 знаков")
    protected String password = "";

    public UserLoginDTO() {

    }
    public UserLoginDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
