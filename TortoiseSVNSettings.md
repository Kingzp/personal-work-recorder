## TortoiseSVN の設定 ##

TortoiseSVN に限らず、Subversion 関連の設定をここに記載する

### TortoiseSVN の設定ファイルの場所 ###

  1. デスクトップを右クリックするなどして、TortoiseSVN → Settings を選択
  1. 一般のページで Subversion グループの編集ボタンを押すと設定ファイルがメモ帳で開く

### Proxy の設定 ###

  1. デスクトップを右クリックするなどして、TortoiseSVN → Settings を選択
  1. ネットワークのページを開くと Proxy を有効にするオプションが開くので、ここにプロキシのアドレスとポートを入力する。
  1. 社内の SVN サーバーを利用している場合は、社内のリポジトリの場所は例外として登録しておく(業務で使ってる人は多分もう設定されている)

編集ボタンを押すと、これらの設定を普通のメモ帳でも編集できる。

### auto property の設定 ###

Subversion で管理されるファイルには、実際のファイルの内容とは別に property という属性が設定できます。
普通は気にしなくてもよいのですが、ファイルの改行コードだけ環境依存なので各自変更するように

  1. 設定ファイルで enable-auto-props = ... と書かれている行を探す
  1. 先頭 '#' がコメントアウトなので、これを外して enable-auto-props = yes とする
  1. [auto-props] セクションに必要な設定を記載する。 `*.java` と `*.xml` について、svn:eol-style=native を設定する

こんな風
```
# (前略)
### Set enable-auto-props to 'yes' to enable automatic properties
### for 'svn add' and 'svn import', it defaults to 'no'.
### Automatic properties are defined in the section 'auto-props'.
enable-auto-props = yes
### Set interactive-conflicts to 'no' to disable interactive
### conflict resolution prompting.  It defaults to 'yes'.
# interactive-conflicts = no

### Section for configuring automatic properties.
[auto-props]
# (いろいろな設定)
*.java = svn:eol-style=native
*.xml  = svn:eol-style=native

```

上記のように設定すると、新規で svn リポジトリに追加される .java ファイルと .xml ファイルは改行コードが LF でリポジトリに格納されます。
Windows 環境でそのファイルをチェックアウトすると、自動的に TortoiseSVN が改行コードを CR+LF に変換するはず。


### CUI の svn クライアントを利用している場合 ###

設定ファイルは $HOME/.subversion/config あたりに存在するので探して編集する