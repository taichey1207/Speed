## 変更点など(羽田、1/17)

基本的にはプログラムが上手く動いていそう？

- 現在の問題点
  - コンソール上で実行すると動くが、Eclipse上で実行するとエラーが出て動かない(エラー内容は後述)
    - JREのバージョンが原因？
  - バリア同期などの排他処理が上手く動いているのか分からない
    - バリア同期を無効にすると失敗し、有効にすると失敗しなくなることを確認できれば良い
    - 良い検証方法が思い付かない
  - playerのstackedCardsが0枚になっても描画されてしまう
    - 解決は面倒そう
    - プログラムの動きには影響しないので後回しでもいい

- Eclipse上での実行時のエラー内容
```
#
# A fatal error has been detected by the Java Runtime Environment:
#
#  EXCEPTION_ACCESS_VIOLATION (0xc0000005) at pc=0x00007ff8fc1ffa4a, pid=23708, tid=17132
#
# JRE version: OpenJDK Runtime Environment AdoptOpenJDK (11.0.8+10) (build 11.0.8+10)
# Java VM: OpenJDK 64-Bit Server VM AdoptOpenJDK (11.0.8+10, mixed mode, tiered, compressed oops, g1 gc, windows-amd64)
# Problematic frame:
# C  [awt.dll+0x8fa4a]
#
# No core dump will be written. Minidumps are not enabled by default on client versions of Windows
#
# If you would like to submit a bug report, please visit:
#   https://github.com/AdoptOpenJDK/openjdk-support/issues
# The crash happened outside the Java Virtual Machine in native code.
# See problematic frame for where to report the bug.
#
```

- 主な変更点
  - build.batでコンパイル、execute.batでプログラムの実行
    - Windows限定
  - unfreeze実行後に描画更新処理を行うよう変更
  - Cardクラスのインスタンスの参照渡しの回避(バグ対策)
  - コンソールへのログの出力

- Speed.java
  - ログ出力の追加
  - userFrameをstaticフィールドに
    - UserFrame.update用
  - unfreezeの前に少しだけ待つよう変更
  - unfreezeに描画更新処理を追加
  - スリープによる動作軽減
  - ログ出力用のlogメソッドの追加

- Card.java
  - コピーコンストラクタを追加

- Table.java
  - Cardのコピーコンストラクタを使用するよう変更

- UserFrame.java
  - UserFrameにupdateメソッドを追加
    - 描画更新

- ManualPlayer.java
  - ログ出力の追加
  - 描画更新をUserFrame.updateに置き換え

- AutoPlayer.java
  - ログ出力の追加
  - 描画更新をUserFrame.updateに置き換え
  - スリープによる動作軽減

# 仕様

#### ゲームの仕様
1. CPUと1対1の対戦を行う。
1. ジョーカーを除いた52枚のカードを用いる。
1. テーブルの中央には2枚のカード(台札)、
手元には最大4枚のカードが置いてあり(場札)、
残りはテーブルの上に裏返して置く(手札)。
1. 台札のカードの数字と1だけ違うカードが場札にあれば、
そのカードを出すことができる。
(ただしKとAは1つ違いだとみなす)
1. 場札が3枚以下になったら手札から補充して4枚にする。
1. どちらのプレイヤーも出せるカードが無くなった場合、
場札のカードの中から1枚ずつランダムにカードが選ばれ、
台札の上に置かれる。
1. 先にカードが無くなったプレイヤーの勝利。

##### GUIの操作方法
画面中央付近にあるラジオボタンを操作することで、2つある内のどちらのカードの上に手札のカードを出すかを選択できる。
手札のカードの下にあるボタンを押すことでカードを出すことができる。

###### ユーザの操作画面のイメージ
<a href=speed_prototype.png>
<img src=speed_prototype.png width="600" height="auto"/>
</a>

#### プログラムの仕様
詳細については[Javadoc](doc/index.html)を読んでください。
(GitBucket上では見れないようです)

3つのスレッドを用いてゲームを実現する。<br>
各スレッドのクラス名はManualPlayer, AutoPlayer, Speedである。<br>
ManualPlayerとAutoPlayerはそれぞれユーザとCPUを表しており、
Speedは以下の処理を担当する。
- 2人のプレイヤーが共にどのカードも出せない状態に陥ったら、
2人のプレイヤーの場札からランダムにカードを選んで台札に置く。
- どちらかのプレイヤーのカードが無くなれば対戦結果を表示し、
自身を含めた3つのスレッド(ManualPlayer, AutoPlayer, Speed)を終了させる。


# 担当
配分に偏りがあるかもしれませんがご了承ください。<br>
必要に応じてフィールド・メソッドを追加して下さい。<br>
(敬称略)
- 平田
    - Speed.java
      - main
      - run
- 井上
    - Player.java
      - tryPutCard
      - removeCard
      - hasNoCard
- 渡辺
    - Card.java
      - numberToString
      - suitToString
    - AutoPlayer.java
      - run
- 原田
    - ManualPlayer.java
      - run
- Cai
    - UserFrame.java
      - paint
