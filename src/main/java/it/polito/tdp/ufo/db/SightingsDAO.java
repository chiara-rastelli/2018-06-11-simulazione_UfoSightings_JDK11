package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.ufo.model.Adiacenza;
import it.polito.tdp.ufo.model.AnnoAvvistamenti;
import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new Sighting(res.getInt("id"),
						res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), 
						res.getString("state"), 
						res.getString("country"),
						res.getString("shape"),
						res.getInt("duration"),
						res.getString("duration_hm"),
						res.getString("comments"),
						res.getDate("date_posted").toLocalDate(),
						res.getDouble("latitude"), 
						res.getDouble("longitude"))) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getStatiAvvistamentiAnno(int anno) {
		String sql = "SELECT DISTINCT state FROM sighting WHERE YEAR(DATETIME) = ? AND country='us' order by state asc";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			List<String> list = new ArrayList<>();
			ResultSet res = st.executeQuery();
			while(res.next()) {
				list.add(res.getString("state"));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Adiacenza> getAdiacenze(int anno) {
		String sql = "SELECT s1.state, s2.state FROM sighting s1, sighting s2 WHERE s1.state <> s2.state AND year(s2.datetime) = ?" + 
					 "	AND YEAR(s1.datetime) = ? AND s1.country = 'us' AND s2.country = 'us' AND s1.datetime < s2.datetime"+
					 "  GROUP BY s1.state, s2.state";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			List<Adiacenza> list = new ArrayList<>();
			ResultSet res = st.executeQuery();
			while(res.next()) {
				list.add(new Adiacenza(res.getString("s1.state"), res.getString("s2.state")));
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<AnnoAvvistamenti> getAnnoAvvistamenti() {
		String sql = "SELECT YEAR(DATETIME) AS anno, COUNT(*) AS numeroAvvistamenti FROM sighting "
				+ " WHERE country = 'us' GROUP BY anno order by anno asc" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<AnnoAvvistamenti> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new AnnoAvvistamenti(res.getInt("anno"), res.getInt("numeroAvvistamenti"))) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
}
