/*
 * 
 * 
 * 待修改 --下拉列表的初始化：此处为直接查询数据库
 */

/*
 * ReaderLogin.java
 *
 * Created on 2012-11-13, 14:56:18
 */
package showFrame;

import java.sql.ResultSet;

import javax.swing.JOptionPane;

import common.Database;
import datadisposing.DataDisposing_Room;

/**
 *
 * @author DELL
 */
public class ReaderLogin extends javax.swing.JFrame {

    /** Creates new form ReaderLogin */
    public ReaderLogin() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jcb_location = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jtf_pwd = new javax.swing.JTextField();
        jb_reset = new javax.swing.JButton();
        jb_ok = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("读写器使用地点：");

        jLabel2.setFont(new java.awt.Font("幼圆", 1, 14)); // NOI18N
        jLabel2.setText("读写器选择");

        jcb_location.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcb_locationActionPerformed(evt);
            }
        });

        jLabel3.setText("用户号：");

        jLabel4.setText("密码：");

        jb_reset.setText("重置");
        jb_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_resetActionPerformed(evt);
            }
        });

        jb_ok.setText("确定");
        jb_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_okActionPerformed(evt);
            }
        });

        
        /*
         * 查询数据库，初始化下拉列表
         * */
        Database db = new Database();
        String sql = "select reader_location from reader ";
        ResultSet rs = db.searchDB(sql);
       try{ while(rs.next()){
        	jcb_location.addItem(rs.getString(1));
        }
       }catch(Exception e){e.printStackTrace();}
       db.close();
       
       
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3))
                                .addGap(51, 51, 51)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1)
                                    .addComponent(jtf_pwd, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jb_reset)
                                    .addComponent(jLabel1))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jcb_location, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jb_ok)
                                        .addGap(9, 9, 9)))))
                        .addGap(21, 21, 21)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtf_pwd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jcb_location, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_reset)
                    .addComponent(jb_ok))
                .addGap(50, 50, 50))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jcb_locationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcb_locationActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jcb_locationActionPerformed

private void jb_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_resetActionPerformed

 jTextField1.setText("");
 jtf_pwd.setText("");
	
}

private void jb_okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_okActionPerformed

	String location = (String) jcb_location.getSelectedItem();
	String userId = jTextField1.getText();
	String pwd = jtf_pwd.getText();
	String userId_database = null;
	String pwd_database = null;
	
	String sql = "select user_Id,pwd from superusers where location = '" + location +"'";
    Database db = new Database();
    ResultSet rs = db.searchDB(sql);
   try{ 
	   while(rs.next()){
    	userId_database = rs.getString(1);
    	pwd_database = rs.getString(2);
    }
   }catch(Exception e){e.printStackTrace();}
   db.close();
	
   if((!userId.equals(userId_database))||(!pwd.equals(pwd_database))){
	   JOptionPane.showMessageDialog(this, "用户号或密码错误，请重新出入",
			     "错误",JOptionPane.ERROR_MESSAGE);       //弹出错误对话框
   }
   else{
	 this.setVisible(false);
   
   DataDisposing_Room dd = new DataDisposing_Room();
   dd.readeTxt("d:\\storageInformation.txt","1406");}
	
	DataDisposing_Room dd = new DataDisposing_Room();
	dd.readeTxt("d:\\storageInformation.txt",location);
}

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
   
    	ReaderLogin rl = new ReaderLogin();
    	rl.setBounds(100,100,300,380);
    	rl.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton jb_ok;
    private javax.swing.JButton jb_reset;
    private javax.swing.JComboBox jcb_location;
    private javax.swing.JTextField jtf_pwd;
    // End of variables declaration//GEN-END:variables
}
