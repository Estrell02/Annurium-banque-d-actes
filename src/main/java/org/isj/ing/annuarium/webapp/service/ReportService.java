package org.isj.ing.annuarium.webapp.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.isj.ing.annuarium.webapp.mapper.ActeMapper;
import org.isj.ing.annuarium.webapp.model.dto.Actedto;
import org.isj.ing.annuarium.webapp.repository.ActeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    ActeRepository acteRepository;
    @Autowired
    ActeMapper acteMapper;


    public byte[] exportReport(Actedto actedto) throws FileNotFoundException, JRException {
        String path="C:\\Users\\CSEG\\JaspersoftWorkspace\\MyReports";
        List<Actedto> actes=new ArrayList<>();
        actes.add(actedto);


        //File file= ResourceUtils.getFile("classpath:acte6.jrxml");
        JasperReport jasperReport= JasperCompileManager.compileReport(new FileInputStream("C:\\Users\\CSEG\\JaspersoftWorkspace\\MyReports\\acte6.jrxml"));

       JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(actes);

        Map<String,Object> parameters= new HashMap<>();
        parameters.put("createdBy","ISJ");

        JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,parameters,dataSource);


        byte[] data= JasperExportManager.exportReportToPdf(jasperPrint);

        return data;
    }
}
