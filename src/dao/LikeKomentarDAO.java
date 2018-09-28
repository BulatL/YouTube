package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.LikeKomentar;

public class LikeKomentarDAO {

	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	
	public static LikeKomentar getById(int id) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {

			String query = "SELECT Id, lajk, DatumKreiranja, Obrisan FROM youtube.LikeKomentar "
					+ "WHERE Obrisan = 0 AND Id = ?;"; 

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			System.out.println(pstmt);
			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();

			// citanje rezultata upita
			if (rset.next()) {
				int index = 1;
				int Id = rset.getInt(index++);
				boolean lajk = rset.getBoolean(index++);
				Date DatumKreiranja = rset.getDate(index++);
				String datum =df.format(DatumKreiranja);
				boolean Obrisan = rset.getBoolean(index++);
				
				return new LikeKomentar(Id, lajk, datum, null, null, Obrisan);
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu!");
			ex.printStackTrace();
		} finally {
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {rset.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
		return null;
	}
	
	public static boolean provera(int komentarId, int vlasnikId, String likeDislike) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT * FROM LikeKomentar WHERE Obrisan = 0 AND VlasnikLajkaId = ? AND KomentarId = ? AND ";
			
			switch (likeDislike) {
			case "like":
				query += "lajk = 1";
				break;

			case "dislike":
				query += "lajk = 0";
				break;
			}

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, vlasnikId);
			pstmt.setInt(2, komentarId);
			System.out.println(pstmt);
			rset = pstmt.executeQuery();
			
			while (rset.next()) {
				return true;
			}
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu!");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean add(Boolean likeDislike, int vlasnikId, int komentarId) {
		Connection conn = ConnectionManager.getConnection();
		String sqlDate = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO LikeKomentar (lajk, DatumKreiranja, VlasnikLajkaId, KomentarId, Obrisan) " +
							"VALUES (?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setBoolean(index++, likeDislike);
			pstmt.setString(index++, sqlDate);
			pstmt.setInt(index++, vlasnikId);
			pstmt.setInt(index++, komentarId);
			pstmt.setBoolean(index++, false);
			System.out.println(pstmt);
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu!");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean update(LikeKomentar LikeKomentar) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			
			String query = "UPDATE LikeKomentar SET lajk = ?, DatumKreiranja = ?, VlasnikLajkaId = ?, "+
						   "KomentarId = ?, Obrisan = ? WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, LikeKomentar.getId());
			int index = 1;
			pstmt.setBoolean(index++, LikeKomentar.getLike());
			pstmt.setString(index++, LikeKomentar.getDatumKreiranja().toString());
			pstmt.setInt(index++, LikeKomentar.getVlasnikLike().getId());
			pstmt.setInt(index++, LikeKomentar.getKomentar().getId());
			pstmt.setBoolean(index++, LikeKomentar.getObrisan());
			System.out.println(pstmt);
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu!");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean delete(int vlasnikLajkaId, int komentarId) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "UPDATE LikeKomentar SET Obrisan = 1 WHERE VlasnikLajkaId = ? AND KomentarId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, vlasnikLajkaId);
			pstmt.setInt(2, komentarId);
			System.out.println(pstmt);
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu!");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	public static boolean deleteAdmin(int vlasnikLajkaId) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM LikeKomentar WHERE VlasnikLajkaId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, vlasnikLajkaId);
			System.out.println(pstmt + "\n delete like komentar");
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu!");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	public static boolean deleteAdminByKomentarID(int komentarId) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM LikeKomentar WHERE KomentarId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, komentarId);
			System.out.println(pstmt + "\n delete like komentar");
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu!");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
}