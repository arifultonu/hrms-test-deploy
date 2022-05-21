package com.hrms.acl.security.filter;


import com.hrms.modules.system.core.ClientInfo;
import com.hrms.modules.system.visitorLog.VisitorsLog;
import com.hrms.modules.system.visitorLog.VisitorsLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;

@Component
public class MyCustomInterceptor implements AsyncHandlerInterceptor {

    String username = null;

    @Autowired
    private VisitorsLogRepository visitorsLogRepository;

    @Override
    public boolean preHandle(HttpServletRequest requestServlet, HttpServletResponse responseServlet, Object handler) throws Exception
    {
//        System.out.println("MINIMAL: INTERCEPTOR PREHANDLE CALLED");
//
//        ClientInfo clientInfo = new ClientInfo();
//        clientInfo.printClientInfo(requestServlet);

//        String ip = requestServlet.getRemoteAddr();
//        int port = requestServlet.getRemotePort();
//        StringBuffer url3 = requestServlet.getRequestURL();
//        String url = requestServlet.getRequestURI();

        HttpSession httpSession = requestServlet.getSession();
        if(httpSession != null){
            if(httpSession.getAttribute("username") != null){
                this.username = httpSession.getAttribute("username").toString();
            }
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {

//        System.out.println("MINIMAL: INTERCEPTOR POSTHANDLE CALLED");




        ClientInfo clientInfo = new ClientInfo();
        clientInfo.printClientInfo(request);
        HashMap<String, String> clientInfoObj = clientInfo.getClientInfo(request);

        VisitorsLog visitorsLog = new VisitorsLog();
        visitorsLog.setUserId(this.username );
        visitorsLog.setIpAddress( clientInfoObj.get("clientIpAddr") );
        visitorsLog.setUserAgent( clientInfoObj.get("userAgent") );
        visitorsLog.setReferrer( clientInfoObj.get("referer") );
        visitorsLog.setVisitPage( clientInfoObj.get("fullURL") );
        visitorsLog.setVisitUrl( clientInfoObj.get("fullURL") );
        visitorsLog.setCreationDateTime( new Date() );
        visitorsLog.setCreationUser( this.username );

     //   visitorsLogRepository.save(visitorsLog);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception
    {
//        System.out.println("MINIMAL: INTERCEPTOR AFTERCOMPLETION CALLED");
    }



}
