package com.breez.web.service;

import com.breez.web.entities.Resume;
import com.breez.web.entities.UserBaseInfo;

public interface ResumeService {
    Resume getResumeById(Long userId);
}
