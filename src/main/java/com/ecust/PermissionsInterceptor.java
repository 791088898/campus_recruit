package com.ecust;

import com.ecust.permision.AdminPermission;
import com.ecust.permission.AdmintPermission;
import com.ecust.pojo.User;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;


public class PermissionsInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        boolean isPass=true;
        if(method.getAnnotation(AdminPermission.class)!=null){
            isPass=isAdmin(request);
        }
        if(!isPass){//δ��Ȩ������401��Ϣ
//            Gson gson=new Gson();
//            ResponseJson json=new ResponseJson();
//            json.setCode(UNAUTHORIZED.getCode());
//            json.setMessage(UNAUTHORIZED.getMessage());
//            response.setCharacterEncoding("UTF-8");
//            response.getWriter().write(gson.toJson(json));
        }
        return isPass;
    }

    private boolean isAdmin(HttpServletRequest request){
        //�ж��Ƿ��ǹ���Ա
        //����session
        HttpSession session = request.getSession();
        //���session�е��û�
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser.getRoleName().equals("����Ա")) {
            return true;
        }
        return false;
    }
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }
}
