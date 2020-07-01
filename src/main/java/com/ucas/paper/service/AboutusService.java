package com.ucas.paper.service;

import com.ucas.paper.entities.Aboutus;

import java.util.List;

public interface AboutusService {
    List<Aboutus> getAboutus();
    Aboutus addAboutus(Aboutus aboutus);
    Aboutus editAboutus(Long id, Aboutus about);
    Aboutus getAndConvert();
}
