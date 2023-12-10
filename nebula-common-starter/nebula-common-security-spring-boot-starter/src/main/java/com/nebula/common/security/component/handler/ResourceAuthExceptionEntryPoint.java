/*
 *  Copyright (c) 2019-2020,.
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>

 */

package com.nebula.common.security.component.handler;

import com.nebula.common.exception.DefaultError;
import com.nebula.common.security.consts.CommonConstants;
import com.nebula.common.utils.JsonUtils;
import com.nebula.common.web.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author feifeixia
 * @date 2019/2/1
 * 客户端异常处理
 * 1. 可以根据 AuthenticationException 不同细化异常处理
 */
@Slf4j
public abstract class ResourceAuthExceptionEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
						 AuthenticationException authException) throws IOException, ServletException {
		final Response resp = Response.failure(DefaultError.AUTHENTICATION_ERROR);
		resp.setErrorMessage(authException.getMessage());
		response.setCharacterEncoding(CommonConstants.UTF8);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().write(JsonUtils.toJsonString(resp));
	}
}
