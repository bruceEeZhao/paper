package com.ucas.paper.service;

import com.ucas.paper.entities.Aboutus;

import java.util.List;

public interface AboutusService {
    Aboutus getAboutus();
    Aboutus updateAboutus(Aboutus aboutus);
    Aboutus getAndConvert();
}
