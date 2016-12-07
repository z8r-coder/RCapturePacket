
//∞¸ Ù–‘
public class PacketAtrr {
	private int length;
	private String Sourceaddr;
	private String DestinationAddr;
	private String Protocol;
	private StringBuilder sb;
	public PacketAtrr(int length,String sender_protoaddr,String target_protoaddr,String Protocol) {
		// TODO Auto-generated constructor stub
		this.Sourceaddr = sender_protoaddr;
		this.DestinationAddr = target_protoaddr;
		this.length = length;
		this.Protocol = Protocol;
	}
	
	public String getSourceaddr() {
		return Sourceaddr;
	}
	
	public void setSourceaddr(String sourceaddr) {
		this.Sourceaddr = sourceaddr;
	}
	
	public String getDestinationAddr() {
		return DestinationAddr;
	}
	public void setDestinationAddr(String destinationAddr) {
		DestinationAddr = destinationAddr;
	}
	
	public String getProtocol() {
		return Protocol;
	}
	
	public void setProtocol(String protocol) {
		Protocol = protocol;
	}
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	public void setDes(StringBuilder sb) {
		this.sb = sb;
	}
	
	public StringBuilder getDes() {
		return sb;
	}
}
