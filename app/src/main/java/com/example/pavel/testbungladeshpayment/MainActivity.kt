package com.example.pavel.testbungladeshpayment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.sslcommerz.library.payment.Util.ConstantData.ErrorKeys
import com.sslcommerz.library.payment.Util.JsonModel.TransactionInfo
import com.sslcommerz.library.payment.Listener.OnPaymentResultListener
import com.sslcommerz.library.payment.Classes.PayUsingSSLCommerz
import com.sslcommerz.library.payment.Util.ConstantData.SdkCategory
import com.sslcommerz.library.payment.Util.ConstantData.SdkType
import com.sslcommerz.library.payment.Util.ConstantData.CurrencyType
import com.sslcommerz.library.payment.Util.Model.MandatoryFieldModel


class MainActivity : AppCompatActivity() {

    val TAG = "PayUsingSSLCommerz"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { sendTestRequest() }
    }

    private fun sendTestRequest() {
        val mandatoryFieldModel = MandatoryFieldModel("testbox", "qwerty", "10", "1012", CurrencyType.BDT, SdkType.TESTBOX, SdkCategory.BANK_LIST)

        PayUsingSSLCommerz.getInstance().setData(this, mandatoryFieldModel, object : OnPaymentResultListener {
            override fun transactionSuccess(transactionInfo: TransactionInfo) {
                // If payment is success and risk label is 0.
                if (transactionInfo.riskLevel == "0") {
                    Log.d(TAG, "Transaction Successfully completed")
                } else {
                    Log.d(TAG, "Transaction in risk. Risk Title : " + transactionInfo.riskTitle.toString())
                }// Payment is success but payment is not complete yet. Card on hold now.
            }

            override fun transactionFail(transactionInfo: TransactionInfo) {
                // Transaction failed
                Log.e(TAG, "Transaction Fail")
            }

            override fun error(errorCode: Int) {
                when (errorCode) {
                    // Your provides information is not valid.
                    ErrorKeys.USER_INPUT_ERROR -> Log.e(TAG, "User Input Error")
                    // Internet is not connected.
                    ErrorKeys.INTERNET_CONNECTION_ERROR -> Log.e(TAG, "Internet Connection Error")
                    // Server is not giving valid data.
                    ErrorKeys.DATA_PARSING_ERROR -> Log.e(TAG, "Data Parsing Error")
                    // User press back button or canceled the transaction.
                    ErrorKeys.CANCEL_TRANSACTION_ERROR -> Log.e(TAG, "User Cancel The Transaction")
                    // Server is not responding.
                    ErrorKeys.SERVER_ERROR -> Log.e(TAG, "Server Error")
                    // For some reason network is not responding
                    ErrorKeys.NETWORK_ERROR -> Log.e(TAG, "Network Error")
                }
            }
        })
    }
}
