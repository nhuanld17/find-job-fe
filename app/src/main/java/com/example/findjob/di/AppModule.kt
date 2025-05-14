package com.example.findjob.di

import android.content.Context
import com.example.findjob.data.remote.api.AIApi
import com.example.findjob.data.remote.api.AuthApi
import com.example.findjob.data.remote.api.EmployeeApi
import com.example.findjob.data.repository.AIRepository
import com.example.findjob.data.repository.AuthRepository
import com.example.findjob.data.repository.EmployeeRepository
import com.example.findjob.utils.CloudinaryConfig
import com.example.findjob.utils.InfoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * @Module: Đánh dấu đây là module cấu hình của Dagger/Hilt. Module là nơi bạn định
 * nghĩa cách tạo ra các dependency
 * @InstallIn(SingletonComponent::class): Tất cả các dependency bên trong module này
 * sẽ được sống trong vòng đời singleton của app (nghĩa là chỉ tạo một lần duy nhất,
 * dùng suốt đời app)
 * @Provides:
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * @Provides: Hilt sẽ dùng hàm này để tạo ra một AuthRepository
     * @Singleton: Hilt sẽ giữ lại một bản duy nhất trong toàn app
     * return AuthRepository(...): Bạn tạo ra repository và Hilt sẽ
     * cung cấp nó cho nơi nào cần
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi,
        infoManager: InfoManager
    ): AuthRepository {
        return AuthRepository(api, infoManager)
    }

    @Provides
    @Singleton
    fun provideEmployeeRepository(
        api: EmployeeApi,
        infoManager: InfoManager
    ) : EmployeeRepository {
        return EmployeeRepository(api, infoManager)
    }

    @Provides
    @Singleton
    fun provideAIRepository(
        api: AIApi,
        @ApplicationContext context: Context
    ): AIRepository {
        return AIRepository(api, context)
    }

    @Provides
    @Singleton
    fun provideAIApi(retrofit: Retrofit): AIApi {
        return retrofit.create(AIApi::class.java)
    }

    /**
     * @Provides: Hilt dùng hàm này để cấp Context
     * @ApplicationContext: Annotation đặc biệt của Hilt, giúp bạn lấy
     * application-level context, cần context của app để làm gì đó (DataStore,
     * file, Toast...)
     */
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideCloudinaryConfig(@ApplicationContext context: Context): CloudinaryConfig {
        return CloudinaryConfig(context)
    }
}
