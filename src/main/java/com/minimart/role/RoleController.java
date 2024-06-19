package com.minimart.role;

import com.minimart.common.ApiResponse;
import com.minimart.role.dto.CreateRoleDto;
import com.minimart.role.dto.RoleDto;
import com.minimart.role.dto.UpdateRoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    RoleService roleService;

    @GetMapping
    private ApiResponse<List<RoleDto>> findAll(){
        List<RoleDto> roles = roleService.findAll();
        return ApiResponse.success(roles, "Roles fetched successfully");
    }

    @GetMapping("/{id}")
    private ApiResponse<RoleDto> findById(@PathVariable int id) throws Exception{
        RoleDto roles = roleService.findById(id);
        return ApiResponse.success(roles, "Role fetched successfully");
    }

    @PostMapping
    private ApiResponse<RoleDto> create(@RequestBody CreateRoleDto createRoleDto) throws Exception{
        RoleDto newRole = roleService.save(createRoleDto);
        return ApiResponse.success(newRole, "Role created successfully");
    }

    @PutMapping("/{id}")
    private ApiResponse<RoleDto> update(@PathVariable int id, @RequestBody UpdateRoleDto updateRoleDto) throws Exception{
        RoleDto newRole = roleService.update(id, updateRoleDto);
        return ApiResponse.success(newRole, "Role updated successfully");
    }

    @DeleteMapping("/{id}")
    private ApiResponse<?> delete(@PathVariable int id) throws Exception{
        roleService.delete(id);
        return ApiResponse.success(null, "Role deleted successfully");
    }
}
