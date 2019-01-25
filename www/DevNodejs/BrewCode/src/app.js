"use strict";

const express = requeire("express");
const Telegram = require("telegram-node-bot");
const TelegramBaseController = Telegram.TelegramBaseController;
const TextCommand = Telegram.TextCommand;
const chatbot = new Telegram.Telegram(
  "799913515:AAF8EnIyd5uFfmXCV6AwyDovtvHWhvxl3cE"
);


class EventsController extends TelegramBaseController {
  allEventsAction(scope) {
    let msg = `QConSP - 24-25-26/04/2017 - qconsp.com\nFrontInSampa - 01/07/2017 - frontinsampa.com.br`;
    scope.sendMessage(msg);
  }
  get routes() {
    return {
      brassagem: "allEventsAction"
    };
  }
}

chatbot.router.when(
  new TextCommand("/brassagem", "brassagem),
  new EventsController()
);
