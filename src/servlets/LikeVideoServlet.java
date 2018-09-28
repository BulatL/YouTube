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

import dao.LikeVideoDAO;
import dao.VideoDAO;
import model.Korisnik;
import model.Video;

public class LikeVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public LikeVideoServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			int videoId = Integer.parseInt(request.getParameter("id"));
			String likeDislike = request.getParameter("action");
			String status = "fail";
			String poruka = "";
			
			if(ulogovaniKorisnik == null) {
				poruka = "U need to login first";
			}else {
				if(ulogovaniKorisnik.blokiran== true) {
					poruka="You'r acount is blocked";
				}else {
					if(likeDislike.equals("like")) {
						boolean provera = LikeVideoDAO.provera(videoId, ulogovaniKorisnik.getId(), likeDislike);
						if(provera == false) {
							LikeVideoDAO.add(true, ulogovaniKorisnik.getId(), videoId);
							VideoDAO.updateBrLajkova(videoId, likeDislike);
							status ="success";
						}else {
							poruka="You already liked this video";
						}
					}
					if(likeDislike.equals("dislike")) {
						boolean provera = LikeVideoDAO.provera(videoId, ulogovaniKorisnik.getId(), likeDislike);
						System.out.println("provera je ? " + provera);
						if(provera == false) {
							LikeVideoDAO.add(false, ulogovaniKorisnik.getId(), videoId);
							VideoDAO.updateBrLajkova(videoId, likeDislike);
							status ="success";
						}
						poruka="You already disliked this video";
					}
				}
			}
			Video video = VideoDAO.getById(videoId);
			Map<String, Object> data = new HashMap<>();
			data.put("status", status);
			data.put("video", video);
			data.put("poruka", poruka);
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
