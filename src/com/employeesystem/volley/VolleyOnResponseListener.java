package com.employeesystem.volley;


public interface VolleyOnResponseListener {

    public void onSuccess(String response, String message);

    public void onFailure(String error);
}
