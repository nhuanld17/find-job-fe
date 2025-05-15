package com.example.findjob

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @HiltAndroidApp: Tự động tạo ra Dagger graph (biểu đồ phụ thuộc)
 * Tự động gắn các dependency (Inject) cho các class dùng @Inject hoặc @AndroidEntryPoint
 * Hilt sẽ bắt đầu từ application scope – từ đây nó sẽ tự động truyền các dependency xuống
 * các class khác như Activity, ViewModel, Fragment...
 */

/**
 * Bạn đang tạo 1 class kế thừa Application của Android.
 * - Mỗi app Android chỉ có 1 class Application
 * - Nó được tạo đầu tiên khi app được khởi động
 * - Có thể dùng để: Khởi tạo Hilt (chính là việc bạn đang làm)
 */
@HiltAndroidApp
class MyApplication : Application(){
}