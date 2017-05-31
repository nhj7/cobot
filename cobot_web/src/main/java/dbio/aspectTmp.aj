package dbio;

import org.aspectj.lang.Signature;

public aspect aspectTmp {
	pointcut callsay(): call (* java.sql.Statement.executeQuery (..));

	 

    before(): callsay () {
		
    	Signature sig = thisJoinPointStaticPart.getSignature(); 
    	
    	Object[] args = thisJoinPoint.getArgs();
    	
		
		System.out.println("point cut : " + sig );
		System.out.println("param : " + args[0] );

    }

    after(): callsay () {

        System.out.println("Good bye");

  }
    
    pointcut callController(): call (* *Controller.*(..));
    
    before(): callController () {
		
    	Signature sig = thisJoinPointStaticPart.getSignature(); 
    	
    	Object[] args = thisJoinPoint.getArgs();
    	
		
		System.out.println("point cut : " + sig );
		System.out.println("param : " + args[0] );

    }
}
