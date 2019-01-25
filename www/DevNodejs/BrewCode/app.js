"use strict";

const TelegramBot = require("node-telegram-bot-api");
const token = "799913515:AAF8EnIyd5uFfmXCV6AwyDovtvHWhvxl3cE";
const bot = new TelegramBot(token, { polling: true });

bot.onText(/\/start/, msg => {
  bot.sendMessage(msg.chat.id, "Welcome", {
    reply_markup: {
      keyboard: [["Sample text", "Second sample"], ["Keyboard"], ["I'm robot"]]
    }
  });
});

router.get("/piezo/:freq", function(request, response) {
  var freq = request.params.freq;

  piezo.play(freq, 200);

  response.end("piezo");
});
