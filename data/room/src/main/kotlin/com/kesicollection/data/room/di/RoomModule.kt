package com.kesicollection.data.room.di

import com.kesicollection.data.api.QuestionApi
import com.kesicollection.data.room.RoomQuestionApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class RoomModule {

    //TODO: Let the app module define this.
    @Binds
    @Named("room_question_api")
    abstract fun bindRoomQuestionApi(
        impl: RoomQuestionApi
    ): QuestionApi
}