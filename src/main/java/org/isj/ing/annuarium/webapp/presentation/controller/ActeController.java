package org.isj.ing.annuarium.webapp.presentation.controller;


import com.itextpdf.commons.utils.Base64;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.isj.ing.annuarium.webapp.model.dto.Actedto;
import org.isj.ing.annuarium.webapp.model.entities.User;
import org.isj.ing.annuarium.webapp.service.IActe;
import org.isj.ing.annuarium.webapp.service.ReportService;
import org.isj.ing.annuarium.webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j //utilises pour les logs, les traces de l'application
public class ActeController {

	@Autowired
	IActe iActe;
	@Autowired
	private ReportService service;
	@Autowired
	UserService userService;


	@GetMapping("/")
	public String pageAccueil(Model model) {
		final Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user!=null)
			model.addAttribute("userName", user.getName()+ " "+user.getLastName());
		else model.addAttribute("");

		return "index";
	}
	@GetMapping("/listactes")
	public String pageListActes(Model model) {
		final Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user!=null)
			model.addAttribute("userName", user.getName()+ " "+user.getLastName());
		else model.addAttribute("");
//appel de la couche service pour avoir la liste des actes dto
		List<Actedto> actedtos= iActe.ListActes();

				model.addAttribute("actedtos", actedtos);

		return "liste";
	}
	@GetMapping("/detail")
	public String pageDetail(@RequestParam(name = "numero") String numero, Model model) {
		final Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user!=null)
			model.addAttribute("userName", user.getName()+ " "+user.getLastName());
		else model.addAttribute("");
//appel de la couche service pour avoir la l'acte
		List<Actedto> actedtos= iActe.ListActes();
		Actedto actedto= iActe.searchActeByNumero(numero);

		model.addAttribute("actedto",  actedto);

		return "details";
	}

	@GetMapping("/supprimer")
	public String pageSupprimer(@RequestParam(name = "numero") String numero, Model model) {
		final Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user!=null)
			model.addAttribute("userName", user.getName()+ " "+user.getLastName());
		else model.addAttribute("");
//appel de la couche service pour supprimerla l'acte
		iActe.deleteActe(numero);

		return "redirect:/listactes";
	}
	//affichage du formulaire
	@GetMapping("/enregistreracteform")
	public String pageEnregistrerActeForm(Model model) {
		final Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user!=null)
			model.addAttribute("userName", user.getName()+ " "+user.getLastName());
		else model.addAttribute("");
		Actedto actedto= new Actedto();

		actedto.setNumero("CM");
		model.addAttribute("actedto",actedto);

		return "enregistrer";
	}
	// Traitemement des valeurs saisies dans le formulaire
	@PostMapping("/enregistreracte")
	public String enregistreracte(@ModelAttribute Actedto actedto,
									  Model model)  {

		ActeController.log.info("enregistrer-acte");
		// appel de la couche service ou metier injectée pour enregistrer un acte

		iActe.saveActe(actedto);

		return "redirect:/listactes";
	}


	@PostMapping("/recherche")
	public String pageRechercher( @RequestParam(value = "keyword") String keyword,
										  Model model) {
		final Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user!=null)
			model.addAttribute("userName", user.getName()+ " "+user.getLastName());
		else model.addAttribute("");

		List<Actedto> actedtos= iActe.searchActeByKeyword(keyword);

		model.addAttribute("actedtos",  actedtos);

		return "rechercher";
	}
	@GetMapping("/rechercherform")
	public String pageRechercher(
								  Model model) {
		final Authentication auth= SecurityContextHolder.getContext().getAuthentication();
		final User user=userService.findUserByEmail(auth.getName());
		if (user!=null)
			model.addAttribute("userName", user.getName()+ " "+user.getLastName());
		else model.addAttribute("");

		List<Actedto> actedtos= iActe.ListActes();

		model.addAttribute("actedtos", actedtos);

		return "rechercher";
	}
	@GetMapping("/demanderform")
	public String pageDemander(Model model) {

		return "demande";
	}

	//affichage du formulaire d'edition d'un acte
	@GetMapping("/editerform")
	public String pageEditerForm(@RequestParam(name = "numero") String numero,Model model) {

		Actedto actedto=iActe.searchActeByNumero(numero);

		model.addAttribute("actedto",actedto);

		return "editer";
	}


	// Traitemement des valeurs saisies dans le formulaire d'edition
	@PostMapping("/editeracte")
	public String editeracte(@ModelAttribute Actedto actedto,
								  Model model)  {

		ActeController.log.info("editer-acte");
		// appel de la couche service ou metier injectée pour enregistrer un acte

		iActe.updateActe(actedto);

		return "redirect:/listactes";
	}
@RequestMapping("/pdf")
	public void getReportPDF(HttpServletResponse response) throws JRException, IOException {
		InputStream jasperStream=(InputStream) this.getClass().getResourceAsStream("/acte6.jasper");

		Map<String,Object> parameters= new HashMap<>();
		parameters.put("createdBy","ISJ");
		final JRBeanCollectionDataSource source=new JRBeanCollectionDataSource(iActe.ListActes());
		JasperReport jasperReport=(JasperReport) JRLoader.loadObject(jasperStream);
		JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport, parameters, source);

		response.setContentType("application/x-pdf");
		response.setHeader("Content-disposition","inline; filename = acte6.pdf");

		final ServletOutputStream outputStream= response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);

	}

	@GetMapping("/telecharger")
	public ResponseEntity<byte[]> generatedReport(@RequestParam(value = "numero") String numero) throws JRException, FileNotFoundException {


		Actedto actedto= iActe.searchActeByNumero(numero);
		final byte[] data = iActe.exportReport(actedto);
		HttpHeaders httpHeaders=new HttpHeaders();
		httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION,"inline;filename=actepdf.pdf" );


		return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_PDF).body(data);

	}
}