package br.com.customer.web.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;

public class StandardError implements Serializable {

	private static final long serialVersionUID = 2034300392942878889L;

	private Integer status;
	private String msg;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime  timestamp;

	public StandardError(Integer status, String msg, LocalDateTime  timestamp) {
		super();
		this.status = status;
		this.msg = msg;
		this.timestamp = timestamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public LocalDateTime  getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime  timestamp) {
		this.timestamp = timestamp;
	}
}
