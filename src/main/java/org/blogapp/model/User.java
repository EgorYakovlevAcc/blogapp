package org.blogapp.model;

import com.sun.corba.se.spi.ior.ObjectId;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
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
    @Column(unique = true)
    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany(/*fetch = FetchType.EAGER, cascade = CascadeType.PERSIST*/)
    @JoinTable(name = "user_followers", joinColumns = {@JoinColumn(name = "subscriber_id")},
    inverseJoinColumns = {@JoinColumn(name = "channel_id")})
    //те, на кого я подписан
    private Set<User> followers = new HashSet<User>();

    @ManyToMany(/*fetch = FetchType.EAGER*/ /*cascade = CascadeType.PERSIST*/)
    @JoinTable(name = "user_followers", joinColumns = {@JoinColumn(name = "channel_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")})
    //те, кто подписался на меня
    private Set<User> subscription = new HashSet<User>();

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Role role;

    public User(String username, String password) {
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
        if (this == obj)
                return true;
        if (obj == null)
                return false;
        if (getClass() != obj.getClass())
                return false;

        User other = (User) obj;
        if (other.getId() != id) {
            return false;
        }
        return true;
    }
}
