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
import dao.VideoDAO;
import model.Komentar;
import model.Korisnik;
import model.Korisnik.VrstaKorisnika;
import model.Video;


public class VideoStranicaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public VideoStranicaServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
					
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			String status = "fail";
			Collection<Komentar> komentari = new ArrayList<>();
			
			int videoId = Integer.parseInt(request.getParameter("videoId"));
			Video video = VideoDAO.getById(videoId);
			if(video != null) status = "success";
			if(ulogovaniKorisnik!= null) {
				if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.ADMIN) {
					komentari = KomentarDAO.getKomentarByVideoId(videoId, "ADMIN", ulogovaniKorisnik.getId());
				}else {
					komentari = KomentarDAO.getKomentarByVideoId(videoId, "USER", ulogovaniKorisnik.getId());
				}
			}else {
				komentari = KomentarDAO.getKomentarByVideoId(videoId,"NOUSER", 0);
			}
			
			for (Komentar komentar : komentari) {
				System.out.println(komentar);
			}
			
			VideoDAO.updateBrPregleda(video);
			video.setBrojPregleda(video.brojPregleda+1);
			Map<String, Object> data = new HashMap<>();
			data.put("status", status);
			data.put("video", video);
			data.put("komentari", komentari);
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
		try {
			
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			String status = "fail";
			String poruka = "";
			int videoId = Integer.parseInt(request.getParameter("videoId"));
			Video video = VideoDAO.getById(videoId);
			if(ulogovaniKorisnik.blokiran== true) {
				poruka="You'r acount is blocked";
			}else {
				if(video != null) { 
					status = "success";
					String videoNaziv = request.getParameter("videoNaziv");
					String videoOpis = request.getParameter("videoOpis");
					boolean dozvoliKomentare = Boolean.valueOf(request.getParameter("dozvoliKomentare"));
					boolean dozvoliRejting = Boolean.valueOf(request.getParameter("dozvoliRejting"));
					
					video.setNaziv(videoNaziv);
					video.setOpis(videoOpis);
					video.setKomentar(dozvoliKomentare);
					video.setRejting(dozvoliRejting);
					System.out.println(video);
					
					VideoDAO.update(video);
				}
			}
			
			Map<String, Object> data = new HashMap<>();
			data.put("poruka", poruka);
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
