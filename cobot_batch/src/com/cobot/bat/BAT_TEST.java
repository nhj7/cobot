package com.cobot.bat;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nhj.db.mybatis.MyBatisFactory;
import nhj.util.PrintUtil;

public class BAT_TEST {
	final static Logger logger = LoggerFactory.getLogger(BAT_TEST.class);
	static BAT_DAO dao;
	static {
		BAT_COMM.init("/log/cobot_batch/cobot_test.log");
		dao = (BAT_DAO) MyBatisFactory.getDAO(BAT_DAO.class);
		logger.info("Entering BAT_ANALYSIS application.");
	}

	public static void main(String[] args) {

		for (int i = 1; i <= 24; i++) {
			logger.info("write log");

			try {
				Thread.sleep(10000L);
			} catch (final InterruptedException e) {
				logger.error("an error occurred", e);
			}
		}

	}

}
