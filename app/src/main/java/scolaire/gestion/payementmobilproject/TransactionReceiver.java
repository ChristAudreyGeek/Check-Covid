package scolaire.gestion.payementmobilproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;


public class TransactionReceiver extends BroadcastReceiver {

    public TransactionReceiver() { }

//    @Override
//    public void onReceive(Context context, Intent i) {
//        Transaction t = Transaction.getByUuid(i.getStringExtra("uuid"), context);
//        if (t != null) {
//            t.status = i.getStringExtra("status");
//            t.updateTimestamp = i.getLongExtra("update_timestamp", Utils.now());
//            t.save(context);
//        }
//        openActivity(context, i);
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String uuid = intent.getStringExtra("uuid");
        String confirmationCode, balance;

        if (intent.hasExtra("parsed_variables")) {
            HashMap<String, String> parsed_variables = (HashMap<String, String>)
                    intent.getSerializableExtra("parsed_variables");

            if (parsed_variables.containsKey("confirmCode"))
                confirmationCode = parsed_variables.get("confirmCode");
            if (parsed_variables.containsKey("balance"))
                balance = parsed_variables.get("balance");
        }

        context.startActivity(new Intent(context,MainActivity2.class));
    }

//    private void openActivity(Context c, Intent receivedIntent) {
//        Intent i = new Intent(receivedIntent);
//        i.setClass(c, MainActivity2.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        i.putExtra(Transaction.UUID, receivedIntent.getStringExtra("uuid");
//        c.startActivity(i);
//    }

}
