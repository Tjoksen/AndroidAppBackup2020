package com.talentjoko.todoapplication.domain.usecases;
import com.talentjoko.todoapplication.domain.Repository;
import com.talentjoko.todoapplication.domain.common.Transformer;
import com.talentjoko.todoapplication.domain.entities.User;

import java.util.Collections;
import java.util.Map;

import io.reactivex.Observable;

public class GetCurrentUserUseCase extends UseCase<User> {

    private Repository repository;

    public GetCurrentUserUseCase(Repository repository, Transformer transformer) {
        super(transformer);
        this.repository = repository;
    }

    public Observable<User> getCurrentUser(){
        return observable(Collections.emptyMap());
    }

    @Override
    Observable<User> createObservable(Map<String, Object> data) {
        return repository.getCurrUser();
    }
}
