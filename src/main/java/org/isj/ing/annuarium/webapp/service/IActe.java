package org.isj.ing.annuarium.webapp.service;

import net.sf.jasperreports.engine.JRException;
import org.isj.ing.annuarium.webapp.model.dto.Actedto;

import java.io.FileNotFoundException;
import java.util.List;

public interface IActe {

    int saveActe(Actedto actedto);

    Actedto searchActeByNumero(String numero);
    List<Actedto> ListActes();
    int deleteActe(String numero);

    List<Actedto> searchActeByKeyword(String keyword);

    //mettre a jour un acte
    Actedto updateActe(Actedto actedto);


    byte[] exportReport(Actedto actedto) throws FileNotFoundException, JRException;
}
