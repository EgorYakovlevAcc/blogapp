package org.blogapp.model;

import com.sun.corba.se.spi.ior.ObjectId;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Please provide a valid e-mail")
    @NotEmpty(message = "Please provide an e-mail")
    private String email;
    private String password;


    @Lob
    @Column(name = "photo", columnDefinition="BLOB")
    private byte[] photo;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_followers", joinColumns = {@JoinColumn(name = "subscriber_id")},
    inverseJoinColumns = {@JoinColumn(name = "channel_id")})
    //те, на кого я подписан
    private List<User> followers = new ArrayList<User>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_followers", joinColumns = {@JoinColumn(name = "channel_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")})
    //те, кто подписался на меня
    private List<User> subscription = new ArrayList<User>();

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Role role;


    public User(String username, String email, String password, byte[] photo) {
        this.photo = photo;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(Role.USER);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            System.out.println("EXIT 1");
            return true;
        }
        if (obj == null) {
            System.out.println("EXIT 2");
            return false;
        }
        if (getClass() != obj.getClass()) {
            System.out.println("EXIT 3");
            return false;
        }

        User other = (User) obj;
//        if (other.getUsername().equals(username)) {
//            System.out.println("EXIT 4");
//            return false;
//        }

        if (other.getId() != id) {
            System.out.println("EXIT 5");
            return false;
        }
        return true;
    }

    @Override
    public String getPassword () {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
