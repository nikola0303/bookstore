package bookstore.annotation;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import bookstore.model.MyUser;
import bookstore.model.Permission;
import bookstore.service.MyUserService;

public class PermissionInterceptor implements HandlerInterceptor {

	@Autowired
	private MyUserService korisnikService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		MyUser currentUser = korisnikService.getCurrentUser();

		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler; 
			Method method = handlerMethod.getMethod();
			String permissionName = null;
			if(method.getDeclaringClass().isAnnotationPresent(RestController.class)) {
				if(method.isAnnotationPresent(PermissionAnnotation.class)) {
					PermissionAnnotation annotation = method.getAnnotation(PermissionAnnotation.class);
					permissionName = annotation.name();
				} else {
					return true;
				}
			} else {
				return true;
			}

			if(currentUser == null) {
				return false;
			}

			for(Permission permission : currentUser.getRole().getPermissions()){
				if(permission.getName().equals(permissionName)){
					return true;
				}
			}

			return false;
		} else {
			return true;
		}
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		
	}
	
}
