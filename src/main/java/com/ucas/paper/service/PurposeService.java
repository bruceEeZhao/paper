package com.ucas.paper.service;

import com.ucas.paper.entities.Purpose;

import java.util.List;

public interface PurposeService {
    List<Purpose> getPurpose();
    Purpose addPurpose(Purpose purpose);
    Purpose editPurpose(Long id, Purpose purpose);
    Purpose getAndConvert();
}
