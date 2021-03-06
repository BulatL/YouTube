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
import dao.LikeKomentarDAO;
import dao.LikeVideoDAO;
import dao.VideoDAO;
import model.Komentar;
import model.Korisnik;
import model.Korisnik.VrstaKorisnika;

public class DeleteVideoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public DeleteVideoServlet() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Integer id =Integer.parseInt(request.getParameter("id"));
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			String status = "fail";
			String poruka = "";
			
			if(ulogovaniKorisnik!= null) {
				if(ulogovaniKorisnik.blokiran== true) {
					poruka="You'r acount is blocked";
				}else {
					if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.ADMIN) {
	
						Collection<Komentar> komentarIdByVideoId = new ArrayList<>();
						komentarIdByVideoId = KomentarDAO.getKomentarByVideoId(id, "ADMIN", ulogovaniKorisnik.getId());
	
						LikeVideoDAO.deleteAdminbyVideoId(id);
	
					    for (Komentar i : komentarIdByVideoId) {
							LikeKomentarDAO.deleteAdminByKomentarID(i.getId());
						}
	
					    KomentarDAO.deleteByVideoIdAdmin(id);
	
						VideoDAO.deleteAdmin(id);
						status="success";
					}else {
						VideoDAO.delete(id);
						status="success";
					}
				}
			}else poruka = "U need to login first";
			
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

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
