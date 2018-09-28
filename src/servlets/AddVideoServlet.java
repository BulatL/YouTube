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
import model.Video;
import model.Video.Vidljivost;


public class AddVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public AddVideoServlet() {
        super();
       
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			
			Map<String, Object> data = new HashMap<>();
			data.put("ulogovaniKorisnik", ulogovaniKorisnik);
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);
			response.setContentType("application/json");
			response.getWriter().write(jsonData);
		} catch (Exception e) {
			
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			String status = "fail";
			String poruka = "";
			
			if(ulogovaniKorisnik.blokiran== true) {
				poruka="You'r acount is blocked";
			}else {
				if(ulogovaniKorisnik.blokiran == false) {
					String url = request.getParameter("url");
					String naziv = request.getParameter("naziv");
					String opis = request.getParameter("opis");
					String vidljivost = request.getParameter("vidljivost");
					boolean dozvoliKomentare = Boolean.valueOf(request.getParameter("dozvoliKomentare"));
					boolean dozvoliRaiting = Boolean.valueOf(request.getParameter("dozvoliRaiting"));
					
					Vidljivost v = Vidljivost.valueOf(vidljivost);
					Video video = new Video();
					video.setVideoUrl(url);
					video.setNaziv(naziv);
					video.setOpis(opis);
					video.setKomentar(dozvoliKomentare);
					video.setRejting(dozvoliRaiting);
					video.setVidljivost(v);
					video.setSlika("https://www.w3schools.com/w3css/img_forest.jpg");
					video.setVlasnik(ulogovaniKorisnik);
					
					boolean provera = VideoDAO.add(video);
					if(provera == true) status = "success";
				}
			}
			
			Map<String, Object> data = new HashMap<>();
			data.put("status", status);
			data.put("poruka", poruka);
			data.put("ulogovaniKorisnik", ulogovaniKorisnik);
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);
			response.setContentType("application/json");
			response.getWriter().write(jsonData);
		} catch (Exception e) {
			
		}
	}

}
