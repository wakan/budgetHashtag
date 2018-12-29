package fr.budgethashtag.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import fr.budgethashtag.BudgetHashtagApplication;
import fr.budgethashtag.R;
import fr.budgethashtag.transverse.event.ErrorEvent;
import fr.budgethashtag.transverse.event.ExceptionManagedEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public abstract class MotherActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        super.onStart();
        BudgetHashtagApplication.instance.onStartActivity();
        EventBus.getDefault().register(this);
        EventBus.getDefault().register(getPresenter());
    }

    private ActionBar actionBar;
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Budget Hashtag");
        actionBar.setSubtitle("Screen name or portefeuille name");
    }
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(getPresenter());
        EventBus.getDefault().unregister(this);
        BudgetHashtagApplication.instance.onStopActivity();
        super.onStop();
    }

    public abstract MotherPresenter getPresenter();

    private TextView txvException;
    private String strClickToDismiss=null;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ExceptionManagedEvent event){
        int errorMessageId=event.getExceptionManaged().getErrorMessageId();
        String errorMessage;
        if(errorMessageId==0){
            errorMessage=event.getExceptionManaged().getErrorMessage();
        }else{
            errorMessage=getResources().getString(errorMessageId);
        }
        displayException(errorMessage);
    }
    private void displayException(String errorMessage) {
        // Display the error to your user
        // To do so, every layout of your Activity or Fragment should include the
        // exceptionLayout at it top level
        if (txvException == null) {
            // instantiate the TextView and the Drawable
            txvException = (TextView) findViewById(R.id.txv_exception_message);
            if(strClickToDismiss==null){
                strClickToDismiss = getString(R.string.txv_exception_clicktodismiss);
            }
            txvException.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideTxvException();
                }
            });
        }
        if (errorMessage == null) {
            txvException.setText(getString(R.string.no_error_message));
        } else {
            txvException.setText(errorMessage + strClickToDismiss);
        }
        txvException.setVisibility(View.VISIBLE);
    }
    private void hideTxvException() {
        txvException.setVisibility(View.GONE);
    }
    private TextView txvErrors;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ErrorEvent event){
        int errorMessageId=event.getErrorMessageId();
        String errorMessage;
        if(errorMessageId==0){
            errorMessage=event.getErrorMessage();
        }else{
            errorMessage=getResources().getString(errorMessageId);
        }
        displayError(errorMessage);
    }
    private void displayError(String errorMessage) {
        // Display the error to your user
        // To do so, every layout of your Activity or Fragment should include the
        // exceptionLayout at it top level
        if (txvErrors == null) {
            // instantiate the TextView and the Drawable
            txvErrors = (TextView) findViewById(R.id.txv_error_message);
            if(strClickToDismiss==null){
                strClickToDismiss = getString(R.string.txv_exception_clicktodismiss);
            }
            txvErrors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideTxvError();
                }
            });
        }
        if (errorMessage == null) {
            txvErrors.setText(getString(R.string.no_error_message));
        } else {
            txvErrors.setText(errorMessage + strClickToDismiss);
        }
        txvErrors.setVisibility(View.VISIBLE);
    }
    private void hideTxvError() {
        txvErrors.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mother, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent browserIntent;
        switch (item.getItemId()) {
            case R.id.action_show_website:
                browserIntent = new Intent("android.intent.action.VIEW");
                browserIntent.setData(Uri.parse(getResources().getString(R.string.website_url)));
                startActivity(browserIntent);
                return true;
            case R.id.action_mail_dev:
                String subject = getResources().getString(R.string.mail_subject);
                String body = getResources().getString(R.string.mail_body);
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { getResources().getString(R.string.dev_email) });
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
