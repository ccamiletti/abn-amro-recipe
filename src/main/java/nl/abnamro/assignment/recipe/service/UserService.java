package nl.abnamro.assignment.recipe.service;

import lombok.AllArgsConstructor;
import nl.abnamro.assignment.recipe.dto.UserDTO;
import nl.abnamro.assignment.recipe.entity.UserEntity;
import nl.abnamro.assignment.recipe.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean isUserPresent(String userName) {
        return userRepository.findByUserName(userName).isPresent();
    }

    public UserDTO findByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .map(userEntity ->
                        UserDTO.builder().username(userEntity.getUserName())
                                .password(userEntity.getPassword())
                                .id(userEntity.getId()).build())
                .orElseThrow(() -> new RuntimeException("User not found"));

    }

    public UserDTO save(UserDTO userDTO) throws Exception {

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userDTO.getUsername());

        userEntity.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        return Optional.ofNullable(userRepository.save(userEntity))
                .map(ue -> UserDTO.builder().username(ue.getUserName()).password(ue.getPassword()).build())
                .orElseThrow(() -> new Exception("error saving user"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUserName(username);
    }
}
