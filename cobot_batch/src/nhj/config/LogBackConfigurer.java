package nhj.config;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.html.HTMLLayout;
import ch.qos.logback.classic.net.SMTPAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.spi.CyclicBufferTracker;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.FileSize;

public class LogBackConfigurer {
	public static void configure(String logPath, String logFileName) {
		
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		
		loggerContext.reset();

		/*
		StatusManager sm = loggerContext.getStatusManager();

		if (sm != null) {
			sm.add(new InfoStatus("Setting configuration from " + getClass().getName(), loggerContext));
		}
		*/
		

		/**
		 * Root Level
		 */
		Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);

		rootLogger.setLevel(Level.DEBUG);

		/**
		 * Custom Log Levels
		 */
		loggerContext.getLogger("jdbc.connection").setLevel(Level.WARN);
		loggerContext.getLogger("jdbc.sqlonly").setLevel(Level.WARN);
		loggerContext.getLogger("jdbc.audit").setLevel(Level.WARN);
		loggerContext.getLogger("jdbc.sqltiming").setLevel(Level.DEBUG);
		loggerContext.getLogger("jdbc.resultset").setLevel(Level.WARN);
		loggerContext.getLogger("jdbc.resultsettable").setLevel(Level.WARN);

		/**
		 * Console Appender
		 */
		configureConsoleAppender(loggerContext, rootLogger);

		/**
		 * File Appender
		 */
		configureFileAppender(loggerContext, rootLogger, logPath, logFileName);

		/**
		 * SMTP Mail Appender
		 */
		//configureMailAppender(loggerContext, rootLogger);
		

