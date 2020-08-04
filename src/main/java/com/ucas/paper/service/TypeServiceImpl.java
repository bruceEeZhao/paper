package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.TypeRespository;
import com.ucas.paper.entities.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService{

    @Autowired
    private TypeRespository typeRespository;

    @Transactional
    @Override
    public Type addType(Type type) {
        Type t = typeRespository.findByName(type.getName());
        if (t!=null) {
            //不能添加重复的数据
            return null;
        } else {
            return typeRespository.save(type);
        }
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRespository.getOne(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return typeRespository.findByName(name);
    }

    @Transactional
    @Override
    public Type updateType(Long id,Type type) {
        Type t = typeRespository.findById(id).orElse(null);
        if (t == null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type, t);
        return typeRespository.save(t);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRespository.findAll(pageable);
    }

    @Override
    public Page<Type> listType(Pageable pageable, String type) {
        if(type!=null && !"".equals(type)) {
            return typeRespository.findByQuery("%"+type+"%", pageable);
        } else {
            return typeRespository.findAll(pageable);
        }
    }

    @Transactional
    @Override
    public List<Type> listType() {
        List<Type> list = typeRespository.findAll();
        Collator com = Collator.getInstance(java.util.Locale.CHINA);

        list.sort(new Comparator<Type>() {
            @Override
            public int compare(Type o1, Type o2) {
                return com.getCollationKey(o1.getName()).compareTo(com.getCollationKey(o2.getName()));
            }
        });
        return list;
    }

    @Transactional
    @Override
    public void delType(Long id) {
        typeRespository.deleteById(id);
    }

    @Override
    public Long count() {
        return typeRespository.count();
    }
}
