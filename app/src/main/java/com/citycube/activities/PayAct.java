package com.citycube.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.citycube.R;
import com.citycube.databinding.ActivityPayBinding;
import com.citycube.model.PaymentModel;
import com.citycube.retrofit.ApiClient;
import com.citycube.retrofit.CityCubeUserInterface;
import com.citycube.retrofit.Constant;
import com.citycube.utility.DataManager;
import com.citycube.utility.NetworkReceiver;
import com.google.gson.Gson;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayAct extends AppCompatActivity {
  ActivityPayBinding binding;
    public String TAG = "PayAct";
    CityCubeUserInterface apiInterface;
    String amount = "", requestId = "", cardNumber = "", expiryMonth = "", expiryDate = "", cvvv = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(CityCubeUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pay);
        initviews();
    }

    private void initviews() {


        if (getIntent() != null) {
            requestId = getIntent().getStringExtra("request_id");
            amount = getIntent().getStringExtra("amount");

            // DecimalFormat df = new DecimalFormat("0.00");
            // binding.btMakePayment.setText("€" + String.format("%.2f", Double.parseDouble(amount1)) + " " + getString(R.string.pay));
            // binding.payment.setText("€" + String.format("%.2f", Double.parseDouble(amount1)) + " " +   getString(R.string.pay));

            binding.reservationToolbar.tvTitle.setText(getString(R.string.pay));

        }


        binding.btnPay.setOnClickListener(v -> {

            if (binding.cardForm.isValid()){
                Card.Builder card = new Card.Builder(binding.cardForm.getCardNumber(),
                        Integer.valueOf(binding.cardForm.getExpirationMonth()),
                        Integer.valueOf(binding.cardForm.getExpirationYear()),
                        binding.cardForm.getCvv());

                if (!card.build().validateCard()) {
                    Toast.makeText(PayAct.this, getString(R.string.card_not_valid), Toast.LENGTH_SHORT).show();
                    return;
                }
                Stripe stripe = new Stripe(PayAct.this, Constant.STRIPE_TEST_KEY);
                //  Stripe stripe = new Stripe(PaymentAct.this, Constant.STRIPE_LIVE_KEY);

                DataManager.getInstance().showProgressMessage(PayAct.this, getString(R.string.please_wait));
                stripe.createCardToken(
                        card.build(), new ApiResultCallback<Token>() {
                            @Override
                            public void onSuccess(Token token) {
                                DataManager.getInstance().hideProgressMessage();
                                Log.e("Stripe Token===", token.getId());
                                // Toast.makeText(mContext, getString(R.string.successful), Toast.LENGTH_SHORT).show();
                                // charge(token);
                                if (NetworkReceiver.isConnected())
                                    PayProvider1(amount, requestId, token.getId());
                                else
                                    Toast.makeText(PayAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
                               /* if(!DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId.equals(""))
                                addCard(DataManager.getInstance().getUserData(PaymentFirstActivity.this).result.custId,token.getId());
                                 else
                                     SaveCard(token.getId());*/
                            }

                            @Override
                            public void onError(@NotNull Exception e) {
                                DataManager.getInstance().hideProgressMessage();
                                e.printStackTrace();
                                Toast.makeText(PayAct.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                // paymentConfirmDialog();
            }

            else Toast.makeText(this, getText(R.string.please_complete_form), Toast.LENGTH_LONG).show();

        });

        binding.reservationToolbar.ivBack.setOnClickListener(v -> onBackPressed());


        cardInit();
    }


    private void cardInit() {
        binding.cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                // .mobileNumberExplanation("SMS is required on this number")
                .setup(PayAct.this);
        binding.cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
    }




    private void PayProvider1(String amount, String requestId, String token) {
        DataManager.getInstance().showProgressMessage(PayAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(PayAct.this).result.id);
        map.put("request_id", requestId);
        map.put("payment_method", "Stripe");
        map.put("total_amount", amount);
        map.put("token", token);
        map.put("currency", "EUR");
        Log.e("MapMap", "PAYMENT REQUEST" + map);
        Call<PaymentModel> payCall = apiInterface.payment(map);
        payCall.enqueue(new Callback<PaymentModel>() {
            @Override
            public void onResponse(Call<PaymentModel> call, Response<PaymentModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    PaymentModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "PAYMENT RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        //String dataResponse = new Gson().toJson(response.body());
                        // Log.e("MapMap", "PAYMENT RESPONSE" + dataResponse);

                        Toast.makeText(PayAct.this, getString(R.string.request_send_successfully), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PayAct.this, HomeActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    } else if (data.status.equals("0")) {
                        Toast.makeText(PayAct.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<PaymentModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }


}
