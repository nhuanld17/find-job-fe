package com.example.findjob.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.findjob.data.model.response.AuthResponse
import com.example.findjob.data.model.response.UpdateEmployeeProfileResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// 1
/**
 * Mục đích của TokenManager:
 * - Lưu access token
 * - Lấy token ra để gắn vào request
 * - Xoá token khi logout
 * - Kiểm tra xem token có còn hợp lệ không
 */

/**
 * @Singleton: app chỉ tạo một instance duy nhất của TokenManager
 * @Inject: cho phép Hilt/Dagger tiêm tự động khi cần
 * @ApplicationContext: chỉ rõ là context của toàn ứng dụng, không phải activity
 */
@Singleton
class InfoManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * - SharedPreferences: Cách đơn giản nhất để lưu trữ dữ liệu nhỏ dạng key–value.
     * - "auth_prefs" là tên file lưu trữ token.
     * - MODE_PRIVATE: Chỉ app của bạn mới truy cập được file này.
     */
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "auth_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val TAG = "TokenManager"
        private const val TOKEN_EXPIRATION_TIME = 86400L // 24 hours in seconds
    }

    // Hàm lưu token
    fun saveInfo(authResponse: AuthResponse) {
        // Ghi log (logcat) 10 ký tự đầu tiên của token để tiện debug.
        Log.d(TAG, "Setting access token: ${authResponse.token.take(10)}...")

        // Calculate token expiration time (current time + 24 hours)
        val expirationTime = System.currentTimeMillis() + (TOKEN_EXPIRATION_TIME * 1000)

        // Dùng putString(...) để lưu token vào SharedPreferences.
        // Gọi apply() để lưu bất đồng bộ (nhanh, không block UI).
        sharedPreferences.edit()
            .putString("email", authResponse.email)
            .putString("name", authResponse.name)
            .putString("role", authResponse.role)
            .putString("access_token", authResponse.token)
            .putString("img_url", authResponse.imageUrl)
            .putLong("token_expiration", expirationTime)
            .apply()
            
        // Verify token was saved
        val savedToken = getAccessToken()
        Log.d(TAG, "Token saved successfully: ${savedToken != null}")
        Log.d(TAG, "Saved token length: ${savedToken?.length}")
        Log.d(TAG, "Token will expire at: ${java.util.Date(expirationTime)}")
    }

    fun updateInfo(updateResponse: UpdateEmployeeProfileResponse?) {
        Log.d(TAG, "Updating info in SharedPref ....")

        // Calculate token expiration time (current time + 24 hours)
        val expirationTime = System.currentTimeMillis() + (TOKEN_EXPIRATION_TIME * 1000)

        if (updateResponse != null) {
            sharedPreferences.edit()
                .putString("email", updateResponse.email)
                .putString("name", updateResponse.fullName)
                .putString("access_token", updateResponse.token)
                .putLong("token_expiration", expirationTime)
                .apply()
        }

        Log.d(TAG, "Updated info")
    }

    // khi update ảnh thì lưu lại link ảnh mới vào sharedpref
    fun updateImage(imgUrl: String?) {
        Log.d(TAG, "Updating img url in SharedPref ....")
        sharedPreferences.edit()
            .putString("img_url", imgUrl)
            .apply()
    }

    // Trả về token nếu có, hoặc null nếu chưa từng lưu.
    fun getAccessToken(): String? = sharedPreferences.getString("access_token", null)

    // Lấy name
    fun getName(): String? = sharedPreferences.getString("name", null)

    // lấy email
    fun getEmail(): String? = sharedPreferences.getString("email", null)

    // lấy role
    fun getRole(): String? = sharedPreferences.getString("role", null)

    // Xoá toàn bộ key–value trong auth_prefs
    fun clearTokens() {
        Log.d(TAG, "Clearing all user information")
        sharedPreferences.edit()
            .remove("access_token")
            .remove("token_expiration")
            .remove("email")
            .remove("name")
            .remove("role")
            .remove("img_url")
            .apply()
    }

    // Kiểm tra token hợp lệ: Trả về true nếu token không rỗng → tức là user đã login thành công.
    fun isLoggedIn(): Boolean {
        val token = getAccessToken()
        val expirationTime = sharedPreferences.getLong("token_expiration", 0)
        val currentTime = System.currentTimeMillis()
        
        return !token.isNullOrEmpty() && currentTime < expirationTime
    }

    fun isTokenExpired(): Boolean {
        val expirationTime = sharedPreferences.getLong("token_expiration", 0)
        val currentTime = System.currentTimeMillis()
        return currentTime >= expirationTime
    }

    /**
     * Kiểm tra trạng thái đăng nhập và role của người dùng
     * @return LoginStatus: NOT_LOGGED_IN, EMPLOYEE, RECRUITER
     */
    fun checkLoginStatus(): LoginStatus {
        if (!isLoggedIn()) {
            Log.d(TAG, "User is not logged in")
            return LoginStatus.NOT_LOGGED_IN
        }

        val role = getRole()
        Log.d(TAG, "User is logged in with role: $role")
        
        return when (role?.uppercase()) {
            "EMPLOYEE" -> LoginStatus.EMPLOYEE
            "RECRUITER" -> LoginStatus.RECRUITER
            else -> {
                Log.e(TAG, "Invalid role: $role")
                clearTokens() // Clear invalid data
                LoginStatus.NOT_LOGGED_IN
            }
        }
    }

    fun getImageUrl(): String? {
        return sharedPreferences.getString("img_url", null)
    }

    enum class LoginStatus {
        NOT_LOGGED_IN,
        EMPLOYEE,
        RECRUITER
    }

    // Setter methods
    fun setName(name: String) {
        Log.d(TAG, "Setting name: $name")
        sharedPreferences.edit()
            .putString("name", name)
            .apply()
    }

    fun setEmail(email: String) {
        Log.d(TAG, "Setting email: $email")
        sharedPreferences.edit()
            .putString("email", email)
            .apply()
    }

    fun setRole(role: String) {
        Log.d(TAG, "Setting role: $role")
        sharedPreferences.edit()
            .putString("role", role)
            .apply()
    }

}
