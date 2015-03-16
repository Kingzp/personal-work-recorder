# Androidでbashを使う方法 #

  * bashをインストールし、PuTTYを使ってアクセスすることでTab補完が使えるようになります。
  * AVDの起動～bashのインストールは、毎回おこなう必要があります。

## bashのインストール ##

  1. bash のダウンロード
    * Android 用にコンパイル済みの単品の bash コマンドを作ってくれた方がいらっしゃいます。これをダウンロードします。
      * http://forum.xda-developers.com/showthread.php?t=537827
  1. AVDの起動
    * ファイルを追加するため、大きめのパーティションサイズ(MB)を指定します。
      * tools\emulator -partition-size 128 -avd 「名前」
  1. bash のインストール
    * /system パーティションを書き込み可能にします。
      * platform-tools\adb remount
    * /system/bin に bash をコピーします。
      * platform-tools\adb push PC上のbash /system/bin
    * bash を実行可能にします。
      * platform-tools\adb shell chmod 555 /system/bin/bash
  1. 確認
    * 念のため、bashがインストールできたか確認します。
      * platform-tools\adb shell
      * ⇒ #
      * bash
      * ⇒ bash-3.2#
    * おおっ！ しかし、タブ補完できません。

## ADB対応版PuTTYのインストール ##

  1. PuTTYのダウンロード
    * Windows用にはexeが提供されています。これをダウンロードします。Macな人はソースからビルドしましょう。
      * https://github.com/sztupy/adbputty/downloads
    * exe単品なので、適当なところに置けばインストール終了です。

## いよいよ接続 ##

  1. PuTTYの起動
    * 適当な場所に置いたPuTTYを起動すると設定画面が表示されます。
  1. 設定の登録
    * HostName ⇒ "transport-device" という値を入力します。
    * Port ⇒ "5037" ですが、放っておいても次のステップで自動入力されます。
    * ConnectionType ⇒ "Adb" を選択します。（Portに5037が自動入力されます。）
    * Saved SEssions ⇒ テキストボックスに適当な名前を入力します。
    * 「Save」ボタンを押すと設定が保存されます。
  1. 接続
    * 「Open」ボタンを押します。
      * ⇒ #
      * bash
      * ⇒ bash-3.2#
      * cd /da まで入力して Tab キーを押すと...
      * ⇒ cd /data ヤターっ！

## 補足 ##

  * 上記で保存した /system/bin/bash は、エミュレータを再起動すると再び無くなります。
  * /system パーティションのイメージを system.img に保存する方法もあるようですが、それはまたの機会に。