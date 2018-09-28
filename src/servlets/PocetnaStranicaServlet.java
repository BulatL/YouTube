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

import dao.KorisnikDAO;
import dao.VideoDAO;
import model.Korisnik;
import model.Korisnik.VrstaKorisnika;
import model.Video;

public class PocetnaStranicaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public PocetnaStranicaServlet() {
        super();
       
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			Collection<Video> listaPopularnihVidea = new ArrayList<>();
			Collection<Video> listaSvihVidea = new ArrayList<>();
			Collection<Korisnik> listaKorisnika = new ArrayList<>();

			if(ulogovaniKorisnik != null){
				if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.ADMIN){
					System.out.println("admin je ulogovan");
					listaPopularnihVidea = VideoDAO.getVideosForPocetnaStranica("BrojPregleda", "desc", "ADMIN", ulogovaniKorisnik.getId(), 5);
					listaSvihVidea = VideoDAO.getVideosForPocetnaStranica("DatumKreiranja", "desc", "ADMIN", ulogovaniKorisnik.getId(), 0);
				}
				else{
					listaPopularnihVidea = VideoDAO.getVideosForPocetnaStranica("BrojPregleda", "desc", "USER", ulogovaniKorisnik.getId(), 5);
					listaSvihVidea = VideoDAO.getVideosForPocetnaStranica("DatumKreiranja", "desc", "USER", ulogovaniKorisnik.getId(), 0);
				}
				
			}else if(ulogovaniKorisnik == null){
				listaPopularnihVidea = VideoDAO.getVideosForPocetnaStranica("BrojPregleda", "desc", "NOUSER", 0, 5);
				listaSvihVidea = VideoDAO.getVideosForPocetnaStranica("DatumKreiranja", "desc", "NOUSER", 0, 0);
				
			}
			
			listaKorisnika = KorisnikDAO.get5MostPopularUsers();
			
			Map<String, Object> data = new HashMap<>();
			data.put("listaPopularnihVidea", listaPopularnihVidea);
			data.put("listaSvihVidea", listaSvihVidea);
			data.put("ulogovaniKorisnik", ulogovaniKorisnik);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);
			//System.out.println("Zavrseno ucitavanje video: " + jsonData);

			response.setContentType("application/json");
			response.getWriter().write(jsonData);
			
		} catch (Exception ex) {
			ex.getMessage();
			ex.getStackTrace();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
