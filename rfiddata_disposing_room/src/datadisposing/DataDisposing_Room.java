/*11��13��
 * 
 * ���ܣ����ʵ�����豸�����߼��ж�
 *       ��֤���豸�Ƿ�Ϊ��ʵ�����豸
 *       
 * ����ɹ�������ȡ��ǩ��Ϣ�Ľ������������豸����
 *            �Ǳ����豸����ʱ��������
 * 
 * ���Ľ��� ���зǱ����豸���ʱ�����ִ���Ի�����ʾ������ʱ����ֹͣ����
 * */



package datadisposing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import javax.swing.JOptionPane;

import showFrame.RoomShow;
import showFrame.RoomShow_V2;
import txtShow.TxtShow;

import common.Database;

public class DataDisposing_Room {
	private static long interval =60;         //ʱ�����趨Ϊ60�룬���ζ���ʱ������60�뿴��Ϊ���β�ͬ�Ľ�/��
	//private static String txtAddress = "txtInformation/roomInformation.txt";//��ų������Ϣ
	
	
	Vector<String> v_eqpId =null;
	Vector<String> v_readingTime = null;
	Vector<Integer> v_flag = null;
	Vector<String> v_eqpIdError = null;       //��¼�Ǳ����豸

	String sql = null;
	
	RoomShow_V2 roomshow ;
	Vector<String> v = new Vector<String>();
	
	String roomId="1406";
	
