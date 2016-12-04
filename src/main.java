import java.util.HashMap;

import jpcap.NetworkInterface;

public class main{   
       
   public static void main(String args[]){   
	   CatchPacket cp = new CatchPacket();
	   cp.getDevices();
	   cp.desNetworkInterface();
	   HashMap<NetworkInterface, StringBuilder> hm = cp.getNetWorkDes();
	   for(NetworkInterface sb : hm.keySet()){
		   System.out.println(hm.get(sb).toString());
	   }
	   cp.getCap(cp.device, true, "");
	   Thread thread = new Thread(cp);
	   thread.start();
   }
}
