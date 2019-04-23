package com.herokuapp.convenient.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.herokuapp.convenient.service.LineBotService;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineBotMessages;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@RestController
@RequestMapping("/linebot")
public class TestLineBotController {

	private final LineBotService lineBotService;

	private final ConfigurableApplicationContext applicationContext;

	public TestLineBotController(LineBotService lineBotService,
						final ConfigurableApplicationContext applicationContext) throws Exception {
		this.lineBotService = lineBotService;
		this.applicationContext = applicationContext;
	}

	@GetMapping(path = "test")
	public void result() {
		System.out.print("LineMessageHandler:");

		// LineMessageHandlerのアノテーションがついたクラスを取得する
		final Map<String, Object> handlerBeanMap =
				applicationContext.getBeansWithAnnotation(LineMessageHandler.class);
		System.out.println(handlerBeanMap);

		System.out.println("EventMapping:" + EventMapping.class.getName());
		// EventMappingのアノテーションがついたメソッドを取得する
		System.out.print("HandlerMethod:");
		handlerBeanMap
			.values().stream()
			.forEach((Object bean) -> {
				final Method[] uniqueDeclaredMethods =
						ReflectionUtils.getUniqueDeclaredMethods(bean.getClass());

				for (Method method : uniqueDeclaredMethods) {
					final EventMapping mapping = AnnotatedElementUtils.getMergedAnnotation(method, EventMapping.class);
					if (mapping == null) {
						System.out.println(method.getName() + ":Null");
					} else {
						System.out.println(method.getName() + ":Not Null");
					}

					// 対象メソッドにアノテーションがついているか調査
					// for (Annotation[] annotation : method.getParameterAnnotations()) {
					// 	System.out.println(annotation);
					// }
					for (Annotation annotation : method.getAnnotations()) {
						System.out.println(annotation.annotationType());
					}
				}
			});
	}

	@PostMapping(path = "create")
	public Message create(@RequestBody CallbackRequest callBackRequest) throws Exception {
		System.out.println(callBackRequest);

		// TODO: 色々汚いから変えたい
		// 暫定的に Listの0番目のものを取得
		// com.linecorp.bot.spring.boot.support.LineMessageHandlerSupportを参考
		//MessageEvent<TextMessageContent> event = callBackRequest.getEvents().get(0);
		Event event = callBackRequest.getEvents().get(0);
		if (event instanceof MessageEvent) {
			MessageEvent<TextMessageContent> messageEvent= (MessageEvent<TextMessageContent>)event;
			System.out.println(messageEvent);
			TextMessageContent message = messageEvent.getMessage();
			return lineBotService.makeReply(messageEvent, message);
		} else {
			return null;
		}
	}

	/*
	LineBotServerInterceptor.java:49に失敗するから試してみたけど無理だった
	@PostMapping(path = "create")
	public Message create(@LineBotMessages List<Event> events) throws Exception {
		MessageEvent<TextMessageContent> messageEvent= (MessageEvent<TextMessageContent>)events.get(0);
		TextMessageContent message = messageEvent.getMessage();
		return lineBotService.makeReply(messageEvent, message);
	}
	*/

	@PostMapping(path = "delete")
	public Message deleteTask(@RequestBody CallbackRequest callBackRequest) throws Exception {
		System.out.println("CallBackRequest:" + callBackRequest);

		Event event = callBackRequest.getEvents().get(0);
		if (event instanceof PostbackEvent) {
			PostbackEvent postBackEvent= (PostbackEvent)event;
			System.out.println("PostBackEvent:" + postBackEvent);
			String deleteId = postBackEvent.getPostbackContent().getData();
			return lineBotService.deleteTask(postBackEvent, deleteId);
		} else {
			return null;
		}
	}

	@PostMapping(path = "bodyprint")
	public String create(@RequestBody String body) {
		System.out.println(body);
		return body;
	}
}
