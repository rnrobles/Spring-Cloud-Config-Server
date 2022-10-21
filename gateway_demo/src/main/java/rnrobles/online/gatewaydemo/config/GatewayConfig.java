package rnrobles.online.gatewaydemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Configuration
public class GatewayConfig {

	@Autowired
	configApp config;

	@Bean
	public RouteLocator configLocal(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("blue",
						r -> r.path("/blue/**").filters(f -> f.rewritePath("/blue/(?<segment>.*)", "/${segment}"))
								.uri("http://" + url()))
				.route("red", r -> r.path("/red/**").filters(f -> f.rewritePath("/red/(?<segment>.*)", "/${segment}"))
						.uri("http://" + url()))
				.build();
	}

	public GatewayFilterSpec redirectCircuitBreaker(GatewayFilterSpec f, String urlRewrite, String urlForward) {
		f.rewritePath(urlRewrite + "/(?<segment>.*)", "/${segment}");
		f.circuitBreaker(c -> {
			c.setName("failoverCB" + urlForward);
			f.filter((exchange, chain) -> {
				ServerHttpRequest req = exchange.getRequest();
				String path = req.getURI().getRawPath();
				c.setFallbackUri("forward:" + urlForward + path);
				return chain.filter(exchange.mutate().request(req).build());
			});
		});
		return f;
	}

	public String url() {
		return config.getUrl();
	}

}
