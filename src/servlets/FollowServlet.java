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
import dao.PratiociDAO;
import model.Korisnik;


public class FollowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public FollowServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			String status = "fail";
			String poruka = "";
			
			if(ulogovaniKorisnik.blokiran== true) {
				poruka="You'r acount is blocked";
			}else {
				status = "success";
				String followUnfollow = request.getParameter("followUnfollow");
				Integer id = Integer.parseInt(request.getParameter("id"));
				if(followUnfollow.equals("follow")) {
					PratiociDAO.add(ulogovaniKorisnik.getId(), id);
				}else if(followUnfollow.equals("unfollow")) {
					PratiociDAO.deleteKoPratiKoga(ulogovaniKorisnik.getId(), id);
				}
			}
			
			Map<String, Object> data = new HashMap<>();
			data.put("ulogovaniKorisnik", ulogovaniKorisnik);
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
