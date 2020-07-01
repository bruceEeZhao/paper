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

import java.util.List;

@Service
public class PurposeServiceImpl implements PurposeService {

    @Autowired
    private PurposeRespository purposeRespository;


    @Override
    public List<Purpose> getPurpose() {
        Pageable pageable = PageRequest.of(0,1);
        return purposeRespository.findTop(pageable);
    }

    @Override
    public Purpose addPurpose(Purpose purpose) {
        if (!getPurpose().isEmpty()) {
            throw new NotFoundException("已经存在宗旨，不能添加多个");
        }

        return purposeRespository.save(purpose);
    }

    @Override
    public Purpose editPurpose(Long id, Purpose purpose) {
        Purpose j = purposeRespository.findById(id).orElse(null);

        if (j == null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(purpose, j);

        return purposeRespository.save(j);
    }

    @Override
    public Purpose getAndConvert() {
        List<Purpose> purpose = getPurpose();

        if (purpose.isEmpty()) {
            return null;
        }


        Purpose n = new Purpose();
        BeanUtils.copyProperties(purpose.get(0), n);
        String content = n.getContent();

        n.setContent(MarkdownHandler.markdownToHtml(content));
        return n;
    }
}
