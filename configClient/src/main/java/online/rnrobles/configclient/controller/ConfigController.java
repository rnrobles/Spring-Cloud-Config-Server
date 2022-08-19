package online.rnrobles.configclient.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import online.rnrobles.configclient.config.configApp;


@RestController
@RequestMapping("/api")
public class ConfigController {
	
	private static final Logger log = LoggerFactory.getLogger(ConfigController.class);

	@Autowired
	configApp config;
	
	@GetMapping
	public String getConfiguracion()
	{
		log.info(config.getName());
		return config.getName();
	}
	
	
	

}
