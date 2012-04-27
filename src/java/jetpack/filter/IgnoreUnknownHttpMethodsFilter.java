package jetpack.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//javac -classpath .:/Users/square/Development/taxi/vendor/jetty/lib/servlet-api-3.0.jar IgnoreUnknownHttpMethodsFilter.java
//
//javac -classpath .:/Users/square/Development/taxi/vendor/jetty/lib/servlet-api-3.0.jar:/Users/square/Development/taxi/WEB-INF/lib/commons-validator-1.4.0.jar ValidUrlFilter.java
//
//
//<filter>
//  <filter-name>IgnoreUnknownHttpMethodsFilter</filter-name>
//  <filter-class>jetpack.filter.IgnoreUnknownHttpMethodsFilter</filter-class>
//</filter>
//
//<filter-mapping>
//  <filter-name>IgnoreUnknownHttpMethodsFilter</filter-name>
//  <url-pattern>/*</url-pattern>
//</filter-mapping>
//
//
// curl -Ik -X GET 'https://localhost:4443/<script>xss</script>.aspx'
//  curl -Ik -X GET 'https://localhost:4443/xss<SCRIPT>.aspx'
// curl -Ik -X DEBUG 'https://localhost:4443/'
// cp ValidUrlFilter.class ~/Development/taxi/WEB-INF/classes/jetpack/filter/
// Does not handle foo.bar.squareup.com type urls


public class IgnoreUnknownHttpMethodsFilter implements Filter {

    java.util.List<String> allowedMethodList;

    public void init(FilterConfig filterConfig) throws ServletException {
        allowedMethodList = new java.util.ArrayList<String>();
        allowedMethodList.add("GET");
        allowedMethodList.add("PUT");
        allowedMethodList.add("DELETE");
        allowedMethodList.add("POST");
        allowedMethodList.add("HEAD");
    }

    public void destroy() {
        allowedMethodList = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)request;

        if ( allowedMethodList.contains(req.getMethod()) ) {
          chain.doFilter(request, response);
        } else {
          HttpServletResponse res = (HttpServletResponse)response;
          res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
          return;
        }
    }
}