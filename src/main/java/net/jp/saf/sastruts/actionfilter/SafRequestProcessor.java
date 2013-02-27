/**
 * Copyright (C) 2013- k-minemoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.jp.saf.sastruts.actionfilter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMapping;
import org.seasar.struts.action.S2RequestProcessor;
import org.seasar.struts.config.S2ActionMapping;

/**
 * S2RequestProcessorのprocessActionCreateで、SafActionWrapperのインスタンスを返すRequestProcessorです.
 * 
 * @author k-minemoto
 */
public class SafRequestProcessor extends S2RequestProcessor {
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Action processActionCreate(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping)
			throws IOException {
		Action action = null;
		try {
			action = new SafActionWrapper(((S2ActionMapping) mapping));
		} catch (Exception e) {
			log.error(getInternal().getMessage("actionCreate", mapping.getPath()), e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, getInternal().getMessage("actionCreate", mapping.getPath()));
			return null;
		}
		action.setServlet(servlet);
		return action;
	}

}
