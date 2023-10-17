package psch.thirdapi.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Resource
	LogInterceptor logInterceptor;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/**").addResourceLocations("file:/data/psch");
	}
	
	/*@Bean
	public MiniInterceptor miniInterceptor() {	
		return new MiniInterceptor(); 
	}*/

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		/*	
		registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
		                                          .addPathPatterns("/video/uploadCover","/video/upload")
		                                          .addPathPatterns("/video/userLike","/video/userUnLike")
		                                          .addPathPatterns("/video/saveComment")
		                                          .addPathPatterns("/bgm/**")
		                                          .excludePathPatterns("/user/queryPublisher");
		*/
		registry.addInterceptor(logInterceptor);
		super.addInterceptors(registry);
	}
	
}
