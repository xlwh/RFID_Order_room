/*11月13日
 * 
 * 功能：完成实验室设备出入逻辑判断
 *       验证该设备是否为本实验室设备
 *       
 * 待完成工作：读取标签信息的解析：解析出设备类型
 *            非本库设备进入时报警功能
 * 
 * 待改进： 当有非本库设备入库时，出现错误对话框提示，但此时程序停止运行
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
	private static long interval =60;         //时间间隔设定为60秒，两次读入时间差大于60秒看做为两次不同的进/出
	//private static String txtAddress = "txtInformation/roomInformation.txt";//存放出入库信息
	
	
	Vector<String> v_eqpId =null;
	Vector<String> v_readingTime = null;
	Vector<Integer> v_flag = null;
	Vector<String> v_eqpIdError = null;       //记录非本库设备

	String sql = null;
	
	RoomShow_V2 roomshow ;
	Vector<String> v = new Vector<String>();
	
	String roomId="1406";
	
	/*
	 * 函数入库参数：读取文件的路径fileaddress(String)
	 * 
	 * 函数功能：将txt文件里的内容按行读出，将一行信息并按','分隔后存入到数组里，即可将一条读写器信息获得
	 * */
	public void readeTxt(String fileadderss,String roomId){
		

		roomshow = new RoomShow_V2(roomId);                                               //实例化仓库出入信息界面
		roomshow.setBounds(100,100,700,700);
		roomshow.getJlb_state().setText("读取状态");
		roomshow.setVisible(true);
		roomshow.repaint();
		roomshow.pack();
		
		TxtShow txtShow = new TxtShow();                                                //将入库信息存入txt方便管理员查看
		
		String s = new String();                     									//存放从txt里读取的行信息
		v_eqpId = new Vector<String>();													//存放标签
		v_readingTime = new Vector<String>();											//读入时间
		v_flag = new Vector<Integer>();													//设备在库标志位
		v_eqpIdError = new Vector<String>();

		//String txt = "flag,读入的标签号,读取时间"+"\r\n";	
		
	while(true){
		try { 
			
			/*
			 * 功能：从数据库查询出该实验室（room）里的所有设备存放到数组v_eqpId_db里
			 *       便于检测读到的设备号是否为本实验室设备
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
			
			
			
			
			
			    //读取流
				BufferedReader input = new BufferedReader(new FileReader(fileadderss)); 
				input.readLine();                     									//从第二行开始读取并存放到数组里
				while((s = input.readLine())!=null){  									//判断是否读到了最后一行
	System.out.println("////////////********************----------------");
					Database db = new Database();
					String info[] = s.split(",");    									//将行信息以','为分隔符存入数组
					
					// 对读取到的标签号info[1]的解析  -------未完成
					String eqpFlag = deleteBlank(info[0]);                             //设备标志位
					String eqpId = deleteBlank(info[1]);                               //设备序列号
					String eqpType = "显示器";
					String eqpTime =info[2];
				
					
					/*
					 * 判断该条信息是否已经处理过
					 * 如果文本里的flag为"0"表明还未处理
					 * 如为处理过则进行处理，否则判断下一条标签信息
					 * */
					
					if (eqpFlag.equals("0"))	{
						/*
						 * 判断是否为该读写器读入的第一条设备信息
						 * 如果是则接将该条信息存入数据库
						 * 否则判断是否为已经读入过的标签信息
						 * */
						if(v_eqpId.size() == 0){
							/*
							 * 判断该标签号是否为本实验室设备
							 * 如果是则继续后续操作
							 * 否则显示经过信息，并报警
							 * */	
							if(checkStrInV(eqpId,v_eqpId_db)){                                    //读取到的标签号为本实验室设备
System.out.println("第一个本实验室设备" + eqpId);					
							
							v_eqpId.add(eqpId);
							v_readingTime.add(eqpTime);
							v_flag.add(1);
							
							//----添加数据库插入语句
							sql = "update room set eqp_IN = '1' where eqp_Id = '"+ eqpId+"'";
							
System.out.println("第133行：设备"+eqpId+"进实验室"+roomId);
							v = new Vector<String>();                                //出入库信息显示界面
							v.add(eqpId);v.add("进");v.add(eqpTime);
							roomshow.updateTable(v);
							
//							String str = eqpId + " " + "入库" + " "+eqpTime +"\r\n";
//							txtShow.writeStr(txtAddress,str);
							
							db.updateDB(sql);
							
							//***2013-06-21日修改：更改route表
							String sql1 = "insert into route(eqp_Id,movetime,action) values('"+eqpId +"',to_date('"+eqpTime+"','yyyy-MM-DD hh24:mi:ss'),'进入-"+roomId+"')";
//System.out.println("第146行："+sql1);
							db.updateDB(sql1);
//System.out.println("第148行：");
							//db.close();
							}
							else if(!checkStrInV(eqpId,v_eqpId_db)){
								if(!checkStrInV(eqpId,v_eqpIdError)){
								v_eqpIdError.add(eqpId);
								String massage = "标签号为"+eqpId+"的设备不是本实验室设备，在"+eqpTime +"时非法进入!!";
								JOptionPane.showMessageDialog(roomshow, massage,"错误",JOptionPane.ERROR_MESSAGE);       //弹出错误对话框
									}
								}
						      }
						
						/*
						 * 判断是否为已经读入过的标签信息
						 * 如果是继续判断是否和已经读入过的标签信息为不同读入过程（不同的进/出过程）
						 *       如果为不同过程则修改数据库
						 *       否则将该条信息标记为已读/删除
						 * 如果为新读入标签信息，将标签信息插入数据库      
						 * */
						 else if(checkEqpId(eqpId)){  //设备号已在数组里存在，即为已读入过的标签信息
System.out.println("第168行：设备序列号已经读入过");
System.out.println("第169行：" + v_eqpId);
							


							if(checkReadingTime(v_readingTime.get(v_eqpId.indexOf(eqpId)),eqpTime)){//时间差大于60秒
System.out.println("时间差大于60秒-------");




								int i = 0;
								if(checkStrInV(eqpId,v_eqpId_db)){                                    //读取到的标签号为本实验室设备
								//修改v_readingTime
								v_readingTime.set(v_eqpId.indexOf(eqpId), eqpTime);
								i = v_flag.get(v_eqpId.indexOf(eqpId));       //在库标志位
								i = -i;											  //标志位取反
								v_flag.set(v_eqpId.indexOf(eqpId), i);
								
								//更新数据库
								if(i>0){                                         //进
									sql = "update room set eqp_in = 1 where eqp_Id = '" + eqpId +"'";
									
									System.out.println("设备"+eqpId+"进实验室" + roomId);
									v = new Vector<String>();                                //存放每一条读入的信息
									v.add(eqpId);v.add("进");v.add(eqpTime);
									roomshow.updateTable(v);
//									
									//***2013-06-21日修改：更改route表
									String sql1 = "insert into route(eqp_Id,movetime,action) values('"+eqpId +"',to_date('"+eqpTime+"','yyyy-MM-DD hh24:mi:ss'),'进入-"+roomId+"')";
									Database db1=new Database();
									db1.updateDB(sql1);
									db1.close();
									
//									String str = eqpId + " " + "入库" +" "+ eqpTime +"\r\n";
//									txtShow.writeStr(txtAddress,str);
									
								}
								if(i<0){                                                  //出
									sql = "update room set eqp_in = 0 where eqp_Id = '" + eqpId +"'";
									
									System.out.println("设备"+eqpId+"出实验室" + roomId);
									v = new Vector<String>();                                //存放每一条读入的信息
									v.add(eqpId);v.add("出");v.add(eqpTime);
									roomshow.updateTable(v);
									
									
									//***2013-06-21日修改：更改route表
									String sql1 = "insert into route(eqp_Id,movetime,action) values('"+eqpId +"',to_date('"+eqpTime+"','yyyy-MM-DD hh24:mi:ss'),'离开"+roomId+"')";
									Database db1=new Database();
									db1.updateDB(sql1);
									db1.close();
//									String str = eqpId + " " + "出库" + " "+eqpTime +"\r\n";
//									txtShow.writeStr(txtAddress,str);
								}
								
								db.updateDB(sql);
								}
								
								
								
								
								
								else if(!checkStrInV(eqpId,v_eqpId_db)){
									if((!checkStrInV(eqpId,v_eqpIdError))&&(i>0)){
									v_eqpIdError.add(eqpId);
									String massage = "标签号为"+eqpId+"的设备不是本实验室设备，在"+eqpTime +"时非法出库!!";
									JOptionPane.showMessageDialog(roomshow, massage,"错误",JOptionPane.ERROR_MESSAGE);       //弹出错误对话框
										}
								}

								
							}
							else{
								System.out.println("时间差小于60秒，表示为同一次进/出");

							}
						}
						else{
							System.out.println("读入序列号为新读入，将序列号添加进数组，并进行数据库操作");
							if(checkStrInV(eqpId,v_eqpId_db)){                                    //读取到的标签号为本实验室设备
							v_eqpId.add(eqpId);
							v_readingTime.add(eqpTime);
							v_flag.add(1);
							
							//----添加数据库插入语句
							sql = "update room set eqp_in = '1'where eqp_Id = '" + eqpId +"'";
//							System.out.println("sql语句为：" + sql);
							
							System.out.println("设备"+eqpId+"进实验室" + roomId);
							v = new Vector<String>();                                //存放每一条读入的信息
							v.add(eqpId);v.add("进");v.add(eqpTime);
							roomshow.updateTable(v);
							
							
							String sql1 = "insert into route(eqp_Id,movetime,action) values('"+eqpId +"',to_date('"+eqpTime+"','yyyy-MM-DD hh24:mi:ss'),'进入-"+roomId+"')";
							Database db1=new Database();
							db1.updateDB(sql1);
							db1.close();
							
							
							
//							String str = eqpId + " " + "入库" +" "+ eqpTime +" "+"\r\n";
//							txtShow.writeStr(txtAddress,str);
							
							
							db.updateDB(sql);
							
							}
							else if(!checkStrInV(eqpId,v_eqpId_db)) {
								if(!checkStrInV(eqpId,v_eqpIdError)){
									v_eqpIdError.add(eqpId);
									String massage = "标签号为"+eqpId+"的设备不是本实验室设备，在"+eqpTime +"时非法进入!!";
									JOptionPane.showMessageDialog(roomshow, massage,"错误",JOptionPane.ERROR_MESSAGE);       //弹出错误对话框
										}
						 }
						
						
						
//						System.out.println("入库的设备序列号为："+v_eqpId);
//						System.out.println("入库的设备读入时间："+v_readingTime);
						
						
						
					}
				
				
				//非本实验室设备入内
//				else{
//					String massage = "标签号为"+eqpId+"的设备不是本实验室设备，在"+eqpTime +"时非法进入!!";
//					JOptionPane.showMessageDialog(roomshow, massage,"错误",JOptionPane.ERROR_MESSAGE);       //弹出错误对话框
//					
//				}
						System.out.println("第296行：一条数据处理结束"+v_eqpId);
				}
				}
				/*
				 * 清空文本文件
				 * */
				BufferedWriter output = new BufferedWriter(new FileWriter(fileadderss));
		        output.write("");
				input.close(); 
				output.close();
				
		}catch (Exception e) {} 
	}
}
		
	/*
	 * 函数入口参数：设备序列号eqpId，读取时间readingTime
	 * 
	 * 函数功能：判断入口参数eqpId在入库设备数组v_eqpId里存在
	 *          若存在返回true；
	 * */
	public boolean checkEqpId(String eqpId){
		boolean flag = false;
		for(int i = 0;i<v_eqpId.size();i++){
			String Id = v_eqpId.get(i);
			if(Id.equals(eqpId)){                 //数组里已经存在
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/*
	 * 函数输入参数：数组里存放的一个设备序列号vReadingTime， 从文本中读取的设备序列号readingTime
	 * 
	 * 函数功能：计算同一个设备两次读入的时间差，
	 *  		若时间差大于60秒，则返回true
	 * */
	public boolean checkReadingTime(String vReadingTime,String readingTime){
		boolean flag = false;
		long between = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
		    java.util.Date d1 =  df.parse(vReadingTime);
		    java.util.Date d2 = df.parse(readingTime);
		    between=(d2.getTime()-d1.getTime())/1000;//除以1000是为了转换成秒
//		    System.out.println("时间差为：" + between);
		}catch (Exception e){}
		
		if (between > interval){flag = true;}

		return flag;
	}
	
	/*
	 * 函数入口参数：字符串initialStr
	 * 函数功能：取出输入字符串中的空格，包括前后的空格
	 * 函数返回值：不不包含空格的字符串
	 * */
	public String deleteBlank(String initialStr){
		return initialStr.replaceAll(" ", "");
	}
	
	
	/*
	 * 输入参数
	 * 
	 * 功能：
	 * 
	 * */
	public boolean checkStrInV(String str ,Vector<String> v){
		System.out.println("---------检测是否为本实验室设备-----");
		System.out.println("本实验室设备-----："+v);
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