	/*
	 * ��������������ȡ�ļ���·��fileaddress(String)
	 * 
	 * �������ܣ���txt�ļ�������ݰ��ж�������һ����Ϣ����','�ָ�����뵽��������ɽ�һ����д����Ϣ���
	 * */
	public void readeTxt(String fileadderss,String roomId){
		

		roomshow = new RoomShow_V2(roomId);                                               //ʵ�����ֿ������Ϣ����
		roomshow.setBounds(100,100,700,700);
		roomshow.getJlb_state().setText("��ȡ״̬");
		roomshow.setVisible(true);
		roomshow.repaint();
		roomshow.pack();
		
		TxtShow txtShow = new TxtShow();                                                //�������Ϣ����txt�������Ա�鿴
		
		String s = new String();                     									//��Ŵ�txt���ȡ������Ϣ
		v_eqpId = new Vector<String>();													//��ű�ǩ
		v_readingTime = new Vector<String>();											//����ʱ��
		v_flag = new Vector<Integer>();													//�豸�ڿ��־λ
		v_eqpIdError = new Vector<String>();

		//String txt = "flag,����ı�ǩ��,��ȡʱ��"+"\r\n";	
		
	while(true){
		try { 
			
			/*
			 * ���ܣ������ݿ��ѯ����ʵ���ң�room����������豸��ŵ�����v_eqpId_db��
			 *       ���ڼ��������豸���Ƿ�Ϊ��ʵ�����豸
			 * */
			Vector<String> v_eqpId_db = new Vector<String>();
			String sql = "select eqp_Id from room where eqp_location = '" + roomId+"'";
		//	System.out.println(sql);
			Database database = new Database();
			ResultSet rs = database.searchDB(sql);
			while (rs.next()){
				v_eqpId_db.add(rs.getString(1));
			}
			//database.close();
			
			
			
			
			
			    //��ȡ��
				BufferedReader input = new BufferedReader(new FileReader(fileadderss)); 
				input.readLine();                     									//�ӵڶ��п�ʼ��ȡ����ŵ�������
				while((s = input.readLine())!=null){  									//�ж��Ƿ���������һ��
	System.out.println("////////////********************----------------");
					Database db = new Database();
					String info[] = s.split(",");    									//������Ϣ��','Ϊ�ָ�����������
					
					// �Զ�ȡ���ı�ǩ��info[1]�Ľ���  -------δ���
					String eqpFlag = deleteBlank(info[0]);                             //�豸��־λ
					String eqpId = deleteBlank(info[1]);                               //�豸���к�
					String eqpType = "��ʾ��";
					String eqpTime =info[2];
				
					
					/*
					 * �жϸ�����Ϣ�Ƿ��Ѿ������
					 * ����ı����flagΪ"0"������δ����
					 * ��Ϊ���������д��������ж���һ����ǩ��Ϣ
					 * */
					
					if (eqpFlag.equals("0"))	{
						/*
						 * �ж��Ƿ�Ϊ�ö�д������ĵ�һ���豸��Ϣ
						 * �������ӽ�������Ϣ�������ݿ�
						 * �����ж��Ƿ�Ϊ�Ѿ�������ı�ǩ��Ϣ
						 * */
						if(v_eqpId.size() == 0){
							/*
							 * �жϸñ�ǩ���Ƿ�Ϊ��ʵ�����豸
							 * ������������������
							 * ������ʾ������Ϣ��������
							 * */	
							if(checkStrInV(eqpId,v_eqpId_db)){                                    //��ȡ���ı�ǩ��Ϊ��ʵ�����豸
System.out.println("��һ����ʵ�����豸" + eqpId);					
							
							v_eqpId.add(eqpId);
							v_readingTime.add(eqpTime);
							v_flag.add(1);
							
							//----������ݿ�������
							sql = "update room set eqp_IN = '1' where eqp_Id = '"+ eqpId+"'";
							
System.out.println("��133�У��豸"+eqpId+"��ʵ����"+roomId);
							v = new Vector<String>();                                //�������Ϣ��ʾ����
							v.add(eqpId);v.add("��");v.add(eqpTime);
							roomshow.updateTable(v);
							
//							String str = eqpId + " " + "���" + " "+eqpTime +"\r\n";
//							txtShow.writeStr(txtAddress,str);
							
							db.updateDB(sql);
							
							//***2013-06-21���޸ģ�����route��
							String sql1 = "insert into route(eqp_Id,movetime,action) values('"+eqpId +"',to_date('"+eqpTime+"','yyyy-MM-DD hh24:mi:ss'),'����-"+roomId+"')";
//System.out.println("��146�У�"+sql1);
							db.updateDB(sql1);
//System.out.println("��148�У�");
							//db.close();
							}
							else if(!checkStrInV(eqpId,v_eqpId_db)){
								if(!checkStrInV(eqpId,v_eqpIdError)){
								v_eqpIdError.add(eqpId);
								String massage = "��ǩ��Ϊ"+eqpId+"���豸���Ǳ�ʵ�����豸����"+eqpTime +"ʱ�Ƿ�����!!";
								JOptionPane.showMessageDialog(roomshow, massage,"����",JOptionPane.ERROR_MESSAGE);       //��������Ի���
									}
								}
						      }
						
						/*
						 * �ж��Ƿ�Ϊ�Ѿ�������ı�ǩ��Ϣ
						 * ����Ǽ����ж��Ƿ���Ѿ�������ı�ǩ��ϢΪ��ͬ������̣���ͬ�Ľ�/�����̣�
						 *       ���Ϊ��ͬ�������޸����ݿ�
						 *       ���򽫸�����Ϣ���Ϊ�Ѷ�/ɾ��
						 * ���Ϊ�¶����ǩ��Ϣ������ǩ��Ϣ�������ݿ�      
						 * */
						 else if(checkEqpId(eqpId)){  //�豸��������������ڣ���Ϊ�Ѷ�����ı�ǩ��Ϣ
System.out.println("��168�У��豸���к��Ѿ������");
System.out.println("��169�У�" + v_eqpId);
							


							if(checkReadingTime(v_readingTime.get(v_eqpId.indexOf(eqpId)),eqpTime)){//ʱ������60��
System.out.println("ʱ������60��-------");




								int i = 0;
								if(checkStrInV(eqpId,v_eqpId_db)){                                    //��ȡ���ı�ǩ��Ϊ��ʵ�����豸
								//�޸�v_readingTime
								v_readingTime.set(v_eqpId.indexOf(eqpId), eqpTime);
								i = v_flag.get(v_eqpId.indexOf(eqpId));       //�ڿ��־λ
								i = -i;											  //��־λȡ��
								v_flag.set(v_eqpId.indexOf(eqpId), i);
								
								//�������ݿ�
								if(i>0){                                         //��
									sql = "update room set eqp_in = 1 where eqp_Id = '" + eqpId +"'";
									
									System.out.println("�豸"+eqpId+"��ʵ����" + roomId);
									v = new Vector<String>();                                //���ÿһ���������Ϣ
									v.add(eqpId);v.add("��");v.add(eqpTime);
									roomshow.updateTable(v);
//									
									//***2013-06-21���޸ģ�����route��
									String sql1 = "insert into route(eqp_Id,movetime,action) values('"+eqpId +"',to_date('"+eqpTime+"','yyyy-MM-DD hh24:mi:ss'),'����-"+roomId+"')";
									Database db1=new Database();
									db1.updateDB(sql1);
									db1.close();
									
//									String str = eqpId + " " + "���" +" "+ eqpTime +"\r\n";
//									txtShow.writeStr(txtAddress,str);
									
								}
								if(i<0){                                                  //��
									sql = "update room set eqp_in = 0 where eqp_Id = '" + eqpId +"'";
									
									System.out.println("�豸"+eqpId+"��ʵ����" + roomId);
									v = new Vector<String>();                                //���ÿһ���������Ϣ
									v.add(eqpId);v.add("��");v.add(eqpTime);
									roomshow.updateTable(v);
									
									
									//***2013-06-21���޸ģ�����route��
									String sql1 = "insert into route(eqp_Id,movetime,action) values('"+eqpId +"',to_date('"+eqpTime+"','yyyy-MM-DD hh24:mi:ss'),'�뿪"+roomId+"')";
									Database db1=new Database();
									db1.updateDB(sql1);
									db1.close();
//									String str = eqpId + " " + "����" + " "+eqpTime +"\r\n";
//									txtShow.writeStr(txtAddress,str);
								}
								
								db.updateDB(sql);
								}
								
								
								
								
								
								else if(!checkStrInV(eqpId,v_eqpId_db)){
									if((!checkStrInV(eqpId,v_eqpIdError))&&(i>0)){
									v_eqpIdError.add(eqpId);
									String massage = "��ǩ��Ϊ"+eqpId+"���豸���Ǳ�ʵ�����豸����"+eqpTime +"ʱ�Ƿ�����!!";
									JOptionPane.showMessageDialog(roomshow, massage,"����",JOptionPane.ERROR_MESSAGE);       //��������Ի���
										}
								}

								
							}
							else{
								System.out.println("ʱ���С��60�룬��ʾΪͬһ�ν�/��");

							}
						}
						else{
							System.out.println("�������к�Ϊ�¶��룬�����к���ӽ����飬���������ݿ����");
							if(checkStrInV(eqpId,v_eqpId_db)){                                    //��ȡ���ı�ǩ��Ϊ��ʵ�����豸
							v_eqpId.add(eqpId);
							v_readingTime.add(eqpTime);
							v_flag.add(1);
							
							//----������ݿ�������
							sql = "update room set eqp_in = '1'where eqp_Id = '" + eqpId +"'";
//							System.out.println("sql���Ϊ��" + sql);
							
							System.out.println("�豸"+eqpId+"��ʵ����" + roomId);
							v = new Vector<String>();                                //���ÿһ���������Ϣ
							v.add(eqpId);v.add("��");v.add(eqpTime);
							roomshow.updateTable(v);
							
							
							String sql1 = "insert into route(eqp_Id,movetime,action) values('"+eqpId +"',to_date('"+eqpTime+"','yyyy-MM-DD hh24:mi:ss'),'����-"+roomId+"')";
							Database db1=new Database();
							db1.updateDB(sql1);
							db1.close();
							
							
							
//							String str = eqpId + " " + "���" +" "+ eqpTime +" "+"\r\n";
//							txtShow.writeStr(txtAddress,str);
							
							
							db.updateDB(sql);
							
							}
							else if(!checkStrInV(eqpId,v_eqpId_db)) {
								if(!checkStrInV(eqpId,v_eqpIdError)){
									v_eqpIdError.add(eqpId);
									String massage = "��ǩ��Ϊ"+eqpId+"���豸���Ǳ�ʵ�����豸����"+eqpTime +"ʱ�Ƿ�����!!";
									JOptionPane.showMessageDialog(roomshow, massage,"����",JOptionPane.ERROR_MESSAGE);       //��������Ի���
										}
						 }
						
						
						
//						System.out.println("�����豸���к�Ϊ��"+v_eqpId);
//						System.out.println("�����豸����ʱ�䣺"+v_readingTime);
						
						
						
					}
				
				
				//�Ǳ�ʵ�����豸����
//				else{
//					String massage = "��ǩ��Ϊ"+eqpId+"���豸���Ǳ�ʵ�����豸����"+eqpTime +"ʱ�Ƿ�����!!";
//					JOptionPane.showMessageDialog(roomshow, massage,"����",JOptionPane.ERROR_MESSAGE);       //��������Ի���
//					
//				}
						System.out.println("��296�У�һ�����ݴ������"+v_eqpId);
				}
				}
				/*
				 * ����ı��ļ�
				 * */
				BufferedWriter output = new BufferedWriter(new FileWriter(fileadderss));
		        output.write("");
				input.close(); 
				output.close();
				
		}catch (Exception e) {} 
	}
}
		
