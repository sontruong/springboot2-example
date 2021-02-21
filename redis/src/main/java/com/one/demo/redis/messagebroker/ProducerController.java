package com.one.demo.redis.messagebroker;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import redis.clients.jedis.Jedis;

@RestController
@Api(tags = "Redis Message broker")
@RequestMapping(value = "api")
public class ProducerController {
	@RequestMapping(value = "push_message", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String saveDetail(@RequestParam String queue, @RequestParam String msg) {
		
		Jedis jedis = null;
		try {
			jedis = new Jedis("localhost");
			jedis.rpush(queue, msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return "Success";
    }
}
