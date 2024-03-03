package com.app.sistemaacademico.security;

import com.app.sistemaacademico.model.Users;
import com.app.sistemaacademico.repository.IUsersRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUsersDetailsService implements UserDetailsService{

    @Autowired
    private IUsersRepository userRepository;
   
    // Metodo para traer autoridades por el rol
    public Collection<GrantedAuthority> mapToAuthorities(String role){
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
    
    // Metodo para traer los datos del usuario por el username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(users.getUsername(), users.getPassword(), mapToAuthorities(users.getRole()));
    }
    
}