	/*
	 * ������ڲ������豸���к�eqpId����ȡʱ��readingTime
	 * 
	 * �������ܣ��ж���ڲ���eqpId������豸����v_eqpId�����
	 *          �����ڷ���true��
	 * */
	public boolean checkEqpId(String eqpId){
		boolean flag = false;
		for(int i = 0;i<v_eqpId.size();i++){
			String Id = v_eqpId.get(i);
			if(Id.equals(eqpId)){                 //�������Ѿ�����
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/*
	 * ��������������������ŵ�һ���豸���к�vReadingTime�� ���ı��ж�ȡ���豸���к�readingTime
	 * 
	 * �������ܣ�����ͬһ���豸���ζ����ʱ��
	 *  		��ʱ������60�룬�򷵻�true
	 * */
	public boolean checkReadingTime(String vReadingTime,String readingTime){
		boolean flag = false;
		long between = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
		    java.util.Date d1 =  df.parse(vReadingTime);
		    java.util.Date d2 = df.parse(readingTime);
		    between=(d2.getTime()-d1.getTime())/1000;//����1000��Ϊ��ת������
//		    System.out.println("ʱ���Ϊ��" + between);
		}catch (Exception e){}
		
		if (between > interval){flag = true;}

		return flag;
	}
	
	/*
	 * ������ڲ������ַ���initialStr
	 * �������ܣ�ȡ�������ַ����еĿո񣬰���ǰ��Ŀո�
	 * ��������ֵ�����������ո���ַ���
	 * */
	public String deleteBlank(String initialStr){
		return initialStr.replaceAll(" ", "");
	}
	
	
	/*
	 * �������
	 * 
	 * ���ܣ�
	 * 
	 * */
	public boolean checkStrInV(String str ,Vector<String> v){
		System.out.println("---------����Ƿ�Ϊ��ʵ�����豸-----");
		System.out.println("��ʵ�����豸-----��"+v);
		boolean flag = false;
		for(int i = 0;i<v.size();i++){
			if (str.equals(v.get(i))){
				flag = true;
				break;
			}
		}
		
		return flag;
	}
	
	
	
	public static void main(String[] args) {
		DataDisposing_Room dd_r = new DataDisposing_Room();
		//dd_r.readeTxt("d:\\roomInformation.txt","1406");
		dd_r.readeTxt("d:\\storageInformation.txt","1406");
		
	}

}
