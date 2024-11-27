package seguridad;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author Samuel Vega
 */
@WebFilter(urlPatterns = "/api/*")
public class JWTFilter implements Filter {
    private final JWTUtil jwtUtil = new JWTUtil();

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        HttpServletResponse response = (HttpServletResponse) sr1;
        
        if(request.getRequestURI().endsWith("/api/padres/add")) {
            fc.doFilter(sr, sr1);
            return;
        }
        
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if(jwtUtil.validateToken(token)) {
                fc.doFilter(sr, sr1);
                return;
            }
        }
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"Error\":\"No autorizado. Token invalido o no proporcionado\"}");
    }
    
    
}
