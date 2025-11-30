package com.lilamaris.capstone.adapter.in.security.authn.credential;

public class CredentialRequest {
    public record SignIn(String email, String password) {}
    public record Register(String displayName, String email, String password) {}
}