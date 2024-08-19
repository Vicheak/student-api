package com.acledabank.vicheak.api.core.service.impl;

import com.acledabank.vicheak.api.core.repository.UserTypeRepository;
import com.acledabank.vicheak.api.core.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeRepository userTypeRepository;

}
