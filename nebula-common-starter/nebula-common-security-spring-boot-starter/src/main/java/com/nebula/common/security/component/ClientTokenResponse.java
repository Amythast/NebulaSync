package com.nebula.common.security.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author feifeixia
 * @date 2023/11/24-00:09
 */
@Getter
@Setter
public class ClientTokenResponse {

	private int code;
	private String msg;
	private Data data;

	public ClientTokenResponse(@JsonProperty("code") int code,
					   @JsonProperty("msg") String msg,
					   @JsonProperty("data") Data data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ClientTokenResponse() {

	}
	@Getter
	@Setter
	static class Data {
		private String token;
		private String refreshToken;
		private String tokenHead;
		private long expiresIn;

		public Data(@JsonProperty("token") String token,
					@JsonProperty("refreshToken") String refreshToken,
					@JsonProperty("tokenHead") String tokenHead,
					@JsonProperty("expiresIn") long expiresIn) {
			this.token = token;
			this.refreshToken = refreshToken;
			this.tokenHead = tokenHead;
			this.expiresIn = expiresIn;
		}

		public Data() {
		}
	}
}
