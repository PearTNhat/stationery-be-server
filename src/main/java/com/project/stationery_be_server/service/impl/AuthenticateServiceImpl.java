package com.project.stationery_be_server.service.impl;

import com.project.stationery_be_server.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticateServiceImpl {
    UserRepository userRepository;
    @Value("${jwt.signerKey}")
    @NonFinal
    protected String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    @NonFinal
    protected long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    @NonFinal
    protected long REFRESHABLE_DURATION;

//    public AuthenticateResponse login(AuthenticateRequest request){
//
//    }
}
