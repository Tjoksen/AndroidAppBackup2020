package com.talentjoko.todoapplication.di;




import com.talentjoko.todoapplication.presentation.edit.EditToDoDetailFragment;
import com.talentjoko.todoapplication.presentation.list.ToDoListActivity;
import com.talentjoko.todoapplication.presentation.view.ToDoDetailFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(EditToDoDetailFragment editToDoDetailFragment);

    void inject(ToDoDetailFragment toDoDetailFragment);

    void inject(ToDoListActivity toDoListActivity);
}
