package seguridad;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;
import java.util.Date;

/**
 *
 * @author Samuel Vega
 */
public class JWTUtil {
    private final String SECRET_KEY = "my1secretly2secret3ultra4secret5keyadaeueadda";
    
    public String generateToken(String username) {
        try {
            return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        }catch(InvalidKeyException e) {
            throw new RuntimeException("Error al generar el token", e);
        }
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch(JwtException jwte) {
            return false;
        }
    }
}
