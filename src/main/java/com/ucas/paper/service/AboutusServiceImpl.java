package com.ucas.paper.service;

import com.ucas.paper.NotFoundException;
import com.ucas.paper.dao.AboutusRespository;
import com.ucas.paper.entities.Aboutus;
import com.ucas.paper.handler.MarkdownHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AboutusServiceImpl implements AboutusService {

    @Autowired
    private AboutusRespository aboutusRespository;

    @Override
    public List<Aboutus> getAboutus() {
        Pageable pageable = PageRequest.of(0,1);
        return aboutusRespository.findTop(pageable);
    }

    @Override
    public Aboutus addAboutus(Aboutus about) {
        if (!getAboutus().isEmpty()) {
            throw new NotFoundException("已经存在宗旨，不能添加多个");
        }

        return aboutusRespository.save(about);
    }

    @Override
    public Aboutus editAboutus(Long id, Aboutus about) {
        Aboutus j = aboutusRespository.findById(id).orElse(null);

        if (j == null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(about, j);

        return aboutusRespository.save(j);
    }

    @Override
    public Aboutus getAndConvert() {
        List<Aboutus> about = getAboutus();

        if (about.isEmpty()) {
            return null;
        }


        Aboutus n = new Aboutus();
        BeanUtils.copyProperties(about.get(0), n);
        String content = n.getContent();

        n.setContent(MarkdownHandler.markdownToHtml(content));
        return n;
    }
}
