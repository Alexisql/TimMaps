package com.alexis.timmaps.ui.login;

import com.alexis.timmaps.domain.login.model.User;

public class LoginState {

    public static final class Loading extends LoginState {
    }

    public static final class Success extends LoginState {
        private final User user;

        public Success(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
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
