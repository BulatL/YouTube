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


public class LoginStranicaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public LoginStranicaServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try { 
			String status = "failed";
			Korisnik korisnik = new Korisnik();
			String korisnickoIme = request.getParameter("userName");
			String lozinka = request.getParameter("password");
			korisnik = KorisnikDAO.login(korisnickoIme, lozinka);
			
			
			if (korisnik != null) { 
				HttpSession session = request.getSession(true); 
				session.setAttribute("ulogovaniKorisnik",korisnik);
				status = "succes";
			} 
			
			Map<String, Object> data = new HashMap<>();
			data.put("status", status);
			ObjectMapper mapper = new ObjectMapper();
			String jsonData = mapper.writeValueAsString(data);

			response.setContentType("application/json");
			response.getWriter().write(jsonData);
		} catch (Throwable theException) { System.out.println(theException); } 
	}

}
