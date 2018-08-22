package com.dentalclinic.capstone.utils;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;

public class Config {

	// PayPal app configuration
	public static final String PAYPAL_CLIENT_ID = "Adlrxyv1TYdbEKdlmnMAl8Lp5dGzUqbddB36_9S8G9FI1BzBhKYloEc2cPnlfSZjL9qiViqQtgcvrqtt";
	public static final String PAYPAL_CLIENT_SECRET = "";

	public static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
	public static final String PAYMENT_INTENT = PayPalPayment.PAYMENT_INTENT_SALE;
	public static final String DEFAULT_CURRENCY = "USD";

	// Our php+mysql server urls
	public static final String URL_PRODUCTS = "http://192.168.0.104/PayPalServer/v1/products";
	public static final String URL_VERIFY_PAYMENT = "http://192.168.0.104/PayPalServer/v1/verifyPayment";
	public static final String URL_CONVER_MONEY ="http://www.apilayer.net/api/live?access_key=8d05da726c83e4b1463e91cb134f2e89";
	public static final String ACCESS_KEY ="87c3a276dd52229dc68f4c51f583602a";
}
