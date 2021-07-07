package ru.vinigret.limit.urlico.uchetul;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vinigret.limit.urlico.UrLico;
import ru.vinigret.limit.users.AppUser;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UchetUL extends UrLico {

    private Boolean grbs;

    @ManyToOne
    private UchetUL uchreditel;

    @ManyToOne
    private UchetUL parent;

    private Boolean groupUchr;

    private UchetULKind uchetULKind;

    private Integer level;

    private String razdelBK;

    private String ierKod;

    /*
    @Transient
    @ManyToMany
    private Set<AppUser> userList;

    @Transient
    @OneToMany
    private Set<UchetUL> childList;
*/
    //public UchetUL() {
    //    super();
    //}

    @Override
    public String toString() {
        return "UchetUL{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                "grbs=" + grbs +
                ", uchreditel=" + uchreditel +
                ", parent=" + parent +
                ", groupUchr=" + groupUchr +
                ", uchetULKind=" + uchetULKind +
                ", level=" + level +
                ", ierKod='" + ierKod + '\'' +
                '}';
    }
}
