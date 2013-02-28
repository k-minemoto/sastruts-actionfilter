sastruts-actionfilter
==============
sastruts-actionfilterは、Actionクラスの実行メソッド実行前に処理を行う仕組みを提供します。

SAStrutsの標準的な処理フローは、次のような流れになります。

　　S2RequestProcessor#processActionPerform  
　　　1)↓　　　8)↑  
　　ActionWrapper#execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  
　　　2)↓　　　7)↑  
　　ActionWrapper#execute(HttpServletRequest request, S2ExecuteConfig executeConfig)  
　　　3)↓　　　6)↑  
　　actionCustomizer等で設定したインターセプターの処理  
　　　4)↓　　　5)↑  
　　POJOのアクションクラスの実行メソッド  

**上記の3)のタイミングでバリデーション処理が実行されるため、通常はバリデーションよりも前に処理を入れることができません。**

当ライブラリは、次の個所に処理を割り込ませる仕組みを提供します。

　　S2RequestProcessor#processActionPerform  
　　　1)↓　　　8)↑  
　　ActionWrapper#execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  
　　　　↓　　　　↑  
　　 **Actionfilter#execute ← このタイミング**  
　　　2)↓　　　7)↑  
　　ActionWrapper#execute(HttpServletRequest request, S2ExecuteConfig executeConfig)  
　　　3)↓　　　6)↑  
　　actionCustomizer等で設定したインターセプターの処理  
　　　4)↓　　　5)↑  
　　POJOのアクションクラスの実行メソッド  

Actionfilterのインスタンスは、サーブレットのFilterのように次々とチェインして実行されます。  
よって、多段に処理を重ねることが可能です。

インストール
------
mavenから取得できます。pomファイルのdependenciesに次のように定義してください。

    <dependency>
        <groupId>net.jp.saf.sastruts</groupId>
        <artifactId>sastruts-actionfilter</artifactId>
        <version>0.1.0</version>
    </dependency>

初期設定
------

SMART deployを利用する場合、creator.diconとcustomizer.diconの設定が必要です。  
SMART deployは必須ではありませんが、利用しない場合はHOT deployでClassCastException等の問題が発生します。

### creator.dicon

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE components PUBLIC "-//SEASAR//DTD S2Container 2.4//EN" "http://www.seasar.org/dtd/components24.dtd">
    <components>
        <include path="convention.dicon"/>
        <include path="customizer.dicon"/>
        ～既存のCreatorの設定～
        ↓追加する
        <component class="net.jp.saf.sastruts.actionfilter.creator.ActionfilterCreator"/>
    </components>

### customizer.dicon

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE components PUBLIC "-//SEASAR//DTD S2Container 2.4//EN" "http://www.seasar.org/dtd/components24.dtd">
    <components>
        <include path="default-customizer.dicon"/>
        ～既存のCustomizerの設定～
        ↓追加する
        <component name="actionfilterCustomizer" class="org.seasar.framework.container.customizer.CustomizerChain">
        </component>
    </components>

### struts-config.xml

ActionWrapperの生成方法を変更するため、strutsのprocessorClassを変更する必要があります。

    <controller
        ～略～
        processorClass="net.jp.saf.sastruts.actionfilter.SafRequestProcessor"

既に別のRequestProcessorに変更している場合、該当のRequestProcessorのprocessActionCreateを  
SafRequestProcessorと同様にオーバーライドして下さい。  
具体的には、new ActionWrapperとしている箇所を、new SafActionWrapperに変更するだけです。

### app.dicon、またはapp.diconからincludeされているdiconファイル

作成したコンポーネントを登録するための設定が必要になります。  
app.dicon、またはapp.diconからincludeされているdiconに、次の設定を追加して下さい。

    ↓追加する
    <component class="net.jp.saf.sastruts.actionfilter.FilterContainer">
    </components>

使い方
------

### パッケージ

SMART deployを使用する場合、ルートパッケージの直下に「actionfilter」というパッケージを作成して下さい。

### 実装

インタフェース net.jp.saf.sastruts.actionfilter.Actionfilter を実装して下さい。  
クラス名は、SMART deployのルールより **サフィックスをActionfilter** として下さい。

#### 実装例(ルートパッケージをtutorialとした場合)

    package tutorial.actionfilter;

    public class SampleActionfilter implements Actionfilter {

        public ActionForward execute(HttpServletRequest request,
                HttpServletResponse response, FilterContext context,
                FilterExecutor executor) {
            ////////////////////////////////////////////////////////////////
            // Actionの実行メソッドよりも先に実行されます。
            ////////////////////////////////////////////////////////////////

            // Actionクラスのインスタンスが取得できます
            Object action = context.getAction();
            // ActionクラスにDIされるFormクラスのインスタンスが取得できます
            // ただし、Actionクラスが@ActionForm付きのフィールドを持っていない場合、Actionクラスが返ってきます
            Object actionForm = context.getActionForm();
            // 実行されるActionクラスのメソッドが取得できます
            Method actionMethod = context.getActionMethod();

            // TODO: Actionの実行メソッドよりも先に実行する処理を実装

            // 次のActionfilterを実行する。最後はActionクラスの実行メソッドが実行される
            ActionForward forward = executor.execute(request, response, context);

            ////////////////////////////////////////////////////////////////
            // ここからはActionの実行メソッドよりも後に実行されます。
            ////////////////////////////////////////////////////////////////

            // TODO: Actionの実行メソッド実行後に実行する処理を実装

            return forward;
        }
    }

SMART deploy時は、次の点に注意して下さい。

**instance属性はsingleton**

デフォルトはsingletonとしてコンポーネント登録するため、  
Actionfilter実装クラスのフィールドは全てのリクエストで共有されることになります。  
このinstance属性を変更する場合は、次のいずれかの方法で変更可能です。

**全てのinstance属性を変更する場合**

creator.diconのCreator設定で変更して下さい。

    <!-- 全てのinstance属性をREQUESTに変更する -->
    <component class="net.jp.saf.sastruts.actionfilter.creator.ActionfilterCreator">
        <property name="instanceDef">@org.seasar.framework.container.deployer.InstanceDefFactory@REQUEST</property>
    </component>

**特定クラスのみinstance属性を変更したい場合**

設定では対応できないため、クラスに@Componentアノテーションを設定して下さい。

    @Component(instance=InstanceType.REQUEST)
    public class RequestScopeActionfilter implements Actionfilter {

### 実行設定

FilterContainerのコンポーネント定義にコンポーネント名を設定します。  
コンポーネント名は、SMART deployの命名ルールに沿った名称になります。  
ルートパッケージをtutorialとした場合、次のようになります。

tutorial.actionfilter.SampleActionfilter  
→sampleActionfilter  
tutorial.actionfilter.hoge.HugaActionfilter  
→hoge_hugaActionfilter  

コンポーネント定義は、次のようになります。

    <component class="net.jp.saf.sastruts.actionfilter.FilterContainer">
        <initMethod name="addActionfilter"><arg>"sampleActionfilter"</arg></initMethod>
        <initMethod name="addActionfilter"><arg>"hoge_hugaActionfilter"</arg></initMethod>
    </component>

実行順序は、FilterContainerにaddActionfilterで登録された順となります。

