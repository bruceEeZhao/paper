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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AboutusServiceImpl implements AboutusService {

    @Autowired
    private AboutusRespository aboutusRespository;

    @Override
    public Aboutus getAboutus() {

        List<Aboutus> aboutuses = aboutusRespository.findAll();
        if (aboutuses.isEmpty()) {
            return null;
        } else {
            return aboutuses.get(0);
        }
    }

    @Transactional
    @Override
    public Aboutus updateAboutus(Aboutus about) {
        aboutusRespository.deleteAll();
        return aboutusRespository.save(about);
    }


    @Override
    public Aboutus getAndConvert() {
        Aboutus about = getAboutus();

        if (about == null) {
            return null;
        }

        Aboutus n = new Aboutus();
        BeanUtils.copyProperties(about, n);
        String content = n.getContent();

        n.setContent(MarkdownHandler.markdownToHtml(content));
        return n;
    }
}
