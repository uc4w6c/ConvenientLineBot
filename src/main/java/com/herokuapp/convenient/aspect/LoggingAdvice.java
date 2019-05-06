package com.herokuapp.convenient.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.herokuapp.convenient.domain.AccessLog;
import com.herokuapp.convenient.repository.AccessLogRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Aspect
@Component
// 試しにAOPしてみた 参考：https://qiita.com/mijinco0612/items/ac03a3717f2c877ac675
// AOPをするとDIがうまくいかない
public class LoggingAdvice {

	private static final String SUCCESS_STATUS = "200";
	private static final String SYSTEM_ERROR_STATUS = "500";

	@Autowired
	private AccessLogRepository accessLogRepository;

	private final Logger logger;

	public LoggingAdvice() {
		this.logger = LoggerFactory.getLogger(getClass());
	}

	// LineBotControllerをAOPするとLineBot SDKのアノテーションが反映されないため、
	// ControllerはAOP対象から除外し、サービスをAOP対象とする
	@Pointcut("execution(* com.herokuapp.convenient.web.*.*(..))") 
	public void controllerPointcut(){}
	@Pointcut("execution(* com.herokuapp.convenient.web.LineBotController.*(..))") 
	public void lineBotControllerPointcut(){} 
	@Pointcut("execution(* com.herokuapp.convenient.service.LineBotService.*(..))")
	public void lineBotServicePointcut(){}
	@Pointcut("(controllerPointcut() && ! lineBotControllerPointcut()) || lineBotServicePointcut()") 
	public void targetPointcut(){} 

	@Around("targetPointcut()")
	public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
		outputLog(joinPoint);
		Object retVal = null;
		String responseStatus = "";
		LocalDateTime requestAt = getNowLocalDateTime();
		try {
			retVal = joinPoint.proceed();
			outputLog(joinPoint, retVal);
			responseStatus = SUCCESS_STATUS;
		} catch (Exception e) {
			outputErrorLog(joinPoint, e);
			responseStatus = SYSTEM_ERROR_STATUS;
		}
		LocalDateTime responseAt = getNowLocalDateTime();

		// リクエストとレスポンスの間の差分をミリ秒単位で計算
		// long型になっているのを無理やりint型へキャストしている
		int aroundMicrosecond = (int) ChronoUnit.MICROS.between(requestAt, responseAt);

		// TODO: 暫定でここに記載しているけど、別スレッドで起動してここでExceptionが発生しても普通にレスポンスするようにしたい
		AccessLog accessLog = new AccessLog(
										null, requestAt, responseAt, 
										getSessionId(), getClassName(joinPoint), getSignatureName(joinPoint),
										getArguments(joinPoint), getHttpMethod(), getRequestUri(),
										responseStatus, aroundMicrosecond);
		accessLogRepository.save(accessLog);

		return retVal;
	}

	/*
	//@Before("execution(* com.herokuapp.convenient.web.ExampleController.*(..))")
	@Before("targetPointcut()")
	public void invokeApiControllerBefore(JoinPoint joinPoint) {
		outputLog(joinPoint);
	}

	//@AfterReturning(pointcut = "execution(* com.herokuapp.convenient.web.*.*(..))", returning = "returnValue")
	@AfterReturning(pointcut = "targetPointcut()", returning = "returnValue")
	public void invokeControllerAfter(JoinPoint joinPoint, Object returnValue) {
		outputLog(joinPoint, returnValue);
	}

	//@AfterThrowing(value = "execution(* com.herokuapp.convenient.web.*.*(..))", throwing = "e")
	@AfterThrowing(value = "targetPointcut()", throwing = "e")
	public void invokeControllerAfterThrowing(JoinPoint joinPoint, Throwable e) {
		outputErrorLog(joinPoint, e);
	}
	*/

	private void outputLog(JoinPoint joinPoint) {
		//String logMessage = "[" + getSessionId() + "]:" + getClassName(joinPoint) + "." + getSignatureName(joinPoint) + ":START:" + getArguments(joinPoint);
		String logMessage = "[" + getSessionId() + "]:" + getClassName(joinPoint) + "." + getSignatureName(joinPoint) + ":START:" + getArguments(joinPoint) + ":METHOD:" + getHttpMethod();
		logger.info(logMessage);
	}

	private void outputLog(JoinPoint joinPoint, Object returnValue) {
		String logMessage = "[" + getSessionId() + "]:" + getClassName(joinPoint) + "." + getSignatureName(joinPoint) + ":END:" + getReturnValue(returnValue);
		logger.info(logMessage);
	}

	private void outputErrorLog(JoinPoint joinPoint, Throwable e) {
		String logMessage = "[" + getSessionId() + "]:" + getClassName(joinPoint) + "." + getSignatureName(joinPoint) + ":arguments:" + getArguments(joinPoint);
		logger.error(logMessage, e);
	}

	private String getHttpMethod() {
		/*
		 * 下で置き換えたものと同じ意味なので色々コメント化
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)attributes;
		HttpServletRequest request = servletRequestAttributes.getRequest();
		return request.getMethod();
		*/
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getMethod();
	}

	private String getRequestUri() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRequestURI();
	}

	private String getSessionId() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession().getId();
	}

	private String getClassName(JoinPoint joinPoint) {
		return joinPoint.getTarget().getClass().toString();
	}

	private String getSignatureName(JoinPoint joinPoint) {
		return joinPoint.getSignature().getName();
	}

	private String getArguments(JoinPoint joinPoint) {
		if (joinPoint.getArgs() == null) {
			return "argument is null";
		}

		Object[] arguments = joinPoint.getArgs();
		ArrayList<String> argumentStrings = new ArrayList<>();

		for (Object argument : arguments) {
			if (argument != null) {
				argumentStrings.add(argument.toString());
			}
		}
		return String.join(",", argumentStrings);
	}

	private String getReturnValue(Object returnValue) {
		return (returnValue != null) ? returnValue.toString() : "return value is null";
	}

	/**
	 * マイクロ秒まで出力したいが、現在の仕様ではミリ秒までしか出力しない
	 * 正確な値が出力したければNTPサーバを検討する必要がある
	 * @return LocalDateTime
	 */
	private LocalDateTime getNowLocalDateTime() {
		return LocalDateTime.now();
	}

}
