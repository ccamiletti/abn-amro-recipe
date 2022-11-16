package nl.abnamro.assignment.recipe.service;

import lombok.AllArgsConstructor;
import nl.abnamro.assignment.recipe.dto.UserDTO;
import nl.abnamro.assignment.recipe.entity.UserEntity;
import nl.abnamro.assignment.recipe.exception.UserException;
import nl.abnamro.assignment.recipe.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDTO findByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .map(userEntity ->
                        UserDTO.builder().username(userEntity.getUserName())
                                .password(userEntity.getPassword())
                                .id(userEntity.getId()).build())
                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    public void save(UserDTO userDTO) throws UserException {
        userRepository.findByUserName(userDTO.getUsername())
                .map(userEntity -> {
                    throw new UserException("error saving user", HttpStatus.INTERNAL_SERVER_ERROR);
                }).orElseGet(() -> userRepository.save(toEntity(userDTO)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUserName(username);
    }

    private UserEntity toEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userDTO.getUsername());
        userEntity.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        return userEntity;
    }
}
