package com.alexis.timmaps.ui.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alexis.timmaps.di.module.Qualifiers;
import com.alexis.timmaps.domain.maps.model.Location;
import com.alexis.timmaps.domain.maps.usecase.GetRouteMapsUseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MapsViewModel extends ViewModel {

    private final GetRouteMapsUseCase mapsUseCase;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    private final MutableLiveData<MapsState> state = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MapsViewModel(GetRouteMapsUseCase mapsUseCase,
                         @Named(Qualifiers.IO_SCHEDULER) Scheduler ioScheduler,
                         @Named(Qualifiers.MAIN_SCHEDULER) Scheduler mainScheduler) {
        this.mapsUseCase = mapsUseCase;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;

    }

    public LiveData<MapsState> getState() {
        return state;
    }

    public void getRoute(Location origen, Location destination) {
        state.setValue(new MapsState.Loading());
        disposables.add(
                mapsUseCase.execute(origen, destination)
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(
                                route -> state.setValue(new MapsState.Success(route)),
                                throwable -> state.setValue(new MapsState.Error(throwable.getMessage()))
                        )
        );
    }

}
