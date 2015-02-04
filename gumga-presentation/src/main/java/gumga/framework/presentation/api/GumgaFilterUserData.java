/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.presentation.api;

import java.io.IOException;
import java.lang.annotation.Annotation;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author munif
 */
@WebFilter(urlPatterns = "*")
public class GumgaFilterUserData implements Filter {

    public static ThreadLocal<String> ip = new ThreadLocal<>();
    public static ThreadLocal<String> user = new ThreadLocal<>();

    private RestTemplate restTemplate;

    @Override
    public void init(FilterConfig fc) throws ServletException {
        restTemplate = new RestTemplate();
    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        long beginTime = System.currentTimeMillis();
        String token = "no token";
        String info = "no info";
        try {
            HttpServletRequest hsq = (HttpServletRequest) sr;
            HttpServletResponse response = (HttpServletResponse) sr1;
            token = hsq.getHeader("gumgaToken");
            String endPoint = hsq.getRequestURL().toString();
            String method = hsq.getMethod();

            ip.set(hsq.getRemoteAddr());
            user.set(hsq.getUserPrincipal() != null ? hsq.getUserPrincipal().getName() : "not logged");

            info = "from:" + user.get() + "@" + ip.get() + " to " + method + "->" + endPoint;

            String url = "http://localhost:8084/gumgasecurity-presentation/public/token/auth/" + token + "/" + (endPoint.replace("/", "|")) + "/" + method;
//            System.out.println(url);

            AuthorizatonResponse ar = restTemplate.getForObject(url, AuthorizatonResponse.class);

            if (ar.isAllowed()) {
                fc.doFilter(sr, sr1);
            } else {
                response.setStatus(401);
                response.getOutputStream().write("Not authorized".getBytes());

            }
        } catch (ClassCastException ex) {
            //Ignore
        } finally {

        }
        long totalTime = System.currentTimeMillis() - beginTime;
        System.out.println("GUMGA API ----------------- " + token + " " + totalTime + "ms " + info);
    }

    @Override
    public void destroy() {

    }

    
}
