const Telegram = require("telegram-node-bot");
const TelegramBaseController = Telegram.TelegramBaseController;
const TextCommand = Telegram.TextCommand;
const tg = new Telegram.Telegram(
  "790864314:AAGepSynRejDpSt41obj9pHncP3Ux_PKrqg"
);

class TeleGramBotNodejs extends TelegramBaseController {
  /**
   * @param {Scope} $
   */
  greetingHandler($) {
    $.sendMessage("Hey, how are you?");
  }
  get routes() {
    return {
      hey: "greetingHandler",
      hi: "greetingHandler",
      hello: "greetingHandler"
    };
  }
}
tg.router.when(["hey", "hi", "hello"], new TeleGramBotNodejs());
