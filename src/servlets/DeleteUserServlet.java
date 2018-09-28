package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.KomentarDAO;
import dao.KorisnikDAO;
import dao.LikeKomentarDAO;
import dao.LikeVideoDAO;
import dao.PratiociDAO;
import dao.VideoDAO;
import model.Korisnik;
import model.Korisnik.VrstaKorisnika;


public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public DeleteUserServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			Integer korisnikId =Integer.parseInt(request.getParameter("id"));
			HttpSession session = request.getSession();
			Korisnik ulogovaniKorisnik = (Korisnik) session.getAttribute("ulogovaniKorisnik");
			
			if(ulogovaniKorisnik!= null) {
				
				if(ulogovaniKorisnik.getId() == korisnikId) {
					request.getSession().invalidate();
				}
				if(ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.ADMIN) {
					Collection<Integer> videoIdByVlasnikId = new ArrayList<>();
					Collection<Integer> komentarIdByVlasnikId = new ArrayList<>();
					Collection<Integer> komentarIdByVideoId = new ArrayList<>();
					
					videoIdByVlasnikId = VideoDAO.getByVlasnikId(korisnikId);
					komentarIdByVlasnikId = KomentarDAO.getByVlasnikId(korisnikId);
					
					for (int i : videoIdByVlasnikId) {
						komentarIdByVideoId.addAll(KomentarDAO.getByVideoId(i));
					}
					
					PratiociDAO.deleteKoPrati(korisnikId);
					PratiociDAO.deleteKogaPratiUser(korisnikId);
				    
				    LikeVideoDAO.adminDeleteByVlasnikId(korisnikId);
				    for (int i : videoIdByVlasnikId) {
						LikeVideoDAO.deleteAdminbyVideoId(i);
					}
				    LikeKomentarDAO.deleteAdmin(korisnikId);
				    for (int i : komentarIdByVlasnikId) {
						LikeKomentarDAO.deleteAdminByKomentarID(i);
					}
				    for (int i : komentarIdByVideoId) {
						LikeKomentarDAO.deleteAdminByKomentarID(i);
					}
				    
				    
				    KomentarDAO.deleteByUserIdAdmin(korisnikId);
				    for (int i : videoIdByVlasnikId) {
						KomentarDAO.deleteByVideoIdAdmin(i);
					}
				    
				    VideoDAO.deleteByVlasnikIdAdmin(korisnikId);
					KorisnikDAO.deleteAdmin(korisnikId);
				}
				else if (ulogovaniKorisnik.vrstaKorisnika == VrstaKorisnika.USER) {
					KorisnikDAO.delete(korisnikId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
