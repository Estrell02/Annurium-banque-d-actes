package org.isj.ing.annuarium.webapp.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.isj.ing.annuarium.webapp.mapper.ActeMapper;
import org.isj.ing.annuarium.webapp.model.dto.Actedto;
import org.isj.ing.annuarium.webapp.model.entities.Acte;
import org.isj.ing.annuarium.webapp.repository.ActeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class ActeServiceImpl implements IActe{

    @Autowired
    ActeRepository acteRepository;
    @Autowired
    ActeMapper acteMapper;


    @Override
    public int saveActe(Actedto actedto) {
       if ( acteRepository.findActeByNumero(actedto.getNumero()).isPresent() ){
           return 0;
        }
       return acteRepository.save(acteMapper.toEntity(actedto)).getId().intValue();

    }

    @Override
    public Actedto searchActeByNumero(String numero) {

        return acteMapper.toDto(acteRepository.findActeByNumero(numero).get());
    }

    @Override
    public List<Actedto> ListActes() {

       /* List<Acte> actes = acteRepository.findAll();
        actes.stream().map(acte -> acteMapper.toDto(acte)).collect(Collectors.toList());

        /*List<Actedto> actedtos = new ArrayList<Actedto>();
        for (Acte acte:actes) {
           Actedto actedto= acteMapper.toDto(acte);
           actedtos.add(actedto);
        }
       return actedtos;*/

        return acteRepository.findAll().stream().map(acte -> acteMapper.toDto(acte)).collect(Collectors.toList());
    }

    @Override
    public int deleteActe(String numero) {
        //Acte acte=acteRepository.findActeByNumero(numero).get();//on recherche dabord en bd a partir de son numero
        // ensuite on suprime le supprime a partir de son numero
        acteRepository.deleteById(acteRepository.findActeByNumero(numero).get().getId());
        return 1;
    }

    @Override
    public List<Actedto> searchActeByKeyword(String keyword) {

       // return acteRepository.findActeByNumeroOrNom(keyword,keyword).get().stream().map(acte -> acteMapper.toDto(acte)).collect(Collectors.toList());
        return acteRepository.findActeByNumeroOrNom(keyword,keyword).get().stream().map(acteMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Actedto updateActe(Actedto actedto) {
        //on recherche l'acte saisi par l'utilisateur dans la base de donnees
        //on recherche l'entite qui correspond a l'acte que nous voulons update
        Acte acte=acteRepository.findActeByNumero(actedto.getNumero()).get();//cherche dans la BD l'acte par le numero
        // on a deja ecrit la methode pour faire la copie on l'appel juste

        acteMapper.copy(actedto, acte);

       return acteMapper.toDto(acteRepository.save(acte)); // la methode save renvoie l'entite mais nous voulons l'objet dto
        //on va doc appeler la methode actemapper.todto pour convertir

    }

    @Override
    public byte[] exportReport(Actedto actedto) throws FileNotFoundException, JRException {
        List<Actedto> actes=new ArrayList<>();
        actes.add(actedto);


        //File file= ResourceUtils.getFile("classpath:acte6.jrxml");
        JasperReport jasperReport= JasperCompileManager.compileReport(new FileInputStream("C:\\Users\\CSEG\\Desktop\\cours\\vro\\Annuarium\\src\\main\\resources\\acte6.jrxml"));

        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(actes);

        Map<String,Object> parameters= new HashMap<>();
        parameters.put("createdBy","ISJ");

        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parameters,dataSource);


        byte[] data= JasperExportManager.exportReportToPdf(jasperPrint);

        return data;
    }
}
