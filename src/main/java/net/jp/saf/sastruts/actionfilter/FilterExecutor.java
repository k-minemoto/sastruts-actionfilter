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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;

/**
 * 登録された{@link Actionfilter}を順次実行します.
 * 
 * @author k-minemoto
 *
 */
public class FilterExecutor {
	/**
	 * 実行する{@link Actionfilter}
	 */
	final protected List<Actionfilter> filters;
	/**
	 * 実行番号
	 */
	protected int sequenceNo;
	/**
	 * 実行する{@link Actionfilter}をもらうコンストラクタです.
	 * 
	 * @param filters 実行する{@link Actionfilter}
	 */
	public FilterExecutor(List<Actionfilter> filters) {
		this.filters = filters;
		this.sequenceNo = 0;
	}
	/**
	 * 次の{@link Actionfilter}を実行します.
	 * 
	 * @param request HTTPリクエスト
	 * @param response HTTPレスポンス
	 * @param context 処理のコンテキスト
	 * @return {@link Actionfilter}が返す{@link ActionForward}
	 */
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response, FilterContext context) {
		return filters.get(sequenceNo++).execute(request, response, context, this);
	}
}
