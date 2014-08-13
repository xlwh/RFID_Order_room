/*
 * ����ʵ�������ݿ�����ӣ���ѯ���͸��µȲ���
 * 
 * ��������ڷ�������ʹ��
 * */



package common;


import java.sql.*;
import java.io.*;
import java.util.*;

public class Database {
	
	
	  public Connection conn = null;
	  public Statement stmt = null;
	  public ResultSet rs = null;
	  private  String databaseInitFilePath ="";
	  private  String propFileName = "/common/databaseInitFilePath.properties";  	//ָ����Դ�ļ������λ��
	  private  Properties prop = new Properties();
	  private  String dbClassName ="";
	  private  String dbUrl ="";
	  private  String dbUser = "";
	  private  String dbPwd = "";
 
  
  public Database(){
	    try {
	    	InputStream in=getClass().getResourceAsStream(propFileName);
	    	prop.load(in);
	    	databaseInitFilePath = prop.getProperty("databaseInitFilePath");
	        in=getClass().getResourceAsStream(databaseInitFilePath);
	        prop.load(in);									          //ͨ���������������Properties�ļ�
	        dbClassName = prop.getProperty("DB_CLASS_NAME");          //��ȡ���ݿ�����
	        dbUrl = prop.getProperty("DB_URL",dbUrl);
	        dbUser=prop.getProperty("DB_USER",dbUser);
	        dbPwd=prop.getProperty("DB_PWD",dbPwd);
	        System.out.println("��������һ�����ݿ�����");
	    	}catch (Exception e) {System.err.println(e.getMessage());}//����쳣��Ϣ
	    	
	    try {
	      Class.forName(dbClassName).newInstance();
	      System.out.println("�������ݿ�����"+dbClassName+"�ɹ���");
	    }
	    catch (Exception e) {
	    	System.out.println("�������ݿ�����"+dbClassName+"ʧ�ܣ�");
	    	System.err.println(e.getMessage());
	    }
	    
	    
	    try {
	       conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
	       System.out.println("�ɹ�������һ�����ݿ�����");
	      }
	      catch (Exception e) {
	    	  System.out.println("���ݿ�����ʧ�ܣ�");
	    	  System.err.println(e.getMessage());
	      }
	    
	    if (conn == null) {
	      System.err.println(
	          "����: DbConnectionManager.getConnection() ������ݿ�����ʧ��.\r\n\r\n��������:" +
	          dbClassName + "\r\n����λ��:" + dbUrl + "\r\n�û�/����" + dbUser + "/" +
	          dbPwd);
	    }
	  } 
  
  /*
   *���ܣ�ִ�в�ѯ���
   */
  public ResultSet  searchDB(String sql) {
    try {
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery(sql);
    }
    catch (SQLException ex) {
      System.err.println(ex.getMessage());
    }
	return rs;
  }

  /*
   *����:ִ�и��²���
   */
  public int updateDB(String sql) {
    int result = 0;
    try {
    	 stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
         result = stmt.executeUpdate(sql);;
      }catch (SQLException e) {
    	System.out.println("����"+result);
    	System.err.println(e.getMessage());
      result = 0;
    }
    try {
      stmt.close();
    }catch (SQLException e) {
    	System.err.println(e.getMessage());
    }
    return result;
  }



  /*
   *����:�ر����ݿ������
   */
  public void close() {
		try {							//��׽�쳣
			if (rs != null) {				//��ResultSet�����ʵ��rs��Ϊ��ʱ
				rs.close();					//�ر�ResultSet����
			}
			if (stmt != null) {				//��Statement�����ʵ��stmt��Ϊ��ʱ
				stmt.close();				//�ر�Statement����
			}
			if (conn != null) {				//��Connection�����ʵ��conn��Ϊ��ʱ
				conn.close();
				//�ر�Connection����
			}
			System.out.println("�ɹ��ر����ݿ����ӣ�");
		    } catch (Exception e) {
			System.out.println("�ر����ݿ�����ʧ�ܣ�");
			e.printStackTrace(System.err);	}//����쳣��Ϣ
	    } 

public static void main(String args[]) throws SQLException{
	Database db = new Database();
	//String sql = "select user_name,pwd from SCOTT.users where SCOTT.USERS.USER_ID = '1221101'";
	
//	String sql = "insert into SCOTT.users(user_id,user_name,pwd) values ('122110123','�ŷ��','12')";
//	int i = db.updateDB(sql);
//System.out.println(i);
	
    String sql = "select * from nls_database_parameters";
	db.searchDB(sql);
	while(db.rs.next()){
		System.out.println(db.rs.getString(1));
	}
}
}
