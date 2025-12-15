package com.alexis.timmaps.domain.logout.usecase;

import com.alexis.timmaps.di.module.Qualifiers;
import com.alexis.timmaps.domain.logout.repository.ILogoutRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Completable;

public class LogoutUseCase {

    private final ILogoutRepository repositoryLocal;
    private final ILogoutRepository repositoryRemote;

    @Inject
    public LogoutUseCase(@Named(Qualifiers.LOGOUT_REPOSITORY_LOCAL) ILogoutRepository repositoryLocal,
                         @Named(Qualifiers.LOGOUT_REPOSITORY_REMOTE) ILogoutRepository repositoryRemote) {
        this.repositoryLocal = repositoryLocal;
        this.repositoryRemote = repositoryRemote;
    }

    public Completable execute() {
        return Completable.mergeArray(repositoryLocal.deleteBackup(), repositoryRemote.deleteBackup());
    }

}