		/*
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
				.getLogger(Logger.ROOT_LOGGER_NAME);
		root.addAppender(fa);
		root.setLevel(Level.ALL);
		root.setAdditive(false);
		
		Appender console = root.getAppender("console");
		
		ch.qos.logback.classic.Logger conn_logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("jdbc.connection");
		conn_logger.setAdditive(false);
		conn_logger.setLevel(Level.WARN);
		conn_logger.addAppender(console);
		conn_logger.addAppender(fa);
		
		ch.qos.logback.classic.Logger sql_sqlonly_logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("jdbc.sqlonly");
		sql_sqlonly_logger.setAdditive(false);
		sql_sqlonly_logger.setLevel(Level.WARN);
		sql_sqlonly_logger.addAppender(console);
		sql_sqlonly_logger.addAppender(fa);
		
		ch.qos.logback.classic.Logger sql_audit_logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("jdbc.audit");
		sql_audit_logger.setAdditive(false);
		sql_audit_logger.setLevel(Level.WARN);
		sql_audit_logger.addAppender(console);
		sql_audit_logger.addAppender(fa);
		
		
		ch.qos.logback.classic.Logger sql_timing_logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("jdbc.sqltiming");
		sql_timing_logger.setAdditive(false);
		sql_timing_logger.setLevel(Level.DEBUG);
		sql_timing_logger.addAppender(console);
		sql_timing_logger.addAppender(fa);
		
		ch.qos.logback.classic.Logger sql_resultset_logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("jdbc.resultset");
		sql_resultset_logger.setAdditive(false);
		sql_resultset_logger.setLevel(Level.WARN);
		sql_resultset_logger.addAppender(console);
		sql_resultset_logger.addAppender(fa);
		
		ch.qos.logback.classic.Logger sql_resultsettable_logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("jdbc.resultsettable");
		sql_resultsettable_logger.setAdditive(false);
		sql_resultsettable_logger.setLevel(Level.WARN);
		sql_resultsettable_logger.addAppender(console);
		sql_resultsettable_logger.addAppender(fa);
		*/
		
		
	}

	protected static Appender configureConsoleAppender(LoggerContext loggerContext, Logger rootLogger) {
		ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<ILoggingEvent>();
		consoleAppender.setContext(loggerContext);
		consoleAppender.setName("myapp-log-console");
		consoleAppender.setEncoder(getEncoder(loggerContext));
		consoleAppender.start();

		rootLogger.addAppender(consoleAppender);
		
		return consoleAppender;
	}

	protected static Appender configureFileAppender(LoggerContext loggerContext, Logger rootLogger,String logPath, String logFileName) {
		
		String maxFileSize = "1";
		
		int maxHistory = 1;
		String fileLogLevel = "WARN"; // if needed

		logPath = parseLogPath(logPath);
		logFileName = parseLogFileName(logFileName);

		RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
		rollingFileAppender.setContext(loggerContext);
		rollingFileAppender.setName("log-file");
		rollingFileAppender.setFile(logPath + File.separator + logFileName + ".log");
		rollingFileAppender.setEncoder(getEncoder(loggerContext));
		rollingFileAppender.setAppend(true);

		TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
		rollingPolicy.setContext(loggerContext);
		rollingPolicy.setParent(rollingFileAppender);
		rollingPolicy.setFileNamePattern(logFileName + "-%d{yyyy-MM-dd}.log");
		rollingPolicy.setMaxHistory(maxHistory);
		rollingPolicy.start();

		SizeBasedTriggeringPolicy<ILoggingEvent> triggeringPolicy = new SizeBasedTriggeringPolicy<ILoggingEvent>();
		triggeringPolicy.setContext(loggerContext);
		triggeringPolicy.setMaxFileSize( new FileSize( Long.parseLong(maxFileSize) ) );
		triggeringPolicy.start();

		rollingFileAppender.setRollingPolicy(rollingPolicy);
		rollingFileAppender.setTriggeringPolicy(triggeringPolicy);

		ThresholdFilter fileThresholdFilter = new ThresholdFilter();
		fileThresholdFilter.setName("file-thresholdFilter");
		fileThresholdFilter.setLevel(fileLogLevel);
		fileThresholdFilter.start();

		rollingFileAppender.start();

		AsyncAppender fileAsyncAppender = new AsyncAppender();
		fileAsyncAppender.setContext(loggerContext);
		fileAsyncAppender.setName(rollingFileAppender.getName() + "-async");
		fileAsyncAppender.addAppender(rollingFileAppender);
		fileAsyncAppender.setDiscardingThreshold(0);
		fileAsyncAppender.start();

		rootLogger.addAppender(fileAsyncAppender);
		
		return fileAsyncAppender;
	}

	protected static void configureMailAppender(LoggerContext loggerContext, Logger rootLogger) {
		List<String> toMails = Arrays.asList("mail1@mail.com", "mail2@mail.com");
		String host = "mail.host";
		Integer port = 22;
		String username = "mail.user";
		String password = "mail.password";
		String from = "mail.from";
		String subject = "mail.subject";
		Boolean startTLS = true;
		String mailLogLevel = "ERROR"; // if needed

		HTMLLayout htmlLayout = new HTMLLayout();
		htmlLayout.setContext(loggerContext);
		htmlLayout.setTitle(subject);
		htmlLayout.start();

		SMTPAppender smtpAppender = new SMTPAppender();
		smtpAppender.setContext(loggerContext);
		smtpAppender.setName("log-mail");
		smtpAppender.setSMTPHost(host);
		smtpAppender.setSMTPPort(port);
		smtpAppender.setUsername(username);
		smtpAppender.setPassword(password);
		smtpAppender.setFrom(from);
		smtpAppender.setSTARTTLS(startTLS);
		smtpAppender.setSubject(subject);
		smtpAppender.setLayout(htmlLayout);

		CyclicBufferTracker<ILoggingEvent> cyclicBufferTracker = new CyclicBufferTracker<ILoggingEvent>();
		cyclicBufferTracker.setBufferSize(1);
		smtpAppender.setCyclicBufferTracker(cyclicBufferTracker);

		ThresholdFilter mailThresholdFilter = new ThresholdFilter();
		mailThresholdFilter.setName("mail-thresholdFilter");
		mailThresholdFilter.setLevel(mailLogLevel);
		mailThresholdFilter.start();

		smtpAppender.addFilter(mailThresholdFilter);

		/**
		 * to mail addresses
		 */
		for (String toMail : toMails) {
			smtpAppender.addTo(toMail);
		}

		smtpAppender.start();

		AsyncAppender mailAsyncAppender = new AsyncAppender();
		mailAsyncAppender.setContext(loggerContext);
		mailAsyncAppender.setName(smtpAppender.getName() + "-async");
		mailAsyncAppender.addAppender(smtpAppender);
		mailAsyncAppender.setDiscardingThreshold(0);
		mailAsyncAppender.start();

		rootLogger.addAppender(mailAsyncAppender);
	}

	private static PatternLayoutEncoder getEncoder(LoggerContext loggerContext) {
		PatternLayoutEncoder encoder = new PatternLayoutEncoder();
		encoder.setContext(loggerContext);
		encoder.setPattern("%d{HH:mm:ss.SSS} %-5level [%thread] %logger{36} - %msg%n");
		encoder.setCharset(Charset.forName("UTF-8"));
		encoder.start();
		return encoder;
	}

	private static String parseLogFileName(String logFileName) {
		if (logFileName.startsWith("/") || logFileName.startsWith("\\")) {
			logFileName = logFileName.substring(1);
		}
		return logFileName;
	}

	private static String parseLogPath(String logPath) {
		if (logPath.startsWith("~")) {
			logPath = logPath.replaceFirst("~", System.getProperty("user.home"));

		}
		if (logPath.endsWith("/") || logPath.endsWith("\\")) {
			logPath = logPath.substring(0, logPath.length() - 1);
		}
		return logPath;
	}

}

