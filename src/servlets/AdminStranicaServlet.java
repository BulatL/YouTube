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
import dao.LikeKomentarDAO;
import dao.LikeVideoDAO;
import dao.PratiociDAO;
import dao.VideoDAO;
import model.Korisnik;
import model.Korisnik.VrstaKorisnika;


public class AdminStranicaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public AdminStranicaServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Collection<Korisnik> listaKorisnika = new ArrayList<>();
			
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");

			boolean prvaPretraga = Boolean.valueOf(request.getParameter("prvaPretraga"));
			
			if(prvaPretraga == true) {
				listaKorisnika = KorisnikDAO.adminStranicaSearch("", "KorisnickoIme", "KorisnickoIme", "asc");
			}else {
				String search = request.getParameter("searchText");
				String searchBy = request.getParameter("searchBy");
				String sortBy = request.getParameter("orderBy");
				String ascDesc = request.getParameter("ascDesc");
				listaKorisnika = KorisnikDAO.adminStranicaSearch(search, searchBy, sortBy, ascDesc);
			}

			Map<String, Object> data = new HashMap<>();
			data.put("listaKorisnika", listaKorisnika);
			data.put("ulogovaniKorisnik", ulogovaniKorisnik);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);

			response.setContentType("application/json");
			response.getWriter().write(jsonData);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			Collection<Korisnik> listaKorisnika = new ArrayList<>();
			Boolean alreadyAdmin = false;
			
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			
			int korisnikId = Integer.parseInt(request.getParameter("korisnikId"));
			String akcija = request.getParameter("action");
			
			if(akcija.equals("promote")) {
				Korisnik korisnik = KorisnikDAO.getById(korisnikId);
				if(korisnik.vrstaKorisnika == VrstaKorisnika.ADMIN) {
					alreadyAdmin = true;
				}else
				KorisnikDAO.promoteToAdmin(korisnikId);
			}else if(akcija.equals("block")) {
				KorisnikDAO.blockUnblock("block", korisnikId);
			}else if(akcija.equals("unblock")) {
				KorisnikDAO.blockUnblock("unblock", korisnikId);
			}

			listaKorisnika = KorisnikDAO.getAdminServlet();
			Map<String, Object> data = new HashMap<>();
			data.put("listaKorisnika", listaKorisnika);
			data.put("ulogovaniKorisnik", ulogovaniKorisnik);
			data.put("alreadyAdmin", alreadyAdmin);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);

			response.setContentType("application/json");
			response.getWriter().write(jsonData);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
