package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators.FollowingIterator;

import model.Korisnik;
import model.Korisnik.VrstaKorisnika;
import model.Video;
import model.Video.Vidljivost;

public class KorisnikDAO {

	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Collection<Korisnik> adminStranicaSearch(String searchText, String searchBy, String orderBy, String ascDesc) {
		Collection<Korisnik> kolekcijaKorisnik = new ArrayList<>();
		Collection<Korisnik> kogaPrati = new ArrayList<>();
		Connection conn = ConnectionManager.getConnection();
		
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT Id, KorisnickoIme, Ime, Prezime, Email, VrstaKorisnika, Blokiran FROM Korisnik WHERE Obrisan = 0 AND";
			
			switch (searchBy) {
			case "KorisnickoIme":
				query += " KorisnickoIme like ?";
				break;

			case "Ime":
				query += " Ime like ?";
				break;
			case "Prezime":
				query += " Prezime like ?";
				break;
			case "Email":
				query += " Email like ?";
				break;
			case "VrstaKorisnika":
				query += " VrstaKorisnika like ?";
				break;
			}
			
			switch (orderBy) {
			case "KorisnickoIme":
				query += " ORDER BY KorisnickoIme";
				break;

			case "Ime":
				query += " ORDER BY Ime";
				break;
			case "Prezime":
				query += " ORDER BY Prezime";
				break;
			case "Email":
				query += " ORDER BY Email";
				break;
			case "VrstaKorisnika":
				query += " ORDER BY VrstaKorisnika";
				break;
			}
			
			switch (ascDesc) {
			case "asc":
				query += " asc;";
				break;

			case "desc":
				query += " desc;";
				break;
			}
			
			//System.out.println(query);
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, "%" + searchText + "%");
			//System.out.println(pstmt);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				String Ime = rset.getString(index++);
				String Prezime = rset.getString(index++);
				String Email = rset.getString(index++);
				VrstaKorisnika VrstaKorisnika = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				Boolean Blokiran = rset.getBoolean(index++);

				Korisnik korisnik = new Korisnik(Id, KorisnickoIme, null, Ime, Prezime, Email, null, null, 
						Blokiran, kogaPrati, null, null, null, null, 
						VrstaKorisnika, false);

