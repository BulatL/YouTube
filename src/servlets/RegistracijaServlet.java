package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.KorisnikDAO;
import model.Korisnik;
import model.Korisnik.VrstaKorisnika;


public class RegistracijaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public RegistracijaServlet() {
        super();
        
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try { 
			String status = "failed";
			Korisnik korisnik = new Korisnik();
			String korisnickoIme = request.getParameter("korisnickoIme");
			String lozinka = request.getParameter("lozinka");
			String ime = request.getParameter("ime");
			String prezime = request.getParameter("prezime");
			String opis = request.getParameter("opis");
			String email = request.getParameter("email");
			
			korisnik.setKorisnickoIme(korisnickoIme);
			korisnik.setLozinka(lozinka);
			korisnik.setIme(ime);
			korisnik.setPrezime(prezime);
			korisnik.setOpisKanala(opis);
			korisnik.setEmail(email);
			korisnik.setVrstaKorisnika(VrstaKorisnika.USER);
			korisnik.setBlokiran(false);
			korisnik.setObrisan(false);
			boolean provera = KorisnikDAO.add(korisnik);
			
			
			if (provera == true) { 
				HttpSession session = request.getSession(true); 
				session.setAttribute("ulogovaniKorisnik",korisnik);
				status = "succes";
				korisnik.setId(KorisnikDAO.getLastId());
			} 
			
			Map<String, Object> data = new HashMap<>();
			data.put("status", status);
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);

			response.setContentType("application/json");
			response.getWriter().write(jsonData);
			
		} catch (Throwable theException) {
			System.out.println(theException); 
			} 
	}

}
