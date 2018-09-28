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
import dao.PratiociDAO;
import dao.VideoDAO;
import model.Korisnik;
import model.Video;
import model.Korisnik.VrstaKorisnika;


public class KanalStranicaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public KanalStranicaServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String status = "failed";
			String poruka = "user is not found";
			boolean provera = false;
			boolean blokiran = false;
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			
			String korisnickoIme = request.getParameter("korisnickoIme");
			Korisnik vlasnikKanala = KorisnikDAO.getByKorisnickoIme(korisnickoIme);
			
			Collection<Video> listaVidea = new ArrayList<>();			
			Collection<Video> lajkovaniVidei = new ArrayList<>();
			
			Integer brojPratioca = 0;
			
			if(vlasnikKanala!= null) {
				status = "success";
				if(ulogovaniKorisnik!= null) {
					if(vlasnikKanala.getId()!= ulogovaniKorisnik.getId()) {
						provera = PratiociDAO.provera(ulogovaniKorisnik.getId(), vlasnikKanala.getId());
					}
					if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.ADMIN) {
						listaVidea = VideoDAO.getVideosByKorisnik(vlasnikKanala, "ADMIN", ulogovaniKorisnik.getId());
						lajkovaniVidei = VideoDAO.getLikedVideosByKorisnik(vlasnikKanala, "ADMIN", ulogovaniKorisnik.getId());
						
					}else if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.USER) {
						listaVidea = VideoDAO.getVideosByKorisnik(vlasnikKanala, "USER", ulogovaniKorisnik.getId());
						lajkovaniVidei = VideoDAO.getLikedVideosByKorisnik(vlasnikKanala, "USER", ulogovaniKorisnik.getId());
						
					}
				}else {
					listaVidea = VideoDAO.getVideosByKorisnik(vlasnikKanala, "NOUSER", 0);
					lajkovaniVidei = VideoDAO.getLikedVideosByKorisnik(vlasnikKanala, "NOUSER", 0);
				}
				brojPratioca = PratiociDAO.brojPratioca(vlasnikKanala.getId());
				blokiran = vlasnikKanala.getBlokiran();
				if(blokiran== true) {
					poruka = "user is blocked";
				}
				
			}
			
			Map<String, Object> data = new HashMap<>();

			data.put("blokiran", blokiran);
			data.put("poruka", poruka);
			data.put("vlasnikKanala", vlasnikKanala);
			data.put("ulogovaniKorisnik", ulogovaniKorisnik);
			data.put("listaVidea", listaVidea);
			data.put("lajkovaniVidei", lajkovaniVidei);	
			data.put("brojPratioca", brojPratioca);
			data.put("provera", provera);
			data.put("status", status);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);
			System.out.println("Zavrseno ucitavanje kanal Strranica: " + jsonData);

			response.setContentType("application/json");
			response.getWriter().write(jsonData);
			
		} catch (Exception ex) {
			ex.getMessage();
			ex.getStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			Boolean alreadyAdmin = false;
			
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			
			int korisnikId = Integer.parseInt(request.getParameter("korisnikId"));
			String akcija = request.getParameter("action");
			Korisnik korisnik = KorisnikDAO.getById(korisnikId);
			Korisnik k = new Korisnik();
			String status = "fail";
			String poruka = "";
			
			if(ulogovaniKorisnik.blokiran== true) {
				poruka="You'r acount is blocked";
				
				if(akcija.equals("unblock")) {
					KorisnikDAO.blockUnblock("unblock", korisnikId);
					status = "success";
				}
			}else {
				if(akcija.equals("promote")) {
					if(korisnik.vrstaKorisnika == VrstaKorisnika.ADMIN) {
						alreadyAdmin = true;
					}else
					KorisnikDAO.promoteToAdmin(korisnikId);
					status = "success";
					
				}else if(akcija.equals("block")) {
					
					KorisnikDAO.blockUnblock("block", korisnikId);
					status = "success";
					
				}else if(akcija.equals("unblock")) {
					KorisnikDAO.blockUnblock("unblock", korisnikId);
					status = "success";
				}
				else if(akcija.equals("edit")) {
					
					String ime = request.getParameter("ime");
					String prezime = request.getParameter("prezime");
					String lozinka = request.getParameter("lozinka");
					String opis = request.getParameter("opis");
					
					korisnik.setIme(ime);
					korisnik.setPrezime(prezime);
					korisnik.setLozinka(lozinka);
					korisnik.setOpisKanala(opis);
					
					boolean provera = KorisnikDAO.update(korisnik);
					if(provera == true) {
						status = "success";
						k= KorisnikDAO.getById(korisnikId);
					}
				}
			}

			Map<String, Object> data = new HashMap<>();
			data.put("ulogovaniKorisnik", ulogovaniKorisnik);
			data.put("alreadyAdmin", alreadyAdmin);
			data.put("korisnik", k);
			data.put("status", status);
			data.put("poruka", poruka);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);

			response.setContentType("application/json");
			response.getWriter().write(jsonData);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
