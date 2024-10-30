package com.demo.shoppingproject.Config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

/*
This class is used to extract the username from the JWT token.
*/

@Service
public class JwtUtil {

    private static final String SECRET_KEY="Bx4MeizkOfmx5buS0AeRdfWkK1H4GI8abShDqtZq6gA"; //32 bytes
    private static final String REFRESH_SECRET = "e0f86a1300873f78f49b4b76805d54f8bd245cc3b381474980c07ff41a942ff1";
    private int  refreshTokenExpirationTime = 7*24*60*60 * 1000; //7 days 7 * 24 * 60 * 60 * 1000
    //Cookie expiration time: 30 minutes
  //  private int cookieExpirationTime  = 10*60*1000;
    //checking JWT token expiration time
    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());



    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    //Get token from the UserName
    public String getUserNameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Extract specific claim (information) from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsAllToken(token);
        return claimsResolver.apply(claims);
    }

    //Provide interface to generate token
    public String generateToken(UserDetails userDetails){

        return generateToken(new HashMap<>(), userDetails);
    }

    //When Generating Token from generateToken(), create a token
    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(claims)
                .claim("username", userDetails.getUsername())
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 10*60*1000)) //10 minutes
                .signWith(getSignInKey())
                .compact();
    }
    public Boolean isTokenValid(String jwtToken, UserDetails userDetails){
        //Reason for use UserDatails: if token is belong to UserDetails
        final String username = getUserNameFromToken(jwtToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));

    }

    //Check if the token is expired
    public boolean isTokenExpired(String jwtToken) {
        return getClaimsAllToken(jwtToken).getExpiration().before(new Date());
    }

    //Extracting specific claims from the JWT token's payload (claims)
    private Claims getClaimsAllToken(String jwtToken) {

        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
    public Date getExpirationDateFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private SecretKey getSignInKey() {
        //Decode the Base64 encoded secret key
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        //Generate a secret key from the decoded bytes,and return it
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //-----------------------------Refresh Token---------------------------------//


    //Generate Refresh Token
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        long refreshTokenExpirationTime = 7*24*60*60*1000;
        return createRefreshToken(claims, userDetails.getUsername(), refreshTokenExpirationTime *1000); // 7 day
    }
    // above
    private String createRefreshToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt((new Date(System.currentTimeMillis())))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getRefreshSignInKey())
                .compact();
    }


    //Check is Refresh token is valid
    public Boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails)
    {
        final String username = extractUsernameFromRefreshToken(refreshToken);
        return (username.equals(userDetails.getUsername()) && !isRefreshTokenExpired(refreshToken));
    }
    //Extract username from the token
    public String extractUsernameFromRefreshToken(String jwtToken) {
        return extractClaimFromRefreshToken(jwtToken, Claims::getSubject);
    }
    //Get expiration time from token
    public Date getRefreshTokenExpiration(String jwtToken) {
        return extractClaimFromRefreshToken(jwtToken, Claims::getExpiration);
    }
    //Get claim from the token
    public <T> T extractClaimFromRefreshToken(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaimsAllRefreshToken(jwtToken);
        return claimsResolver.apply(claims);
    }

    //Extract all claims from the token
    private Claims getClaimsAllRefreshToken(String jwtToken) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(REFRESH_SECRET)))
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
    private boolean isRefreshTokenExpired(String jwtToken) {
        return getClaimsAllRefreshToken(jwtToken).getExpiration().before(new Date());
    }
    private SecretKey getRefreshSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(REFRESH_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}









