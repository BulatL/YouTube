package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.VideoDAO;
import model.Komentar;
import model.Korisnik;
import model.Korisnik.VrstaKorisnika;
import model.Video;


public class PretragaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public PretragaServlet() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Collection<Video> listaVidea = new ArrayList<>();
			Collection<Komentar> listaKomentara = new ArrayList<>();
			Collection<Korisnik> listaKorisnika = new ArrayList<>();
			
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			
			String pretraga = request.getParameter("pretraga");
			Boolean prvaPretraga = Boolean.valueOf(request.getParameter("prvaPretraga"));
			Boolean videoPretraga = Boolean.valueOf(request.getParameter("videoPretraga"));
			Boolean korisnikPretraga = Boolean.valueOf(request.getParameter("korisnikPretraga"));
			Boolean komentarPretraga = Boolean.valueOf(request.getParameter("komentarPretraga"));
			
			System.out.println("video pretraga " + videoPretraga);
			System.out.println("korisnik pretraga " + korisnikPretraga);
			System.out.println("komentar pretraga " + komentarPretraga);
			
			String sortVideo = request.getParameter("sortVideo");
			String sortKorisnik = request.getParameter("sortKorisnik");
			String sortKomentar = request.getParameter("sortKomentar");
			
			String ascDesc = request.getParameter("ascDesc");
			
			if(prvaPretraga==true) {
				if(ulogovaniKorisnik!= null) {
					if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.ADMIN){
						listaVidea = VideoDAO.SearchVideo(pretraga, "DatumKreiranja", "asc", "ADMIN", ulogovaniKorisnik.getId());
						listaKorisnika = KorisnikDAO.Search(pretraga, "KorisnickoIme", "DatumKreiranja", "asc", "ADMIN", ulogovaniKorisnik.getId());
						listaKomentara = KomentarDAO.getBySadrzajSearch(pretraga, "DatumKreiranja", "asc", "ADMIN", ulogovaniKorisnik.getId());
						
					}else {
						listaVidea = VideoDAO.SearchVideo(pretraga, "DatumKreiranja", "asc", "USER", ulogovaniKorisnik.getId());
						listaKorisnika = KorisnikDAO.Search(pretraga, "KorisnickoIme", "DatumKreiranja", "asc", "USER", ulogovaniKorisnik.getId());
						listaKomentara = KomentarDAO.getBySadrzajSearch(pretraga, "DatumKreiranja", "asc", "USER", ulogovaniKorisnik.getId());
					}
				}else {
					listaVidea = VideoDAO.SearchVideo(pretraga, "DatumKreiranja", "asc", "NOUSER", 0);
					listaKorisnika = KorisnikDAO.Search(pretraga, "KorisnickoIme", "DatumKreiranja", "asc", "NOUSER", 0);
					listaKomentara = KomentarDAO.getBySadrzajSearch(pretraga, "DatumKreiranja", "asc", "NOUSER", 0);
				}
			}else {
				if(videoPretraga == true) {
					if(ulogovaniKorisnik != null) {
						if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.ADMIN){
							listaVidea = VideoDAO.SearchVideo(pretraga, sortVideo, ascDesc, "ADMIN", ulogovaniKorisnik.getId());
							
						}else {
							listaVidea = VideoDAO.SearchVideo(pretraga, sortVideo, ascDesc, "USER", ulogovaniKorisnik.getId());
						}
					}
					else {
						listaVidea = VideoDAO.SearchVideo(pretraga, sortVideo, ascDesc, "NOUSER", 0);
					}
				}
				if(korisnikPretraga == true) {
					if(ulogovaniKorisnik != null) {
						if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.ADMIN){
							listaKorisnika = KorisnikDAO.Search(pretraga, "KorisnickoIme", sortKorisnik, ascDesc, "ADMIN", ulogovaniKorisnik.getId());
							
						}else {
							listaKorisnika = KorisnikDAO.Search(pretraga, "KorisnickoIme", sortKorisnik, ascDesc, "USER", ulogovaniKorisnik.getId());
						}
					}
					else {
						listaKorisnika = KorisnikDAO.Search(pretraga, "KorisnickoIme", sortKorisnik, ascDesc, "NOUSER", 0);
					}
				}
				if(komentarPretraga == true) {
					if(ulogovaniKorisnik != null) {
						if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.ADMIN){
							listaKomentara = KomentarDAO.getBySadrzajSearch(pretraga, sortKomentar, ascDesc, "ADMIN", ulogovaniKorisnik.getId());
							
						}else {
							listaKomentara = KomentarDAO.getBySadrzajSearch(pretraga, sortKomentar, ascDesc, "USER", ulogovaniKorisnik.getId());
						}
					}
					else {
						listaKomentara = KomentarDAO.getBySadrzajSearch(pretraga, sortKomentar, ascDesc, "NOUSER", 0);
					}
				}
			}

			Map<String, Object> data = new HashMap<>();
			data.put("listaVidea", listaVidea);
			data.put("listaKomentara", listaKomentara);
			data.put("listaKorisnika", listaKorisnika);
			data.put("ulogovaniKorisnik", ulogovaniKorisnik);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);

			response.setContentType("application/json");
			response.getWriter().write(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
