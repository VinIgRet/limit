package ru.vinigret.limit.users;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {

    @Getter
    @Setter
    protected Integer id;

    @Getter
    @Setter
    @Size(min = 5, max = 20, message = "не меньше 5 и не более 20 знаков")
    protected String login;

    @Getter
    @Setter
    @Size(min = 5, max = 20, message = "не меньше 5 и не более 20 знаков")
    protected String password;

    @Getter
    @Setter
    protected String passwordConfirm;

    @Getter
    @Setter
    @Email
    protected String email;

    @Getter
    @Setter
    protected String emailConfirm;

    @Getter
    @Setter
    private String emailOld;

    @Getter
    @Setter
    @Size(max = 255, message = "не более 255 знаков")
    @NotBlank
    protected String title;

    @Getter
    @Setter
    protected String action = ".osn";

    @Getter
    @Setter
    private boolean enabled;

    @Getter
    @Setter
    private boolean isAdmin;

    @Getter
    @Setter
    private Integer uchetUL;

    @Getter
    @Setter
    protected String returnhttp;

}
