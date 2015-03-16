## Issue を起票するときのルール ##
基本的にどんな事でも Issue に登録してよい。

### Issue の入力要領 ###

| **項目**   | **必須**  | **説明** | **備考** |
|:-------------|:------------|:-----------|:-----------|
| Summay   | <font color='red'>必須</font> | 課題を一言で表現 | 一覧に表示されるので簡潔に30字程度で |
| Description | 任意 | 課題の詳細を記載 | Summary で十分わかれば特に書かなくても大丈夫。とりあえず起票した時に考えていることを色々書く |
| Status   | <font color='red'>必須</font> | 課題の状態を記載 | 後述 |
| Owner    | 任意 | 課題を挙げた人が実施する予定であれば自分の名前を入れる。自分でする予定がなければ空欄にしておく。勝手に他人をアサインしない |  |
| Cc       | 任意 | ....よくわからない |  |
| Labels   | <font color='red'>必須</font> | Type-XXX と Priority-XXX は必須。 Milestone は運用するかもしれないけど未定 | Type については後述 |


### Status ###
使用する Issue の Status は以下の通り。

  * ソースコードを直す予定の Issue
    * New → Started → Fixed (or Invalid, WontFix)
  * それ以外のタスク
    * New → Started → Done

| **Status**  | **Open/Close** | **説明** |
|:------------|:---------------|:-----------|
| New       | Open         | とりあえず起票した。担当未定 |
| Started   | Open         | 誰かが作業中。起票してすぐに開始するか、もしくは New 状態の課題に対して着手したら Started に変更する |
| Fixed     | Close        | 課題が Defect の場合はその問題が解決したら、解決した人が Fixed に変更する。通常はコミットメッセージに (Fixes Issue NNN) と記載することで Fixed の状態に変更すること。課題が Enhancement の場合には、その機能を実装したら Fixed に変更する。|
| Invalid   | Close        | Defect だと思いきや、そうではなかった場合に Invalid に変更する |
| Duplicate | Close        | 既に別の Issue として挙がっている場合に Duplicate としてクローズする |
| WontFix   | Close        | Defect なんだけど、諸々の事情によって解決不能な場合に Won't Fix とする |
| Done      | Close        | Task は完了したら Done に変更する |

### Type ###
使用する Type-XXX タグは以下の通り

| **Type** | **説明** |
|:---------|:-----------|
| Type-Defect | バグに限らず、自分の思うような操作性でない場合はすべて Defect 扱いとする。|
| Type-Enhancement | 新たな機能を実装する場合。どうやって実装するかは後で考えればよいので、思いついたら起票する |
| Type-Task | ソースコードに修正が不要な作業。例えばこの Wiki に何かを書いたり、誰かに実装を督促してみたり、色々 |