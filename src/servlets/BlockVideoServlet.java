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

import dao.VideoDAO;
import model.Korisnik;
import model.Korisnik.VrstaKorisnika;
import model.Video;


public class BlockVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public BlockVideoServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			String status = "fail";
			int id = Integer.parseInt(request.getParameter("id"));
			String akcija = request.getParameter("action");
			
			if(ulogovaniKorisnik!= null) {
				if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.ADMIN) {
					if(akcija.equals("block")) {
						VideoDAO.block(1, id);
						status = "success";
					}else {
						VideoDAO.block(0, id);
						status = "success";
					}
				}
			}
			
			Video video = VideoDAO.getById(id);
			Map<String, Object> data = new HashMap<>();
			data.put("status", status);
			data.put("video", video);
			data.put("ulogovaniKorisnik", ulogovaniKorisnik);
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);
			response.setContentType("application/json");
			response.getWriter().write(jsonData);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
