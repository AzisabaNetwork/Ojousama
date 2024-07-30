# お嬢様プラグインですわ

## 使い方ですわ

1. プラグインをビルドしてpluginsの中に入れますわ。このプロジェクトはGradleを使用しているため、「gradlew build」や「./gradlew build」を使用するとbuild/libs/の中にjarファイルが生成されますのよ。
2. サーバーを起動しますわ
3. `/ojousama`で切り替えますわ、`/ojousama <メッセージ>`で一つのメッセージを翻訳いたしますわ

## 注意事項ですわ

- このプラグインはOpenAIのAPIを使用していますわ。OpenAIの利用規約やプライバシーポリシーをご確認くださいませ。
- config.ymlの中のpromptを書き換えるとお嬢様以外にも使えますわ。
- このプラグインはMinecraft 1.15.2で動作確認しておりますわ。他のバージョンでも動くと思われますけれど、未確認ですの。
- 動作テストしているのはRyuZUPluginChatのみですわ、LunaChatとRyuZUPluginChatとも併用して使えるはずですわ。

## ライセンスですわ

ライセンスはGPL v3を使用していますわ。詳しくはLICENSEファイルをご確認くださいませ。
