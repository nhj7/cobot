package nhj.api;

public class VoDeal {
	public String type;	// deposits : �Ա�, withdrawals : ���
	public String currency;
	public String address;
	public double amount;
	public short confirmations;  
	public String txid;
	public long timestamp;
	public String status;
	@Override
	public String toString() {
		return "VoDeal [type=" + type + ", currency=" + currency + ", address=" + address + ", amount=" + amount
				+ ", confirmations=" + confirmations + ", txid=" + txid + ", timestamp=" + timestamp + ", status="
				+ status + "]";
	}
	
	
}
