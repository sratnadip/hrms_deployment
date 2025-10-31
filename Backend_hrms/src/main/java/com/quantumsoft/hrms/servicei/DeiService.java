package com.quantumsoft.hrms.servicei;

import com.quantumsoft.hrms.entity.DeiProfile;
import java.util.UUID;

public interface DeiService {
    DeiProfile getDeiProfile(UUID empId);
    DeiProfile upsertDeiProfile(DeiProfile profile);
}
