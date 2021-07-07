package ru.vinigret.limit.users;

import lombok.*;
import ru.vinigret.limit.urlico.uchetul.UchetUL;

import javax.persistence.*;

@Entity
@Table(name = "appusers",  uniqueConstraints = {@UniqueConstraint(name = "UnicLogin", columnNames = "login"), @UniqueConstraint(name = "unicEmail", columnNames = "email")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login = "";

    private String password = "";

    private String email = "";

    private String title = "";

    private boolean enabled = false;

    private boolean isAdmin = false;

    @ManyToOne
    private UchetUL uchetUL;

}