package gr.hua.dit.ds.ds2024Team77.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//Implementation of Spring Security UserDetails for user authentication and authorization.
//This class represents the user as stored by Spring Security to check authentication.
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String email;

    //Ignores the property during serialization (eg, when returning the user to JSON)
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    //Constructor to initialize a UserDetailsImpl object.
    public UserDetailsImpl(Long id, String username, String email,
                           String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    //Creates and returns a UserDetailsImpl object from the user.
    public static UserDetailsImpl build(User user){
        //Converts the user's roles to GrantedAuthority.
        List<GrantedAuthority> authorities = (user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList()));
        //Returns a new UserDetailsImpl object.
        return new UserDetailsImpl(
                user.getId(), user.getUsername(),
                user.getEmail(), user.getPassword(), authorities);

    }

    //Returns the user's authorizations/roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    public Long getId(){return id;}

    public String getEmail(){return email;}

    @Override
    public String getPassword(){return password;}

    @Override
    public String getUsername(){return username;}

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    //Implements the equals method to compare two UserDetailsImpl objects based on their ID.
    @Override
    public boolean equals(Object o){

        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);


    }
}
