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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.struts.action.ActionWrapper;
import org.seasar.struts.config.S2ActionMapping;
import org.seasar.struts.config.S2ExecuteConfig;
import org.seasar.struts.util.S2ExecuteConfigUtil;

/**
 * {@link ActionWrapper#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)}の実行前に、
 * {@link Actionfilter}による処理を割り込ませるActionWrapperです.
 * 
 * @author k-minemoto
 */
public class SafActionWrapper extends ActionWrapper {

	/**
	 * アクションマッピングをもらうコンストラクタです.
	 * 
	 * @param actionMapping アクションマッピング
	 */
	public SafActionWrapper(S2ActionMapping actionMapping) {
		super(actionMapping);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		S2ExecuteConfig executeConfig = S2ExecuteConfigUtil.getExecuteConfig();
		if (executeConfig != null) {
			final FilterContext context = new FilterContext(this.actionMapping, executeConfig, this.action, this.actionForm);
			List<Actionfilter> filters = getComponentContainer().getActionfilters();
			filters.add(new ActionWrapperExecuteActionfilter());
			return new FilterExecutor(filters).execute(request, response, context);
		}
		return null;
	}
	/**
	 * {@link FilterContainer}を取得します.
	 * 
	 * @return {@link FilterContainer}のインスタンス
	 */
	protected FilterContainer getComponentContainer() {
		return SingletonS2Container.getComponent(FilterContainer.class);
	}
	/**
	 * Actionクラスの実行メソッドを実行します.
	 * 
	 * {@link FilterExecutor}の最後に実行します.
	 * 
	 * @author k-minemoto
	 */
	private final class ActionWrapperExecuteActionfilter implements Actionfilter {
		/**
		 * {@inheritDoc}
		 */
		public ActionForward execute(HttpServletRequest request,
				HttpServletResponse response, FilterContext context,
				FilterExecutor executor) {
			return SafActionWrapper.this.execute(request, context.getExecuteConfig());
		}
	}
}
