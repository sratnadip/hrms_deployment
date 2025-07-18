package com.quantumsoft.hrms.controller;

import com.quantumsoft.hrms.entity.DeiProfile;
import com.quantumsoft.hrms.servicei.DeiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/dei")
@RequiredArgsConstructor
public class DeiController {

    private final DeiService deiService;


    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR', 'ADMIN')")
    @GetMapping("/{empId}")
    public DeiProfile getProfile(@PathVariable UUID empId) {
        return deiService.getDeiProfile(empId);
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR', 'ADMIN')")
    @PostMapping
    public DeiProfile upsertProfile(@RequestBody DeiProfile profile) {
        return deiService.upsertDeiProfile(profile);
    }
}
