package com.minimart.role;

import com.minimart.common.CommonService;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.helpers.ListMapper;
import com.minimart.role.dto.CreateRoleDto;
import com.minimart.role.dto.RoleDto;
import com.minimart.role.dto.UpdateRoleDto;
import com.minimart.role.entity.Role;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements CommonService<CreateRoleDto, UpdateRoleDto, RoleDto, Integer> {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ListMapper listMapper;

    @Override
    @SuppressWarnings("unchecked")
    public List<RoleDto> findAll() {
        return (List<RoleDto>) listMapper.mapList(roleRepository.findAll(),new RoleDto());
    }

    @Override
    public RoleDto findById(Integer id) throws Exception {
        Role role = roleRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Role found with provided id"));
        return modelMapper.map(role, RoleDto.class);
    }

    @Override
    public RoleDto save(CreateRoleDto createRoleDto) throws Exception {
       Role newRole = roleRepository.save(modelMapper.map(createRoleDto, Role.class));
       return modelMapper.map(newRole, RoleDto.class);
    }

    @Override
    public RoleDto update(Integer id, UpdateRoleDto updateDto) throws Exception {
        Role existingRole = roleRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No Role found with provided id"));
        existingRole.setTitle(updateDto.getTitle());
        Role updatedRole = roleRepository.save(existingRole);
        return modelMapper.map(updatedRole, RoleDto.class);
    }

    @Override
    public void delete(Integer id) throws Exception {
        findById(id);
        roleRepository.deleteById(id);
    }
}
