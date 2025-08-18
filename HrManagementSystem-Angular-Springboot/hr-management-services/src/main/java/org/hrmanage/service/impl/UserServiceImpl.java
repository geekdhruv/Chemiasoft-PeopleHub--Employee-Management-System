package org.hrmanage.service.impl;

import lombok.RequiredArgsConstructor;
import org.hrmanage.dto.UserDto;
import org.hrmanage.entity.UserEntity;
import org.hrmanage.repository.UserRepository;
import org.hrmanage.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDto registerUser(UserDto userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return null;
        }

        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        userEntity.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        UserEntity savedUserEntity = userRepository.save(userEntity);
        UserDto savedUserDto = modelMapper.map(savedUserEntity, UserDto.class);
        savedUserDto.setPassword(null);

        return savedUserDto;
    }

    @Override
    public UserDto getUserByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            return null;
        }
        UserEntity userEntity = userRepository.findByUsername(username);
        UserDto userDTO = modelMapper.map(userEntity, UserDto.class);
        userDTO.setPassword(null);
        return userDTO;
    }
}
