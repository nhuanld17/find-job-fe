package com.example.findjob.data.remote.interceptor

import com.example.findjob.utils.InfoManager
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

/**
 * Bạn đang xem một class tên là TokenAuthenticator, dùng để:
 * - Khi server trả về lỗi 401 Unauthorized (tức là Access Token hết hạn),
 * - Nó sẽ tự động gọi API refresh token
 * - Nếu thành công, nó sẽ thử gửi lại request cũ nhưng với access token mới
 * - Nếu thất bại, nó sẽ báo về OkHttp rằng: "Thôi khỏi, đừng thử lại nữa"
 */

// @Inject constructor(...): Hilt sẽ tự động truyền TokenManager vào.
// TokenManager: dùng để lưu hoặc lấy token cũ/mới.
// : Authenticator: đây là class đặc biệt của OkHttp cho việc retry khi bị 401 (token hết hạn).
class TokenAuthenticator @Inject constructor(
    private val infoManager: InfoManager
) : Authenticator {

    // Khi server trả về lỗi 401 Unauthorized, OkHttp sẽ gọi hàm này.
    // Mục đích: thử lấy access token mới rồi gửi lại request.
    override fun authenticate(route: Route?, response: Response): Request? {
        // Khi gặp lỗi 401, trả về null để OkHttp biết rằng không thể thử lại
        return null
    }
} 