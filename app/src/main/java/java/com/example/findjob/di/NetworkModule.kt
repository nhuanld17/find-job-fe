package com.example.findjob.di

import com.example.findjob.data.remote.api.AuthApi
import com.example.findjob.data.remote.api.EmployeeApi
import com.example.findjob.data.remote.api.JobPostApi
import com.example.findjob.data.remote.interceptor.AuthInterceptor
import com.example.findjob.data.remote.interceptor.TokenAuthenticator
import com.example.findjob.utils.InfoManager
import com.example.findjob.data.remote.api.RecruiterApi
import com.example.findjob.data.remote.api.NotificationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Nơi cấu hình Retrofit, OkHttp, và các thành phần cần thiết để gọi API trong ứng dụng
 * Android.
 * @Module: Đánh dấu đây là nơi cung cấp các dependency
 * @InstallIn(SingletonComponent::class): Các dependency sống suốt vòng đời của app
 * object: Dùng object thay vì class vì ta không cần nhiều instance
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Địa chỉ server (dùng 10.0.2.2 thay vì localhost để Android Emulator
    // truy cập máy tính của bạn)
    private const val BASE_URL = "http://172.20.10.4:8080/api/"

    /**
     * Dùng để ghi log request & response khi bạn gọi API.
     * Tạo interceptor để ghi lại toàn bộ thông tin request/response
     * Level.BODY: Ghi cả header và body của request/response
     */
    // @Provides: Hilt sẽ gọi hàm này để tạo HttpLoggingInterceptor.
    // @Singleton: Tạo 1 lần duy nhất cho toàn app.
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * @Provides: Hilt sẽ gọi hàm này để tạo AuthInterceptor.
     * @Singleton: Tạo 1 lần duy nhất cho toàn app.
     */
    @Provides
    @Singleton
    fun provideAuthInterceptor(infoManager: InfoManager): AuthInterceptor = AuthInterceptor(infoManager)

    @Provides
    @Singleton
    fun provideTokenAuthenticator(infoManager: InfoManager): TokenAuthenticator {
        return TokenAuthenticator(infoManager)
    }

    /**
     * Retrofit – Thư viện gọi API
     * BASE_URL: là địa chỉ API gốc (ở đây là máy chủ backend chạy trên máy thật).
     * addConverterFactory(...): giúp Retrofit hiểu dữ liệu JSON.
     */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder() // Tạo một instance mới của Retrofit với cấu hình tùy chỉnh.
            .baseUrl(BASE_URL) // Thiết lập địa chỉ gốc của API (ví dụ "https://api.example.com/").
            .client(client) // Sử dụng OkHttpClient đã tiêm vào, cho phép cấu hình các middleware,
                            // interceptor, logging, cache, retry...
            // Dùng Gson để chuyển đổi JSON thành object Kotlin/Java và ngược lại.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * - Sử dụng Dagger (hoặc Hilt) để cung cấp một implementation của AuthApi từ Retrofit
     * @Provides: Là annotation của Dagger/Hilt dùng để đánh dấu rằng hàm này cung
     * cấp một dependency. Dagger sẽ gọi hàm này khi cần một instance của AuthApi.
     * @Singleton: Dagger sẽ tạo một instance duy nhất (singleton) cho toàn bộ vòng
     * đời của app. Mọi nơi inject AuthApi đều dùng cùng một instance Retrofit đã tạo ra từ đây.
     * - Hàm này nhận vào một instance của Retrofit (đã được Dagger/Hilt cung cấp ở đâu đó).
     * - Sau đó gọi retrofit.create(AuthApi::class.java) để tạo ra implementation của interface
     * AuthApi.
     */
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEmployeeApi(retrofit: Retrofit): EmployeeApi {
        return retrofit.create(EmployeeApi::class.java)
    }

    /**
     * OkHttpClient – Cấu hình client mạng tổng thể
     * - addInterceptor(logging): In log JSON request/response
     * - addInterceptor(authInterceptor): Gắn access token vào header
     * - authenticator(tokenAuthenticator): Lỗi token hết hạn thì yêu cầu đăng nhập lại
     * - timeout: Cài thời gian chờ (30 giây) cho các loại kết nối
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .connectTimeout(300, TimeUnit.SECONDS)  // Tăng thời gian kết nối lên 5 phút
            .readTimeout(300, TimeUnit.SECONDS)     // Tăng thời gian đọc lên 5 phút
            .writeTimeout(300, TimeUnit.SECONDS)    // Tăng thời gian ghi lên 5 phút
            .callTimeout(300, TimeUnit.SECONDS)     // Tổng thời gian cho một request
            .retryOnConnectionFailure(true)         // Tự động thử lại khi kết nối thất bại
            .build()
    }

    @Provides
    @Singleton
    fun provideJobPostApi(retrofit: Retrofit): JobPostApi {
        return retrofit.create(JobPostApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecruiterApi(retrofit: Retrofit): RecruiterApi {
        return retrofit.create(RecruiterApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi {
        return retrofit.create(NotificationApi::class.java)
    }
}
