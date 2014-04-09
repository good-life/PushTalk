package org.pushtalk.server.data.h2;

import java.sql.SQLException;

import org.apache.log4j.Logger;

public class TalkService {

	private static Logger LOG = Logger.getLogger(TalkService.class);

	public TalkService() throws SQLException {
		H2Database.getInstance().init();
		LOG.info("h2 database init finished.");

	}
}
