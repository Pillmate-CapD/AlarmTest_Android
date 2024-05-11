## 트러블 슈팅(알람 기능)

### 알람테스트 목표
1) 원하는 시간에 알람 설정하기
2) 화면이 꺼져있는 상태에서도 fullScreen으로 알람 띄우기

### 트러블 리스트
1) 알람 설정관련 문제
2) 알람 설정 후 fullscreen으로 화면 띄우기 문제
3) 앱을 나가거나 테스트폰의 화면이 꺼져도 알람이 울리도록 해야 함
</br>

**1. 알람 설정 관련 문제**
- 권한의 문제로 인해서 android 13(API Level 33)or High인 경우 알람과 같은 기능을 사용하려면 SCHEDULE_EXACT_ALARM 권한이 필요함

  ```
  <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
  <uses-permission android:name="com.android.alarm.permission.SET_ALARM" />
  ```
- 앱 자체적으로 알람 권한을 필요로 하기 때문에 앱에 권한이 있는 지 확인해야 함
  ```
  private fun openNotificationSettings() {
        val settingsIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        settingsIntent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(settingsIntent)
    }
  ```
  => intent로 알람 권한이 있는 지 확인하도록 하고, 권한을 ON할 수 있도록 함 + 해당 내용 후에 권한 여부를 코드로 파악해 알람이 울릴 수 있도록 함
</br>

**2. 알람 설정 후 FullScreen으로 화면 띄우기**
- 알람은 notification을 이용해서 띄우도록 하고, 띄우는 intent를 fullscreenIntent로 설정함 
  ```
  return NotificationCompat.Builder(this, CHANNEL_ID)
              .setContentTitle("Alarm")
              .setContentText("Alarm is ringing")
              .setContentIntent(pendingIntent)
              .setPriority(NotificationCompat.PRIORITY_MAX) // 중요도를 MAX로 변경하여 전체 화면 알림으로 설정
              .setAutoCancel(true)
              .setFullScreenIntent(fullscreenPendingIntent, true) // 전체 화면으로 설정
              .build()
  ```
  => fullScreenIntent를 하기 위해서는 알람 noti 채널을 생성하고 알람을 생성할 떄 중요도를 high와 max로 설정해주어야 함
</br>

**3. 앱을 나가거나 테스트폰의 화면이 꺼져도 알람 울리기**
- 일단 window에서 시스템 알람을 띄우려면 권한이 필요함
  ```
  <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
  ```
  => 계속 안되다가 이거 권한 추가하자마자 잘돌아가기 시작했음
</br>

- 잠금 상태일때도 잠금된 화면 위에 알람 액티비티가 띄워져야 하고, 화면이 꺼진 상태에서도 켜져야 하기 때문에 WindowManager와 KeyguardManager를 이용해서 화면을 띄울 수 있도록 함
  ```
  @SuppressLint("NewApi")
    private fun turnScreenOnAndKeyguardOff() {
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )

        setShowWhenLocked(true)
        setTurnScreenOn(true)

        // Android 12 이상에서는 KeyguardManager를 사용하여 잠금 화면을 해제합니다.
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (keyguardManager.isKeyguardLocked) {
            keyguardManager.requestDismissKeyguard(this, null)
        }
    }
  ```
  - FLAG_KEEP_SCREEN_ON : 알람이 울리는 동안에 사용자가 디바이스 화면을 볼 수 있도록 하는 플래그
  - setShowWhenLocked(true) : 잠금 화면 위에 액티비티를 표시할 수 있도록 허용
  - setTurnScreenOn(true) : 잠긴 화면이나 꺼진 화면에서 알람이 울릴 때 화면이 켜지도록 함
  - keyguardManager.requestDismissKeyguard(this, null) : Android 12 이상에서 잠금 화면을 자동으로 해제해주어 알람을 볼 수 있도록 함 
  
