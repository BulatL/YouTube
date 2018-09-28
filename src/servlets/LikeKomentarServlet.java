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

import dao.KomentarDAO;
import dao.LikeKomentarDAO;
import model.Korisnik;


public class LikeKomentarServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public LikeKomentarServlet() {
        super();
      
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
try {
			
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			int komentarId = Integer.parseInt(request.getParameter("id"));
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
						boolean provera = LikeKomentarDAO.provera(komentarId, ulogovaniKorisnik.getId(), likeDislike);
						if(provera == false) {
							LikeKomentarDAO.add(true, ulogovaniKorisnik.getId(), komentarId);
							KomentarDAO.updateBrLajkova(komentarId, likeDislike);
							status ="success";
						}else {
							poruka="U already liked this video";
						}
					}
					if(likeDislike.equals("dislike")) {
						boolean provera = LikeKomentarDAO.provera(komentarId, ulogovaniKorisnik.getId(), likeDislike);
						if(provera == false) {
							LikeKomentarDAO.add(false, ulogovaniKorisnik.getId(), komentarId);
							KomentarDAO.updateBrLajkova(komentarId, likeDislike);
							status ="success";
						}
						poruka="U already disliked this video";
					}
				}
			}
			Map<String, Object> data = new HashMap<>();
			data.put("status", status);
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
