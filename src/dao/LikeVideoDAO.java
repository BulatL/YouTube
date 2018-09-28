package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import model.Korisnik;
import model.LikeVideo;
import model.Video;
import model.Korisnik.VrstaKorisnika;
import model.Video.Vidljivost;

public class LikeVideoDAO {

	public static boolean provera(int videoId, int idKorisnika, String likeDislike){
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT * FROM LikeVideo lv INNER JOIN Korisnik k ON k.Id = lv.VlasnikLajkaId "
					+ "INNER JOIN video v ON lv.VideoId = v.Id "
					+ "WHERE k.Obrisan = 0 AND  lv.Obrisan = 0 AND v.Obrisan = 0 AND lv.VideoId = ? AND k.Id = ? "; 
			
			switch (likeDislike) {
			
			case "like":
				query += "AND lv.lajk = 1";
				break;

			case "dislike":
				query += "AND lv.lajk = 0";
				break;
			}
			

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, videoId);
			pstmt.setInt(2, idKorisnika);
			System.out.println(pstmt);
			rset = pstmt.executeQuery();


			while (rset.next()) {
				return true;
			}
			
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! add LikeVideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}

	public static boolean add(boolean lajk, int vlasnikId, int videoId) {
		Connection conn = ConnectionManager.getConnection();
		String sqlDate = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());

		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO LikeVideo (lajk, DatumKreiranja, VlasnikLajkaId, VideoId, Obrisan) " +
							"VALUES (?, ?, ?, ?, ?)";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setBoolean(index++, lajk);
			pstmt.setString(index++, sqlDate);
			pstmt.setInt(index++, vlasnikId);
			pstmt.setInt(index++, videoId);
			pstmt.setBoolean(index++, false);
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! add LikeVideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean update(LikeVideo likeVideo) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			
			String query = "UPDATE LikeVIdeo SET lajk = ?, DatumKreiranja = ?, VlasnikLajkaId = ?, "+
						   "VideoId = ?, Obrisan = ? WHERE Id = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, likeVideo.getId());
			int index = 1;
			pstmt.setBoolean(index++, likeVideo.getLike());
			pstmt.setString(index++, likeVideo.getDatumKreiranja().toString());
			pstmt.setInt(index++, likeVideo.getVlasnikLike().getId());
			pstmt.setInt(index++, likeVideo.getVideo().getId());
			pstmt.setBoolean(index++, likeVideo.getObrisan());

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! update LikeVideoDAO");
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
			String query = "UPDATE LikeVIdeo SET Obrisan = 1 WHERE VideoId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			System.out.println(pstmt + "\nDelete dao");

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete LikeVideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteAdminbyVideoId(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM  likeVideo WHERE VideoId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			System.out.println(pstmt + "\n Delete likeVideo");

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete LikeVideoDAO");
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
			String query = "UPDATE LikeVIdeo SET Obrisan = 1 WHERE VlasnikLajkaId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			System.out.println(pstmt + "\nDelete dao");

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete LikeVideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean adminDeleteByVlasnikId(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM  likeVideo WHERE VlasnikLajkaId = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			System.out.println(pstmt + "\n Delete likeVideo");

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! delete LikeVideoDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
}