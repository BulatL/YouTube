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
import model.Komentar;
import model.Korisnik;


public class EditKomentarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public EditKomentarServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			
			Collection<Komentar> komentari = new ArrayList<>();
			
			String komentarSadrzaj = request.getParameter("komentarSadrzaj");
			int id = Integer.parseInt(request.getParameter("id"));
			String status = "fail";
			String akcija = request.getParameter("akcija");
			int lastId = 0;
			String poruka= "";
			
			if(ulogovaniKorisnik.blokiran== true) {
				poruka="You'r acount is blocked";
			}else {
				if(akcija.equals("edit")) {
					boolean uspesno = KomentarDAO.update(komentarSadrzaj, id);
					if(uspesno == true) status = "success";
				}
				else if(akcija.equals("add")){
					boolean uspesno = KomentarDAO.add(komentarSadrzaj, ulogovaniKorisnik.getId(), id);
					if(uspesno == true) {
						status = "success";
						komentari = KomentarDAO.getByVideo(id);
						lastId = KomentarDAO.getLastId();
					}
				}
			}
			Map<String, Object> data = new HashMap<>();
			data.put("status", status);
			data.put("komentari", komentari);
			data.put("lastId", lastId);
			data.put("poruka", poruka);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);
			
			response.setContentType("application/json");
			response.getWriter().write(jsonData);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
