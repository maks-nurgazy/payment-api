package com.maksnurgazy.model;

import lombok.*;

import java.time.LocalDateTime;


@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
	private String token;
	private String type;
	private String username;
	private LocalDateTime expiryDate;
	private LocalDateTime currentTime;
}