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

import model.Komentar;
import model.Korisnik;
import model.Korisnik.VrstaKorisnika;
import model.Video;

public class KomentarDAO {
	
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Collection<Komentar> getBySadrzajSearch(String searchText, String orderBy, String ascDesc, String vrstaKorisnika, int idKorisnik) {
		Collection<Komentar> kolekcijaKomentara = new ArrayList<>();
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT k.Id, Sadrzaj, k.DatumKreiranja, k.BrojLajkova, VlasnikKomentaraId, KorisnickoIme, v.Id, v.Naziv FROM Komentar k "
					+ "INNER JOIN Korisnik k2 ON k.VlasnikKomentaraId = k2.Id "
					+ "INNER JOIN Video v ON k.VideoId = v.Id AND k2.Id = v.VlasnikVideaId "
					+ "WHERE k.Obrisan = 0 AND k2.Obrisan = 0 AND v.Obrisan = 0 AND Sadrzaj like ? "; 
			
			switch (vrstaKorisnika) {
			case "ADMIN":
				query += " AND k2.Blokiran IN (0,1) "; 
				break;

			case "USER":
				query += " AND (k2.Blokiran = 0 OR k2.Id = ?) AND(v.Blokiran = 0 OR v.Id = ?) AND(v.VlasnikVideaId = ? OR k2.Blokiran= 0) ";
				break;
				
			case "NOUSER":
				query += " AND k2.Blokiran = 0 AND v.Blokiran = 0 AND v.Vidljivost = 'PUBLIC' ";
				break;
			}
			
			switch (orderBy) {
			case "Sadrzaj":
					query+= "ORDER BY Sadrzaj ";
				break;

			case "DatumKreiranja":
					query+= "ORDER BY k.DatumKreiranja ";
				break;
			
			case "BrojLajkova":
					query+= "ORDER BY k.BrojLajkova ";
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
			pstmt.setString(1, "%" + searchText + "%");
			if(vrstaKorisnika.equals("USER")) {
				pstmt.setInt(2, idKorisnik);
				pstmt.setInt(3, idKorisnik);
				pstmt.setInt(4, idKorisnik);
			}
			
			System.out.println(pstmt);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String Sadrzaj = rset.getString(index++);
				Date DatumKreiranja = rset.getDate(index++);
				String datum =df.format(DatumKreiranja);
				int BrojLajkovaK = rset.getInt(index++);
				int VlasnikKomentaraId = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				int IdVideo = rset.getInt(index++);
				String VideoNaziv = rset.getString(index++);
				Korisnik korisnik = new Korisnik();
				korisnik.setId(VlasnikKomentaraId);
				korisnik.setKorisnickoIme(KorisnickoIme);
				
				Video video = new Video();
				video.setId(IdVideo);
				video.setNaziv(VideoNaziv);

				Komentar komentar = new Komentar(Id, Sadrzaj, datum, korisnik, video, BrojLajkovaK, false);
				
				kolekcijaKomentara.add(komentar);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getBySadrzajSearch komentarDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		return kolekcijaKomentara;
	}
	
	
	public static Komentar getById(int id) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT Id, Sadrzaj, DatumKreiranja, BrojLajkova, Obrisan FROM Komentar "
					+ "WHERE Obrisan = 0 AND Id = ?;";

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			while (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String Sadrzaj = rset.getString(index++);
				Date DatumKreiranja = rset.getDate(index++);
				String datum =df.format(DatumKreiranja);
				int BrojLajkova = rset.getInt(index++);
				boolean Obrisan = rset.getBoolean(index++);

				Komentar komentar = new Komentar(Id, Sadrzaj, datum, null, null, BrojLajkova, Obrisan);		
								
				return komentar;
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getByVideoId komentarDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		return null;
	}
	
	public static Collection<Komentar> getKomentarByVideoId(int idK, String vrstaKorisnika, int idU) {
		Connection conn = ConnectionManager.getConnection();
		Collection<Komentar> komentari = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT * FROM Komentar k INNER JOIN Korisnik k2 ON k.VlasnikKomentaraId = k2.Id "
					+ "WHERE k.Obrisan = 0 AND k2.Obrisan = 0 AND VideoId = ? ";
			
			switch (vrstaKorisnika) {
			case "ADMIN":
				query += ";";
				break;

			case "USER":
				query += "AND (k2.blokiran = 0 OR k2.Id = ?); ";
				break;
			case "NOUSER":
				query += "AND k2.Blokiran = 0";
				break;
			}
			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, idK);
			if(vrstaKorisnika.equals("USER")) pstmt.setInt(2, idU);
			System.out.println(pstmt);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			while (rset.next()) {
				int index = 1;
				int IdK = rset.getInt(index++);
				String Sadrzaj = rset.getString(index++);
				Date DatumKreiranja = rset.getDate(index++);
				String datumK =df.format(DatumKreiranja);
				int VlasnikKomentaraId = rset.getInt(index++);
				int VideoId = rset.getInt(index++);
				int BrojLajkova = rset.getInt(index++);
				boolean Obrisan = rset.getBoolean(index++);

				int Id = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);
				String Password = rset.getString(index++);
				String Ime = rset.getString(index++);
				String Prezime = rset.getString(index++);
				String Email = rset.getString(index++);
				String OpisKanala = rset.getString(index++);
				Date Datum = rset.getDate(index++);
				String datum =df.format(Datum);
				boolean KBlokiran = rset.getBoolean(index++);
				VrstaKorisnika VrstaKorisnika = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				boolean Obrisank = rset.getBoolean(index++);
				
				Korisnik korisnik = new Korisnik(Id, KorisnickoIme, Password, Ime, Prezime, Email, OpisKanala, 
						datum, KBlokiran, null, null, null, null, null, VrstaKorisnika, Obrisank);
				
				
				Komentar komentar = new Komentar(IdK, Sadrzaj, datumK, korisnik, null, BrojLajkova, Obrisan);		

				komentari.add(komentar);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getByVideoId komentarDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		for (Komentar komentar : komentari) {
			System.out.println("iz kometarDAO" + komentar);
		}
		return komentari;
	}
	
