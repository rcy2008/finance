package com.cwmd.core.exception;

/**
 * 远程调用抛出的基类异常
 * Created by CWMD .
 * @author: chaoyang.ren
 * @date:2015年7月21日
 * @time:上午10:37:58
 * @email:chaoyang.ren@foxmail.com
 * @version: 1.0
 */
public class RemotingServiceException extends RuntimeException {
	private static final long serialVersionUID = -5179053849454546587L;

	public RemotingServiceException() {
		super();
	}

	public RemotingServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public RemotingServiceException(String message) {
		super(message);
	}

	public RemotingServiceException(Throwable cause) {
		super(cause);
	}
}
