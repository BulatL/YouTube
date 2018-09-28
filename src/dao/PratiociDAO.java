package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import model.Korisnik;
import model.Korisnik.VrstaKorisnika;

public class PratiociDAO {
	
	
	public static boolean provera(int koPrati, int kogaPrati) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT * FROM Pratioci WHERE KoPrati = ? AND KogaPrati = ? ";

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, koPrati);
			pstmt.setInt(2, kogaPrati);
			
			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();
			
			// citanje rezultata upita
			if (rset.next()) {
				int index = 1;
				int id1 = rset.getInt(index++);
				int id2 = rset.getInt(index++);
				
				return  true;
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! provera PratiociDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static Integer brojPratioca(int kogaPrati) {
		Connection conn = ConnectionManager.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			String query = "SELECT count(*) FROM Pratioci WHERE KogaPrati = ? ";

			// kreiranje SQL naredbe, jednom za svaki SQL upit
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, kogaPrati);
			
			// izvrsavanje naredbe i prihvatanje rezultata (SELECT), jednom za svaki SQL upit
			rset = pstmt.executeQuery();
			
			// citanje rezultata upita
			if (rset.next()) {
				int index = 1;
				int brojPratioca = rset.getInt(index++);
				
				return  brojPratioca;
			}
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! provera PratiociDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return 0;
	}

	public static boolean add(int koPrati, int kogaPrati) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "INSERT INTO Pratioci (KoPrati, KogaPrati) " +
							"VALUES (?, ?)";

			pstmt = conn.prepareStatement(query);
			int index = 1;
			pstmt.setInt(index++, koPrati);
			pstmt.setInt(index++, kogaPrati);

			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! add PratiociDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	
	public static boolean deleteKoPrati(int id) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM Pratioci WHERE KoPrati = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, id);
			System.out.println(pstmt + "\n delete koPrati");
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! deleteKoPrati PratiociDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteKoPratiKoga(int koPrati, int kogaPrati) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM Pratioci WHERE KoPrati = ? AND KogaPrati = ?";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, koPrati);
			pstmt.setInt(2, kogaPrati);
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! deleteKoPratiKoga PratiociDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	public static boolean deleteKoPratiUser(int koPrati) {
		Connection conn = ConnectionManager.getConnection();

		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM Pratioci WHERE KoPrati = ?;";

			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, koPrati);
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! deleteKoPratiKoga PratiociDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}

		return false;
	}
	
	public static boolean deleteKogaPratiUser(int kogaPrati) {
		Connection conn = ConnectionManager.getConnection();
	
		PreparedStatement pstmt = null;
		try {
			String query = "DELETE FROM Pratioci WHERE KogaPrati = ?";
	
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, kogaPrati);
			System.out.println(pstmt + "\n delete kogaPrati");
			// izvrsavanje naredbe i prihvatanje rezultata (INSERT, UPDATE, DELETE), jednom za svaki SQL upit
			return pstmt.executeUpdate() == 1;
		} catch (SQLException ex) {
			System.out.println("Greska u SQL upitu! deleteKoPratiKoga PratiociDAO");
			ex.printStackTrace();
		} finally {
			// zatvaranje naredbe i rezultata
			try {pstmt.close();} catch (SQLException ex1) {ex1.printStackTrace();}
			try {conn.close();} catch (SQLException ex1) {ex1.printStackTrace();System.out.println(ex1);}
		}
	
		return false;
	}
		
	
}