	public static Collection<Integer> getByVlasnikId(int id) {
		Connection conn = ConnectionManager.getConnection();
		Collection<Integer> ids = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT Id FROM Komentar "
					+ "WHERE VlasnikKomentaraId = ?;";

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			while (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				
				ids.add(Id);				
			}
			return ids;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getByVlasnikId komentarDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		return ids;
	}
	
	public static Collection<Integer> getByVideoId(int id) {
		Connection conn = ConnectionManager.getConnection();
		Collection<Integer> ids = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT Id FROM Komentar "
					+ "WHERE VideoId = ?;";

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			while (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				
				ids.add(Id);				
			}
			return ids;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getByVideoId komentarDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		return ids;
	}
	
	public static Collection<Komentar> getByVideo(int  id) {
		Collection<Komentar> kolekcijaKomentara = new ArrayList<>();
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT k.Id, Sadrzaj, DatumKreiranja, BrojLajkova, k.Obrisan, k2.* FROM Komentar k inner join Korisnik k2 on k.VlasnikKomentaraId = k2.Id "
					+ "WHERE k.Obrisan = 0 AND k2.Obrisan = 0 AND VideoId = ?;";

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			while (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				String Sadrzaj = rset.getString(index++);
				Date DatumKreiranja = rset.getDate(index++);
				String datumK =df.format(DatumKreiranja);
				int BrojLajkova = rset.getInt(index++);
				boolean Obrisan = rset.getBoolean(index++);
				
				int Idp = rset.getInt(index++);
				String KorisnickoImep = rset.getString(index++);
				String Passwordp = rset.getString(index++);
				String Imep = rset.getString(index++);
				String Prezimep = rset.getString(index++);
				String Emailp = rset.getString(index++);
				String OpisKanalap = rset.getString(index++);
				Date Datump = rset.getDate(index++);
				String datum =df.format(Datump);
				Boolean Blokiranp = rset.getBoolean(index++);
				VrstaKorisnika VrstaKorisnikap = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index++));
				Boolean Obrisanp = rset.getBoolean(index++);
				
				Korisnik vlasnik = new Korisnik(Idp, KorisnickoImep, Passwordp, Imep, Prezimep, Emailp, OpisKanalap, 
						datum, Blokiranp, null, null, null, null, null, VrstaKorisnikap, Obrisanp);
				
				Komentar komentar = new Komentar(Id, Sadrzaj, datumK, vlasnik, null, BrojLajkova, Obrisan);		
				
					
				
				kolekcijaKomentara.add(komentar);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getByVideoId komentarDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		return kolekcijaKomentara;
	}
	public static int getLastId() {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			
			String query = "SELECT Id FROM Komentar ORDER BY Id DESC LIMIT 1"; 

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
	
	public static boolean add(String Sadrzaj, int vlasnikId, int videoId) {
		Connection conn = ConnectionManager.getConnection();
		String sqlDate = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
		
		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO Komentar (Sadrzaj, VlasnikKomentaraId, VideoId, BrojLajkova, Obrisan, DatumKreiranja) " +
							"VALUES (?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, Sadrzaj);
			pstmt.setInt(index++, vlasnikId);
			pstmt.setInt(index++, videoId);
			pstmt.setInt(index++, 0);
			pstmt.setBoolean(index++, false);
			pstmt.setString(index++, sqlDate);
			
			System.out.println(pstmt);
			System.out.println("ovo je insert upit komentara");
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! add komentarDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean update(String sadrzaj, int komentarId) {
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement pstmt = null;
		try {

			
			String query = "UPDATE Komentar SET Sadrzaj = ? WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, sadrzaj);
			pstmt.setInt(2, komentarId);
			
			System.out.println(pstmt);
			System.out.println("ovo je upit za update");
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! update komentarDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean updateBrLajkova(int komentarId, String likeDislike) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			
			String query = "UPDATE Komentar SET BrojLajkova = BrojLajkova";
			
			switch (likeDislike) {
			case "like":
				query += " +1 WHERE Id = ?";
				break;

			case "dislike":
				query += " -1 WHERE Id = ?";
				break;
			}

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, komentarId);

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! updateBrPregleda");
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
			String query = "UPDATE Komentar SET Obrisan = 1 WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			System.out.println(pstmt);
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete komentarDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteAdmin(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM Komentar WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			System.out.println(pstmt);
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete komentarDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteByUserId(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Komentar SET Obrisan = 1 WHERE VlasnikKomentaraId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete komentarDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteByUserIdAdmin(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM Komentar WHERE VlasnikKomentaraId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			System.out.println(pstmt + "\n delete komentar");
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete komentarDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteByVideoId(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Komentar SET Obrisan = 1 WHERE VideoId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete komentarDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteByVideoIdAdmin(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM Komentar WHERE VideoId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			System.out.println(pstmt + "\n delete komentar");
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete komentarDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
}