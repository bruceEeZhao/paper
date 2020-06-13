package com.ucas.paper.service;

import com.ucas.paper.entities.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    Type addType(Type type);
    Type getType(Long id);
    Type getTypeByName(String name);
    Type updateType(Long id,Type type);
    Page<Type> listType(Pageable pageable);
    Page<Type> listType(Pageable pageable, String type);
    List<Type> listType();
    void delType(Long id);
}
