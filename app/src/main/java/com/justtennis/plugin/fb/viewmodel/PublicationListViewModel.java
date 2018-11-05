package com.justtennis.plugin.fb.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import com.justtennis.plugin.fb.model.Publication;

import java.util.List;

public class PublicationListViewModel extends AndroidViewModel
{
    private final LiveData<List<Publication>> livePublications;

    public PublicationListViewModel(Application application, LiveData<List<Publication>> livePublications) {
        super(application);
        this.livePublications = livePublications;
    }

    // Observe change in viewmodel's data
    public void addObserver(LifecycleOwner owner, Observer<List<Publication>> observer) {
        livePublications.observe(owner, observer);
    }

    // Expose the LiveData so the UI can addObserver it.
    public void removeObserver(Observer<List<Publication>> observer) {
        livePublications.removeObserver(observer);
    }
}
