package com.alexis.timmaps.ui.login;

public class LoginState {

    public static final class Loading extends LoginState {
    }

    public static final class Success extends LoginState {

        public Success() {
        }
    }

    public static final class Error extends LoginState {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
