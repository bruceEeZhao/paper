package com.ucas.paper.service;

import com.ucas.paper.entities.Purpose;

import java.util.List;

public interface PurposeService {
    Purpose getPurpose();
    Purpose updatePurpose(Purpose purpose);
    Purpose getAndConvert();
}
