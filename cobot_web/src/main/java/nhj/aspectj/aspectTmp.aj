package nhj.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

public aspect aspectTmp {
	pointcut callsay(): call (* java.sql.Statement.executeQuery (..));

	before(): callsay () {

		Signature sig = thisJoinPointStaticPart.getSignature();

		Object[] args = thisJoinPoint.getArgs();

		System.out.println("[aspectJ] point cut : " + sig);
		System.out.println("[aspectJ] param : " + args[0]);

	}

	after(): callsay () {

		// System.out.println("Good bye");

	}

	pointcut callController(): call (* *Controller.* (..));

	before(): callController () {

		Signature sig = thisJoinPointStaticPart.getSignature();

		Object[] args = thisJoinPoint.getArgs();

		// System.out.println("point cut : " + sig);
		System.out.println("[aspectJ] Controller param : " + args[0]);

	}

	@Pointcut("within(@org.springframework.stereotype.Controller *)")
	public void controller() {
	}

	@Pointcut("execution(* *(..))")
	public void method() {
	}

	@Before("controller() && method()")
	public void doConCheck(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		//System.out.println("[aspectJ] doConCheck : "+joinPoint.getSignature()); // For testing purposes.
		if( args != null && args.length > 1 ){
			//System.out.println("[aspectJ] Controller param : " + args[1]);
		}
	}
	
	/*
	@Before("execution(* kr.co.cobot.entity.*.*(..))")
	public void setEntity(JoinPoint joinPoint) {

		Object[] args = joinPoint.getArgs();
		//System.out.println("[aspectJ] doConCheck : "+joinPoint.getSignature()); // For testing purposes.
		if( args != null && args.length > 0 ){
			System.out.println("[aspectJ] setEntity : " + args[0]);
		}

	}
	*/
	
	
	

}
