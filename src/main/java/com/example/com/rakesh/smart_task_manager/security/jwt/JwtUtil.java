package com.example.com.rakesh.smart_task_manager.security.jwt;

import java.security.Key;
import java.util.Date;

//import java.awt.RenderingHints.Key;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final String SECRET = "mySecretKeymySecretKeymySecretKeymySecretKey";
	private final Key key =  Keys.hmacShaKeyFor(SECRET.getBytes());
	
	//Generation Token
	
	public String generateToken(String username) {
		
		return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+1000*60*60*10)).signWith(key, SignatureAlgorithm.HS256).compact();
	}
	
	// Extract UserName
	
	public String extractUserName(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

}