				kolekcijaKorisnik.add(korisnik);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getAllFroAdminPage KorisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return kolekcijaKorisnik;
	}
	
	public static Collection<Korisnik> getAdminServlet() {
		Collection<Korisnik> kolekcijaKorisnik = new ArrayList<>();
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT Id, KorisnickoIme, Ime, Prezime, Email, "
					+ "VrstaKorisnika, Blokiran FROM Korisnik WHERE Obrisan = 0 "; 

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			while (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				String Ime = rset.getString(index++);
				String Prezime = rset.getString(index++);
				String Email = rset.getString(index++);
				VrstaKorisnika VrstaKorisnika = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				Boolean Blokiran = rset.getBoolean(index++);

				Korisnik korisnik = new Korisnik(Id, KorisnickoIme, null, Ime, Prezime, Email, null, null, 
						Blokiran, null, null, null, null, null, 
						VrstaKorisnika, false);
			
				kolekcijaKorisnik.add(korisnik);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getAdminServlet KorisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return kolekcijaKorisnik;
	}
	
	public static Collection<Korisnik> Search(String parametar, String searchBy, String orderBy, String ascDesc, String vrstaKorisnika, int idKorisnika) {
		Collection<Korisnik> kolekcijaKorisnik = new ArrayList<>();
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
				
			String query = "SELECT Id, KorisnickoIme, Ime, Prezime, Email, "
					+ "VrstaKorisnika, Blokiran FROM Korisnik WHERE Obrisan = 0 "
					+ "AND"; 
			
			switch (searchBy) {
			case "KorisnickoIme":
				query += " KorisnickoIme like ? ";
				break;

			case "Ime":
				query += " Ime like ? ";
				break;
			case "Prezime":
				query += " Prezime like ? ";
				break;
			case "Email":
				query += " Email like ? ";
				break;
			}
			switch (vrstaKorisnika) {
			case "ADMIN":
				query += " AND Blokiran IN (0,1) "; 
				break;

			case "USER":
				query += " AND (Blokiran = 0 OR Id = ?) ";
				break;
				
			case "NOUSER":
				query += " AND Blokiran = 0 ";
				break;
			}
			
			switch (orderBy) {
			case "KorisnickoIme":
				query += " ORDER BY KorisnickoIme ";
				break;

			case "Ime":
				query += " ORDER BY Ime ";
				break;
			case "Prezime":
				query += " ORDER BY Prezime ";
				break;
			case "Email":
				query += " ORDER BY Email ";
				break;
			case "VrstaKorisnika":
				query += " ORDER BY VrstaKorisnika ";
				break;
			case "DatumKreiranja":
				query += " ORDER BY Datum ";
				break;
			}
			
			
			
			switch (ascDesc) {
			case "asc":
				
				query += "asc";
				
				break;
				
			case "desc":
				
				query += "desc";
				
				break;

			}
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,"%" +parametar+"%");
			if(vrstaKorisnika.equals("USER")) pstmt.setInt(2, idKorisnika);
			System.out.println("dao za korisnika");
			System.out.println(pstmt);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				String Ime = rset.getString(index++);
				String Prezime = rset.getString(index++);
				String Email = rset.getString(index++);
				VrstaKorisnika VrstaKorisnika = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				Boolean Blokiran = rset.getBoolean(index++);

				Korisnik korisnik = new Korisnik(Id, KorisnickoIme, null, Ime, Prezime, Email, null, null, 
						Blokiran, null, null, null, null, null, 
						VrstaKorisnika, false);;
			
				kolekcijaKorisnik.add(korisnik);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! Search KorisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return kolekcijaKorisnik;
	}
	
	
	
	public static Collection<Korisnik> get5MostPopularUsers() {
		Collection<Korisnik> kolekcijaKorisnik = new ArrayList<>();
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT DISTINCT Id, KorisnickoIme, Ime, Prezime, Email, "
					+ "VrstaKorisnika, Blokiran FROM Korisnik k LEFT JOIN Pratioci p "
					+ "ON k.Id = p.KogaPrati "
					+ "WHERE Obrisan = 0 "
					+ "GROUP BY Id, KorisnickoIme, Ime, Prezime, Email, "
					+ "VrstaKorisnika, Blokiran "
					+ "ORDER BY COUNT(p.KogaPrati) DESC LIMIT 5;"; 

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			while (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				String Ime = rset.getString(index++);
				String Prezime = rset.getString(index++);
				String Email = rset.getString(index++);
				VrstaKorisnika VrstaKorisnika = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				Boolean Blokiran = rset.getBoolean(index++);

				Korisnik korisnik = new Korisnik(Id, KorisnickoIme, null, Ime, Prezime, Email, null, null, 
						Blokiran, null, null, null, null, null, 
						VrstaKorisnika, false);
			
				kolekcijaKorisnik.add(korisnik);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! get5MostPopularUsers KorisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return kolekcijaKorisnik;
	}
	
	public static Collection<Korisnik> getByKorisnickoImeSearch(String korisnickoIme, String ascDesc) {
		Collection<Korisnik> kolekcijaKorisnik = new ArrayList<>();
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			String query = "SELECT Id, KorisnickoIme, Ime, Prezime, Email, "
					+ "VrstaKorisnika, Blokiran FROM Korisnik WHERE Obrisan = 0 "
					+ "AND KorisnickoIme like ? ORDER BY KorisnickoIme "; 
			
			switch (ascDesc) {
			case "asc":
				
				query += "asc";
				
				break;
				
			case "desc":
				
				query += "desc";
				
				break;

			}
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,"%" +korisnickoIme+"%");

			System.out.println("dao za korisnika");
			System.out.println(pstmt);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				String Ime = rset.getString(index++);
				String Prezime = rset.getString(index++);
				String Email = rset.getString(index++);
				VrstaKorisnika VrstaKorisnika = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				Boolean Blokiran = rset.getBoolean(index++);

				Korisnik korisnik = new Korisnik(Id, KorisnickoIme, null, Ime, Prezime, Email, null, null, 
						Blokiran, null, null, null, null, null, 
						VrstaKorisnika, false);;
			
				kolekcijaKorisnik.add(korisnik);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getbyKorisnickoImeSearch KorisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return kolekcijaKorisnik;
	}
	
	public static boolean getByKorisnickoImeRegistracijaProvera(String korisnickoIme) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT * FROM Korisnik WHERE Obrisan = 0 AND KorisnickoIme = ? ;"; 

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,korisnickoIme);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			while (rset.next()) {
		
				return true;
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getbyKorisnickoImeRegistracija");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return false;
	}
	
	
	
	public static Korisnik login(String korisnickoIme, String password) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			String query = "SELECT Id, KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, "+
			"Blokiran, VrstaKorisnika, Obrisan FROM youtube.korisnik WHERE KorisnickoIme = ? AND Password = ? "
			+ "AND Obrisan = 0"; 

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, korisnickoIme);
			pstmt.setString(2, password);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			if (rset.next()) {
				
				int index = 1;
				int Id = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				String Password = rset.getString(index++);
				String Ime = rset.getString(index++);
				String Prezime = rset.getString(index++);
				String Email = rset.getString(index++);
				String OpisKanala = rset.getString(index++);
				Date Datum = rset.getDate(index++);
				String datum =df.format(Datum);
				Boolean Blokiran = rset.getBoolean(index++);
				VrstaKorisnika VrstaKorisnika = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				Boolean Obrisan = rset.getBoolean(index++);
				
				Korisnik korisnik = new Korisnik(Id, KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, datum, 
						Blokiran, null, null, null, null, null, 
						VrstaKorisnika, Obrisan);
				
				korisnik.setKogaPratiKorisnik(following(Id));
				korisnik.setKoNjegaPrati(followers(Id));	
				
				return  korisnik;
			
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! login KorisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return null;
	}
	
	public static Korisnik getById(int id) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			String query = "SELECT Id, KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, "
					+ "Blokiran, VrstaKorisnika, Obrisan FROM youtube.korisnik WHERE Id = ? AND Obrisan = 0"; 

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();
			
			// citanje rezultata upita
			if (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				String Password = rset.getString(index++);
				String Ime = rset.getString(index++);
				String Prezime = rset.getString(index++);
				String Email = rset.getString(index++);
				String OpisKanala = rset.getString(index++);
				Date Datum = rset.getDate(index++);
				String datum =df.format(Datum);
				Boolean Blokiran = rset.getBoolean(index++);
				VrstaKorisnika VrstaKorisnika = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				Boolean Obrisan = rset.getBoolean(index++);
				
				Korisnik korisnik = new Korisnik(Id, KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, datum, 
						Blokiran, null, null, null, null, null, 
						VrstaKorisnika, Obrisan);
				
				return  korisnik;
			
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getById korisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return null;
	}
	
	public static int getLastId() {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			String query = "SELECT Id FROM youtube.korisnik ORDER BY Id DESC LIMIT 1"; 

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();
			
			// citanje rezultata upita
			if (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);

				return  Id;
			
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getLastId korisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return 0;
	}
	
	public static Korisnik getByKorisnickoIme(String korisnickoIme) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			String query = "SELECT Id, KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, "
					+ "Blokiran, VrstaKorisnika, Obrisan FROM youtube.korisnik WHERE KorisnickoIme = ? AND Obrisan = 0"; 

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, korisnickoIme);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();
			
			// citanje rezultata upita
			if (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				String Password = rset.getString(index++);
				String Ime = rset.getString(index++);
				String Prezime = rset.getString(index++);
				String Email = rset.getString(index++);
				String OpisKanala = rset.getString(index++);
				Date Datum = rset.getDate(index++);
				String datum =df.format(Datum);
				Boolean Blokiran = rset.getBoolean(index++);
				VrstaKorisnika VrstaKorisnika = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				Boolean Obrisan = rset.getBoolean(index++);
				
				Korisnik korisnik = new Korisnik(Id, KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, datum, 
						Blokiran, null, null, null, null, null, 
						VrstaKorisnika, Obrisan);
				
				korisnik.setKogaPratiKorisnik(following(Id));
				korisnik.setKoNjegaPrati(followers(Id));

				return  korisnik;
			
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getById korisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return null;
	}
	
	public static Collection<Korisnik> followers(int id){

		Connection conn = ConnectionManager.getConnection();
		Collection<Korisnik> pratioci = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT k.* FROM pratioci p inner join korisnik k on p.KoPrati = k.Id "
					+ "WHERE k.Obrisan = 0 AND KogaPrati  = ?;";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, id);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				int index2 = 1;
				int Idp = rset.getInt(index2++);
				String KorisnickoImep = rset.getString(index2++);
				String Passwordp = rset.getString(index2++);
				String Imep = rset.getString(index2++);
				String Prezimep = rset.getString(index2++);
				String Emailp = rset.getString(index2++);
				String OpisKanalap = rset.getString(index2++);
				Date Datump = rset.getDate(index2++);
				String datum =df.format(Datump);
				Boolean Blokiranp = rset.getBoolean(index2++);
				VrstaKorisnika VrstaKorisnikap = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index2++));
				Boolean Obrisanp = rset.getBoolean(index2++);
				
				Korisnik pratioc = new Korisnik(Idp, KorisnickoImep, Passwordp, Imep, Prezimep, Emailp, OpisKanalap, 
						datum, Blokiranp, null, null, null, null, null, VrstaKorisnikap, Obrisanp);

				pratioci.add(pratioc);
			
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! followers KorisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return pratioci;
	}
	
	public static Collection<Korisnik> following(int id){

		Connection conn = ConnectionManager.getConnection();
		Collection<Korisnik> pratioci = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT k.* FROM pratioci p inner join korisnik k on p.KogaPrati = k.Id "
					+ "WHERE k.Obrisan = 0 AND KoPrati  = ?;";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, id);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				int index2 = 1;
				int Idp = rset.getInt(index2++);
				String KorisnickoImep = rset.getString(index2++);
				String Passwordp = rset.getString(index2++);
				String Imep = rset.getString(index2++);
				String Prezimep = rset.getString(index2++);
				String Emailp = rset.getString(index2++);
				String OpisKanalap = rset.getString(index2++);
				Date Datump = rset.getDate(index2++);
				String datum =df.format(Datump);
				Boolean Blokiranp = rset.getBoolean(index2++);
				VrstaKorisnika VrstaKorisnikap = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index2++));
				Boolean Obrisanp = rset.getBoolean(index2++);
				
				Korisnik pratioc = new Korisnik(Idp, KorisnickoImep, Passwordp, Imep, Prezimep, Emailp, OpisKanalap, 
						datum, Blokiranp, null, null, null, null, null, VrstaKorisnikap, Obrisanp);

				pratioci.add(pratioc);
			
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! following KorisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return pratioci;
	}
	
	
	public static Korisnik getByIdForBlock(int id) {
		Connection conn = ConnectionManager.getConnection();
		Collection<Korisnik> kogaPrati = new ArrayList<>();
		Collection<Korisnik> koNjegaPrati = new ArrayList<>();
		Collection<Video> videolista = new ArrayList<>();
		Collection<Video> likeVIdeo = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			String query = "SELECT Id, KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, "
					+ "Blokiran, VrstaKorisnika, Obrisan FROM youtube.korisnik WHERE Id = ? AND Obrisan = 0"; 

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();
			
			// citanje rezultata upita
			if (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				String Password = rset.getString(index++);
				String Ime = rset.getString(index++);
				String Prezime = rset.getString(index++);
				String Email = rset.getString(index++);
				String OpisKanala = rset.getString(index++);
				Date Datum = rset.getDate(index++);
				String datum =df.format(Datum);
				Boolean Blokiran = rset.getBoolean(index++);
				VrstaKorisnika VrstaKorisnika = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				Boolean Obrisan = rset.getBoolean(index++);
				
				Korisnik korisnik = new Korisnik(Id, KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, datum, 
						Blokiran, kogaPrati, koNjegaPrati, videolista, likeVIdeo, null, 
						VrstaKorisnika, Obrisan);
				

				return  korisnik;
			
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getById korisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return null;
	}
	
	public static boolean add(Korisnik korisnik) {
		Connection conn = ConnectionManager.getConnection();
		String datum = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO korisnik (KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, Datum, "
					+ "Blokiran, VrstaKorisnika, Obrisan) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, korisnik.getKorisnickoIme());
			pstmt.setString(index++, korisnik.getLozinka());
			pstmt.setString(index++, korisnik.getIme());
			pstmt.setString(index++, korisnik.getPrezime());
			pstmt.setString(index++, korisnik.getEmail());
			pstmt.setString(index++, korisnik.getOpisKanala());
			pstmt.setString(index++, datum);
			pstmt.setBoolean(index++, korisnik.getBlokiran());
			pstmt.setString(index++, korisnik.getVrstaKorisnika().toString());
			pstmt.setBoolean(index++, korisnik.getObrisan());

			System.out.println(pstmt);
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! add KorisnikDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean update(Korisnik korisnik) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Korisnik SET KorisnickoIme = ?, Password = ?, Ime = ?, "+
						   "Prezime = ?, Email = ?, OpisKanala = ?, Blokiran = ?, "+
						   "VrstaKorisnika = ?, Obrisan = ? WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(10, korisnik.getId());
			int index = 1;
			pstmt.setString(index++, korisnik.getKorisnickoIme());
			pstmt.setString(index++, korisnik.getLozinka());
			pstmt.setString(index++, korisnik.getIme());
			pstmt.setString(index++, korisnik.getPrezime());
			pstmt.setString(index++, korisnik.getEmail());
			pstmt.setString(index++, korisnik.getOpisKanala());
			pstmt.setBoolean(index++, korisnik.getBlokiran());
			pstmt.setString(index++, korisnik.getVrstaKorisnika().toString());
			pstmt.setBoolean(index++, korisnik.getObrisan());

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! update KorisnikDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean promoteToAdmin(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Korisnik SET VrstaKorisnika = 'ADMIN' WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! promoteToAdmin KorisnikDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean blockUnblock(String blockUnblock, int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Korisnik SET Blokiran = ? WHERE Id = ?";

			int blokiran = 0;
			if(blockUnblock.equals("block"))blokiran = 1;
			else blokiran = 0;
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, blokiran);
			pstmt.setInt(2, id);
			
			
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! promoteToAdmin KorisnikDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean delete(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Korisnik SET Obrisan = 1 WHERE Id = ?;";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			
			System.out.println(pstmt);
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete KorisnikDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteAdmin(int id){

			Connection conn = ConnectionManager.getConnection();

			PreparedStatement pstmt = null;
			try{				
				String query = "DELETE FROM Korisnik WHERE Id = ?;";
	
				pstmt = conn.prepareStatement(query);
			    pstmt.setInt(1, id);

			    System.out.println(pstmt + "\n delete user");
	
				return pstmt.executeUpdate() ==1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete KorisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
}
