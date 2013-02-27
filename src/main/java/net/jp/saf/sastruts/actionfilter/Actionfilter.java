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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;

/**
 * Actionの実行メソッドの実行前後に処理を入れるためのインタフェースです.
 * 
 * @author k-minemoto
 */
public interface Actionfilter {
	/**
	 * Actionの実行メソッドの前後に、任意の処理を行います.
	 * <p>
	 * {@link FilterExecutor#execute(HttpServletRequest, HttpServletResponse, FilterContext)}の前に処理を書いた場合、
	 * Actionの実行メソッド実行前に処理されます.<br />
	 * 後に書いた場合、Actionの実行メソッド終了後に実行されることになります.
	 * </p>
	 * 
	 * @param request HTTPリクエスト
	 * @param response HTTPレスポンス
	 * @param context 評価中のコンテキスト
	 * @param executor Actionfilterの実行クラス
	 * @return 次の遷移先を示す{@link ActionForward}のインスタンス. 遷移先が無い場合はnull
	 */
	ActionForward execute(
			HttpServletRequest request,
			HttpServletResponse response, 
			FilterContext context,
			FilterExecutor executor);
}
