package nhj.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IInvestmentAPI {
	public abstract Map returnBalances() throws Throwable;	
	
	public abstract List returnDepositAddresses() throws Throwable;	
	
	public abstract List returnDepositsWithdrawals() throws Throwable;	
	
	public abstract List returnTicker() throws Throwable;	
	
	
	  
	
	public abstract void buy() throws Throwable;
	public abstract void sell() throws Throwable;

	public Map buy(String currencyPair, BigDecimal rate, BigDecimal amount) throws Throwable;
}
