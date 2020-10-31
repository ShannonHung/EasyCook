package com.seminar.easyCookWeb.Controller.Auth;

import com.seminar.easyCookWeb.Config.JWTService;
import com.seminar.easyCookWeb.Config.SecurityConstants;
import com.seminar.easyCookWeb.entity.app_user.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    @Autowired
    private JWTService jwtService;
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> issueToken(@RequestBody AuthRequest request) {
        String token = jwtService.generateToken(request);
        Map<String, String> response = Collections.singletonMap(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/parse")
    public ResponseEntity<Map<String, Object>> parseToken(@RequestBody Map<String, String> request) {
        String Bearer = request.get(SecurityConstants.HEADER_STRING);
        String token = Bearer.replace(SecurityConstants.TOKEN_PREFIX,"");
        Map<String, Object> response = jwtService.parseToken(token);

        return ResponseEntity.ok(response);
    }

}
