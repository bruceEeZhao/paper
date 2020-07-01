package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.PurposeRespository;
import com.ucas.paper.entities.Purpose;
import com.ucas.paper.handler.MarkdownHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurposeServiceImpl implements PurposeService {

    @Autowired
    private PurposeRespository purposeRespository;


    @Override
    public Purpose getPurpose() {
        List<Purpose> purposes = purposeRespository.findAll();
        if (purposes.isEmpty()) {
            return null;
        } else {
            return purposes.get(0);
        }
    }

    @Transactional
    @Override
    public Purpose updatePurpose(Purpose purpose) {
        purposeRespository.deleteAll();
        return purposeRespository.save(purpose);
    }

    @Override
    public Purpose getAndConvert() {
        Purpose purpose = getPurpose();

        if (purpose==null) {
            return null;
        }


        Purpose n = new Purpose();
        BeanUtils.copyProperties(purpose, n);
        String content = n.getContent();

        n.setContent(MarkdownHandler.markdownToHtml(content));
        return n;
    }
}
