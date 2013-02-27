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

import java.lang.reflect.Method;

import org.seasar.struts.config.S2ActionMapping;
import org.seasar.struts.config.S2ExecuteConfig;

/**
 * {@link Actionfilter}が評価中のコンテキストです.
 * 
 * @author k-minemoto
 */
public class FilterContext {

	final private S2ActionMapping actionMapping;

	final private S2ExecuteConfig executeConfig;

	final private Object action;

	final private Object actionForm;

	/**
	 * コンストラクタ.
	 * 
	 * @param actionMapping アクションマッピング
	 * @param executeConfig  実行メソッド設定
	 * @param action アクションクラス(POJO)のインスタンス
	 * @param actionForm アクションフォーム(POJO)のインスタンス
	 */
	public FilterContext(S2ActionMapping actionMapping, S2ExecuteConfig executeConfig, Object action, Object actionForm) {
		this.actionMapping = actionMapping;
		this.executeConfig = executeConfig;
		this.action = action;
		this.actionForm = actionForm;
	}

	/**
	 * アクションマッピングを取得します.
	 * 
	 * @return 評価中のアクションマッピング
	 */
	public S2ActionMapping getActionMapping() {
		return actionMapping;
	}

	/**
	 * 実行メソッド用の設定を取得します.
	 * 
	 * @return 実行メソッド用の設定
	 */
	public S2ExecuteConfig getExecuteConfig() {
		return executeConfig;
	}

	/**
	 * 評価中のActionクラス(POJO)のインスタンスを取得します.
	 * 
	 * @return Actionクラスのインスタンス
	 */
	public Object getAction() {
		return action;
	}

	/**
	 * 評価中のActionクラスにDIされるActionFormクラスのインスタンスを取得します.
	 * Actionクラスに@ActionFormアノテーションの付いたフィールドが存在しない場合、Actionクラスが戻ってきます.
	 * 
	 * @return ActionFormクラスのインスタンス、またはActionクラスのインスタンス.
	 */
	public Object getActionForm() {
		return actionForm;
	}

	/**
	 * Actionクラスで実行されるメソッドを返します.
	 * 
	 * @return 実行されるメソッド
	 */
	public Method getActionMethod() {
		return executeConfig.getMethod();
	}
}
