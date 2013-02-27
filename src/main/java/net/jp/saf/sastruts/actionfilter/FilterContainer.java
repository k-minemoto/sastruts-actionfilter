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

import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link Actionfilter}設定の保持とリスト化を行います.
 * 
 * @author k-minemoto
 *
 */
public class FilterContainer {

	private final List<String> componentNames = CollectionsUtil.newCopyOnWriteArrayList();
	
	private int listSize = 1;
	
	/**
	 * {@link Actionfilter}コンポーネント定義名を追加します.
	 * SMART deploy時は、SMART deployの命名ルールに沿った値を設定して下さい.
	 * 
	 * @param componentName コンポーネント名.
	 */
	public void addActionfilter(String componentName) {
		this.listSize++;
		this.componentNames.add(componentName);
	}
	
	/**
	 * 定義された{@link Actionfilter}コンポーネントをリスト化します.
	 * 
	 * @return {@link Actionfilter}のインスタンスリスト.
	 */
	public List<Actionfilter> getActionfilters() {
		List<Actionfilter> list = CollectionsUtil.newArrayList(this.listSize);
		for(String componentName : componentNames) {
			list.add((Actionfilter) SingletonS2Container.getComponent(componentName));
		}
		return list;
	}
}
