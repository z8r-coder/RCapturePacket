
//∞¸ Ù–‘
public class PacketAtrr {
	private int length;
	private byte[] Sourceaddr;
	private byte[] DestinationAddr;
	private String Protocol;
	public PacketAtrr(int length,byte[] sender_protoaddr,byte[] target_protoaddr,String Protocol) {
		// TODO Auto-generated constructor stub
		this.Sourceaddr = sender_protoaddr;
		this.DestinationAddr = target_protoaddr;
		this.length = length;
		this.Protocol = Protocol;
	}
	
	public byte[] getSourceaddr() {
		return Sourceaddr;
	}
	
	public void setSourceaddr(byte[] sourceaddr) {
		this.Sourceaddr = sourceaddr;
	}
	
	public byte[] getDestinationAddr() {
		return DestinationAddr;
	}
	public void setDestinationAddr(byte[] destinationAddr) {
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
}
