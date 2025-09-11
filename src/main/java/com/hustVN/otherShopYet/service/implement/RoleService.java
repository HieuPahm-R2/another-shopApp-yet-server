package com.hustVN.otherShopYet.service.implement;

import com.hustVN.otherShopYet.model.entity.Role;
import com.hustVN.otherShopYet.repo.RoleRepository;
import com.hustVN.otherShopYet.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    @Override
    public List<Role> getAllRoles() {
        return List.of();
    }

    private final RoleRepository roleRepository;
}
