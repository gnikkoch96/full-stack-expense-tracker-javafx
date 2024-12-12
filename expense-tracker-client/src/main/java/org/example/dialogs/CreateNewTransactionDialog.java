package org.example.dialogs;

import org.example.models.User;

public class CreateNewTransactionDialog extends CustomDialog{
    public CreateNewTransactionDialog(User user) {
        super(user);
        setWidth(700);
        setHeight(595);
    }
}
