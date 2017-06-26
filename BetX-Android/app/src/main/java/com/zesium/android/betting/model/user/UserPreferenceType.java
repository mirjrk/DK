package com.zesium.android.betting.model.user;

/**
 * Created by Ivan Panic_2 on 7/4/2016.
 */
public enum UserPreferenceType {
    EmailSendNewsLeter(0),
    EmailSendInfoAboutTicketDetail(1),
    EmailSendInfoAboutWinningTicket(2),
    EmailSendInfoAboutMoneyTransactions(3),
    SmsSendAInfoAboutAllTicketDetail(4),
    SmsSendInfoAboutWin(5),
    SmsSendInfoAboutMoney(6),
    SendClubCard(7);

    private final int id;

    UserPreferenceType(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
