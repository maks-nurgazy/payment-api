package com.maksnurgazy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="response")
public class StatusResponse {

	private Integer status;
	private String message;
	private Object object;
	
	public StatusResponse build(Integer status, String message) {
		setStatus(status);
		setMessage(message);
		return this;
	}
	
	public StatusResponse(Integer status, String message) {
		setStatus(status);
		setMessage(message);
	}
}
