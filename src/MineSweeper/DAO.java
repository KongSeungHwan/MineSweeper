package MineSweeper;

import java.sql.*;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class DAO {
	private Connection con;
	private String connect="jdbc:mysql://localhost:3307/minesweeper?serverTimezone=UTC";
	private String dbid ="root";
	private String dbpw ="3520";
	private Statement state;
	private ResultSet rs;//최대한 중복되는 객체들은 필드로 처리(자바는 초기화 안 할 시 디폴트 값이 null이거나 0이지만, c는 쓰레기 값이 초기화된다.)
	HashMap<String,Client> databaseClientList;
	DAO(){
	try {
		con = DriverManager.getConnection(connect,dbid,dbpw);
		databaseClientList=accessAllDataSQL();
	} catch (SQLException e) {
	}
	} //생성시 바로 데베에있는 모든 데이터 hashmap으로 초기화
	final void closeResource(){//템플릿 메소드(공부한 것 응용)
		try {
			if (state != null)rs.close();
			if (rs != null)rs.close();
			if (con != null)con.close();
		}catch(SQLException e) {
		}
	}//시스템 자원 반환 한꺼번에 처리하는 메소드
	boolean insertSQL(String name,String id, String pw){
		try {
			state=con.createStatement();
			String sql = "insert into minesweeper.client(client_name,client_id,client_pw,total_rounds,victory_rounds)"
					+ "values('"+name+"','"+id+"','"+pw+"',"+0+","+0+");";
			//entity 추가 insert문 활용
			state.execute(sql);
			return true;
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null, "SQL문법 오류");
			return false;
		}
	} //처음 만들 시에는 판수가 0으로 되어야하니까 victory,total 둘 다 0으로 insert문을 구성.
	synchronized HashMap<String,Client> accessAllDataSQL(){
		HashMap<String,Client> resultList = new HashMap<>();
		try {
			String sql="select * from minesweeper.client;";
			state = con.createStatement();
			rs = state.executeQuery(sql);
			while (rs.next())resultList.put(rs.getString("client_id")
					,new Client(rs.getString("client_name")
					,rs.getString("client_id")
					,rs.getString("client_pw")
					,rs.getInt("total_rounds")
					,rs.getInt("victory_rounds")
					));
			return resultList;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "SQL문법 오류");
			return null;
		}
	}
	synchronized void updateGameDataSQL(boolean vic,Client cl){
		try {
			String sql1 ="update client set total_rounds = total_rounds+1";
			String sql2 =", victory_rounds = victory_rounds+1";
			String sql3 =" where client_id = '"+cl.getId()+"'";
			state=con.createStatement();
			if(vic==true) {
				state.execute(sql1+sql2+sql3); //이길 경우
			}else{
				state.execute(sql1+sql3);//질 경우
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "SQL문법 오류");
			e.printStackTrace();
		}
		
	}
	boolean deleteSQL(String id){
		try {
			state= con.createStatement();
			String sql ="delete from client where (client_id ='"+id+"')";
			state.execute(sql);
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "SQL문법 오류");
			return false;
		}
	}
	boolean updateSQL(Client cl, String col,String val){
		try {
			state= con.createStatement();
			String sql ="update client set "+col+" = '"+val+
					"' where client_id = '"+cl.getId()+"'";
			state.execute(sql);
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "SQL문법 오류");
			return false;
		}
	}
	
	//각자 메소드의 리턴 값이 다르기에 예외처리도 다 제각각 함.
	//Connector 객체는 필드로 놓고
	//PreparedStatement를 안쓰고 Statement 객체를 쓰는 이유 
	//전자 객체는 보안 공격에 취약하다. 그 반면 Statement 객체는 안전하다.
	//+DB연결 예외와 SQL문법 오류 가능성을 나눔.
}
