package bookstore;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import bookstore.annotation.PermissionInterceptor;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@EnableJpaRepositories
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class BookstoreApplication extends SpringBootServletInitializer implements WebMvcConfigurer {
	
	//zbog lazy initialization problema
	@Bean
	public FilterRegistrationBean registerOpenEntityManagerInViewFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	    OpenEntityManagerInViewFilter filter = new OpenEntityManagerInViewFilter();
	    registrationBean.setFilter(filter);
	    registrationBean.setOrder(5);
	    return registrationBean;
	}
	
	@Bean
	public PermissionInterceptor permisijaInterceptor() {
	    return new PermissionInterceptor();
	}

	@Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
    	interceptorRegistry.addInterceptor(permisijaInterceptor()).addPathPatterns("/**");
    }
	
	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {
		
	}

	@Override
	public void addCorsMappings(CorsRegistry arg0) {
		
	}

	@Override
	public void addFormatters(FormatterRegistry arg0) {
		
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry arg0) {
		
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> arg0) {
		
	}

	@Override
	public void addViewControllers(ViewControllerRegistry arg0) {
		
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer arg0) {
		
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer arg0) {
		
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer arg0) {
		
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> arg0) {
		
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> arg0) {
		
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer arg0) {
		
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry arg0) {
		
	}

	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> arg0) {
		
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> arg0) {
		
	}

	@Override
	public MessageCodesResolver getMessageCodesResolver() {
		return null;
	}

	@Override
	public Validator getValidator() {
		return null;
	}
	
	@Bean
    public RestTemplate restTemplate() 
                    throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
     
        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                        .loadTrustMaterial(null, acceptingTrustStrategy)
                        .build();
     
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
     
        CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLSocketFactory(csf)
                        .build();
     
        HttpComponentsClientHttpRequestFactory requestFactory =
                        new HttpComponentsClientHttpRequestFactory();
     
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
     }

}
