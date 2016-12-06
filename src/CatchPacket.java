import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JOptionPane;
import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.ARPPacket;
import jpcap.packet.DatalinkPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

public class CatchPacket implements PacketReceiver,Runnable{
	public NetworkInterface[] devices;
	public NetworkInterface device;//该网卡进行监听
	public JpcapCaptor jCaptor;
	public LinkedList<Packet> packets = new LinkedList();
	public HashMap<NetworkInterface, StringBuilder> hm_inface_sb = new HashMap<>();
	public LinkedList<PacketAtrr> vc_patrr = new LinkedList<>();
	static StringBuilder sb_analysis = new StringBuilder();
	
	//获得网络接口
	public NetworkInterface[] getDevices() {
		devices = JpcapCaptor.getDeviceList();
		return devices;
	}
	//获得网卡list描述
	public void desNetworkInterface() {
		for(int i = 0; i < devices.length; i++){
			StringBuilder sb_network = new StringBuilder(); 
			sb_network.append("接口" + (i + 1) + ":\n");
			sb_network.append("接口名称:" + devices[i].name + "\n");


			if (!(devices[i].name.contains("GenericDialupAdapter"))) {
				device = devices[i];
			}
			
			sb_network.append("网络接口描述:" + devices[i].description + "\n");
			sb_network.append("数据链路层名称:" + devices[i].datalink_name + "\n");
			sb_network.append("数据链路层描述:" + devices[i].description + "\n");
			//loopback为环回地址
			sb_network.append("是否存在LOOPBACK设备:" + devices[i].loopback + "\n");
			sb_network.append("MAC地址:");
			int count = 0;
			for(byte b:devices[i].mac_address){
				count++;
				if (count < devices[i].mac_address.length) {
					sb_network.append(Integer.toHexString(b & 0xff) + ":");
				}else {
					sb_network.append(Integer.toHexString(b & 0xff) + "\n");
				}
			}
			hm_inface_sb.put(devices[i], sb_network);
		}
	}
	//获得某个网卡的连接
	public void getCap(NetworkInterface nInterface, boolean mixMode,String filter){
		try {
			/*参数含义：
			 * 1.需要使用的某个网卡接口
			 * 2.一次性抓取包的最大长度
			 * 3.设置是否混杂模式。处于混杂模式将接收所有数据包
			 * ，如果设置为混杂模式后调用了包过滤函数setFilter()将不起任何作用；
			 * 4.指定超时的时间*/
			jCaptor = JpcapCaptor.openDevice(nInterface, 65535, mixMode, 20);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//设置过滤器
	public void setFilter(String filter) {
		try {
			jCaptor.setFilter(filter, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//设置抓包模式开始抓包
	public void beginCatch() {
		jCaptor.processPacket(1, this);
		//jCaptor.loopPacket(-1, new CatchPacket());
	}
	
	//结束抓包
	public void endCatch() {
		if (jCaptor != null) {
			jCaptor.breakLoop();
		}
	}
	
	//将捕获的包存到文件中
	public void saveFile(String fileName) {
		if (jCaptor == null) {
			JOptionPane.showMessageDialog(null, "未捕获到包！");
		}else {
			try {
				JpcapWriter writer = JpcapWriter.openDumpFile(jCaptor, fileName);
				while(packets.size() != 0){
					writer.writePacket(packets.removeFirst());
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//打开存在文件中的包
	public void openFile(String fileName) {
		try {
			jCaptor = jCaptor.openFile(fileName);
			while(true){
				Packet packet = jCaptor.getPacket();
				if (packet == null) {//??
					break;
				}
				analysis(packet);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (jCaptor != null) {
				jCaptor.close();
			}
		}
	}
	public void analysis(Packet packet) {
		sb_analysis = new StringBuilder();
		sb_analysis.append("Captured Length:"+packet.caplen+" byte\n");
		sb_analysis.append("Length of this Packet:"+packet.len+" byte\n");
		sb_analysis.append("Header:"+packet.header+"\n");
		sb_analysis.append("Length of Header:"+packet.header.length+" byte\n");
		sb_analysis.append("Data:"+packet.data+"\n");
		sb_analysis.append("Length of Data:"+packet.data.length+" byte\n");
		DatalinkPacket dpacket = packet.datalink;
		if (dpacket instanceof EthernetPacket) {//以太网帧
			EthernetPacket ePacket = (EthernetPacket)dpacket;
			int count = 0;
			int count2 = 0;
			sb_analysis.append("src_mac:");
			for(byte b:ePacket.src_mac){
				count++;
				if (count < ePacket.src_mac.length) {
					sb_analysis.append(Integer.toHexString(b & 0xff)+" : ");
				}else {
					sb_analysis.append(Integer.toHexString(b & 0xff) + "\n");
				}
			}
			sb_analysis.append("dst_mac:");
			for(byte b : ePacket.dst_mac){
				count2++;
				if (count2 < ePacket.dst_mac.length) {
					sb_analysis.append(Integer.toHexString(b & 0xff) + ":");
				}else {
					sb_analysis.append(Integer.toHexString(b & 0xff) + "\n");
				}
			}
			sb_analysis.append("frametype:" + Integer.toHexString(ePacket.frametype & 0xffff) + "\n");
		}else {
			sb_analysis.append(dpacket + "\n");
		}
		if(packet instanceof ARPPacket){               //分析ARP协议
			sb_analysis.append("---ARP---\n");
			ARPPacket aPacket = (ARPPacket)packet;
			sb_analysis.append("硬件类型："+aPacket.hardtype+"\n");
			sb_analysis.append("协议类型："+aPacket.prototype+"\n");
			sb_analysis.append("硬件地址长度："+aPacket.hlen+"\n");
			sb_analysis.append("协议地址长度："+aPacket.plen+"\n");
			sb_analysis.append("Operation："+aPacket.operation+"\n");
			sb_analysis.append("发送者硬件地址："+aPacket.sender_hardaddr+"\n");
			sb_analysis.append("发送者协议地址："+aPacket.sender_protoaddr+"\n");
			sb_analysis.append("目标硬件地址："+aPacket.target_hardaddr+"\n");
			sb_analysis.append("目标协议地址："+aPacket.target_protoaddr+"\n");
			sb_analysis.append("------------------\n");
			PacketAtrr pa = new PacketAtrr(aPacket.plen, aPacket.sender_protoaddr
					, aPacket.target_protoaddr, "ARP");
			vc_patrr.add(pa);
		}
		if(packet instanceof ICMPPacket){          //分析ICMP协议
			sb_analysis.append("---ICMP---\n");        
			ICMPPacket iPacket = (ICMPPacket)packet;
			sb_analysis.append("ICMP_TYPE:"+iPacket.type+"\n");
			sb_analysis.append("由于ICMP格式种类繁多，故省去不分析\n");
			sb_analysis.append("------------------\n");
		}
		if(packet instanceof IPPacket){        //分析IP
			IPPacket iPacket = (IPPacket)packet;
			sb_analysis.append("---IP版本: "+iPacket.version+" ---\n");
			if(iPacket.version==4){                //分析IPv4协议
				sb_analysis.append("Type of service:"+iPacket.rsv_tos+"\n");
				sb_analysis.append("Priprity:"+iPacket.priority+"\n");
				sb_analysis.append("Packet Length:"+iPacket.length+"\n");
				sb_analysis.append("Identification:"+iPacket.ident+"\n");
				sb_analysis.append("Don't Frag? "+iPacket.dont_frag+"\n");
				sb_analysis.append("More Frag? "+iPacket.more_frag+"\n");
				sb_analysis.append("Frag Offset:"+iPacket.offset+"\n");
				sb_analysis.append("Time to Live:"+iPacket.hop_limit+"\n");
				sb_analysis.append("Protocol:"+iPacket.protocol+"        (TCP = 6; UDP = 17)\n");
				sb_analysis.append("Source address:"+iPacket.src_ip.toString()+"\n");
				sb_analysis.append("Destination address:"+iPacket.dst_ip.toString()+"\n");
				sb_analysis.append("Options:"+iPacket.option+"\n");
				sb_analysis.append("------------------\n");
			}
			if(iPacket instanceof UDPPacket){      //分析UDP协议
				sb_analysis.append("---UDP---\n");
				UDPPacket uPacket = (UDPPacket)iPacket;
				sb_analysis.append("Source Port:"+uPacket.src_port+"\n");
				sb_analysis.append("Destination Port:"+uPacket.dst_port+"\n");
				sb_analysis.append("Length:"+uPacket.length+"\n");
				sb_analysis.append("------------------\n");
				if(uPacket.src_port==53||uPacket.dst_port==53){  //分析DNS协议
					sb_analysis.append("---DNS---\n");
					sb_analysis.append("此包已抓获，分析略...\n");
					sb_analysis.append("------------------\n");
				}
			}
			if(iPacket instanceof TCPPacket){      //分析TCP协议
				sb_analysis.append("---TCP---\n");
				TCPPacket tPacket = (TCPPacket)iPacket;
				sb_analysis.append("Source Port:"+tPacket.src_port+"\n");
				sb_analysis.append("Destination Port:"+tPacket.dst_port+"\n");
				sb_analysis.append("Sequence Number:"+tPacket.sequence+"\n");
				sb_analysis.append("Acknowledge Number:"+tPacket.ack_num+"\n");
				sb_analysis.append("URG:"+tPacket.urg+"\n");
				sb_analysis.append("ACK:"+tPacket.ack+"\n");
				sb_analysis.append("PSH:"+tPacket.psh+"\n");
				sb_analysis.append("RST:"+tPacket.rst+"\n");
				sb_analysis.append("SYN:"+tPacket.syn+"\n");
				sb_analysis.append("FIN:"+tPacket.fin+"\n");
				sb_analysis.append("Window Size:"+tPacket.window+"\n");
				sb_analysis.append("Urgent Pointer:"+tPacket.urgent_pointer+"\n");
				sb_analysis.append("Option:"+tPacket.option+"\n");
				sb_analysis.append("------------------\n");
				if(tPacket.src_port==80 || tPacket.dst_port==80){     //分析HTTP协议
					sb_analysis.append("---HTTP---\n");
					byte[] data = tPacket.data;
					if(data.length==0){
						sb_analysis.append("此为不带数据的应答报文！\n");
					}else{
						if(tPacket.src_port == 80){                 //接受HTTP回应
							String str = null;
							try {
								String str1 = new String(data,"UTF-8");
								if(str1.contains("HTTP/1.1")){
									str = str1;
								}else{
									String str2 = new String(data,"GB2312");
									if(str2.contains("HTTP/1.1")){
										str = str2;
									}else{
										String str3 = new String(data,"GBK");
										if(str3.contains("HTTP/1.1")){
											str = str3;
										}else{
											str = new String(data,"Unicode");
										}
									}
								}

								sb_analysis.append(str+"\n");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if(tPacket.dst_port==80){
							try{
								String str = new String(data,"ASCII");
								sb_analysis.append(str);
							}catch (Exception e) {
								// TODO: handle exception
							}
						}
					}
				}
			}
		}
		sb_analysis.append("\n");
		System.out.println(sb_analysis);
	}

	public HashMap<NetworkInterface, StringBuilder> getNetWorkDes() {
		return hm_inface_sb;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(5);
				beginCatch();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receivePacket(Packet packet) {
		// TODO Auto-generated method stub
		System.out.println(packet);
		packets.add(packet);
		analysis(packet);
	}

}
