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

import model.Korisnik;
import model.Korisnik.VrstaKorisnika;
import model.Video;
import model.Video.Vidljivost;


public class VideoDAO {

	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Collection<Video> getVideosForPocetnaStranica(String orderBy, String ascDesc, String vrstaKorisnika, int vlasnikVideaId, int limit) {
		Collection<Video> kolekcijaVidea = new ArrayList<>();
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query ="SELECT * FROM Video v INNER JOIN Korisnik k on v.VlasnikVideaId = k.Id"
					+ " WHERE k.Obrisan = 0 AND v.Obrisan = 0 AND ";
			
			switch (vrstaKorisnika) {
			case "ADMIN":
				query += "Vidljivost IN ('PUBLIC', 'PRIVATE', 'UNLISTED') ";
				break;

			case "USER":
				query += "(v.blokiran = 0 OR VlasnikVideaId = ?) AND (k.Blokiran = 0 OR k.Id = ?) AND (Vidljivost = 'PUBLIC' OR VlasnikVideaId = ?) AND Vidljivost != 'UNLISTED' ";
				break;
			case "NOUSER":
				query += "v.blokiran = 0 AND k.Blokiran = 0 AND Vidljivost = 'PUBLIC' ";
				break;
			}
			
			switch (orderBy) {
			case "Naziv":
				query += "ORDER BY Naziv  ";
				break;
			case "BrojPregleda":
				query += "ORDER BY BrojPregleda ";
				break;
			case "DatumKreiranja":
				query += "ORDER BY DatumKreiranja ";				
				break;
			}
			
			switch (ascDesc) {
			case "asc":
				query += "asc ";
				break;
			case "desc":
				query += "desc ";
				break;

			}
			if(limit == 5) {
				query += "LIMIT 5";
			}

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			if(vrstaKorisnika.equals("USER")) {
				pstmt.setInt(1, vlasnikVideaId);
				pstmt.setInt(2, vlasnikVideaId);
				pstmt.setInt(3, vlasnikVideaId);
			}
			System.out.println(pstmt);
			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			while (rset.next()) {
				int index = 1;
				int IdV = rset.getInt(index++);
				String VideoUrl = rset.getString(index++);
				String Slika = rset.getString(index++);
				String Naziv = rset.getString(index++);
				String Opis = rset.getString(index++);
				Vidljivost Vidljivost = model.Video.Vidljivost.valueOf(rset.getString(index++));
				boolean Blokiran = rset.getBoolean(index++);
				boolean Rejting = rset.getBoolean(index++);
				boolean Komentari = rset.getBoolean(index++);
				int BrojPregleda = rset.getInt(index++);
				int BrojLajkova = rset.getInt(index++);
				Date DatumKreiranja = rset.getDate(index++); 
				String datumKreiranja =df.format(DatumKreiranja);
				int VlasnikId = rset.getInt(index++);
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
				
				Video video = new Video(IdV, VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari,
						BrojPregleda, BrojLajkova, datumKreiranja, korisnik, Obrisan);
			
				kolekcijaVidea.add(video);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getVideosForPocetnaStranica VideoDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return kolekcijaVidea;
	}
	
	public static Collection<Video> SearchVideo(String searchText, String orderBy, String ascDesc, String vrstaKorisnika, int idKorisnika) {
		Collection<Video> kolekcijaVidea = new ArrayList<>();
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT v.Id, Slika, Naziv, Opis, BrojPregleda, BrojLajkova, DatumKreiranja, k.Id, KorisnickoIme "
						 + "FROM Video v INNER JOIN korisnik k ON v.VlasnikVideaId = k.Id "
						 + "WHERE  Naziv like ? AND v.Obrisan = 0 AND k.Obrisan = 0 AND ";
			
			switch (vrstaKorisnika) {
			case "ADMIN":
				query += "v.Vidljivost IN ('PUBLIC', 'PRIVATE' , 'UNLISTED') "; 
				break;
				
			case "USER":
				query += "(v.Blokiran = 0 OR VlasnikVideaId = ?) AND (Vidljivost = 'PUBLIC' OR VlasnikVideaId = ?) AND Vidljivost != 'UNLISTED'";
					break;
					
			case "NOUSER":
				query += "v.Blokiran = 0 AND k.Blokiran = 0 AND v.Vidljivost = 'PUBLIC' ";
				break;	
			}
			
			switch (orderBy) {
			case "Naziv":
				query += "ORDER BY Naziv ";
				break;

			case "BrojLajkova":
				query += "ORDER BY BrojLajkova ";
				break;
				
			case "DatumKreiranja":
				query += "ORDER BY DatumKreiranja ";
				break;
				
			case "BrojPregleda":
				query += "ORDER BY BrojPregleda ";
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

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1,"%" +searchText+"%");
			if(vrstaKorisnika.equals("USER")) {
				pstmt.setInt(2, idKorisnika);
				pstmt.setInt(3, idKorisnika);
			}
			System.out.println(pstmt);
			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			while (rset.next()) {
				int index = 1;
				int IdV = rset.getInt(index++);
				String Slika = rset.getString(index++);
				String Naziv = rset.getString(index++);
				String Opis = rset.getString(index++);
				int BrojPregleda = rset.getInt(index++);
				int BrojLajkova = rset.getInt(index++);
				Date DatumKreiranja = rset.getDate(index++);
				String datum =df.format(DatumKreiranja);
				
				int IdK = rset.getInt(index++);
				String KorisnickoIme = rset.getString(index++);

				
				Korisnik korisnik = new Korisnik(IdK, KorisnickoIme, null, null, null, null, null, 
						null, null, null, null, null, null, null, null, null);
				
				Video video = new Video(IdV, null, Slika, Naziv, Opis, null, null, null, null,
						BrojPregleda, BrojLajkova, datum, korisnik, null);
			
				kolekcijaVidea.add(video);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! searchVideo VideoDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return kolekcijaVidea;
	}
	
	public static Video getById(int id) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT Id, VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, "
					+ "BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan FROM Video WHERE Id = ?"; 

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			if (rset.next()) {
				int index = 1;
				int IdV = rset.getInt(index++);
				String VideoUrl = rset.getString(index++);
				String Slika = rset.getString(index++);
				String Naziv = rset.getString(index++);
				String Opis = rset.getString(index++);
				Vidljivost Vidljivost = model.Video.Vidljivost.valueOf(rset.getString(index++));
				boolean Blokiran = rset.getBoolean(index++);
				boolean Rejting = rset.getBoolean(index++);
				boolean Komentari = rset.getBoolean(index++);
				int BrojPregleda = rset.getInt(index++);
				int BrojLajkova = rset.getInt(index++);
				Date DatumKreiranja = rset.getDate(index++);
				String datum =df.format(DatumKreiranja);
				int VlasnikId = rset.getInt(index++);
				boolean Obrisan = rset.getBoolean(index++);
				
				
				Video video = new Video(IdV, VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, 
						BrojPregleda, BrojLajkova, datum, new Korisnik(), Obrisan);
				
				pstmt.close();
				rset.close();
				
				query = "SELECT k.* FROM korisnik k inner join video p on p.VlasnikVideaId = k.Id "
						+ "WHERE k.Obrisan = 0 AND k.Id = ?;";
				pstmt = conn.prepareStatement(query);

				pstmt.setInt(1, VlasnikId);
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
					String datump =df.format(Datump);
					Boolean Blokiranp = rset.getBoolean(index2++);
					VrstaKorisnika VrstaKorisnikap = model.Korisnik.VrstaKorisnika.valueOf(rset.getString(index2++));
					Boolean Obrisanp = rset.getBoolean(index2++);
					
					Korisnik vlasnik = new Korisnik(Idp, KorisnickoImep, Passwordp, Imep, Prezimep, Emailp, OpisKanalap, 
							datump, Blokiranp, null, null, null, null, null, VrstaKorisnikap, Obrisanp);
					
					video.setVlasnik(vlasnik);
				}
					
				return video;
				
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getById VideoDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		return null;
	}
	
	public static Collection<Integer> getByVlasnikId(int id) {
		Connection conn = ConnectionManager.getConnection();
		Collection<Integer> listaVideoId = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT Id FROM Video WHERE VlasnikVideaId = ?"; 

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			rset = pstmt.executeQuery();

			while (rset.next()) {
				int index = 1;
				int IdV = rset.getInt(index++);
				
				listaVideoId.add(IdV);
			}
			
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getById VideoDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		return listaVideoId;
	}
	
	public static Collection<Video> getVideosByKorisnik(Korisnik korisnik, String vrstaKorisnika, int idKorisnika){

		Connection conn = ConnectionManager.getConnection();
		Collection<Video> videos = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT video.* FROM Video INNER JOIN Korisnik ON VlasnikVideaId = korisnik.Id "
					+ "WHERE video.Obrisan = 0  AND VlasnikVideaId = ? AND ";
			
			switch (vrstaKorisnika) {
			case "ADMIN":
				query += "video.Vidljivost IN ('PUBLIC', 'PRIVATE' , 'UNLISTED') "; 
				break;
				
			case "USER":
				query += "(video.Blokiran = 0 OR VlasnikVideaId = ?) AND (Vidljivost = 'PUBLIC' OR VlasnikVideaId = ?) AND Vidljivost != 'UNLISTED'";
					break;
					
			case "NOUSER":
				query += "video.Blokiran = 0 AND korisnik.Blokiran = 0 AND video.Vidljivost = 'PUBLIC' ";
				break;	
			}
			query += "ORDER BY DatumKreiranja asc;";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, korisnik.getId());
			if(vrstaKorisnika.equals("USER")) {
				pstmt.setInt(2, idKorisnika);
				pstmt.setInt(3, idKorisnika);
			}
			System.out.println("video \n" + pstmt);
			rset = pstmt.executeQuery();

			while (rset.next()) {
				int index2 = 1;
				int IdV = rset.getInt(index2++);
				String VideoUrl = rset.getString(index2++);
				String Slika = rset.getString(index2++);
				String Naziv = rset.getString(index2++);
				String Opis = rset.getString(index2++);
				Vidljivost Vidljivost = model.Video.Vidljivost.valueOf(rset.getString(index2++));
				boolean BlokiranV = rset.getBoolean(index2++);
				boolean Rejting = rset.getBoolean(index2++);
				boolean Komentari = rset.getBoolean(index2++);
				int BrojPregleda = rset.getInt(index2++);
				int BrojLajkova = rset.getInt(index2++);
				Date DatumKreiranja = rset.getDate(index2++);
				String datum =df.format(DatumKreiranja);
				int VlasnikId = rset.getInt(index2++);
				boolean ObrisanV = rset.getBoolean(index2++);
				
				
				Video video = new Video(IdV, VideoUrl, Slika, Naziv, Opis, Vidljivost, BlokiranV, Rejting, 
						Komentari, BrojPregleda, BrojLajkova, datum, korisnik, ObrisanV);

				videos.add(video);
			
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getVideosByKorisnik KorisnikDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return videos;
	}
	
	public static Collection<Video> getLikedVideosByKorisnik(Korisnik korisnik, String vrstaKorisnika,int idKorisnika){

		Connection conn = ConnectionManager.getConnection();
		Collection<Video> videos = new ArrayList<>();
		System.out.println("\n korisnik " + "\n" +korisnik.korisnickoIme);
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			String query = "SELECT v.* FROM Likevideo lv INNER JOIN Korisnik k on lv.VlasnikLajkaId = k.Id "
					+ "INNER JOIN video v on lv.VideoId = v.Id "
					+ "WHERE lv.Obrisan = 0 AND v.Obrisan = 0 AND ";
			
			switch (vrstaKorisnika) {
			case "ADMIN":
				query += "v.Vidljivost IN ('PUBLIC', 'PRIVATE' , 'UNLISTED') "; 
				break;
				
			case "USER":
				query += "(v.Blokiran = 0 OR VlasnikVideaId = ?) AND (Vidljivost = 'PUBLIC' OR VlasnikVideaId = ?) AND Vidljivost != 'UNLISTED' AND(k.blokiran = 0 OR k.id=?) AND k.obrisan = 0 ";
					break;
					
			case "NOUSER":
				query += "v.Blokiran = 0 AND k.Blokiran = 0 AND v.Vidljivost = 'PUBLIC' AND k.blokiran = 0 AND k.obrisan = 0 ";
				break;	
			}
			query += "AND k.Id = ? ORDER BY v.DatumKreiranja asc;";
			pstmt = conn.prepareStatement(query);
			
			int index = 1;
			if(vrstaKorisnika.equals("USER")) {
				pstmt.setInt(index++, idKorisnika);
				pstmt.setInt(index++, idKorisnika);
				pstmt.setInt(index++, idKorisnika);
			}
			pstmt.setInt(index++, korisnik.getId());
			
			System.out.println("\n"+pstmt);
			
			rset = pstmt.executeQuery();

			while (rset.next()) {
				int index2 = 1;
				int IdV = rset.getInt(index2++);
				String VideoUrl = rset.getString(index2++);
				String Slika = rset.getString(index2++);
				String Naziv = rset.getString(index2++);
				String Opis = rset.getString(index2++);
				Vidljivost Vidljivost = model.Video.Vidljivost.valueOf(rset.getString(index2++));
				boolean BlokiranV = rset.getBoolean(index2++);
				boolean Rejting = rset.getBoolean(index2++);
				boolean Komentari = rset.getBoolean(index2++);
				int BrojPregleda = rset.getInt(index2++);
				int BrojLajkova = rset.getInt(index2++);
				Date DatumKreiranja = rset.getDate(index2++);
				String datum =df.format(DatumKreiranja);
				int VlasnikId = rset.getInt(index2++);
				boolean ObrisanV = rset.getBoolean(index2++);
				
				
				Video video = new Video(IdV, VideoUrl, Slika, Naziv, Opis, Vidljivost, BlokiranV, Rejting, 
						Komentari, BrojPregleda, BrojLajkova, datum, korisnik, ObrisanV);

				videos.add(video);
			
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! getLikedVideosByKorisnik videoDAO");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		
		return videos;
	}
	
	public static boolean add(Video video) {
		Connection conn = ConnectionManager.getConnection();
		String sqlDate = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

		
		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO Video (VideoUrl, Slika, Naziv, Opis, Vidljivost, Blokiran, Rejting, Komentari, "
					+ "BrojPregleda, BrojLajkova, DatumKreiranja, VlasnikVideaId, Obrisan) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, video.getVideoUrl());
			pstmt.setString(index++, video.getSlika());
			pstmt.setString(index++, video.getNaziv());
			pstmt.setString(index++, video.getOpis());
			pstmt.setString(index++, video.getVidljivost().toString());
			pstmt.setBoolean(index++, false);
			pstmt.setBoolean(index++, video.getRejting());
			pstmt.setBoolean(index++, video.getKomentar());
			pstmt.setInt(index++, 0);
			pstmt.setInt(index++, 0);
			pstmt.setString(index++, sqlDate);
			pstmt.setInt(index++, video.getVlasnik().getId());
			pstmt.setBoolean(index++, false);

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! add VideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean update(Video video) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {

			
			String query = "UPDATE Video SET Naziv = ?, Opis = ?, Vidljivost = ?, "+
						   "Blokiran = ?, Rejting = ?, Komentari = ?, BrojPregleda = ?, BrojLajkova=?, Obrisan = ? WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setString(index++, video.getNaziv());
			pstmt.setString(index++, video.getOpis());
			pstmt.setString(index++, video.getVidljivost().toString());
			pstmt.setBoolean(index++, video.getBlokiran());
			pstmt.setBoolean(index++, video.getRejting());
			pstmt.setBoolean(index++, video.getKomentar());
			pstmt.setInt(index++, video.getBrojPregleda());
			pstmt.setInt(index++, video.getBrojLajkova());
			pstmt.setBoolean(index++, video.getObrisan());
			pstmt.setInt(index++, video.getId());

			System.out.println(pstmt);
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! update VideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean block(int blokiran, int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {

			
			String query = "UPDATE Video SET Blokiran = ? WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, blokiran);
			pstmt.setInt(2, id);

			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! block VideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean updateBrPregleda(Video video) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			
			String query = "UPDATE Video SET BrojPregleda = BrojPregleda + 1 WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, video.getId());
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
	
	public static boolean updateBrLajkova(int videoId, String likeDislike) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			
			String query = "UPDATE Video SET BrojLajkova = BrojLajkova";
			
			switch (likeDislike) {
			case "like":
				query += " +1 WHERE Id = ?";
				break;

			case "dislike":
				query += " -1 WHERE Id = ?";
				break;
			}

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, videoId);

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
			String query = "UPDATE Video SET Obrisan = 1 WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete VideoDAO");
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
			String query = "DELETE FROM Video WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete VideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteByVlasnikId(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE Video SET Obrisan = 1 WHERE VlasnikVideaId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete VideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteByVlasnikIdAdmin(int id) {
		Connection conn = ConnectionManager.getConnection();

		System.out.println("\n usao u video dao \n");
		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM Video WHERE VlasnikVideaId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			System.out.println(pstmt + "\n delete video");

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete VideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
}
