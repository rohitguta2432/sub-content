package com.vedanta.vpmt.web.config;


//@Component("redisServerUtil")
public class RedisServerUtil {

/*	private final static Logger logger = LoggerFactory.getLogger(RedisServerUtil.class);

	@Value("${redis.port}")
	private int redisPort;

	private RedisServer redisServer;

	@PostConstruct
	public void init() {
		try {
			redisServer = new RedisServer(redisPort);
			redisServer.start();
		} catch (IOException | URISyntaxException e) {
			logger.error(String.format("Cannot start embedded redis server with port : %d ",
					redisPort));
			throw new SCBAdminException(String.format(
					"Cannot start embedded redis server with port : %d ", redisPort), e);
		}
	}

	@PreDestroy
	public void stopServer() {
		if (redisServer != null) {
			try {
				redisServer.stop();
			} catch (InterruptedException e) {
				logger.error(String.format(
						"Error while stopping embedded redis server with port : %d ", redisPort));
				throw new SCBAdminException(String.format(
						"Error while stopping embedded redis server with port : %d ", redisPort), e);
			}
		}
	}*/

}
