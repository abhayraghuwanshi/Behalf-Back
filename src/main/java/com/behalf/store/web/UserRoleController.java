package com.behalf.store.web;

import com.behalf.store.model.UserRole;
import com.behalf.store.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping("/assign-role")
    public void assignRole(@RequestBody RoleRequest request) {
        userRoleService.assignRole(request.userId(), request.role());
    }

    @PostMapping("/remove-role")
    public void removeRole(@RequestBody RoleRequest request) {
        userRoleService.removeRole(request.userId(), request.role());
    }

    @GetMapping("/role/{role}")
    public List<UserRole> getUsersByRole(@PathVariable String role) {
        return userRoleService.getUsersByRole(role);
    }

    @GetMapping("/{userId}/is-admin-or-manager")
    public boolean isAdminOrManager(@PathVariable Long userId) {
        return userRoleService.isAdminOrManager(userId);
    }

    public record RoleRequest(Long userId, String role) {}
}
