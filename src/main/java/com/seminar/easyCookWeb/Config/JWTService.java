package com.seminar.easyCookWeb.Config;

import com.seminar.easyCookWeb.Repository.MemberRepository;
import com.seminar.easyCookWeb.entity.app_user.AuthRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JWTService {
    //主要在幫助進行帳密驗證, 注入元件的來源在SecurityConfig
    @Autowired
    private AuthenticationManager authenticationManager;

    private final String KEY = "ThisIsEasyCookSecretKeyCreatedByShannon";

    /**
     * @param request 用來接收帳密的請求主體
     * @return
     */
    public String generateToken(AuthRequest request) {
        //如果在SecurityConfig中configure()裡面使用者驗證成功，就會再次得到UsernamePasswordAuthenticationToken
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        //當AuthenticationManager進行身分驗證時，會接收到一個Authentication介面的物件
            //其中帳密的驗證所對應的是UsernamePasswordAuthenticationToken
                //UsernamePasswordAuthenticationToken(principal, credentials)
                    //principal: 代表與伺服器進行互動的人 | credentials:用來證明自己確實是principal的資料通常是密碼
        authentication = authenticationManager.authenticate(authentication);
        //得到UsernamePasswordAuthenticationToken物件之後用Authentication介面接收他
            //authntication裡面的資料會變成UserDetailsService回傳的使用者詳情 把她取出轉型為UserDetails物件後續繼續使用
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //payload
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, SecurityConstants.EXPIRATION_TIME_MINUTES); //產生期限五分鐘的JWT

        //Claims類別本身實作了Map<String, Object>介面，所以能使用Map的方法來存放或讀取資料
        Claims claims = Jwts.claims();
        claims.put("username", authentication.getPrincipal());
        claims.put("authorities", authentication.getAuthorities());
        claims.setExpiration(calendar.getTime());
        claims.setIssuer("ShannonHung From EasyCook");

        //產生密鑰: 產生方式是提供一個字串轉換成位元組陣列byte[]候傳入hmacShaKeyFor方法得到金鑰
        //這個字串經過轉換後壁機要夠長32位元組否則會發生例外
        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());

        //透過Jwts.builder方法得到Builder物件
        return Jwts.builder() //他事先內建了JWT的header
                .setClaims(claims) //payload 內容放入
                .signWith(secretKey) //signature 用密鑰簽章
                .compact(); //呼叫compact來產生token字串
    }

    public Map<String, Object> parseToken(String token){
        //產生密鑰: 產生方式是提供一個字串轉換成位元組陣列byte[]候傳入hmacShaKeyFor方法得到金鑰
            //目的: 產生token字串的簽名
        Key secretKey = Keys.hmacShaKeyFor(KEY.getBytes());

        //建立JWT解析器 需要將密鑰傳入
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
        //透過JWT解析器.parseClaimsJws方法來解析含有簽名的JWT也就是JWS
        Claims claims = parser
                .parseClaimsJws(token)
                //再呼叫getBody方法取得前面認識的claims物件值得注意的是，若讀者在JWT的內容中有存放第二節提到的「nbf」（生效時間）或「exp」（到期時間）欄位，在解析時，函式庫會判斷這個Token是否在有效期限內。若不在期限內，則會拋出例外，造成「Internal Server Error」。
                .getBody();

        //claims繼承map屬性，map無法使用iterator所以必須先透過entrySet轉換成Set在來使用stream()
        return claims.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
