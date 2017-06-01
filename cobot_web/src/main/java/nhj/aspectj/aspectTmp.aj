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

		System.out.println("point cut : " + sig);
		System.out.println("param : " + args[0]);

	}

	after(): callsay () {

		// System.out.println("Good bye");

	}

	pointcut callController(): call (* *Controller.* (..));

	before(): callController () {

		Signature sig = thisJoinPointStaticPart.getSignature();

		Object[] args = thisJoinPoint.getArgs();

		// System.out.println("point cut : " + sig);
		System.out.println("Controller param : " + args[0]);

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
		System.out.println("doConCheck : "+joinPoint.getSignature()); // For testing purposes.
		System.out.println("Controller param : " + args[1]);
		

		// System.out.println("point cut : " + sig);
		
		
	}

}
